package io.memoria.recipes.core.repo;

import io.memoria.jutils.jcore.id.Id;
import io.memoria.recipes.core.recipe.Recipe;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RecipeRepo {
  Mono<Id> create(Recipe recipe);

  Flux<Recipe> recipes(String filterByCategory);

  Flux<Recipe> recipes();
}
