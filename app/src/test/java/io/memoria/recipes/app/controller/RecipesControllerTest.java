package io.memoria.recipes.app.controller;

import io.memoria.jutils.jweb.netty.NettyClientUtils;
import io.memoria.recipes.app.AppConfig;
import io.memoria.recipes.app.AppDependencies;
import io.memoria.recipes.app.TestResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import reactor.netty.DisposableServer;
import reactor.test.StepVerifier;

import java.time.Duration;

import static io.memoria.recipes.app.Defaults.file;
import static io.memoria.recipes.app.Defaults.yaml;
import static io.memoria.recipes.app.TestResource.allRecipes;
import static io.memoria.recipes.app.TestResource.vegetableRecipes;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;

class RecipesControllerTest {
  private static final String omeletteRecipe = TestResource.recipeOmeletteV6;
  private static String serverUrl;
  private static String recipesPath;
  private static String recipesSearchPath;
  private static DisposableServer disposableServer;

  @AfterAll
  static void afterAll() {
    disposableServer.disposeNow();
  }

  @BeforeAll
  static void beforeAll() {
    var conf = file.read("test.yaml").block();
    var appConfig = yaml.deserialize(conf, AppConfig.class).get();
    var dependencies = new AppDependencies(appConfig);
    serverUrl = appConfig.server().url();
    recipesPath = appConfig.apiV6().recipes();
    recipesSearchPath = appConfig.apiV6().recipesSearch();
    disposableServer = dependencies.httpServer.bindNow(Duration.ofSeconds(10));
  }

  @Test
  void createRecipe() {
    // when
    var result = NettyClientUtils.post(omeletteRecipe, serverUrl, recipesPath);
    // Then
    StepVerifier.create(result)
                .expectNextMatches(t -> t._1.equals(OK) && t._2.equals("omelette"))
                .expectComplete()
                .verify();
  }

  @Test
  void recipes() {
    // when
    var all = NettyClientUtils.get(serverUrl, recipesPath);
    var veg = NettyClientUtils.get(serverUrl, recipesPath + "?category=Vegetables");
    // Then
    StepVerifier.create(all)
                .expectNextMatches(t -> t._1.equals(OK) && t._2.equals(allRecipes))
                .expectComplete()
                .verify();
    StepVerifier.create(veg)
                .expectNextMatches(t -> t._1.equals(OK) && t._2.equals(vegetableRecipes))
                .expectComplete()
                .verify();
  }

  @Test
  void search() {
    // when
    var all = NettyClientUtils.get(serverUrl, recipesSearchPath);
    var veg = NettyClientUtils.get(serverUrl, recipesSearchPath + "?text=Zucchini");
    // Then
    StepVerifier.create(all)
                .expectNextMatches(t -> t._1.equals(OK) && t._2.equals(allRecipes))
                .expectComplete()
                .verify();
    StepVerifier.create(veg)
                .expectNextMatches(t -> t._1.equals(OK) && t._2.equals(vegetableRecipes))
                .expectComplete()
                .verify();
  }
}
