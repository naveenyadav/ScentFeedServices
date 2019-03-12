package com.scent.feedservice.steps;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.springframework.context.ApplicationContext;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.map.PassiveExpiringMap;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;

import com.alcs.configuration.ApplicationContextProvider;
import com.alcs.data.JsonServiceResponse;
import com.alcs.data.RequestData;
import com.alcs.data.ResponseData;
import com.alcs.services.GlobalConfigService;

/**
 * This class file is used for defining the common utility functions.
 * 
 * @author bnagaraju2
 * 
 */
public final class CommonUtil {

    private static final Logger LOG = LogManager.getLogger(CommonUtil.class);
    private static final String HTML_PATTERN = Constants.HTML_PATTERN_REGEX;
    private static Pattern pattern = Pattern.compile(HTML_PATTERN);

    private CommonUtil() {
        super();
    }

    /**
     * This method is used to check if the HTTP status has error.
     * 
     * @param status
     *            Input parameter of type HttpStatus
     * @return boolean Returns true or false based on HttpStatus.
     */
    public static boolean isError(final HttpStatus status) {
        final HttpStatus.Series series = status.series();
        return HttpStatus.Series.CLIENT_ERROR.equals(series) || HttpStatus.Series.SERVER_ERROR.equals(series);
    }

    /**
     * This method is used to get Subtracted Milliseconds.
     * 
     * @param startTime
     *            Input Start time in milli seconds.
     * @return long Returns Subtracted Milliseconds.
     */
    public static long getSubtractedMilliseconds(final long startTime) {
        return System.currentTimeMillis() - startTime;
    }

   

    /**
     * Check if the given string is null, empty or all spaces.
     * 
     * @param value
     *            string to check
     * @return true if the string is null or trimmed has zero length.
     */
    public static boolean isNullTrimmedString(String value) {
        return value == null || StringUtils.isEmpty(value.trim()) || StringUtils.isBlank(value.trim())
                || Constants.NULL.equalsIgnoreCase(value);
    }

    /**
     * This method is used to return the bearer token from the available caching
     * implementation <code>PassiveExpiringMap</code>
     * 
     * @param tokensMap
     *            map of bearer tokens map
     * @param tokenKey
     *            bearer token key
     * @return returns the bearer token
     */
    public static String getBearerTokenFromCache(Map<String, PassiveExpiringMap<String, String>> tokensMap,
            String tokenKey) {

        String bearerToken = null;
        if (null != tokensMap && tokensMap.containsKey(tokenKey)) {
            Map<String, String> map = tokensMap.get(tokenKey);
            bearerToken = map.get(tokenKey);
        }
        return bearerToken;
    }

    /**
     * This method verifies if service is not having error response.
     * 
     * @param responseData
     *            response data
     * @param eventName
     *            name of event
     * @return is success boolean
     */
    public static boolean isSuccessResponse(ResponseData responseData, String eventName) {
        boolean isSuccessResponse = true;
        if (null == responseData || null == responseData.getData(eventName)) {
            isSuccessResponse = false;
        } else {
            JSONObject jsonObject = null;
            if (responseData.getData(eventName) instanceof JsonServiceResponse) {
                JsonServiceResponse jsonServiceResponse = (JsonServiceResponse) responseData.getData(eventName);
                jsonObject = jsonServiceResponse.getData();
            } else if (responseData.getData(eventName) instanceof JSONObject) {
                jsonObject = (JSONObject) responseData.getData(eventName);
            }
            if (null == jsonObject || jsonObject.containsKey(Constants.ERROR_STATUS_KEY)) {
                isSuccessResponse = false;
            }

        }

        return isSuccessResponse;
    }

    /**
     * This method is used to check if the service response contains error.
     * 
     * @param jsonServiceResponse
     *            the service response
     * @return isSuccessResponse the boolean value
     */
    public static boolean isSuccessResponse(JsonServiceResponse jsonServiceResponse) {
        boolean isSuccessResponse = true;

        if (null == jsonServiceResponse) {
            isSuccessResponse = false;
        } else {
            JSONObject jsonObject = jsonServiceResponse.getData();
            if (null == jsonObject || jsonObject.containsKey(Constants.ERROR_STATUS_KEY)) {
                isSuccessResponse = false;
            }
        }

        return isSuccessResponse;
    }

