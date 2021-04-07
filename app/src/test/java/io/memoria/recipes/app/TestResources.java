package io.memoria.recipes.app;

import io.memoria.jutils.jcore.file.FileUtils;
import io.vavr.control.Option;
import reactor.core.scheduler.Schedulers;

public class TestResources {
  // Read & Write
  public static final FileUtils file;
  public static final String recipe30MinChili;
  public static final String recipeAmarettoCake;
  public static final String RecipeZucchiniDish;

  static {
    // FILE
    file = FileUtils.createDefault(Option.some("include:"), true, Schedulers.boundedElastic());

    // Recipes
    recipe30MinChili = file.read("recipes/30_Minute_Chili.json").block();
    recipeAmarettoCake = file.read("recipes/Amaretto_Cake.json").block();
    RecipeZucchiniDish = file.read("recipes/Another_Zucchini_Dish.json").block();
  }

  private TestResources() {}
}
