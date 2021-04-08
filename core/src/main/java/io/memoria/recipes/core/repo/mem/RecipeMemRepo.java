package io.memoria.recipes.core.repo.mem;

import io.memoria.jutils.jcore.id.Id;
import io.memoria.recipes.core.common.RecipeException.RecipeAlreadyExists;
import io.memoria.recipes.core.recipe.Head;
import io.memoria.recipes.core.recipe.Recipe;
import io.memoria.recipes.core.repo.CategoryRepo;
import io.memoria.recipes.core.repo.RecipeRepo;
import io.memoria.recipes.core.repo.RecipeSearchRepo;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.ConcurrentHashMap;

import static io.memoria.jutils.jcore.vavr.ReactorVavrUtils.toMono;

public record RecipeMemRepo(ConcurrentHashMap<Id, Recipe> db) implements RecipeRepo, CategoryRepo, RecipeSearchRepo {

  @Override
  public Flux<String> categories() {
    return Mono.fromCallable(this::categoriesSet).flatMapMany(Flux::fromIterable);
  }

  @Override
  public Mono<Id> create(Recipe recipe) {
    var id = Id.of(recipe.head().title());
    return Mono.fromCallable(() -> !db.containsKey(id))
               .flatMap(toMono(() -> db.put(id, recipe), new RecipeAlreadyExists()))
               .thenReturn(id);
  }

  @Override
  public Flux<Recipe> recipes(String category) {
    return Mono.fromCallable(() -> filterByCategory(category)).flatMapMany(Flux::fromIterable);
  }

  @Override
  public Flux<Recipe> recipes() {
    return Mono.fromCallable(db::values).flatMapMany(Flux::fromIterable);
  }

  @Override
  public Flux<Recipe> search(String text) {
    return Mono.fromCallable(() -> HashSet.ofAll(this.db.values()).filter(r -> contains(r, text)))
               .flatMapMany(Flux::fromIterable);
  }

  private Set<String> categoriesSet() {
    return HashSet.ofAll(this.db.values()).map(Recipe::head).flatMap(Head::categories);
  }

  private boolean contains(Recipe r, String text) {
    text = text.toLowerCase();
    var titleContains = r.head().title().toLowerCase().contains(text);
    var directionsContains = r.directions().mkString(" ").toLowerCase().contains(text);
    return titleContains || directionsContains;
  }

  private Set<Recipe> filterByCategory(String category) {
    return HashSet.ofAll(this.db.values()).filter(r -> r.head().categories().contains(category));
  }
}
