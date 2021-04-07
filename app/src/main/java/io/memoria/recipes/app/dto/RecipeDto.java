package io.memoria.recipes.app.dto;

import io.memoria.recipes.core.recipe.Recipe;

public sealed interface RecipeDto permits RecipeDtoV05, RecipeDtoV06 {
  Recipe toRecipe();
}
