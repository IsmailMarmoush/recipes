package io.memoria.recipes.app.dto;

import io.memoria.recipes.app.Defaults;
import io.memoria.recipes.app.TestResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RecipeDtoV5Test {

  @Test
  void deserializeDto() {
    // When
    var recipeDtoV5 = Defaults.jsonV5.deserialize(TestResource.recipe30MinChili, RecipeDto.class).get();
    var recipeDtoV6 = Defaults.jsonV6.deserialize(TestResource.recipeAmarettoCake, RecipeDto.class).get();
    // Then
    Assertions.assertTrue(recipeDtoV5 instanceof RecipeDtoV5);
    Assertions.assertTrue(recipeDtoV6 instanceof RecipeDtoV6);
  }

  @Test
  void serializeDto() {
    // When
    var recipeDtoV5 = Defaults.jsonV5.serialize(TestResource.omeletteV5).get();
    var recipeDtoV6 = Defaults.jsonV6.serialize(TestResource.omeletteV6).get();
    // Then
    Assertions.assertEquals(TestResource.recipeOmeletteV5, recipeDtoV5);
    Assertions.assertEquals(TestResource.recipeOmeletteV6, recipeDtoV6);
  }
}
