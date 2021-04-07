package io.memoria.recipes.app;

import com.fasterxml.jackson.databind.JsonNode;
import io.memoria.recipes.app.dto.RecipeDtoV05;
import io.memoria.recipes.app.dto.RecipeDtoV06;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openapi4j.core.exception.DecodeException;
import org.openapi4j.core.exception.EncodeException;
import org.openapi4j.core.model.v3.OAI3;
import org.openapi4j.core.util.TreeUtil;
import org.openapi4j.core.validation.ValidationException;
import org.openapi4j.core.validation.ValidationResults;
import org.openapi4j.parser.OpenApi3Parser;
import org.openapi4j.parser.model.v3.OpenApi3;
import org.openapi4j.parser.validation.v3.OpenApi3Validator;
import org.openapi4j.schema.validator.ValidationContext;
import org.openapi4j.schema.validator.v3.SchemaValidator;

import java.net.URL;

class OpenApiSpecTest {
  private static OpenApi3 api;

  static {
    try {
      var openApiFile = ClassLoader.getSystemResource("api.yaml");
      api = new OpenApi3Parser().parse(openApiFile, false);
    } catch (Throwable e) {
      e.printStackTrace();
    }
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
    schemaValidator("model/recipe", RecipeDtoV05.class.getSimpleName());
    schemaValidator("model/recipe", RecipeDtoV06.class.getSimpleName());
  }

  private static URL getSchemaURL(String path, String schemaName) {
    return ClassLoader.getSystemResource("%s/%s.json".formatted(path, schemaName));
  }

  private static void schemaValidator(String path, String schemaName)
          throws EncodeException, DecodeException, ValidationException {
    JsonNode schemaNode = api.getComponents().getSchema(schemaName).toNode();
    JsonNode contentNode = TreeUtil.load(getSchemaURL(path, schemaName));
    var context = new ValidationContext<OAI3>(api.getContext());
    SchemaValidator schemaValidator = new SchemaValidator(context, null, schemaNode);
    schemaValidator.validate(contentNode);
  }
}
