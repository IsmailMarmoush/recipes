package io.memoria.recipes.core.repo.mem;

import io.memoria.jutils.jcore.id.Id;
import io.memoria.recipes.core.common.RecipeException.RecipeAlreadyExists;
import io.memoria.recipes.core.recipe.Recipe;
import io.memoria.recipes.core.repo.RecipeRepo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.ConcurrentHashMap;

import static io.memoria.jutils.jcore.vavr.ReactorVavrUtils.toMono;

public class RecipeMemRepo implements RecipeRepo {
  private final ConcurrentHashMap<Id, Recipe> db = new ConcurrentHashMap<>();

  @Override
  public Flux<Recipe> all() {
    return Mono.fromCallable(db::values).flatMapMany(Flux::fromIterable);
  }

  @Override
  public Mono<Id> create(Recipe recipe) {
    var id = Id.of(recipe.head().title());
    return Mono.fromCallable(() -> db.containsKey(id))
               .flatMap(toMono(() -> db.put(id, recipe), new RecipeAlreadyExists()))
               .thenReturn(id);
  }
}
