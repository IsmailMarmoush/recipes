package io.memoria.recipes.core.recipe;

import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Set;

public record QuickRecipe(Head head, Map<String, Set<String>> ingredients, List<String> directions) implements Recipe {}
