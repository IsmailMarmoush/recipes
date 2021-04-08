package io.memoria.recipes.core.service;

import io.memoria.jutils.jcore.id.Id;
import io.memoria.recipes.core.recipe.Recipe;
import io.memoria.recipes.core.repo.RecipeRepo;
import io.memoria.recipes.core.repo.RecipeSearchRepo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SuppressWarnings("ClassCanBeRecord")
public class RecipeService {
  private final RecipeRepo recipeRepo;
  private final RecipeSearchRepo recipeSearchRepo;

  public RecipeService(RecipeRepo recipeRepo, RecipeSearchRepo recipeSearchRepo) {
    this.recipeRepo = recipeRepo;
    this.recipeSearchRepo = recipeSearchRepo;
  }

  Mono<Id> create(Recipe recipe) {
    return this.recipeRepo.create(recipe);
  }

  Flux<Recipe> recipes() {
    return this.recipeRepo.recipes();
  }

  Flux<Recipe> recipes(String filterByCategory) {
    return this.recipeRepo.recipes(filterByCategory);
  }

  Flux<Recipe> search(String text) {
    return this.recipeSearchRepo.search(text);
  }
}
