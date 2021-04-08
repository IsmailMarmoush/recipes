package io.memoria.recipes.app;

import io.memoria.recipes.app.dto.RecipeDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openapi4j.core.exception.ResolutionException;
import org.openapi4j.core.validation.ValidationException;
import org.openapi4j.core.validation.ValidationResults;
import org.openapi4j.parser.OpenApi3Parser;
import org.openapi4j.parser.model.v3.OpenApi3;
import org.openapi4j.parser.validation.v3.OpenApi3Validator;

class ApiV6V5Test {
  private final OpenApi3 api;

  public ApiV6V5Test() throws ResolutionException, ValidationException {
    var openApiFile = ClassLoader.getSystemResource("API_V5.yaml");
    api = new OpenApi3Parser().parse(openApiFile, false);
  }

  @Test
  @DisplayName("OpenApi syntax should be valid")
  void openApiSyntax() throws ValidationException {
    ValidationResults results = OpenApi3Validator.instance().validate(api);
    Assertions.assertTrue(results.isValid());
  }

  @Test
  @DisplayName("Objects Schema should be valid")
  void schema() throws Exception {
    OpenAPITestUtils.schemaValidator(api, "model/recipe/RecipeDtoV5.json", RecipeDto.class.getSimpleName());
  }
}
