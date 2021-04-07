package io.memoria.recipes.app.dto;

import io.memoria.recipes.core.recipe.Head;
import io.memoria.recipes.core.recipe.Ingredient;
import io.memoria.recipes.core.recipe.Recipe;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;

import java.util.function.Function;

public final record RecipeDtoV05(Head head, List<Ingredient> ingredients, String directions) implements RecipeDto {
  public RecipeDtoV05(Recipe recipe) {
    this(recipe.head(),
         recipe.ingredients().values().flatMap(Function.identity()).toList(),
         recipe.directions().mkString("\n"));
  }

  @Override
  public Recipe toRecipe() {
    return new Recipe(head, HashMap.of("", ingredients), List.of(directions));
  }
}
