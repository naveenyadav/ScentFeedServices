package com.scent.feedservice.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.alcs.data.RequestData;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.InvalidPathException;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

/**
 * This class file is used for defining the JSON utility functions. This class
 * has below methods convertObjectToString addKeyValuePairToJson
 * convertObjectToJavaObject convertJavaObjectToJsonObject
 * convertStringToJsonObjcet
 * 
 * @author bnagaraju2
 * 
 */
public final class JsonUtil {
    private static final Logger LOG = LogManager.getLogger(JsonUtil.class);
    private static ObjectMapper mapper = new ObjectMapper();
    static {
        mapper = mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapper = mapper.enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL);
        mapper = mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper = mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    private JsonUtil() {
        super();
    }

    /**
     * This API converts java object to structured json String
     * 
     * @param jsonResponse
     *            Input java Object to covert to json string.
     * @return String Returns json string.
     */
    public static String convertObjectToString(Object jsonResponse) {
        return convertObjectToString(jsonResponse, null);
    }

    /**
     * This API converts java object to structured json String.
     *
     * @param jsonResponse
     *            Input java Object to covert to json string.
     * @param filterProvider
     *            the filter provider
     * @return String Returns json string.
     */
    public static String convertObjectToString(Object jsonResponse, FilterProvider filterProvider) {
        String returnString = StringUtils.EMPTY;
        // create ObjectMapper instance
        ObjectMapper mapper = new ObjectMapper();
        FilterProvider filters = filterProvider;
        if (null == filters) {
            filters = new SimpleFilterProvider().setFailOnUnknownId(Boolean.TRUE);
        }
        mapper.setFilterProvider(filters);

        try {

            // if the jsonResponse is empty, throw exception
            if (null == jsonResponse || CommonUtil.isNullTrimmedString(jsonResponse.toString())) {
                throw new IllegalArgumentException("IllegalArgumentException  in convertObjectToString");
            } else {
                // else serialize the jsonResponse using ObjectMapper
                returnString = mapper.writeValueAsString(jsonResponse);

                // check if string has html tags
                boolean hasHtmlTags = CommonUtil.hasHtmlTags(returnString);
                if (hasHtmlTags) {
                    returnString = returnString.replace(Constants.ESCAPE_DOUBLE_QUOTE, Constants.SINGLE_QUOTES);

                } else {

                    returnString = returnString
                            .replace(Constants.ESCAPE_DOUBLE_QUOTE, Constants.HTML_ENCODE_DOUBLE_QUOTE)
                            .replace(Constants.ESCAPE_BACK_SLASH, Constants.HTML_ENCODE_BACK_SLASH);
                }
            }

        } catch (JsonGenerationException e) {
            LoggerUtil.error(LOG, String.format(
                    "JsonGenerationException in convertObjectToString for jsonResponse: [%s]", jsonResponse), e);
        } catch (JsonMappingException e) {
            LoggerUtil.error(LOG,
                    String.format("JsonMappingException in convertObjectToString for jsonResponse: [%s]", jsonResponse),
                    e);
        } catch (IOException e) {
            LoggerUtil.error(LOG,
                    String.format("IOException in convertObjectToString for jsonResponse: [%s]", jsonResponse), e);
        }
        return returnString.replaceAll(Constants.REGEX_DOUBLE_BACK_SLASH, StringUtils.EMPTY);
    }

    /**
     * This method will convert string to JSONObject
     * 
     * @param input
     *            Input string to convert to json Object.
     * @return JSONObject Returns json object.
     */
    @SuppressWarnings("unchecked")
    public static JSONObject convertStringToJsonObject(String input) {
        JSONObject jsonObj = null;
        JSONArray jsonArray = null;
        try {
            JSONParser parser = new JSONParser();
            Object object = parser.parse(input);
            if (object instanceof JSONObject) {
                jsonObj = (JSONObject) parser.parse(input);
            } else if (object instanceof JSONArray) {
                jsonArray = (JSONArray) parser.parse(input);
                jsonObj = new JSONObject();
                jsonObj.put(Constants.ITEMS, jsonArray);
            }

        } catch (ParseException e) {
            LoggerUtil.error(LOG, String.format("JsonParseException in convertObjectToString, for input: [%s]", input),
                    e);
        }
        return jsonObj;
    }

