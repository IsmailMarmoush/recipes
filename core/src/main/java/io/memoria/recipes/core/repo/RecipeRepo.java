package io.memoria.recipes.core.repo;

import io.memoria.jutils.jcore.id.Id;
import io.memoria.recipes.core.recipe.Recipe;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RecipeRepo {
  Flux<Recipe> all();

  Mono<Id> create(Recipe recipe);
}
