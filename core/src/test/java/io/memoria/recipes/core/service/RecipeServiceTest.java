package io.memoria.recipes.core.service;

import io.memoria.jutils.jcore.id.Id;
import io.memoria.recipes.core.recipe.Recipe;
import io.memoria.recipes.core.repo.mem.RecipeMemRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.concurrent.ConcurrentHashMap;

import static io.memoria.recipes.core.TestResource.amarettoCake;
import static io.memoria.recipes.core.TestResource.omeletteRecipe;

class RecipeServiceTest {
  private final ConcurrentHashMap<Id, Recipe> db = new ConcurrentHashMap<>();
  private final RecipeMemRepo memRepo = new RecipeMemRepo(db);
  private final RecipeService service = new RecipeService(memRepo, memRepo);

  @BeforeEach
  void beforeEach() {
    db.clear();
  }

  @Test
  @DisplayName("Mem repo should create recipe successfully")
  void create() {
    // Given
    var id = Id.of(omeletteRecipe.head().title());
    StepVerifier.create(service.create(omeletteRecipe)).expectNext(id).verifyComplete();
    Assertions.assertEquals(omeletteRecipe, db.get(id));
  }

  @Test
  @DisplayName("Mem repo should find all recipes with specific category")
  void filterByCategory() {
    // Given
    db.put(Id.of(omeletteRecipe.head().title()), omeletteRecipe);
    db.put(Id.of(amarettoCake.head().title()), amarettoCake);
    // When
    StepVerifier.create(service.recipes("breakfast")).expectNext(omeletteRecipe).verifyComplete();
    StepVerifier.create(service.recipes("eggs")).expectNext(amarettoCake, omeletteRecipe).verifyComplete();
    StepVerifier.create(service.recipes("none")).verifyComplete();
  }

  @Test
  @DisplayName("Mem repo should return all recipes")
  void recipes() {
    // Given
    db.put(Id.of(omeletteRecipe.head().title()), omeletteRecipe);
    db.put(Id.of(amarettoCake.head().title()), amarettoCake);
    // When
    StepVerifier.create(service.recipes()).expectNext(omeletteRecipe, amarettoCake).verifyComplete();
  }

  @Test
  @DisplayName("Mem repo should find all recipes which contains a text")
  void search() {
    // Given
    db.put(Id.of(omeletteRecipe.head().title()), omeletteRecipe);
    db.put(Id.of(amarettoCake.head().title()), amarettoCake);
    // When
    StepVerifier.create(service.search("amaretto")).expectNext(amarettoCake).verifyComplete();
    StepVerifier.create(service.search("eggs")).expectNext(amarettoCake, omeletteRecipe).verifyComplete();
    StepVerifier.create(service.search("none")).verifyComplete();
  }
}
