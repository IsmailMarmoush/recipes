package io.memoria.recipes.core.recipe;

import io.vavr.collection.List;

public record Head(String title, List<String> categories, int yield) {}