    /**
     * Converts json string to java given java object
     * 
     * @param json
     *            json string
     * @param clazz
     *            class to be converted to
     * @return convereted java object
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static Object convertJsonToJavaObject(String json, Class clazz) {
        Object obj = null;
        if (StringUtils.isNotEmpty(json)) {
            try {
                obj = mapper.readValue(json, clazz);
            } catch (JsonParseException | JsonMappingException e) {
                LoggerUtil.error(LOG,
                        String.format(
                                "JsonParseException or JsonMappingException in JsonUtil for json: [%s],Class: [%s]",
                                json, clazz),
                        e);
            } catch (IOException e) {
                LoggerUtil.error(LOG, String.format("IOException in JsonUtil for json: [%s],Class: [%s]", json, clazz),
                        e);
            }
        }

        return obj;
    }

    /**
     * This method converts object to string and then to maps to java object.
     * 
     * @param obj
     *            objec to convert from
     * @param clazz
     *            class to convert to
     * @return java object
     */
    @SuppressWarnings("rawtypes")
    public static Object convertObjectToJavaObject(Object obj, Class clazz) {
        // convert the obj into string
        String string = JsonUtil.convertObjectToString(obj);
        // convert the string into JavaObject
        return JsonUtil.convertJsonToJavaObject(string, clazz);
    }

    /**
     * converts java object to json object
     * 
     * @param obj
     *            java object
     * @return JSONObject
     */
    public static JSONObject convertJavaObjectToJsonObject(Object obj) {
        JSONObject jsonObj = null;
        if (null != obj) {
            try {
                String jsonString = mapper.writeValueAsString(obj);
                if (StringUtils.isNotEmpty(jsonString)) {
                    jsonObj = convertStringToJsonObject(jsonString);
                }
            } catch (JsonParseException | JsonMappingException e) {
                LoggerUtil.error(LOG,
                        String.format(
                                "JsonParseException | JsonMappingException in JsonUtil.convertJavaObjectToJsonObject for input obj [%s]",
                                obj),
                        e);
            } catch (IOException e) {
                LoggerUtil.error(LOG, String.format("IOException in JsonUtil for input obj [%s]", obj), e);
            }
        }
        return jsonObj;
    }

    /**
     * This method converts json string of map format to map.
     * 
     * @param jsonString
     *            mapjson string
     * @return map returns a map
     */
    public static Map<String, String> convertJsonStringToMap(String jsonString) {
        Map<String, String> map = null;
        if (StringUtils.isNotEmpty(jsonString)) {
            try {
                // read the value from jsonString into Map
                map = mapper.readValue(jsonString, new TypeReference<Map<String, String>>() {
                });
            } catch (IOException e) {
                LoggerUtil.error(LOG, String.format("IOException in JsonUtil for input jsonString [%s]", jsonString),
                        e);
            }
        }
        return map;
    }

    /**
     * This method converts json string to JSONArray.
     * 
     * @param input
     *            input json String
     * @return map returns a JSONArray
     */
    public static JSONArray convertListToJsonArray(String input) {
        JSONArray jsonArr = null;
        try {
            JSONParser parser = new JSONParser();
            // parse the input string
            jsonArr = (JSONArray) parser.parse(input);

        } catch (ParseException e) {
            LoggerUtil.error(LOG, String.format("JsonParseException in convertObjectToString for input [%s]", input),
                    e);
        }
        return jsonArr;
    }

    /**
     * This method will chcek provided String is valid json or not
     * 
     * @param input
     *            Input string to convert to json Object.
     * @return return true if valid json.
     */
    public static boolean isValidJson(String input) {
        boolean isValidjson = true;
        if (null == input) {
            LoggerUtil.error(LOG, "Input json string is NULL and cannot be parsed", new ParseException(0));
            isValidjson = false;
        } else {
            try {
                JSONParser parser = new JSONParser();
                // parse the input string
                parser.parse(input);
            } catch (ParseException e) {
                LoggerUtil.error(LOG,
                        String.format("JsonParseException in convertObjectToString for input [%s]", input), e);
                isValidjson = false;
            }
        }
        return isValidjson;
    }

    /**
     * This method will chcek provided String is valid json or not.
     *
     * @param input
     *            Input string to convert to json Object.
     * @param requestData
     *            the request data
     * @return return true if valid json.
     */
    public static boolean isValidInlineFormResponseJson(String input, RequestData requestData) {
        boolean isValidjson = true;
        if (null == input) {
            LoggerUtil.error(LOG, "Input json string is NULL for template:"
                    + requestData.getParam(Constants.TEMPLATE_NAME) + " and cannot be parsed", new ParseException(0));
            isValidjson = false;
        } else {
            try {
                JSONParser parser = new JSONParser();
                // parse the input string
                parser.parse(input);
                LoggerUtil.info(LOG,
                        String.format(
                                "Error json response from inline services in isValidInlineFormResponseJson for input [%s]",
                                input));
            } catch (ParseException e) {
                isValidjson = false;
            }
        }
        return isValidjson;
    }