    /**
     * This method is used to return the JsonServiceResponse for the given data
     * and response time.
     * 
     * @param data
     *            json data
     * @param responseTime
     *            time taken by service
     * @return service response
     */
    public static JsonServiceResponse getServiceResponse(final JSONObject data, long responseTime) {
        JsonServiceResponse serviceResponse = new JsonServiceResponse();
        serviceResponse.setData(data);
        serviceResponse.setResponseTime(responseTime);
        return serviceResponse;
    }

    /**
     * This method is used to create the error response object. The error
     * response contains error status and error details (if query parameters
     * contains getException).
     * 
     * @param errorMessage
     *            the error message.
     * @param includeErrorDetails
     *            the flag to include error details.
     * @return errorResponse the error response object in JSON format.
     */
    @SuppressWarnings("unchecked")
    public static JSONObject getErrorResponse(String errorMessage, boolean includeErrorDetails) {
        JSONObject errorResponse = new JSONObject();
        String errormessage = errorMessage;
        errorResponse.put(Constants.ERROR_STATUS_KEY, Constants.ERROR_STATUS_VALUE);

        if (includeErrorDetails && StringUtils.isNotBlank(errormessage)) {
            errormessage = errormessage.replaceAll(Constants.DOUBLE_QUOTE, StringUtils.EMPTY);
            errorResponse.put(Constants.EXCEPTION_TEXT, errormessage);
        }

        return errorResponse;
    }

    /**
     * This method is used to create the error response object. The error
     * response contains error status and error details (if query parameters
     * contains getException).
     * 
     * @param errorMessage
     *            the error message.
     * @param includeErrorDetails
     *            the flag to include error details.
     * @param errorCode
     *            the statuscode of the error
     * @return errorResponse the error response object in JSON format.
     */
    @SuppressWarnings("unchecked")
    public static JSONObject getErrorResponse(String errorMessage, boolean includeErrorDetails, String errorCode) {
        JSONObject errorResponse = new JSONObject();
        String errormessage = errorMessage;
        errorResponse.put(Constants.ERROR_STATUS_KEY, Constants.ERROR_STATUS_VALUE);

        if (includeErrorDetails && StringUtils.isNotBlank(errormessage)) {
            errormessage = errormessage.replaceAll(Constants.DOUBLE_QUOTE, StringUtils.EMPTY);
            errorResponse.put(Constants.EXCEPTION_TEXT, errormessage);
        }
        populateErrorCodeInResp(errorCode, errorResponse);

        return errorResponse;
    }

    /**
     * This method is used to create the error response object. The error
     * response contains error status and error details (if query parameters
     * contains getException).
     * 
     * @param errorMessage
     *            the error message.
     * @param responseBody
     *            the responseBody in String format response body to be
     *            validated
     * @param includeErrorDetails
     *            the flag to include error details.
     * @param errorCode
     *            the statuscode of the error
     * @return errorResponse the error response object in JSON format.
     */
    @SuppressWarnings("unchecked")
    public static JSONObject getErrorResponse(String errorMessage, String responseBody, boolean includeErrorDetails,
            String errorCode) {
        JSONObject errorResponse = null;

        // Return error response from API.
        if (StringUtils.isNotBlank(responseBody)) {

            try {
                Object obj = new JSONParser().parse(responseBody);
                if (obj instanceof JSONObject) {
                    errorResponse = (JSONObject) obj;
                    errorResponse.put(Constants.ERROR_STATUS_KEY, Constants.ERROR_STATUS_VALUE);
                    populateErrorCodeInResp(errorCode, errorResponse);
                    errorResponse = setErrorMessage(errorMessage, errorResponse, includeErrorDetails);
                }

            } catch (org.json.simple.parser.ParseException e) {
                LoggerUtil.error(LOG,
                        String.format("JsonParseException in getErrorResponse while parsing {%s}", responseBody), e);
            }
        }

        // Return generic error response for unknown errors.
        if (errorResponse == null) {
            errorResponse = getErrorResponse(errorMessage, includeErrorDetails);
            populateErrorCodeInResp(errorCode, errorResponse);
        }

        return errorResponse;
    }

