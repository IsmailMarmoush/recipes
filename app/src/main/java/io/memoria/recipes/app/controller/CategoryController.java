package io.memoria.recipes.app.controller;

import io.memoria.jutils.jcore.text.TextTransformer;
import io.memoria.recipes.core.service.CategoryService;
import io.vavr.Function2;
import io.vavr.control.Try;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;

public record CategoryController(TextTransformer transformer, CategoryService categoryService)
        implements Function2<HttpServerRequest, HttpServerResponse, Mono<Void>> {
  @Override
  public Mono<Void> apply(HttpServerRequest req, HttpServerResponse resp) {
    var categoriesMono = categoryService.categories().collectList().map(transformer::serialize).map(Try::get);
    return resp.status(OK).sendString(categoriesMono).then();
  }
}
