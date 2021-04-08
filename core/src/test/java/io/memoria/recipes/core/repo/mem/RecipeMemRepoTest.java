package io.memoria.recipes.core.repo.mem;

import io.memoria.jutils.jcore.id.Id;
import io.memoria.recipes.core.recipe.Recipe;
import io.vavr.collection.HashSet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.concurrent.ConcurrentHashMap;

import static io.memoria.recipes.core.TestResource.amarettoCake;
import static io.memoria.recipes.core.TestResource.omeletteRecipe;

class RecipeMemRepoTest {
  private final ConcurrentHashMap<Id, Recipe> db = new ConcurrentHashMap<>();
  private final RecipeMemRepo repo = new RecipeMemRepo(db);

  @BeforeEach
  void beforeEach() {
    db.clear();
  }

  @Test
  @DisplayName("Mem Repo should return all categories")
  void categories() {
    // Given
    db.put(Id.of(omeletteRecipe.head().title()), omeletteRecipe);
    db.put(Id.of(amarettoCake.head().title()), amarettoCake);
    var expectedCats = HashSet.of("eggs", "breakfast", "Liquor", "Cakes", "Cake mixes");
    // When
    StepVerifier.create(repo.categories().collectList().map(HashSet::ofAll)).expectNext(expectedCats).verifyComplete();
  }

  @Test
  @DisplayName("Mem repo should create recipe successfully")
  void create() {
    // Given
    var id = Id.of(omeletteRecipe.head().title());
    StepVerifier.create(repo.create(omeletteRecipe)).expectNext(id).verifyComplete();
    Assertions.assertEquals(omeletteRecipe, db.get(id));
  }
  @Test
  @DisplayName("Mem repo should find all recipes which contains a text")
  void findByText(){
    // Given
    db.put(Id.of(omeletteRecipe.head().title()), omeletteRecipe);
    db.put(Id.of(amarettoCake.head().title()), amarettoCake);
    // When
    StepVerifier.create(repo.findContains("amaretto")).expectNext(amarettoCake).verifyComplete();
    StepVerifier.create(repo.findContains("eggs")).expectNext(amarettoCake, omeletteRecipe).verifyComplete();
    StepVerifier.create(repo.findContains("none")).verifyComplete();
  }
  @Test
  @DisplayName("Mem repo should find all recipes with specific category")
  void findByCategory() {
    // Given
    db.put(Id.of(omeletteRecipe.head().title()), omeletteRecipe);
    db.put(Id.of(amarettoCake.head().title()), amarettoCake);
    // When
    StepVerifier.create(repo.findByCategory("breakfast")).expectNext(omeletteRecipe).verifyComplete();
    StepVerifier.create(repo.findByCategory("eggs")).expectNext(amarettoCake, omeletteRecipe).verifyComplete();
    StepVerifier.create(repo.findByCategory("none")).verifyComplete();
  }

  @Test
  @DisplayName("Mem repo should return all recipes")
  void recipes() {
    // Given
    db.put(Id.of(omeletteRecipe.head().title()), omeletteRecipe);
    db.put(Id.of(amarettoCake.head().title()), amarettoCake);
    // When
    StepVerifier.create(repo.recipes()).expectNext(omeletteRecipe, amarettoCake).verifyComplete();
  }
}