    /**
     * This method is used to create the error response object. The error
     * response contains error status and error details (if query parameters
     * contains getException).
     * 
     * @param errorMessage
     *            the error message.
     * @param responseBody
     *            the responseBody in String format response body to be
     *            validated
     * @param includeErrorDetails
     *            the flag to include error details.
     * @return errorResponse the error response object in JSON format.
     */
    @SuppressWarnings("unchecked")
    public static JSONObject getErrorResponse(String errorMessage, String responseBody, boolean includeErrorDetails) {
        JSONObject errorResponse = null;

        // Return error response from API.
        if (StringUtils.isNotBlank(responseBody)) {

            try {
                Object obj = new JSONParser().parse(responseBody);
                if (obj instanceof JSONObject) {
                    errorResponse = (JSONObject) obj;
                    errorResponse.put(Constants.ERROR_STATUS_KEY, Constants.ERROR_STATUS_VALUE);
                    errorResponse = setErrorMessage(errorMessage, errorResponse, includeErrorDetails);
                }

            } catch (org.json.simple.parser.ParseException e) {
                LoggerUtil.error(LOG,
                        String.format("JsonParseException in getErrorResponse while parsing {%s}", responseBody), e);
            }
        }

        // Return generic error response for unknown errors.
        if (errorResponse == null) {
            errorResponse = getErrorResponse(errorMessage, includeErrorDetails);
        }

        return errorResponse;
    }

    /**
     * This method populates the error code in the errorResponse
     * 
     * @param errorCode
     *            the statuscode of the error
     * @param errorResponse
     *            the error response object in JSON format
     */
    @SuppressWarnings("unchecked")
    private static void populateErrorCodeInResp(String errorCode, JSONObject errorResponse) {
        if (StringUtils.isNotBlank(errorCode)) {
            errorResponse.put(Constants.STATUS_CODE, errorCode);
        }
    }

    /**
     * This method is used to convert byte array to string value.
     * 
     * @param bytes
     *            the byte array
     * @return str the converted string value
     */
    public static String readBytesAsString(byte[] bytes) {
        String str = "";

        try {
            if (bytes != null && bytes.length > 0) {
                StringWriter writer = new StringWriter();
                IOUtils.copy(new ByteArrayInputStream(bytes), writer, Constants.ENCODING_UTF8);
                str = writer.toString().replaceAll(Constants.NULL_STRING, "");
            }
        } catch (IOException e) {
            LoggerUtil.error(LOG, "IOException in readBytesAsString", e);
        }
        return str;
    }

    /**
     * This method is used to set error message in error response.
     * 
     * @param errorMessage
     *            the error message.
     * @param errorResponse
     *            the error response object in JSON format.
     * @param includeErrorDetails
     *            the flag to include error details.
     * @return errorResponse the error response object in JSON format.
     */
    @SuppressWarnings("unchecked")
    private static JSONObject setErrorMessage(String errorMessage, JSONObject errorResponse,
            boolean includeErrorDetails) {
        if (includeErrorDetails && StringUtils.isNotBlank(errorMessage)) {
            String errMessage = errorMessage.replaceAll(Constants.DOUBLE_QUOTE, StringUtils.EMPTY);
            errorResponse.put(Constants.EXCEPTION_TEXT, errMessage);
        }
        return errorResponse;
    }

    /**
     * This method is used to set Log4j Thread Context
     * 
     * @param name
     *            the key to be set in thread context.
     * @param value
     *            the value to be set in thread context.
     */
    public static void setLog4jThreadContext(String name, String value) {
        ThreadContext.put(name, value);
    }

    /**
     * This method is used to return the server domain from current request.
     * 
     * @param request
     *            the HTTP servlet request object.
     * @return serverDomain the server domain.
     */
    public static String getServerDomain(HttpServletRequest request) {
        String serverDomain = "";

        if (request != null && StringUtils.isNotBlank(request.getRequestURL())) {
            serverDomain = StringUtils.substringBefore(XssSanitizerUtil.stripXSS(request.getRequestURL().toString()),
                    request.getContextPath());
        }

        return serverDomain;
    }

