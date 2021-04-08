package io.memoria.recipes.app.controller;

import io.memoria.jutils.jcore.id.IdGenerator;
import io.memoria.jutils.jcore.text.Json;
import io.vavr.Function2;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

public record RecipeHandler(IdGenerator idGen, Json json)
        implements Function2<HttpServerRequest, HttpServerResponse, Mono<Void>> {
  @Override
  public Mono<Void> apply(HttpServerRequest httpServerRequest, HttpServerResponse httpServerResponse) {
    //  var query = new QueryStringDecoder(req.uri());
    //      query.parameters().get("")
    return null;
  }
  //  public Mono<Void> handle(HttpServerRequest req, HttpServerResponse resp) {
  //    return req.receive()
  //              .aggregate()
  //              .asString()
  //              .flatMap(s -> toMono(json.deserialize(s, Recipe.class)))
  //              .flatMapMany()
  //              .then(resp.status(OK).sendString(Mono.just(OK.reasonPhrase())).then())
  //              .onErrorResume(t -> t instanceof TextException || t instanceof ESException,
  //                             t -> resp.status(BAD_REQUEST).sendString(Mono.just(t.getMessage())).then())
  //              .onErrorResume(t -> t instanceof Exception,
  //                             t -> resp.status(HttpResponseStatus.INTERNAL_SERVER_ERROR)
  //                                      .sendString(Mono.just(t.getMessage()))
  //                                      .then());
  //  }
}
