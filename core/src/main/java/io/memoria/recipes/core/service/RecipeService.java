package io.memoria.recipes.core.service;

import io.memoria.jutils.jcore.id.Id;
import io.memoria.recipes.core.recipe.Recipe;
import io.vavr.control.Option;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RecipeService {
  Mono<Id> createRecipe(Recipe recipe);

  Flux<Recipe> recipes(Option<String> category, Option<String> text);
}
