package com.scent.feedservice.Util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;


import java.util.List;

import static com.scent.feedservice.Util.Constants.*;


/**
 * This class file is used for logging the messages. This class has following
 * methods debug error info controller service repository
 * 
 * @author ssared
 *
 */
public final class LoggerUtil {

    private static final Logger LOG = LogManager.getLogger();
    private static final int INFO_LOG_LEVEL = 400;
    private static final int DEBUG_LOG_LEVEL = 500;

    /**
     * private constructor to avoid class instantiation.
     */
    private LoggerUtil() {
    }

    /**
     * This method will log the debug message.
     * 
     * @param logger
     *            use this logger to log logger
     * @param message
     *            message to log message to log
     */
    public static void debug(Logger logger, String message) {
        if (LoggerUtil.getLogLevel() >= DEBUG_LOG_LEVEL) {
            logger.debug(message);
        }
    }

    /**
     * This method will log the debug message with arguments.
     * 
     * @param logger
     *            use this logger to log
     * @param message
     *            message to log
     * @param arg
     *            arguments to be logged
     */
    public static void debug(Logger logger, String message, Object arg) {
        if (LoggerUtil.getLogLevel() >= DEBUG_LOG_LEVEL) {
            logger.debug(message, arg);
        }
    }

    /**
     * This method will log the info message.
     * 
     * @param logger
     *            use this logger to log
     * @param message
     *            message to log
     */
    public static void info(Logger logger, String message) {
        if (LoggerUtil.getLogLevel() >= INFO_LOG_LEVEL) {
            logger.info(message);
        }
    }

    /**
     * This method will log the info message with arguments.
     * 
     * @param logger
     *            use this logger to log
     * @param message
     *            message to log
     * @param arg
     *            arguments to be logged
     */
    public static void info(Logger logger, String message, Object arg) {
        if (LoggerUtil.getLogLevel() >= INFO_LOG_LEVEL) {
            logger.info(message, arg);
        }
    }

    /**
     * This method will log the error message with throwable object.
     * 
     * @param logger
     *            use this logger to log
     * @param message
     *            message to log
     * @param t
     *            the error trace to log.
     */
    public static void error(Logger logger, String message, Throwable t) {
        logger.error(message, t);
    }

    /**
     * This method will log the error message.
     * 
     * @param logger
     *            use this logger to log
     * @param message
     *            message to log
     * @param t
     *            error
     * @param logError
     *            error status
     */
    public static void error(Logger logger, String message, Throwable t, boolean logError) {
        if (logError) {
            logger.error(message, t);
        }
    }

    /**
     * This method will log controller level log.
     * 
     * @param logger
     *            use this logger to log
     * @param message
     *            message to log
     */
    public static void controller(Logger logger, String message) {
        logger.log(Level.getLevel(LOG_CONTROLLER), message);
    }

    /**
     * This method will log service level log.
     * 
     * @param logger
     *            use this logger to log
     * @param message
     *            message to log
     */
    public static void service(Logger logger, String message) {
        logger.log(Level.getLevel(LOG_SERVICE), message);
    }

    /**
     * This method will log repository level log.
     * 
     * @param logger
     *            use this logger to log
     * @param message
     *            message to log
     */
    public static void repository(Logger logger, String message) {
        logger.log(Level.getLevel(LOG_REPOSITORY), message);
    }

    /**
     * This method returns the log level set currently for the application.
     * 
     * @return loglevel.
     */
    public static int getLogLevel() {
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        final Configuration conf = ctx.getConfiguration();
        LoggerConfig lconf = conf.getLoggerConfig(LOG.getName());
        Level level = lconf.getLevel();
        return level.intLevel();
    }

    /**
     * This returns comma Separated String from object array
     * 
     * @param array
     *            variable args.
     * @param logSuppressHeaders
     *            headers
     * @param logSuppressUrlParams
     *            url params
     * @return string.
     */
    public static String getCommnaSeparatedString(Object[] array, List<String> logSuppressHeaders,
            List<String> logSuppressUrlParams) {
        // Cloning the object to print as operating on the original object is
        // likely to throw concurrentModificationexception
        Object[] objectCopy = array.clone();
        String str = CommonUtil.join(objectCopy, ", ");
        objectCopy = null;
        if (str.length() <= TWO) {
            return EMPTY;
        } else {
            str = removeParamsFrmString(str, logSuppressHeaders, logSuppressUrlParams);
            return "[ " + str + " ]";
        }
    }

    /**
     * This method is used to remove parameters from headers and URL parameters.
     * 
     * @param str
     *            String from which parameters has to be removed
     * @param logSuppressHeaders
     *            array of params to be removed from headers
     * @param logSuppressUrlParams
     *            array of params to be removed from URL params
     * @return string after parameter removal.
     */

    public static String removeParamsFrmString(String str, List<String> logSuppressHeaders,
            List<String> logSuppressUrlParams) {

        String finalString = str;
        finalString = removeParamsFrmString(finalString, logSuppressHeaders);
        finalString = removeParamsFrmString(finalString, logSuppressUrlParams);
        return finalString;
    }

    /**
     * This method is used to remove parameters from a given string.
     * 
     * @param str
     *            String from which parameters has to be removed
     * @param params
     *            array of params to be removed
     * @return string after parameter removal.
     */
    public static String removeParamsFrmString(String str, List<String> params) {
        String finalString = str;

        if (CommonUtil.isNotBlank(str) && !params.isEmpty()) {
            for (String param : params) {
                if (CommonUtil.isNotBlank(param)) {
                    finalString = finalString.replaceAll(param + "=[\\[\\s\\w.-]*[^,]", "");
                }
            }
        }
        return finalString;
    }

    /**
     * This method is used to return the thread details for logging.
     * 
     * @param id
     *            the current thread id
     * @param ip
     *            the client IP
     * @param userId
     *            the user ID
     * @param uri
     *            the request URI
     * @param domain
     *            the domain
     * @return the formatted thread details
     */
    public static String getThreadDetails(long id, String ip, String userId, String uri, String domain) {
        String userNo = userId;
        if (CommonUtil.isEmpty(userNo)) {
            userNo = EMPTY;
        }
        StringBuilder threadLogDeatils = new StringBuilder(THREAD).append(EQUAL + id)
                .append(COMMA).append(WHITESPACE).append(CLIENT_IP)
                .append(EQUAL + ip).append(COMMA).append(WHITESPACE)
                .append(USER_ID).append(EQUAL + userNo).append(COMMA)
                .append(WHITESPACE).append(DOMAIN).append(EQUAL + domain)
                .append(COMMA).append(WHITESPACE).append(URI)
                .append(EQUAL + uri);
        return threadLogDeatils.toString();
    }

}
