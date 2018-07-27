package ndhiep.tma.com.common;


import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

public class DefaultJsonValidation {
    /** The Constant JSON_V4_SCHEMA_IDENTIFIER. */
    private static final String JSON_V4_SCHEMA_IDENTIFIER = "http://json-schema.org/draft-04/schema#";

    /** The Constant JSON_SCHEMA_IDENTIFIER_ELEMENT. */
    private static final String JSON_SCHEMA_IDENTIFIER_ELEMENT = "$schema";

    /** The Constant logger. */
    private final static Logger logger = LoggerFactory.getLogger(DefaultJsonValidation.class);

    /**
     * Gets the json node.
     *
     * @param jsonText the json text
     * @return the json node
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static JsonNode getJsonNode(String jsonText) throws IOException {
        return JsonLoader.fromString(jsonText);
    }

    /**
     * Gets the json node from path.
     *
     * @param path the path
     * @return the json node from path
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static JsonNode getJsonNodeFromPath(String path) throws IOException {
        return JsonLoader.fromPath(path);
    }

    /**
     * Gets the schema node from path.
     *
     * @param resource the path
     * @return the schema node from path
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws ProcessingException the processing exception
     */
    private static JsonSchema getSchemaNodeFromPath(String path) throws IOException, ProcessingException {
        final JsonNode schemaNode = getJsonNodeFromPath(path);
        return _getSchemaNode(schemaNode);
    }

    /**
     * Gets the schema node.
     *
     * @param jsonNode the json node
     * @return the json schema
     * @throws ProcessingException the processing exception
     */
    private static JsonSchema _getSchemaNode(JsonNode jsonNode) throws ProcessingException {
        final JsonNode schemaIdentifier = jsonNode.get(JSON_SCHEMA_IDENTIFIER_ELEMENT);
        if (null == schemaIdentifier) {
            ((ObjectNode) jsonNode).put(JSON_SCHEMA_IDENTIFIER_ELEMENT, JSON_V4_SCHEMA_IDENTIFIER);
        }

        final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
        return factory.getJsonSchema(jsonNode);
    }

    /**
     * Check json validation.
     *
     * @param schemaFile the schema file
     * @param json the json
     * @return the check result
     */
    public static boolean validate(String schemaFile, String json) {
        logger.debug("IN - validate()");
        logger.debug("Validate json with schema file: {}", schemaFile);

        boolean result = true;
        try {
            JsonSchema jsonSchema = DefaultJsonValidation.getSchemaNodeFromPath(schemaFile);
            JsonNode jsonNode = DefaultJsonValidation.getJsonNode(json);

            ProcessingReport report = jsonSchema.validate(jsonNode);
            if (!report.isSuccess()) {
                result = false;
            }
            logger.debug("OUT - validate()");
        } catch (Exception e) {
            logger.error("Exception: ", e);
            result = false;
        }
        return result;
    }
}