    /**
     * This method adds up the time taken by individual response and merging
     * response time.
     * 
     * @param responseData
     *            responeData
     * @param firstEvent
     *            first event
     * @param secondEvent
     *            second event
     * @param elapsedTime
     *            time for merging
     * @param isSequential
     *            did the service run sequential or parallel.
     * @return total time for service.
     */
    public static long collateTime(ResponseData responseData, String firstEvent, String secondEvent, long elapsedTime,
            boolean isSequential) {
        long totalTime;
        long firstEventResponseTime = Constants.ZERO_LONG;
        long secondEventResponseTime = Constants.ZERO_LONG;

        if (responseData.getData(firstEvent) != null) {
            firstEventResponseTime = ((JsonServiceResponse) responseData.getData(firstEvent)).getResponseTime();
        }
        if (responseData.getData(secondEvent) != null) {
            secondEventResponseTime = ((JsonServiceResponse) responseData.getData(secondEvent)).getResponseTime();
        }

        if (isSequential) {
            totalTime = firstEventResponseTime + secondEventResponseTime + elapsedTime;
        } else {
            long maxServiceTime;
            if (firstEventResponseTime > secondEventResponseTime) {
                maxServiceTime = firstEventResponseTime;
            } else {
                maxServiceTime = secondEventResponseTime;
            }
            totalTime = maxServiceTime + elapsedTime;
        }
        return totalTime;
    }

    /**
     * This method returns the value of the property in the given ResponseData
     * for the given event.
     * 
     * @param responseData
     *            the ResponseData object
     * @param eventName
     *            the name of the event
     * @param propertyName
     *            name of the property for which the value is required.
     * @return the value of the given property
     */
    public static String getResponseDataProperty(ResponseData responseData, String eventName, String propertyName) {

        String propertyValue = null;
        if (responseData != null && responseData.getDataMap() != null && responseData.getData(eventName) != null) {
            JsonServiceResponse serviceResponse = (JsonServiceResponse) responseData.getData(eventName);
            propertyValue = (String) serviceResponse.getData().get(propertyName);
        }
        return propertyValue;
    }

    /**
     * This method fetches the Loyalty Flag from globalConfigService and returns
     * the boolean flag.
     * 
     * @param brandName
     *            the name of the Brand.
     * @param globalConfigService
     *            service to fetch property values from config file
     * @return boolean Loyalty flag
     */
    public static boolean isLoyaltyEnabled(String brandName, GlobalConfigService globalConfigService) {
        Boolean loyaltyFlag = false;
        String loyaltyStatus = globalConfigService.getPropertyValueAsString(brandName, Constants.PROP_LOYALTY_FLAG);
        if (null != loyaltyStatus && StringUtils.isNotEmpty(loyaltyStatus) && Boolean.valueOf(loyaltyStatus)) {
            loyaltyFlag = true;
        }
        return loyaltyFlag;
    }

    /**
     * This method will return the URL Constant based on Loyalty User Type. <br>
     * If the event is called from Rewards Gallery Showcase, the URL will be
     * always Non-Enrolled URL.<br>
     * If the event is called from Reward Detail, the Reward Detail URL is
     * returned.
     * 
     * @param requestData
     *            The RequestData object
     * @param paramsMap
     *            The Parameter map
     * @return URL value
     */
    public static String getUrlConstantByLoyaltyUserType(RequestData requestData, Map<String, String> paramsMap) {
        String userType = getDecryptedLoyaltyStatus(requestData);
        String urlConstant = StringUtils.EMPTY;

        boolean showcaseFlag = false;

        if (paramsMap.containsKey(Constants.TYPE) && paramsMap.get(Constants.TYPE).equals(Constants.SHOWCASE)) {
            showcaseFlag = true;
        }

        if (!showcaseFlag && Constants.LSTATUS_ENROLLED.equalsIgnoreCase(userType)) {
            if (paramsMap.containsKey(Constants.REWARD_CODE)) {
                // URL to get Rewards Detail components (only for enrolled user)
                urlConstant = Constants.LOYALTY_REWARDS_DETAIL_URL;
            } else {
                // URL to get Rewards Catalog and Wishlist components (only for
                // enrolled user)
                urlConstant = Constants.LOYALTY_REWARDS_ENROLLED_URL;
            }
        } else {
            // URL to get Rewards Catalog for Non-enrolled users and Most
            // Popular Items for all users
            urlConstant = Constants.LOYALTY_REWARDS_NONENROLLED_URL;
        }
        return urlConstant;
    }

