package io.memoria.recipes.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.memoria.jutils.jcore.file.FileUtils;
import io.memoria.jutils.jcore.text.Json;
import io.memoria.jutils.jcore.text.Yaml;
import io.memoria.jutils.jtext.jackson.JacksonUtils;
import io.memoria.jutils.jtext.jackson.JsonJackson;
import io.memoria.jutils.jtext.jackson.YamlJackson;
import io.memoria.recipes.app.dto.RecipeDto;
import io.memoria.recipes.app.dto.RecipeDtoV05;
import io.memoria.recipes.app.dto.RecipeDtoV06;
import io.memoria.recipes.core.recipe.Amount;
import io.memoria.recipes.core.recipe.Head;
import io.memoria.recipes.core.recipe.Ingredient;
import io.vavr.control.Option;
import reactor.core.scheduler.Schedulers;

public class Defaults {
  public static final FileUtils file;
  public static final Json json;
  public static final Yaml yaml;

  static {
    // File
    file = FileUtils.createDefault(Option.some("include:"), true, Schedulers.boundedElastic());
    // Json
    var jom = JacksonUtils.defaultJson();
    JacksonUtils.mixinPropertyFormat(jom, RecipeDto.class, Amount.class, Head.class, Ingredient.class);
    json = new JsonJackson(registerRecipeSubtypes(jom));
    // Yaml
    var yom = JacksonUtils.defaultYaml();
    JacksonUtils.mixinPropertyFormat(yom);
    yaml = new YamlJackson(yom);
  }

  private Defaults() {}

  private static ObjectMapper registerRecipeSubtypes(ObjectMapper om) {
    om.registerSubtypes(RecipeDtoV05.class, RecipeDtoV06.class);
    return om;
  }
}
