package io.memoria.recipes.app;

import io.memoria.recipes.app.dto.RecipeDto;
import io.memoria.recipes.app.dto.RecipeDtoV5;
import io.memoria.recipes.app.dto.RecipeDtoV6;
import io.memoria.recipes.core.recipe.Head;
import io.memoria.recipes.core.recipe.Recipe;
import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;

import static io.memoria.recipes.app.Defaults.file;

public class TestResource {
  // Recipes json
  public static final String recipe30MinChili;
  public static final String recipeAmarettoCake;
  public static final String recipeZucchiniDish;
  public static final String recipeOmeletteV6;
  public static final String recipeOmeletteV5;

  // Controllers Json
  public static final String categories;

  // Recipes objects
  public static final RecipeDto omeletteV5 = new RecipeDtoV5(omeletteRecipe());
  public static final RecipeDto omeletteV6 = new RecipeDtoV6(omeletteRecipe());

  static {
    // Recipes json
    recipe30MinChili = file.read("recipes/30_Minute_Chili.json").block();
    recipeAmarettoCake = file.read("recipes/Amaretto_Cake.json").block();
    recipeZucchiniDish = file.read("recipes/Another_Zucchini_Dish.json").block();
    recipeOmeletteV5 = file.read("recipes/omeletteV5.json").block();
    recipeOmeletteV6 = file.read("recipes/omeletteV6.json").block();
    // Controllers Json
    categories = file.read("controller/categories.json").block();
  }

  private TestResource() {}

  private static Recipe omeletteRecipe() {
    // Omelette
    var head = new Head("omelette", HashSet.of("eggs", "breakfast"), 2);
    var ingredients = HashSet.of("2 eggs", "sprinkle of salt", "2 table spoon of oil");
    var directions = List.of("break the eggs",
                             "whisk the eggs",
                             "put the salt on eggs",
                             "heat the pan with oil",
                             "pour the whisked eggs");
    return new Recipe(head, HashMap.of("base", ingredients), directions);
  }
}
