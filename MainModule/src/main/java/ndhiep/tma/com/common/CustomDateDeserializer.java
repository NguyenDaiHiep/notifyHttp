package ndhiep.tma.com.common;



import java.io.IOException;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 * The Class CustomDateSerializer.
 */
public class CustomDateDeserializer extends StdDeserializer<Date> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 9155978461710008511L;

//    /** The formatter. */
//    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
//
//    static {
//        formatter.setTimeZone(TimeZone.getDefault());
//    }

    /**
     * Instantiates a new custom date deserializer.
     */
    public CustomDateDeserializer() {
        this(null);
    }

    /**
     * Instantiates a new custom date deserializer.
     *
     * @param vc the vc
     */
    public CustomDateDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Date deserialize(JsonParser jsonparser, DeserializationContext context) throws IOException, JsonProcessingException {
        String date = jsonparser.getText();
//        try {
//            return formatter.parse(date);
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        }
        DateTime utc = new DateTime(date, DateTimeZone.UTC);
        return utc.toDate();
    }
}
