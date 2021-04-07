package io.memoria.recipes.core.repo;

import io.memoria.recipes.core.recipe.Recipe;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RecipeSearchRepo {
  Mono<Recipe> addRecipe(Recipe recipe);

  Flux<Recipe> findByCategory(String category);

  Flux<Recipe> findContains(String text);
}
