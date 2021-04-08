package io.memoria.recipes.app;

import io.memoria.jutils.jcore.config.ConfigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

import static io.memoria.jutils.jcore.vavr.ReactorVavrUtils.toMono;

public class App {
  private static final Logger LOG = LoggerFactory.getLogger(App.class.getName());
  private static final String DEFAULT_CONFIG_PATH = "app.yaml";

  public static void main(String[] args) {
    LOG.debug("DEBUG logging is on");
    LOG.info("INFO logging is on");
    LOG.warn("WARNING logging is on");
    LOG.error("ERROR logging is on");
    var configPath = ConfigUtils.readMainArgs(args).get("--config").getOrElse(DEFAULT_CONFIG_PATH);
    var appConfig = Defaults.file.read(configPath)
                                 .flatMap(f -> toMono(Defaults.yaml.deserialize(f, AppConfig.class)))
                                 .block();
    assert appConfig != null;
    new AppDependencies(appConfig).httpServer.bindNow(Duration.ofSeconds(10)).onDispose().block();
    LOG.info("Server Shutdown!");
  }
}
