package io.memoria.recipes.app;

import io.memoria.jutils.jcore.id.Id;
import io.memoria.jutils.jcore.id.IdGenerator;
import io.memoria.jutils.jcore.id.SerialIdGenerator;
import io.memoria.jutils.jcore.id.UUIDGenerator;
import io.memoria.recipes.app.AppConfig.Server;
import io.memoria.recipes.app.controller.CategoryController;
import io.memoria.recipes.app.controller.RecipesController;
import io.memoria.recipes.app.controller.VarzController;
import io.memoria.recipes.app.dto.RecipeDto;
import io.memoria.recipes.app.dto.RecipeDtoV5;
import io.memoria.recipes.app.dto.RecipeDtoV6;
import io.memoria.recipes.core.recipe.Recipe;
import io.memoria.recipes.core.repo.mem.RecipeMemRepo;
import io.memoria.recipes.core.service.CategoryService;
import io.memoria.recipes.core.service.RecipeService;
import io.netty.handler.ssl.SslContextBuilder;
import io.vavr.Function1;
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
  public enum API {
    V5,
    V6
  }

  public final HttpServer httpServer;
  // Services
  private final RecipeService recipeService;
  private final CategoryService categoryService;

  public AppDependencies(AppConfig appConfig) {
    var repo = new RecipeMemRepo(createDB());
    this.recipeService = new RecipeService(repo, repo);
    this.categoryService = new CategoryService(repo);

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
    var varzCont = new VarzController();
    var recCont = new RecipesController(jsonV5, toDto(API.V5), recipeService);
    var catCont = new CategoryController(jsonV5, categoryService);
    return r -> r.get(appConfig.apiV5().varz(), varzCont)
                 .get(appConfig.apiV5().categories(), catCont)
                 .get(appConfig.apiV5().recipes(), recCont::recipes)
                 .get(appConfig.apiV5().recipesSearch(), recCont::recipesSearch)
                 .post(appConfig.apiV5().recipes(), recCont::createRecipe);
  }

  private Consumer<HttpServerRoutes> routesV6(AppConfig appConfig) {
    var varzCont = new VarzController();
    var recCont = new RecipesController(jsonV6, toDto(API.V6), recipeService);
    var catCont = new CategoryController(jsonV6, categoryService);
    return r -> r.get(appConfig.apiV6().varz(), varzCont)
                 .get(appConfig.apiV6().categories(), catCont)
                 .get(appConfig.apiV6().recipes(), recCont::recipes)
                 .get(appConfig.apiV6().recipesSearch(), recCont::recipesSearch)
                 .post(appConfig.apiV6().recipes(), recCont::createRecipe);
  }

  // Todo for Id creation instead of memory based ID which uses the title
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

  private static Function1<Recipe, RecipeDto> toDto(API api) {
    if (api.equals(API.V5))
      return RecipeDtoV5::new;
    else
      return RecipeDtoV6::new;
  }
}
