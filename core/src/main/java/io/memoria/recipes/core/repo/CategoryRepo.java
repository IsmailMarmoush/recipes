package io.memoria.recipes.core.repo;

import reactor.core.publisher.Flux;

public interface CategoryRepo {
  Flux<String> categories();
}
