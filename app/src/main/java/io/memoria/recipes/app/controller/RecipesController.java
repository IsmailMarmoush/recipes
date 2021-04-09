package io.memoria.recipes.app.controller;

import io.memoria.jutils.jcore.text.Json;
import io.memoria.jutils.jcore.text.TextException;
import io.memoria.recipes.app.dto.RecipeDto;
import io.memoria.recipes.core.recipe.Recipe;
import io.memoria.recipes.core.service.RecipeService;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.vavr.Function1;
import io.vavr.control.Try;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import static io.memoria.jutils.jcore.vavr.ReactorVavrUtils.toMono;
import static io.memoria.jutils.jweb.netty.NettyServerUtils.stringReply;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;

public record RecipesController(Json json, Function1<Recipe, RecipeDto> toDto, RecipeService recipeService) {
  public Mono<Void> createRecipe(HttpServerRequest req, HttpServerResponse resp) {
    return req.receive()
              .aggregate()
              .asString()
              .flatMap(s -> toMono(json.deserialize(s, RecipeDto.class)))
              .map(RecipeDto::toRecipe)
              .flatMap(recipeService::create)
              .flatMap(id -> stringReply.apply(resp, OK, id.value()).then())
              .onErrorResume(t -> t instanceof TextException,
                             t -> resp.status(BAD_REQUEST).sendString(Mono.just(t.getMessage())).then())
              .onErrorResume(t -> t instanceof Exception,
                             t -> resp.status(INTERNAL_SERVER_ERROR).sendString(Mono.just(t.getMessage())).then());
  }

  public Mono<Void> recipes(HttpServerRequest req, HttpServerResponse resp) {
    var query = new QueryStringDecoder(req.uri());
    var catOpt = Try.of(() -> query.parameters().get("category").get(0)).toOption();
    return catOpt.map(category -> resp.status(OK).sendString(recipes(category)).then())
                 .getOrElse(() -> resp.status(OK).sendString(recipes()).then());
  }

  public Mono<Void> recipesSearch(HttpServerRequest req, HttpServerResponse resp) {
    var query = new QueryStringDecoder(req.uri());
    var catOpt = Try.of(() -> query.parameters().get("text").get(0)).toOption();
    return catOpt.map(text -> resp.status(OK).sendString(search(text)).then())
                 .getOrElse(() -> resp.status(OK).sendString(recipes()).then());
  }

  private Mono<String> recipes() {
    return recipeService.recipes().map(toDto).collectList().map(json::serialize).map(Try::get);
  }

  private Mono<String> recipes(String category) {
    return recipeService.recipes(category).map(toDto).collectList().map(json::serialize).map(Try::get);
  }

  private Mono<String> search(String text) {
    return recipeService.search(text).map(toDto).collectList().map(json::serialize).map(Try::get);
  }
}
