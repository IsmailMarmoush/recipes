package io.memoria.recipes.app.controller;

import io.vavr.Function2;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;

public record VarzController() implements Function2<HttpServerRequest, HttpServerResponse, Mono<Void>> {
  @Override
  public Mono<Void> apply(HttpServerRequest req, HttpServerResponse resp) {
    return resp.status(OK).sendString(Mono.just(OK.reasonPhrase())).then();
  }
}
