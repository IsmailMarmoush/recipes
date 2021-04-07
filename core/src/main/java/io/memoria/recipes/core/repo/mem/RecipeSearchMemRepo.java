package io.memoria.recipes.core.repo.mem;

import io.memoria.recipes.core.recipe.Recipe;
import io.memoria.recipes.core.repo.RecipeSearchRepo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class RecipeSearchMemRepo implements RecipeSearchRepo {
  private final Set<Recipe> recipesIndex = new HashSet<>();
  private final ConcurrentHashMap<String, Set<Recipe>> categoryIndex = new ConcurrentHashMap<>();

  @Override
  public Mono<Recipe> addRecipe(Recipe recipe) {
    return Mono.fromRunnable(() -> {
      recipesIndex.add(recipe);
      indexCategories(recipe);
    }).thenReturn(recipe);
  }

  @Override
  public Flux<Recipe> findByCategory(String category) {
    return Mono.fromCallable(() -> categoryIndex.getOrDefault(category, Set.of())).flatMapMany(Flux::fromIterable);
  }

  @Override
  public Flux<Recipe> findContains(String text) {
    return Mono.fromCallable(() -> filterByText(text)).flatMapMany(Flux::fromIterable);
  }

  private Set<Recipe> filterByText(String text) {
    return recipesIndex.stream().filter(e -> hasText(e, text)).collect(Collectors.toSet());
  }

  private boolean hasText(Recipe recipe, String text) {
    return recipe.directions().mkString(" ").contains(text) || recipe.head().title().contains(text);
  }

  private void indexCategories(Recipe recipe) {
    recipe.head().categories().forEach(cat -> indexCategory(cat, recipe));
  }

  private void indexCategory(String category, Recipe recipe) {
    categoryIndex.putIfAbsent(category, Set.of(recipe));
    categoryIndex.computeIfPresent(category, (cat, sett) -> {
      sett.add(recipe);
      return sett;
    });
  }
}
