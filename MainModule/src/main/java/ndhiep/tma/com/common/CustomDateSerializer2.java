package ndhiep.tma.com.common;



import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * The Class CustomDateSerializer.
 */
public class CustomDateSerializer2 extends StdSerializer<Date> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 5715876674020926094L;

    /** The formatter. */
    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    static {
        formatter.setTimeZone(TimeZone.getDefault());
    }

    /**
     * Instantiates a new custom date serializer.
     */
    public CustomDateSerializer2() {
        this(null);
    }

    /**
     * Instantiates a new custom date serializer.
     *
     * @param T the t
     */
    public CustomDateSerializer2(Class<Date> T) {
        super(T);
    }

    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider arg2) throws IOException, JsonProcessingException {
        gen.writeString(formatter.format(value));
    }
}
