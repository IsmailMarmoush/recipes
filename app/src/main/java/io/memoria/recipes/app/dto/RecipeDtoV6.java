package io.memoria.recipes.app.dto;

import io.memoria.recipes.core.recipe.Head;
import io.memoria.recipes.core.recipe.Recipe;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Set;

public final record RecipeDtoV6(Head head, Map<String, Set<String>> ingredients, List<String> directions)
        implements RecipeDto {
  public RecipeDtoV6(Recipe recipe) {
    this(recipe.head(), recipe.ingredients(), recipe.directions());
  }

  @Override
  public Recipe toRecipe() {
    return new Recipe(head, ingredients, directions);
  }
}
