package ndhiep.tma.com.listener.common;


import java.io.IOException;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jackson.NodeType;
import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.format.AbstractFormatAttribute;
import com.github.fge.jsonschema.format.FormatAttribute;
import com.github.fge.jsonschema.library.DraftV4Library;
import com.github.fge.jsonschema.library.Library;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.processors.data.FullData;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.google.common.collect.ImmutableList;

public class ListenerJsonValidation {
    /** The Constant JSON_APIO_NTT_SCHEMA_IDENTIFIER. */
    private static final String JSON_APIO_NTT_SCHEMA_IDENTIFIER = "http://com.tma.vn/schema#";

    /** The Constant TMF_API_DATETIME_FORMAT_NAME. */
    private static final String TMF_API_DATETIME_FORMAT_NAME = "tmf-apis-date-time";

    /** The Constant logger. */
    private final static Logger logger = LoggerFactory.getLogger(ListenerJsonValidation.class);

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
        /*
         * Build a new library with our added format attribute
         */
        final Library library = DraftV4Library.get().thaw().addFormatAttribute(TMF_API_DATETIME_FORMAT_NAME, TMFDateTimeAttribute.getInstance()).freeze();

        /*
         * Build our dedicated validation configuration
         */
        final ValidationConfiguration cfg = ValidationConfiguration.newBuilder().addLibrary(JSON_APIO_NTT_SCHEMA_IDENTIFIER, library).freeze();

        final JsonSchemaFactory factory = JsonSchemaFactory.newBuilder().setValidationConfiguration(cfg).freeze();

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
            JsonSchema jsonSchema = ListenerJsonValidation.getSchemaNodeFromPath(schemaFile);
            JsonNode jsonNode = ListenerJsonValidation.getJsonNode(json);

            ProcessingReport report = jsonSchema.validate(jsonNode);
            if (!report.isSuccess()) {
                result = false;
                logger.error(report.toString());
            }
            logger.debug("OUT - validate()");
        } catch (Exception e) {
            logger.error("Exception: ", e);
            result = false;
        }
        return result;
    }

    /**
     * Declare new format attribute
     */
    private static final class TMFDateTimeAttribute extends AbstractFormatAttribute {
        private static final List<String> FORMATS = ImmutableList.of("ProductOrder");
        private static final FormatAttribute INSTANCE = new TMFDateTimeAttribute();

        private TMFDateTimeAttribute() {
            super(TMF_API_DATETIME_FORMAT_NAME, NodeType.STRING);
        }

        public static FormatAttribute getInstance() {
            return INSTANCE;
        }

        @Override
        public void validate(final ProcessingReport report, final MessageBundle bundle, final FullData data) throws ProcessingException {
            final String value = data.getInstance().getNode().textValue();
            try {
                new DateTime(value, DateTimeZone.UTC);
            } catch (IllegalArgumentException ignored) {
                report.error(newMsg(data, bundle, "err.format.invalidDate").putArgument("value", value).putArgument("expected", FORMATS));
            }
        }
    }
}
