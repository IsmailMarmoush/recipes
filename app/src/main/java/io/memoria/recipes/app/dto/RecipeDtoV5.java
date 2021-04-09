package io.memoria.recipes.app.dto;

import io.memoria.recipes.core.recipe.Head;
import io.memoria.recipes.core.recipe.QuickRecipe;
import io.memoria.recipes.core.recipe.Recipe;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Set;

import java.util.function.Function;

public final record RecipeDtoV5(Head head, Set<String> ingredients, String directions) implements RecipeDto {
  public static final String BASE_INGREDIENTS = "Base Ingredients";

  public RecipeDtoV5(Recipe recipe) {
    this(recipe.head(),
         recipe.ingredients().values().toSet().flatMap(Function.identity()),
         recipe.directions().mkString(" "));
  }
  
  @Override
  public Recipe toRecipe() {
    return new QuickRecipe(head, HashMap.of(BASE_INGREDIENTS, ingredients), List.of(directions));
  }
}
