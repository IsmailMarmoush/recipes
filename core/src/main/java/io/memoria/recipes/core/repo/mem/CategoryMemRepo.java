package io.memoria.recipes.core.repo.mem;

import io.memoria.recipes.core.repo.CategoryRepo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;

public class CategoryMemRepo implements CategoryRepo {
  private final Set<String> db = new HashSet<>();

  @Override
  public Flux<String> all() {
    return Flux.fromIterable(db);
  }

  @Override
  public Mono<String> put(String category) {
    return Mono.fromRunnable(() -> db.add(category)).thenReturn(category);
  }
}