    /**
     * This method returns current application context
     * 
     * @return application context
     */
    public static ApplicationContext getApplicationContext() {
        return ApplicationContextProvider.getApplicationContext();
    }

    /**
     * This method returns the decrypted customerId from request data map. If
     * the customerId is not present in the data map, the method returns null.
     * 
     * @param requestData
     *            the request data consisting of the customer id
     * 
     * @return the customer Id
     */
    public static String getDecryptedCustomerId(RequestData requestData) {

        String customerId = null;

        EncryptionUtil util = getApplicationContext().getBean(EncryptionUtil.class);

        if (requestData.getDataMap().containsKey(Constants.CUSTOMER_ID)
                && StringUtils.isNotEmpty(requestData.getDataMap().get(Constants.CUSTOMER_ID))) {
            customerId = util.decrypt(requestData.getDataMap().get(Constants.CUSTOMER_ID), requestData.getBrandName());
        }

        return customerId;
    }

    /**
     * This method returns the decrypted loyalty status from request data map.
     * If the loyalty status is not present in the data map, the method returns
     * empty string.
     * 
     * @param requestData
     *            the request data consisting of the loyalty status
     * @return the loyalty status
     */
    public static String getDecryptedLoyaltyStatus(RequestData requestData) {

        String loyaltyStatus = StringUtils.EMPTY;

        EncryptionUtil util = getApplicationContext().getBean(EncryptionUtil.class);

        if (requestData.getDataMap().containsKey(Constants.LOYALTY_STATUS)
                && StringUtils.isNotEmpty(requestData.getDataMap().get(Constants.LOYALTY_STATUS))) {
            loyaltyStatus = util.decrypt(requestData.getDataMap().get(Constants.LOYALTY_STATUS),
                    requestData.getBrandName());
        }

        return loyaltyStatus;
    }

