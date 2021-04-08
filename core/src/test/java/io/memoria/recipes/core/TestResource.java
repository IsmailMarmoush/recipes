package io.memoria.recipes.core;

import io.memoria.recipes.core.recipe.Head;
import io.memoria.recipes.core.recipe.Recipe;
import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;

public class TestResource {
  public static final Recipe omeletteRecipe = omelette();
  public static final Recipe amarettoCake = amaretto();

  private static Recipe amaretto() {
    // Omelette
    var head = new Head("Amaretto Cake", HashSet.of("Liquor", "Cakes", "Cake mixes", "eggs"), 1);
    var ingredients = HashSet.of("1 1/2 cups of Toasted Almonds; chopped",
                                 "1 package of Yellow cake mix; no pudding",
                                 "1 package of Vanilla instant pudding");
    var directions = List.of("Sprinkle 1 cup almonds into bottom of a well-greased and floured 10\" tube pan set aside.",
                             "Combine cake mix, pudding mix, eggs, oil, water,amaretto, and almond extract in a mixing bowl beat on low speed of an electric mixer til dry ingredients are moistened. ",
                             "Increase speed to medium, and beat 4 minutes.");
    return new Recipe(head, HashMap.of("base", ingredients), directions);
  }

  private static Recipe omelette() {
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
