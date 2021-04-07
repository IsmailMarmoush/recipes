package io.memoria.recipes.core.repo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CategoryRepo {
  Flux<String> all();

  Mono<String> put(String create);
}
