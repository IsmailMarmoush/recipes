package io.memoria.recipes.app.dto;

import io.memoria.recipes.model.recipe.Directions;
import io.memoria.recipes.model.recipe.Head;
import io.memoria.recipes.model.recipe.Ingredient;
import io.vavr.collection.List;
import io.vavr.collection.Map;

public final record RecipeDtoV06(Head head, Map<String, List<Ingredient>> ingredients, Directions directions)
        implements RecipeDto {}
