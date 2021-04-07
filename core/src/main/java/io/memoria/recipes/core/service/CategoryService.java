package io.memoria.recipes.core.service;

import reactor.core.publisher.Flux;

public interface CategoryService {
  Flux<String> all();
}
