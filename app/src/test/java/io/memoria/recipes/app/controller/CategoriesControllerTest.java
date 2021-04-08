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
import static io.netty.handler.codec.http.HttpResponseStatus.OK;

class CategoriesControllerTest {
  private static final String categories = TestResource.categories;
  private static String serverUrl;
  private static String categoriesPath;
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
    categoriesPath = appConfig.apiV6().categories();
    disposableServer = dependencies.httpServer.bindNow(Duration.ofSeconds(10));
  }

  @Test
  void getCategories() {
    // when
    var result = NettyClientUtils.get(serverUrl, categoriesPath).doOnNext(t -> System.out.println(t._2));
    // Then
    StepVerifier.create(result)
                .expectNextMatches(t -> t._1.equals(OK) && t._2.equals(categories))
                .expectComplete()
                .verify();
  }
}
