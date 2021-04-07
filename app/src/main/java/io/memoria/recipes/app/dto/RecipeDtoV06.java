package io.memoria.recipes.app.dto;

import io.memoria.recipes.core.recipe.Head;
import io.memoria.recipes.core.recipe.Ingredient;
import io.memoria.recipes.core.recipe.Recipe;
import io.vavr.collection.List;
import io.vavr.collection.Map;

public final record RecipeDtoV06(Head head, Map<String, List<Ingredient>> ingredients, List<String> directions)
        implements RecipeDto {
  public RecipeDtoV06(Recipe recipe) {
    this(recipe.head(), recipe.ingredients(), recipe.directions());
  }

  @Override
  public Recipe toRecipe() {
    return new Recipe(head, ingredients, directions);
  }
}
