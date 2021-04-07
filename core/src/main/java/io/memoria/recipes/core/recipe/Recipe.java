package io.memoria.recipes.core.recipe;

import io.vavr.collection.List;
import io.vavr.collection.Map;

public record Recipe(Head head, Map<String, List<Ingredient>> ingredients, List<String> directions) {}
