package io.memoria.recipes.app.dto;

import io.memoria.recipes.model.recipe.Directions;
import io.memoria.recipes.model.recipe.Head;
import io.memoria.recipes.model.recipe.Ingredient;
import io.vavr.collection.List;

public final record RecipeDtoV05(Head head, List<Ingredient> ingredients, Directions directions) implements RecipeDto {}
