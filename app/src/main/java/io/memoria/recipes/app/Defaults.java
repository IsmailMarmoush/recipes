package io.memoria.recipes.app;

import io.memoria.jutils.jcore.file.FileUtils;
import io.memoria.jutils.jcore.text.Json;
import io.memoria.jutils.jcore.text.Yaml;
import io.memoria.jutils.jtext.jackson.JacksonUtils;
import io.memoria.jutils.jtext.jackson.JsonJackson;
import io.memoria.jutils.jtext.jackson.YamlJackson;
import io.memoria.recipes.app.dto.RecipeDto;
import io.memoria.recipes.app.dto.RecipeDtoV5;
import io.memoria.recipes.app.dto.RecipeDtoV6;
import io.memoria.recipes.core.recipe.Head;
import io.vavr.control.Option;
import reactor.core.scheduler.Schedulers;

public class Defaults {
  public static final FileUtils file;
  public static final Json jsonV6 = createJsonV6();
  public static final Json jsonV5 = createJsonV5();
  public static final Yaml yaml;

  static {
    // File
    file = FileUtils.createDefault(Option.some("#{include}:"), true, Schedulers.boundedElastic());
    // Yaml
    var yom = JacksonUtils.defaultYaml();
    JacksonUtils.mixinPropertyFormat(yom);
    yaml = new YamlJackson(yom);
  }

  private Defaults() {}

  private static Json createJsonV5() {
    var jomV5 = JacksonUtils.defaultJson();
    JacksonUtils.mixinPropertyFormat(jomV5, RecipeDto.class, Head.class);
    jomV5.registerSubtypes(RecipeDtoV5.class);
    return new JsonJackson(jomV5);
  }

  private static Json createJsonV6() {
    var jomV6 = JacksonUtils.defaultJson();
    JacksonUtils.mixinPropertyFormat(jomV6, RecipeDto.class, Head.class);
    jomV6.registerSubtypes(RecipeDtoV6.class);
    return new JsonJackson(jomV6);
  }
}
