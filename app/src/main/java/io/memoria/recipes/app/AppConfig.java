package io.memoria.recipes.app;

public record AppConfig(Generator generator, ApiV5 apiV5, ApiV6 apiV6, Server server) {

  public record ApiV5(String root, String varz, String categories, String recipes, String recipesSearch) {}

  public record ApiV6(String root, String varz, String categories, String recipes, String recipesSearch) {}

  public record Generator(String type) {}

  public record Server(String host,
                       int port,
                       int maxStartupTime,
                       boolean isWiretapping,
                       boolean isSecure,
                       String sslCertificate,
                       String privateKey) {
    public String url() {
      return host + ":" + port;
    }
  }
}
