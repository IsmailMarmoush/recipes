package io.memoria.recipes.core.service;

import io.memoria.jutils.jcore.id.Id;
import io.memoria.recipes.core.recipe.QuickRecipe;
import io.memoria.recipes.core.recipe.Recipe;
import io.memoria.recipes.core.repo.mem.RecipeMemRepo;
import io.vavr.collection.HashSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.concurrent.ConcurrentHashMap;

import static io.memoria.recipes.core.TestResource.amarettoCake;
import static io.memoria.recipes.core.TestResource.omeletteRecipe;

class CategoryServiceTest {
  private final ConcurrentHashMap<Id, Recipe> db = new ConcurrentHashMap<>();
  private final CategoryService service = new CategoryService(new RecipeMemRepo(db));

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
    StepVerifier.create(service.categories().collectList().map(HashSet::ofAll))
                .expectNext(expectedCats)
                .verifyComplete();
  }
}
