package io.memoria.recipes.app;

import io.memoria.jutils.jcore.id.Id;
import io.memoria.jutils.jcore.id.IdGenerator;
import io.memoria.jutils.jcore.id.SerialIdGenerator;
import io.memoria.jutils.jcore.id.UUIDGenerator;
import io.memoria.recipes.app.AppConfig.Server;
import io.memoria.recipes.app.controller.CategoryController;
import io.memoria.recipes.app.controller.VarzController;
import io.memoria.recipes.app.dto.RecipeDto;
import io.memoria.recipes.core.recipe.Recipe;
import io.memoria.recipes.core.repo.mem.RecipeMemRepo;
import io.memoria.recipes.core.service.CategoryService;
import io.netty.handler.ssl.SslContextBuilder;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.server.HttpServer;
import reactor.netty.http.server.HttpServerRoutes;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import static io.memoria.recipes.app.Defaults.file;
import static io.memoria.recipes.app.Defaults.jsonV5;
import static io.memoria.recipes.app.Defaults.jsonV6;

public class AppDependencies {
  public final HttpServer httpServer;
  private final IdGenerator idGenerator;
  private final CategoryService categoryService;

  public AppDependencies(AppConfig appConfig) {
    this.idGenerator = createIdGenerator(appConfig.generator());
    this.categoryService = new CategoryService(new RecipeMemRepo(createDB()));
    this.httpServer = createServer(appConfig.server(), routesV5(appConfig).andThen(routesV6(appConfig)));
  }

  private ConcurrentHashMap<Id, Recipe> createDB() {
    var db = new ConcurrentHashMap<Id, Recipe>();
    // E
    var r1Str = file.read("recipes/30_Minute_Chili.json").block();
    var r2Str = file.read("recipes/Another_Zucchini_Dish.json").block();
    var r3Str = file.read("recipes/Amaretto_Cake.json").block();
    // T
    var r1 = jsonV5.deserialize(r1Str, RecipeDto.class).get().toRecipe();
    var r2 = jsonV5.deserialize(r2Str, RecipeDto.class).get().toRecipe();
    var r3 = jsonV6.deserialize(r3Str, RecipeDto.class).get().toRecipe();
    // L
    db.put(Id.of(r1.head().title()), r1);
    db.put(Id.of(r2.head().title()), r2);
    db.put(Id.of(r3.head().title()), r3);
    return db;
  }

  private Consumer<HttpServerRoutes> routesV5(AppConfig appConfig) {
    return r -> r.get(appConfig.apiV5().varz(), new VarzController())
                 .get(appConfig.apiV5().categories(), new CategoryController(jsonV5, categoryService));
  }

  private Consumer<HttpServerRoutes> routesV6(AppConfig appConfig) {
    return r -> r.get(appConfig.apiV6().varz(), new VarzController())
                 .get(appConfig.apiV6().categories(), new CategoryController(jsonV6, categoryService));
  }

  private static IdGenerator createIdGenerator(AppConfig.Generator generator) {
    return switch (generator.type()) {
      case "uuid" -> new UUIDGenerator();
      case "constant" -> () -> Id.of(0);
      default -> new SerialIdGenerator(new AtomicLong());
    };
  }

  private static HttpServer createServer(Server serverConfig, Consumer<HttpServerRoutes> routes) {
    var server = HttpServer.create()
                           .host(serverConfig.host())
                           .port(serverConfig.port())
                           .route(routes)
                           .wiretap(serverConfig.isWiretapping());
    if (serverConfig.isSecure()) {
      SslContextBuilder serverSSLContextBuilder = SslContextBuilder.forServer(new File(serverConfig.sslCertificate()),
                                                                              new File(serverConfig.privateKey()));
      return server.protocol(HttpProtocol.H2).secure(spec -> spec.sslContext(serverSSLContextBuilder));
    }
    return server;
  }
}