    /**
     * Read the json string data and converts the same into JsonNode.In case of
     * any parsing error, it wraps the same in {@link JSONException} and throws
     * it to the calling method.
     * 
     * @param jsonObject
     *            JSONObject to be converted into JsonNode
     * @return converted JsonNode
     */
    public static JsonNode getRootJsonNode(JSONObject jsonObject) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = null;
        try {
            root = mapper.readTree(jsonObject.toJSONString());
        } catch (IOException ioEx) {
            throw new JSONException(ioEx);
        }
        return root;
    }

    /**
     * Read the json string data and converts the same into JsonNode.In case of
     * any parsing error, it wraps the same in {@link JSONException} and throws
     * it to the calling method.
     * 
     * @param jsonObject
     *            JSONObject to be converted into JsonNode
     * @return converted JsonNode
     */
    public static JsonNode convertJsonObjectTOJsonNode(JSONObject jsonObject) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = null;
        try {
            root = mapper.readTree(jsonObject.toJSONString());
        } catch (IOException ioEx) {
            throw new JSONException(ioEx);
        }
        return root;
    }

    // /**
    // * This method get the get likes response, converts to java object and
    // then
    // * builds likes map from likes list.Forms map with x_refid as key of get
    // * likes set map in the upcoming response
    // *
    // * @param responseData
    // * response data
    // * @return likes map
    // */
    // public static Map<String, String> getLikesMap(ResponseData responseData)
    // {
    // Map<String, String> likesMap = null;
    // // Get the response for get likes and convert it to map
    // if (CommonUtil.isSuccessResponse(responseData,
    // Constants.EVENTS_LIKES_LIST)) {
    // JSONObject data = ((JsonServiceResponse)
    // responseData.getDataMap().get(Constants.EVENTS_LIKES_LIST))
    // .getData();
    // if (data.containsKey(Constants.CONTENT)
    // && ((JSONObject)
    // data.get(Constants.CONTENT)).containsKey(Constants.LIKES)) {
    // UgcGetLikesDataVO getLikesDataVO = (UgcGetLikesDataVO) JsonUtil
    // .convertJsonToJavaObject(data.toJSONString(), UgcGetLikesDataVO.class);
    // if (null != getLikesDataVO) {
    // likesMap = getLikesDataVO.getContent().getLikesMap();
    // }
    // }
    // }
    //
    // return likesMap;
    // }

    /**
     * This method get the get likes response, filters based on xreftype
     * 
     * @param likesMap
     *            likes map
     * @param type
     *            type
     * @return likes map
     */

    public static Map<String, String> getFilteredLikesMap(Map<String, String> likesMap, String type) {
        Map<String, String> filteredLikesMap = likesMap;
        if (null != likesMap) {
            filteredLikesMap = new HashMap<String, String>();
            Iterator likesMapIterator = likesMap.entrySet().iterator();
            while (likesMapIterator.hasNext()) {
                Map.Entry eachEntry = (Map.Entry) likesMapIterator.next();
                if (eachEntry.getValue().toString().equals(type)) {
                    filteredLikesMap.put(eachEntry.getKey().toString(), eachEntry.getValue().toString());
                }
                likesMapIterator.remove();
            }
        }
        return filteredLikesMap;
    }

    /**
     * This method will give string value from JSON, if jsonPath to read that
     * string is defined
     * 
     * @param jsonObject
     *            Input json object
     * @param jsonPath
     *            Input json path
     * @return JSON string Returns string value.
     */
    public static String getStringValueFromJson(JSONObject jsonObject, String jsonPath) {
        String value = null;
        String jsonString = jsonObject.toString();
        try {
            DocumentContext context = JsonPath.parse(jsonString);
            value = context.read(jsonPath);
        } catch (PathNotFoundException e) {
            logError(LOG, jsonPath, e);
        }
        return value;
    }

    /**
     * This method will give integer value from JSON, if jsonPath to read
     * particular value is defined
     * 
     * @param jsonPath
     *            Input jsonPath.
     * @param jsonObject
     *            Input json object.
     * @return JSON Returns integer value.
     */
    public static int getIntValueFromJson(JSONObject jsonObject, String jsonPath) {
        String jsonString = jsonObject.toString();
        int value = Integer.MIN_VALUE;
        try {
            DocumentContext context = JsonPath.parse(jsonString);
            value = context.read(jsonPath);
        } catch (PathNotFoundException e) {
            logError(LOG, jsonPath, e);
        }
        return value;
    }

    /**
     * This method will give long value from JSON, if jsonPath to read
     * particular value is defined
     * 
     * @param jsonPath
     *            Input jsonPath.
     * @param jsonObject
     *            Input json object.
     * @return JSON Returns long integer value.
     */
    public static long getLongValueFromJson(JSONObject jsonObject, String jsonPath) {
        String jsonString = jsonObject.toString();
        long value = Long.MIN_VALUE;
        try {
            DocumentContext context = JsonPath.parse(jsonString);
            value = context.read(jsonPath);
        } catch (PathNotFoundException e) {
            logError(LOG, jsonPath, e);
        }
        return value;
    }

    /**
     * This method will give double value from JSON, if jsonPath to read that
     * string is defined
     * 
     * @param jsonPath
     *            Input jsonPath.
     * @param jsonObject
     *            Input json object.
     * @return JSON Returns double integer value.
     */
    public static double getDoubleValueFromJson(JSONObject jsonObject, String jsonPath) {
        String jsonString = jsonObject.toString();
        double value = Double.MIN_VALUE;
        try {
            DocumentContext context = JsonPath.parse(jsonString);
            value = context.read(jsonPath);
        } catch (PathNotFoundException e) {
            logError(LOG, jsonPath, e);
        }
        return value;
    }

    /**
     * This method will give string list value from JSON, if jsonPath to read
     * particular value is defined
     * 
     * @param jsonObject
     *            Input json object
     * @param jsonPath
     *            Input json path
     * @return JSON Returns list value.
     */
    public static List<String> getListFromJson(JSONObject jsonObject, String jsonPath) {
        String jsonString = jsonObject.toString();
        List<String> list = new ArrayList<>();
        try {
            DocumentContext context = JsonPath.parse(jsonString);
            list.addAll(context.read(jsonPath));
        } catch (PathNotFoundException e) {
            logError(LOG, jsonPath, e);
        } catch (InvalidPathException invalidPathEx) {
            logError(LOG, jsonPath, invalidPathEx);
        }
        return list;
    }

    /**
     * This method will give object list value from JSON, if jsonPath to read
     * particular value is defined
     * 
     * @param jsonObject
     *            {@link JSONObject} Input jsonObject
     * @param jsonPath
     *            Input jsonPath
     * @return JSON Returns list value.
     */
    public static List<Object> getObjectListFromJson(JSONObject jsonObject, String jsonPath) {
        String jsonString = jsonObject.toString();
        List<Object> list = new ArrayList<>();
        try {
            DocumentContext context = JsonPath.parse(jsonString);
            list.addAll(context.read(jsonPath));
        } catch (PathNotFoundException e) {
            logError(LOG, jsonPath, e);
        }
        return list;
    }

    /**
     * This method will give map value from JSON, if jsonPath to read particular
     * value is defined
     * 
     * @param jsonObject
     *            Input json object
     * @param jsonPath
     *            Input json path
     * @return JSON string Returns Map value.
     */
    public static Map<String, String> getMapFromJson(JSONObject jsonObject, String jsonPath) {
        String jsonString = jsonObject.toString();
        Map<String, String> map = new HashMap<>();
        try {
            DocumentContext context = JsonPath.parse(jsonString);
            if (null != context && null != context.read(jsonPath)) {
                map.putAll(context.read(jsonPath));
            }
        } catch (PathNotFoundException e) {
            logError(LOG, jsonPath, e);
        }
        return map;
    }

    /**
     * This method logs error message for JsonPath Api's
     * 
     * @param logger
     *            log4j logger
     * @param jsonPath
     *            json path
     * @param e
     *            exception
     */
    public static void logError(Logger logger, String jsonPath, Exception e) {
        LoggerUtil.error(logger, "PathNotFoundException. jsonpath or property :" + jsonPath + " missing in json!!", e,
                false);
    }

    /**
     * This method will give string value from JSON, if jsonPath to read that
     * string is defined
     * 
     * @param jsonObject
     *            Input json object
     * @param jsonPath
     *            Input json path
     * @return JSON string Returns string value.
     */
    public static String getStringFromJsonArray(JSONObject jsonObject, String jsonPath) {
        String val = null;
        net.minidev.json.JSONArray value = null;
        String jsonString = jsonObject.toString();
        try {
            DocumentContext context = JsonPath.parse(jsonString);
            value = context.read(jsonPath);
            if (!value.isEmpty()) {
                val = value.get(value.size() - 1).toString();
            }
        } catch (PathNotFoundException e) {
            logError(LOG, jsonPath, e);
        }
        return val;
    }

}
