package io.memoria.recipes.core.common;

public abstract class RecipeException extends Exception {
  public static class RecipeAlreadyExists extends RecipeException {
    public RecipeAlreadyExists() {
      this("A Recipe with the same title already exists");
    }

    public RecipeAlreadyExists(String message) {
      super(message);
    }
  }

  public RecipeException(String message) {
    super(message);
  }
}
