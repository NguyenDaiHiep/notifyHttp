package ndhiep.tma.com.common;



import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

public class Utils {
    private final static Logger logger = LoggerFactory.getLogger(Utils.class);

    /** The Constant mapper. */
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.registerModule(new JodaModule());
        mapper.configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS , false);
    }

    public static Object getObjectFromJson(String json, Class<?> T) {
        logger.debug("IN - getObjectFromJson()");

        if (json == null) {
            return null;
        }
        Object object = null;
        try {
            object = mapper.readValue(json, T);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        logger.debug("OUT - getObjectFromJson()");
        return object;
    }

    public static String trimJsonData(String jsonData) {
        logger.debug("IN - trimJsonData");
        if (jsonData == null) {
            return null;
        }
        String trimmingJson;
        JsonNode df;
        try {
            df = mapper.readValue(jsonData, JsonNode.class);
            trimmingJson = df.toString();
        } catch (Exception e) {
            logger.warn(e.getMessage());
            trimmingJson = jsonData;
        }
        return trimmingJson;
    }

    public static String parseObjectToJson(Object object) {
        logger.debug("IN - parseObjectToJson");
        if (object == null) {
            logger.debug("OUT - parseObjectToJson");
            return null;
        }
        String json = null;
        try {
            logger.debug("OUT - parseObjectToJson");
            json = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error("Exception: ", e);
        }
        return json;
    }

    public static ResponseEntity<?> createResponse(HttpStatus httpStatus, String href, Object data) {
        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity<Object>(data, responseHeaders, httpStatus);
    }

    public static Object mergeObject(Object destination, Object source, boolean isPatch) {
        logger.debug("IN - mergeObject(), Object: {}", destination.getClass().getSimpleName());
        // result
        Object result = null;
        try {
            Object objDB, objUpdate;

            // Get all fields of class
            Field[] fields = destination.getClass().getDeclaredFields();
            Class<?> sourceClass = source.getClass();

            for (int i = 0; i < fields.length; i++) {
                // Access to private field
                fields[i].setAccessible(true);

                // Is no update ?
                Field fieldSource = null;
                try {
                    fieldSource = sourceClass.getDeclaredField(fields[i].getName());
                    fieldSource.setAccessible(true);
                    if (fieldSource.get(source) == null && isPatch) {
                        continue;
                    } else if (fieldSource.get(source) == null && !isPatch) {
                        fields[i].set(destination, null);
                        continue;
                    }
                } catch (Exception e) {
                    // Field does not exist in Source object
                    logger.warn(e.getMessage());
                    continue;
                }

                // Is List<jp.co.ntt.apio.*> type
                boolean isList =
                        (fields[i].getGenericType().getTypeName().contains("List<ndhiep.tma.com.common") || fields[i].getGenericType().getTypeName()
                                .contains("List<ndhiep.tma.com.common.dto"));

                if (isList) {
                    // get the type as generic
                    ParameterizedType fieldGenericType = (ParameterizedType) fields[i].getGenericType();

                    // get it's first type parameter
                    Class<?> itemClass = (Class<?>) fieldGenericType.getActualTypeArguments()[0];

                    Field[] itemFields = itemClass.getDeclaredFields();

                    // Default: length is equal
                    List<Object> objDBList = (List<Object>) fields[i].get(destination);
                    List<Object> objUpdateList = (List<Object>) fieldSource.get(source);
                    List<Object> resultList = new ArrayList<Object>();

                    if (objDBList == null || objDBList.isEmpty()) {
                        for (int item = 0; item < objUpdateList.size(); item++) {
                            Object destItem = itemClass.newInstance();
                            Object resultItem = mergeChild(destItem, objUpdateList.get(item), itemFields, destination.getClass().getName(), isPatch);
                            if (resultItem == null) {
                                return null;
                            }
                            resultList.add(resultItem);
                        }
                        fields[i].set(destination, resultList);
                        continue;
                    }

                    // if size of list object update < size of list object in DB
                    boolean isRemove = objDBList.size() > objUpdateList.size();
                    int minSize = isRemove ? objUpdateList.size() : objDBList.size();
                    for (int item = 0; item < minSize; item++) {
                        // Is no update ?
                        if (objUpdateList.get(item) == null && isPatch) {
                            continue;
                        }
                        Object resultItem = mergeChild(objDBList.get(item), objUpdateList.get(item), itemFields, destination.getClass().getName(), isPatch);
                        if (resultItem == null) {
                            return null;
                        }
                        resultList.add(resultItem);
                    }
                    if (!isPatch && isRemove) {
                        fields[i].set(destination, resultList);
                        continue;
                    }
                    if (!isRemove) {
                        for (int item = minSize; item < objUpdateList.size(); item++) {
                            Object destItem = itemClass.newInstance();
                            Object resultItem = mergeChild(destItem, objUpdateList.get(item), itemFields, destination.getClass().getName(), isPatch);
                            if (resultItem == null) {
                                return null;
                            }
                            resultList.add(resultItem);
                        }
                    } else {
                        for (int item = minSize; item < objDBList.size(); item++) {
                            resultList.add(objDBList.get(item));
                        }
                    }
                    fields[i].set(destination, resultList);
                    continue;
                }

                // Check current field has children or not
                boolean checkType = fields[i].getType().getName().startsWith("ndhiep.tma.com");

                if (checkType) {
                    /**
                     * Is data from DB null ?
                     */
                    objDB = fields[i].get(destination);
                    objUpdate = fieldSource.get(source);
                    if (objDB == null) {
                        if (objUpdate != null) {
                            Object resultItem = fields[i].getType().newInstance();
                            resultItem =
                                    mergeChild(resultItem, objUpdate, resultItem.getClass().getDeclaredFields(), destination.getClass().getName(), isPatch);
                            if (resultItem == null) {
                                return null;
                            }
                            fields[i].set(destination, resultItem);
                        }
                        continue;
                    }

                    // Get all fields of child
                    Field[] childFields = objDB.getClass().getDeclaredFields();

                    // Merge child object
                    Object child = mergeChild(objDB, objUpdate, childFields, destination.getClass().getName(), isPatch);
                    if (child == null) {
                        return null;
                    }
                    fields[i].set(destination, child);
                } else {
                    // Get new value
                    Object value = fieldSource.get(source);

                    // Is value final ?
                    boolean isFinal = Modifier.isFinal(fields[i].getModifiers());

                    // Set new value if not null or not final
                    if (value != null && !isFinal) {
                        fields[i].set(destination, value);
                    }
                }
            }
            result = destination;
        } catch (Exception e) {
            logger.error("Exception: ", e);
            return null;
        }
        logger.debug("OUT - mergeObject()");
        // Return merged object
        return result;
    }

    private static Object mergeChild(Object destination, Object source, Field[] fields, String parentObject, boolean isPatch) {
        Object result = null;
        try {
            Object objDB, objUpdate;
            Class<?> sourceClass = source.getClass();
            for (int i = 0; i < fields.length; i++) {
                // Access to private field
                fields[i].setAccessible(true);

                // Is no update ?
                Field fieldSource = null;
                try {
                    fieldSource = sourceClass.getDeclaredField(fields[i].getName());
                    fieldSource.setAccessible(true);
                    if (fieldSource.get(source) == null && isPatch) {
                        continue;
                    } else if (fieldSource.get(source) == null && !isPatch) {
                        fields[i].set(destination, null);
                        continue;
                    }
                } catch (Exception e) {
                    // Field does not exist in Source object
                    logger.warn(e.getMessage());
                    continue;
                }

                // Is List<jp.co.ntt.apio.*> type
                boolean isList =
                        (fields[i].getGenericType().getTypeName().contains("List<ndhiep.tma.com.common") || fields[i].getGenericType().getTypeName()
                                .contains("List<ndhiep.tma.com.common.dto"));

                if (isList) {
                    // get the type as generic
                    ParameterizedType fieldGenericType = (ParameterizedType) fields[i].getGenericType();

                    // get it's first type parameter
                    Class<?> itemClass = (Class<?>) fieldGenericType.getActualTypeArguments()[0];

                    Field[] itemFields = itemClass.getDeclaredFields();

                    // Default: length is equal
                    List<Object> objDBList = (List<Object>) fields[i].get(destination);
                    List<Object> objUpdateList = (List<Object>) fieldSource.get(source);
                    List<Object> resultList = new ArrayList<Object>();

                    // If DB is null or empty
                    if (objDBList == null || objDBList.isEmpty()) {
                        for (int item = 0; item < objUpdateList.size(); item++) {
                            Object destItem = itemClass.newInstance();
                            Object resultItem = mergeChild(destItem, objUpdateList.get(item), itemFields, destination.getClass().getName(), isPatch);
                            if (resultItem == null) {
                                return null;
                            }
                            resultList.add(resultItem);
                        }
                        fields[i].set(destination, resultList);
                        continue;
                    }

                    // if size of list object update < size of list object in DB
                    boolean isRemove = objDBList.size() > objUpdateList.size();
                    int minSize = isRemove ? objUpdateList.size() : objDBList.size();
                    for (int item = 0; item < minSize; item++) {
                        // Is no update ?
                        if (objUpdateList.get(item) == null && isPatch) {
                            continue;
                        }
                        Object resultItem = mergeChild(objDBList.get(item), objUpdateList.get(item), itemFields, destination.getClass().getName(), isPatch);
                        if (resultItem == null) {
                            return null;
                        }
                        resultList.add(resultItem);
                    }
                    if (!isPatch && isRemove) {
                        fields[i].set(destination, resultList);
                        continue;
                    }
                    if (!isRemove) {
                        for (int item = minSize; item < objUpdateList.size(); item++) {
                            Object destItem = itemClass.newInstance();
                            Object resultItem = mergeChild(destItem, objUpdateList.get(item), itemFields, destination.getClass().getName(), isPatch);
                            if (resultItem == null) {
                                return null;
                            }
                            resultList.add(resultItem);
                        }
                    } else {
                        for (int item = minSize; item < objDBList.size(); item++) {
                            resultList.add(objDBList.get(item));
                        }
                    }
                    fields[i].set(destination, resultList);
                    continue;
                }

                // Check current field has children or not
                boolean checkType = (fields[i].getType().getName().startsWith("ndhiep.tma.com"));

                if (checkType) {
                    /**
                     * Is data from DB null ?
                     */
                    objDB = fields[i].get(destination);
                    objUpdate = fieldSource.get(source);
                    if (objDB == null) {
                        if (objUpdate != null) {
                            Object resultItem = fields[i].getType().newInstance();
                            resultItem =
                                    mergeChild(resultItem, objUpdate, resultItem.getClass().getDeclaredFields(), destination.getClass().getName(), isPatch);
                            if (resultItem == null) {
                                return null;
                            }
                            fields[i].set(destination, resultItem);
                        }
                        continue;
                    }

                    // Get all fields of child
                    Field[] childFields = objDB.getClass().getDeclaredFields();

                    // Merge child object
                    Object child = mergeChild(objDB, objUpdate, childFields, destination.getClass().getName(), isPatch);
                    if (child == null) {
                        return null;
                    }
                    fields[i].set(destination, child);
                } else {
                    // Get new value
                    Object value = fieldSource.get(source);

                    // Is value final ?
                    boolean isFinal = Modifier.isFinal(fields[i].getModifiers());

                    // Set new value if not null or not final
                    if (value != null && !isFinal) {
                        fields[i].set(destination, value);
                    }
                }
            }
            result = destination;
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        // Return merged object
        return result;
    }
}