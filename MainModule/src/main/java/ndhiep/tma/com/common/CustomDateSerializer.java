package ndhiep.tma.com.common;


import java.io.IOException;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * The Class CustomDateSerializer.
 */
public class CustomDateSerializer extends StdSerializer<Date> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 5715876674020926094L;

    /**
     * Instantiates a new custom date serializer.
     */
    public CustomDateSerializer() {
        this(null);
    }

    /**
     * Instantiates a new custom date serializer.
     *
     * @param T the t
     */
    public CustomDateSerializer(Class<Date> T) {
        super(T);
    }

    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider arg2) throws IOException, JsonProcessingException {
        DateTime utc = new DateTime(value, DateTimeZone.UTC);
        gen.writeString(utc.toString());
    }
}