    /**
     * This method return value from property file
     * 
     * @param key
     *            key to property value
     * @param fileName
     *            filename to read property value
     * @return the value of the given property
     */
    public static String getProperty(String key, String fileName) {
    	fileName = getConfigDir().concat(fileName);
    	System.out.println(getConfigDir());
        System.out.println(fileName);
        System.out.println("configggggg:: " + CommonUtil.class.getResource(fileName));
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource(fileName));
        final Properties props = yaml.getObject();
        return String.valueOf(props.getProperty(key));
    }
    /**
     * This returns list of property files from shared location
     * 
     * @return name with lower case letters.
     */
    public static File[] getPropertyFileList() {
    	System.out.println(CommonUtil.class.getResource(getConfigDir()).getPath());
    	final File dir = new File(CommonUtil.class.getResource(getConfigDir()).getPath());
        
        return dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(Constants.YML_EXTENSION);
            }
        });
    }

    /**
     * This method return properties from fileName
     * 
     * @param fileName
     *            filename to read property value
     * @return the list of properties from the filename
     */
    public static Properties getProperties(String fileName) {
        fileName = getConfigDir().concat(fileName);
        System.out.println(fileName);
        System.out.println("configggggg:: " + CommonUtil.class.getResource(fileName));
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();        
        yaml.setResources(new ClassPathResource(fileName));
        return yaml.getObject();
    }
    
    /**
     * This method will check if string contains HTML tag
     * 
     * @param text
     *            String to check
     * @return boolean true or false
     */
    public static String getConfigDir() {
    	
        String configDir = null;
        Context ctx = null;
        try {
            ctx = new InitialContext();
            configDir = (String) ctx.lookup("java:comp/env/configDir");
            System.out.println("1 configDir: "+configDir);
            // from Tomcat's server.xml
        } catch (NamingException e1) {
            LoggerUtil.controller(LOG, "Error caught while getting InitialContext(): "+ctx);
        }
        if(null == configDir) {
            configDir = Constants.CONFIG_DIR;
        } else {
            configDir = wrapWithString(configDir, Constants.FORWARD_SLASH);
            System.out.println("2 configDir: "+configDir);
        }
        return configDir;
    }
    private static String wrapWithString(String sourceStr, String stringToWrap) {

        String finalString = sourceStr;
        System.out.println("1 finalString: "+finalString);
        if(null == sourceStr) {
            return sourceStr;
        }
        if(!sourceStr.startsWith(stringToWrap)) {
            finalString = stringToWrap.concat(sourceStr);
            System.out.println("2 finalString: "+finalString);
        }
        if(!sourceStr.endsWith(stringToWrap)) {
            finalString = finalString.concat(stringToWrap);
            System.out.println("3 finalString: "+finalString);
        }
        System.out.println("4 finalString: "+finalString);
        return finalString;
    }
    
   

    /**
     * This is a private utility method to add a request parameter to the url
     * under creation.
     * 
     * @param url
     *            the url under creation
     * @param paramToSearch
     *            the parameter that needs to be searched from the params map
     * @param paramToSet
     *            the parameter to be set in the request url
     * 
     * @param paramsMap
     *            the params map
     * @return the url
     */
    public static String addParamToURL(String url, String paramToSearch, String paramToSet,
            Map<String, String> paramsMap) {
        String apiUrl = url;
        if (paramsMap.containsKey(paramToSearch)) {
            boolean hasParam = url.contains(Constants.SYMBOL_QUESTION);
            if (hasParam) {
                apiUrl = url.concat(Constants.AMPERSAND);
            } else {
                apiUrl = url.concat(Constants.SYMBOL_QUESTION);
            }
            apiUrl = apiUrl.concat(paramToSet).concat(Constants.EQUAL).concat(paramsMap.get(paramToSearch));
        }
        return apiUrl;
    }

    /**
     * Checks if is resposne available.
     *
     * @param jsonServiceResponse
     *            the json service response
     * @return true, if is resposne available
     */
    public static boolean isResposneAvailable(JsonServiceResponse jsonServiceResponse) {
        boolean isResposneAvailable = false;
        JSONObject data = jsonServiceResponse.getData();
        if (data.containsKey(Constants.ERROR_STATUS_KEY)
                && Constants.ERROR_STATUS_VALUE.equals(data.get(Constants.ERROR_STATUS_KEY))
                && data.containsKey(Constants.STATUS_CODE)
                && data.get(Constants.STATUS_CODE).toString().equals(Constants.HTTP_STATUS_204)) {
            isResposneAvailable = true;
        }
        return isResposneAvailable;
    }

    /**
     * This method is used to return the brand name from the given domain-brand
     * mapping for the respective branded web site domain.
     * 
     * @param domain
     *            the site domain
     * @param globalConfigService
     *            service to fetch property values from config file
     * @return brandName the brand name of the site
     */
    public static String getBrandNameFrmDomain(String domain, GlobalConfigService globalConfigService) {
        String brandName = "";
        String domainName = domain;

        if (StringUtils.isNotBlank(domain)) {
            domainName = StringUtils.substringAfterLast(StringUtils.substringBeforeLast(domain, Constants.DOT),
                    Constants.DOT);
        }

        brandName = globalConfigService.getPropertyValueAsString(Constants.DOMAIN_CONFIG,
                Constants.DOMAIN_BRAND_MAPPING_KEY.concat(domainName));

        if (StringUtils.isBlank(brandName)) {
            LoggerUtil.error(LOG, "No brand found for the domain: " + domain, null);
        }
        return brandName;
    }

    /**
     * This method will remove special characters from the string and return the
     * string without special characters
     * 
     * @param stringToChange
     *            string with special characters
     * @return modified string
     */
    public static String removeSpecialCharFrmStrng(String stringToChange) {
        String replacedString = stringToChange;
        Pattern pattern = Pattern.compile(Constants.REGEX_WITHOUT_SPECIAL_CHARACTERS);
        if (StringUtils.isNotBlank(stringToChange)) {
            Matcher match = pattern.matcher(stringToChange);

            while (match.find()) {
                String matchedString = match.group();
                replacedString = replacedString.replaceAll(Constants.REGEX_SLASH + matchedString,
                        Constants.BLANK_STRING);
            }
        }
        return replacedString;
    }

    /**
     * This method will converts the loyalty attributes key from map to json
     * array
     * 
     * @param jsonObject
     *            JSONObject
     * @param key
     *            String
     * @return JSONObject
     */
    public static JSONObject convertAttributesToJsonFromMap(JSONObject jsonObject, String key) {
        JSONObject updatedJson = new JSONObject();

        if (null != jsonObject && null != jsonObject.get(key)) {
            JSONArray attributes = (JSONArray) jsonObject.get(key);
            Iterator iterator = attributes.iterator();

            while (iterator.hasNext()) {
                JSONObject jsonData = (JSONObject) iterator.next();

                if (null != jsonData.get(Constants.CODE) && null != jsonData.get(Constants.VALUE)) {
                    updatedJson.put(jsonData.get(Constants.CODE), jsonData.get(Constants.VALUE));
                }
            }

            jsonObject.remove(key);

            jsonObject.put(Constants.LOYALTY_ATTRIBUTES_KEY, updatedJson);
        }

        return jsonObject;
    }

    /**
     * This method updated the StatusCode when UnknownHostException error occurs
     * 
     * @param response
     *            JsonServiceResponse
     * @return JsonServiceResponse
     */
    public static JsonServiceResponse updateStatusCode(JsonServiceResponse response) {

        if (!CommonUtil.isSuccessResponse(response) && null != response && null != response.getData()
                && response.getData().toString().contains(Constants.UNKNOWN_HOST_EXCPETION)) {
            response.getData().put(Constants.ERROR_CODE, Constants.SYSTEM_FAILURE_ERROR);
        }

        return response;
    }

    /**
     * This method will check if string contains HTML tag
     * 
     * @param text
     *            String to check
     * @return boolean true or false
     */
    public static boolean hasHtmlTags(String text) {
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    /**
     * This method will construct response data object.
     *
     * @param key
     *            service name
     * @param value
     *            service data
     * @return the response data
     */
    public static ResponseData getResponseData(String key, Object value) {
        ResponseData responseData = new ResponseData();
        responseData.setData(key, value);
        return responseData;
    }

    /**
     * This method is used to check if the Hello World service response contains
     * error.
     * 
     * @param jsonServiceResponse
     *            the service response
     * @return isSuccessResponse the boolean value
     */
    public static boolean isHelloWorldSuccessResponse(JsonServiceResponse jsonServiceResponse) {
        boolean isSuccessResponse = false;

        if (null == jsonServiceResponse || null == jsonServiceResponse.getData()) {
            isSuccessResponse = true;
        } else {
            JSONObject jsonObject = jsonServiceResponse.getData();
            if (jsonObject.containsKey(Constants.STATUS) && null != jsonObject.get(Constants.STATUS)) {
                JSONObject status = (JSONObject) jsonObject.get(Constants.STATUS);
                if (status.containsKey(Constants.STATUS_CODE) && null != status.get(Constants.STATUS_CODE)
                        && status.get(Constants.STATUS_CODE).toString().equalsIgnoreCase(Constants.TWO_HUNDRED)) {
                    isSuccessResponse = true;
                }
            }
        }
        return isSuccessResponse;
    }

    /**
     * This method is used to check if the Hello World service response contains
     * data.
     * 
     * @param jsonServiceResponse
     *            the service response
     * @return hasdata the boolean value
     */
    public static boolean hasResponseData(JsonServiceResponse jsonServiceResponse) {
        boolean hasData = true;

        if (null == jsonServiceResponse || null == jsonServiceResponse.getData()) {
            hasData = false;
        } 
        return hasData;
    }

    /**
     * This method is used to top n lines from server log fies.
     * 
     * @param lineNumber
     *            top lines
     * @param path
     *            log file path
     * @return top n lines
     */
    public static List<String> readServerLogFileLines(long lineNumber, String path) {
        List<String> lastLines = new ArrayList<>();
        Path file = Paths.get(path);
        long count = 0;

        try (Stream<String> lines = Files.lines(file, StandardCharsets.UTF_8);
                Stream<String> lines2 = Files.lines(file, StandardCharsets.UTF_8);) {
            count = lines.parallel().count();

            int i = 0;
            for (String line : (Iterable<String>) lines2::iterator) {
                if (i >= count - lineNumber) {
                    lastLines.add(line);
                }
                i++;
            }
        } catch (IOException e) {
            LoggerUtil.error(LOG, "IOException in readServerLogFileLines", e);
        }
        return lastLines;
    }

}
