package io.memoria.recipes.core.repo;

import io.memoria.recipes.core.recipe.Recipe;
import reactor.core.publisher.Flux;

public interface RecipeSearchRepo {
  Flux<Recipe> search(String text);
}
