package io.memoria.recipes.app.controller;

import io.memoria.recipes.app.AppConfig;
import io.memoria.recipes.app.AppDependencies;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import reactor.netty.DisposableServer;
import reactor.test.StepVerifier;

import java.time.Duration;

import static io.memoria.jutils.jweb.netty.NettyClientUtils.get;
import static io.memoria.recipes.app.Defaults.file;
import static io.memoria.recipes.app.Defaults.yaml;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;

class VarzControllerTest {
  private static String serverUrl;
  private static String varzPath;
  private static AppDependencies deps;
  private static DisposableServer disposableServer;

  @AfterAll
  static void afterAll() {
    disposableServer.disposeNow();
  }

  @BeforeAll
  static void beforeAll() {
    var conf = file.read("test.yaml").block();
    var appConfig = yaml.deserialize(conf, AppConfig.class).get();
    deps = new AppDependencies(appConfig);
    serverUrl = appConfig.server().url();
    varzPath = appConfig.apiV6().varz();
    disposableServer = deps.httpServer.bindNow(Duration.ofSeconds(10));
  }

  @Test
  void varz() {
    // when
    var result = get(serverUrl, varzPath);
    // Then
    StepVerifier.create(result).expectNextMatches(t -> t._1.equals(OK)).expectComplete().verify();
  }
}
