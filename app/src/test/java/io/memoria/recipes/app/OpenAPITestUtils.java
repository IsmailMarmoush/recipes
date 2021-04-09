package io.memoria.recipes.app;

import com.fasterxml.jackson.databind.JsonNode;
import org.openapi4j.core.exception.DecodeException;
import org.openapi4j.core.exception.EncodeException;
import org.openapi4j.core.model.v3.OAI3;
import org.openapi4j.core.util.TreeUtil;
import org.openapi4j.core.validation.ValidationException;
import org.openapi4j.parser.model.v3.OpenApi3;
import org.openapi4j.schema.validator.ValidationContext;
import org.openapi4j.schema.validator.v3.SchemaValidator;

public class OpenAPITestUtils {

  public static void validateSchema(OpenApi3 api, String filePath, String schemaName)
          throws EncodeException, DecodeException, ValidationException {
    JsonNode schemaNode = api.getComponents().getSchema(schemaName).toNode();
    var url = ClassLoader.getSystemResource(filePath);

    JsonNode contentNode = TreeUtil.load(url);
    var context = new ValidationContext<OAI3>(api.getContext());
    SchemaValidator schemaValidator = new SchemaValidator(context, null, schemaNode);
    schemaValidator.validate(contentNode);
  }

  private OpenAPITestUtils() {}
}
