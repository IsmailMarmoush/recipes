package io.memoria.recipes.core.recipe;

import io.vavr.collection.Set;

public record Head(String title, Set<String> categories, int yield) {}
