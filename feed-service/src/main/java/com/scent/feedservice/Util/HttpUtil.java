/**
 * 
 */
package com.scent.feedservice.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;

/**
 * This class has methods for HttpRequest, HttpResponse and Cookie.
 * 
 * @author pmis30
 *
 */
public final class HttpUtil {

    // no arg constructor
    private HttpUtil() {
        // do nothing
        super();
    }

    /**
     * This method is used to fetch cookies values
     * 
     * @param cookieName
     *            the cookie name.
     * @param httpRequest
     *            the http request.
     * @return cookie value.
     */
    public static String getCookieValue(String cookieName, HttpServletRequest httpRequest) {
        String cookieValue = StringUtils.EMPTY;
        Cookie[] cookies = httpRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    cookieValue = XssSanitizerUtil.stripXSS(cookie.getValue());
                }
            }
        }
        return cookieValue;
    }

    /**
     * This method is used to create login cookies from server side. Login
     * cookies are created from server side only for first time login.
     * 
     * @param response
     *            the http servlet response object
     * @param cookiesData
     *            the cookies data
     * @param secureCookiesList
     *            cookie list for which secure has to be set.
     * @param cookieDomain
     *            the cookie domain for accessing in sub domains.
     */
    public static void createCookie(HttpServletResponse response, JSONObject cookiesData,
            List<String> secureCookiesList, String cookieDomain) {
        List<String> cookieList = getCookieList();
        for (String cookieName : cookieList) {
            if (cookiesData.containsKey(cookieName) && StringUtils.isNotBlank((String) cookiesData.get(cookieName))) {
                if (cookieName.equals(Constants.SESSION_KEY_AKAMAI_COOKIE)
                        || cookieName.equals(Constants.COOKIE_ACCESS_TOKEN_EXPIRES)) {

                    // Set cookie domain for sub domain access.
                    if (StringUtils.isNotBlank(cookieDomain)) {
                        response.addHeader(Constants.SET_COOKIE,
                                XssSanitizerUtil.stripXSS(cookieName + Constants.EQUAL
                                        + (String) cookiesData.get(cookieName) + Constants.COOKIE_DOMAIN + cookieDomain
                                        + Constants.AKAMAI_PATH_HTTP_SECURE));
                    } else {
                        response.addHeader(Constants.SET_COOKIE, XssSanitizerUtil.stripXSS(cookieName + Constants.EQUAL
                                + (String) cookiesData.get(cookieName) + Constants.AKAMAI_PATH_HTTP_SECURE));
                    }
                } else if (cookiesData.get(cookieName) != null) {
                    String cookieValue = (String) cookiesData.get(cookieName);
                    cookieValue = cookieValue.replaceAll(Constants.REGEX_WHITESPACE, StringUtils.EMPTY);
                    Cookie cookie = new Cookie(cookieName, XssSanitizerUtil.stripXSS(cookieValue));
                    cookie.setPath(Constants.COOKIE_PATH);
                    cookie.setSecure(true);
                    cookie = setExpiryTime(cookie,
                            ((Long) cookiesData.get(Constants.REMAINING_TIME)).intValue() / Constants.ONE_THOUSAND);
                    cookie = setHttpForCookie(cookie, secureCookiesList);

                    // Set cookie domain for sub domain access.
                    setCookieDomain(cookie, cookieDomain);

                    response.addCookie(cookie);
                }
            }
        }
    }

    /**
     * This method is used to create given cookies from server side. Login
     * cookies are created from server side only when requested.
     * 
     * @param response
     *            the http servlet response object
     * @param cookiesData
     *            the cookies data
     * @param cookieNameToBeCreated
     *            the name of the cookie to be created
     */
    public static void createCookie(HttpServletResponse response, JSONObject cookiesData,
            String cookieNameToBeCreated) {

        if (cookiesData.containsKey(Constants.TOTAL_COUNT)) {
            String cookieValue = XssSanitizerUtil.stripXSS(String.valueOf(cookiesData.get(Constants.TOTAL_COUNT)));
            Cookie cookie = new Cookie(cookieNameToBeCreated, cookieValue);
            cookie.setPath(Constants.COOKIE_PATH);
            cookie.setSecure(true);
            response.addCookie(cookie);
        }

    }

    /**
     * This method is used to create given cookies from server side. Login
     * cookies are created from server side only when requested.
     * 
     * @param response
     *            the http servlet response object
     * @param cookiesData
     *            the cookies data
     * @param cookieDataName
     *            the name of property in cookieData
     * @param cookieName
     *            the name of the cookie to be created
     * @param isHttpOnly
     *            boolean value representing whether the cookie should be HTTPS
     */
    public static void createCookie(HttpServletResponse response, JSONObject cookiesData, String cookieDataName,
            String cookieName, boolean isHttpOnly) {

        if (cookiesData.containsKey(cookieDataName)) {
            Cookie cookie = new Cookie(cookieName, Long.toString((Long) cookiesData.get(cookieDataName)));
            cookie.setPath(Constants.COOKIE_PATH);
            cookie.setSecure(true);
            cookie.setHttpOnly(isHttpOnly);
            response.addCookie(cookie);
        }

    }
    
    /**
     * This method will set the cookie with expiration time
     * 
     * @param response
     *            the http servlet response object
     * @param cookiesData
     *            the cookies data
     * @param cookieNameToBeCreated
     *            the String data
     * @param expirationTime
     *            the int data
     */
    public static void createCookie(HttpServletResponse response, String cookiesData, String cookieNameToBeCreated,
            int expirationTime) {

        Cookie cookie = new Cookie(cookieNameToBeCreated, XssSanitizerUtil.stripXSS(cookiesData));
        cookie.setMaxAge(expirationTime);
        cookie.setPath(Constants.COOKIE_PATH);
        response.addCookie(cookie);

    } 
    
    /**
     * This method is used to create secure and Httponly cookies from server
     * side.
     * 
     * @param response
     *            the http servlet response object
     * @param cookieString
     *            the cookies data
     * @param cookieNameToBeCreated
     *            the name of the cookie to be created
     */
    public static void createCookie(HttpServletResponse response, String cookieString,
            String cookieNameToBeCreated) {
        Cookie cookie = new Cookie(cookieNameToBeCreated, cookieString);
        cookie.setPath(Constants.COOKIE_PATH);
        cookie.setSecure(true);
        response.addCookie(cookie);
    }    

    /**
     * This method is used to check if DVP related cookies are present in the
     * request.
     * 
     * @param paramsMap
     *            request data map
     * @return boolean value
     */
    public static boolean checkDvpCookies(Map<String, String> paramsMap) {
        return paramsMap.containsKey(Constants.STATE) && paramsMap.containsKey(Constants.ZIP);
    }

    /**
     * This method is used to check if UGC related cookies are present in the
     * request.
     * 
     * @param paramsMap
     *            request data map
     * @return boolean value
     */
    public static boolean checkUgcCookies(Map<String, String> paramsMap) {
        return paramsMap.containsKey(Constants.COOKIE_UGC_USER_ID);
    }

    /**
     * This method is used to set cookie expiry time
     * 
     * @param cookie
     *            cookie
     * @param time
     *            expiry time to be set
     * @return cookie
     */
    private static Cookie setExpiryTime(Cookie cookie, int time) {
        if (Constants.COOKIE_ACCESS_TOKEN.equalsIgnoreCase(cookie.getName())) {
            cookie.setMaxAge(time);
        }
        return cookie;
    }

    /**
     * This method is used to create secure and Httponly cookies from server
     * side.
     * 
     * @param response
     *            the http servlet response object
     * @param cookieString
     *            the cookies data
     * @param cookieNameToBeCreated
     *            the name of the cookie to be created
     */
    public static void createSecureCookie(HttpServletResponse response, String cookieString,
            String cookieNameToBeCreated) {
        Cookie cookie = new Cookie(cookieNameToBeCreated, cookieString);
        cookie.setPath(Constants.COOKIE_PATH);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    /**
     * This method is used to get the cookie name list.
     * 
     * @return ArrayList which contains cookie name list.
     */
    public static List<String> getCookieList() {
        List<String> cookieList = new ArrayList<>();
        cookieList.add(Constants.COOKIE_CCN);
        cookieList.add(Constants.COOKIE_ACCESS_TOKEN);
        cookieList.add(Constants.COOKIE_ACCESS_TOKEN_EXPIRES);
        cookieList.add(Constants.COOKIE_REFRESH_TOKEN);
        cookieList.add(Constants.STATE);
        cookieList.add(Constants.ZIP);
        cookieList.add(Constants.COOKIE_UGC_USER_ID);
        cookieList.add(Constants.SESSION_KEY_AKAMAI_COOKIE);
        cookieList.add(Constants.OFFER_COUNT);
        cookieList.add(Constants.FLAVOR_ID);
        cookieList.add(Constants.DOI_STATUS);
        cookieList.add(Constants.LOYALTY_STATUS);
        cookieList.add(Constants.CUSTOMER_ID);
        cookieList.add(Constants.REWARD_DELIVERY_TYPE);
        cookieList.add(Constants.FLEX_STATUS);
        cookieList.add(Constants.COOKIE_DM);
        cookieList.add(Constants.COOKIE_FLAVOUR_PREFERENCE);
        cookieList.add(Constants.COOKIE_BRAND_PREFERENCE);
        cookieList.add(Constants.COOKIE_PACK_PREFERENCE);
        cookieList.add(Constants.COOKIE_ELIGIBILITY_STATUS);
        cookieList.add(Constants.COOKIE_ENROLLMENT_STATUS);
        cookieList.add(Constants.COOKIE_AGE_RANGE);
        cookieList.add(Constants.COOKIE_MC_AVAILABLE);

        return cookieList;
    }

    /**
     * Invalidates all cookies from request.
     *
     * @param request
     *            HttpServletRequest for which cookie needs to be removed
     * @param response
     *            HttpServletResponse to set the response
     * @param cookieDomain
     *            the cookie domain for accessing in sub domains.
     */
    public static void clearCookies(HttpServletRequest request, HttpServletResponse response, String cookieDomain) {

        // if either of request or response is null, then return nothing
        if (request == null || response == null) {
            return;
        }
        Cookie[] cookies = request.getCookies();

        // get cookie name list
        List<String> cookieList = getCookieList();
        cookieList.add(Constants.COOKIE_BRAND_NAME);
        cookieList.add(Constants.COOKIE_DEEPLINK);
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                if (cookieList.contains(cookie.getName())) {
                    cookie.setValue(XssSanitizerUtil.stripXSS(StringUtils.EMPTY));
                    cookie.setMaxAge(0);
                    cookie.setSecure(true);
                    cookie.setPath(Constants.COOKIE_PATH);

                    // Set cookie domain for sub domain access.
                    setCookieDomain(cookie, cookieDomain);

                    response.addCookie(cookie);
                }
            }
        }
    }

    /**
     * This method is used to set Http only for cookies
     * 
     * @param cookie
     *            Cookie for which http only has to be set.
     * @param secureCookiesList
     *            cookie list for which secure has to be set.
     * @return cookie
     */
    private static Cookie setHttpForCookie(Cookie cookie, List<String> secureCookiesList) {
        if (!secureCookiesList.contains(cookie.getName())) {
            cookie.setHttpOnly(true);
        }
        return cookie;
    }

    /**
     * This method is used to updated cookies for extend session
     * 
     * @param request
     *            HttpServletRequest for which cookie needs to be updated
     * @param response
     *            the http servlet response object
     * @param cookiesData
     *            the cookies data
     * @param cookieDomain
     *            the cookies domain
     */
    public static void updateLoginCookies(HttpServletRequest request, HttpServletResponse response,
            JSONObject cookiesData, String cookieDomain) {
        if (request == null || response == null) {
            return;
        }
        List<String> cookieList = new ArrayList<String>();
        cookieList.add(Constants.COOKIE_ACCESS_TOKEN);
        cookieList.add(Constants.COOKIE_ACCESS_TOKEN_EXPIRES);
        cookieList.add(Constants.COOKIE_REFRESH_TOKEN);
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                if (cookieList.contains(cookie.getName())) {

                    String cookieName = cookie.getName();
                    if (cookieName.equals(Constants.COOKIE_ACCESS_TOKEN_EXPIRES)) {

                        // Set cookie domain for sub domain access.
                        if (StringUtils.isNotBlank(cookieDomain)) {
                            response.addHeader(Constants.SET_COOKIE,
                                    XssSanitizerUtil.stripXSS(cookieName + Constants.EQUAL
                                            + (String) cookiesData.get(cookieName) + Constants.COOKIE_DOMAIN
                                            + cookieDomain + Constants.AKAMAI_PATH_HTTP_SECURE));
                        } else {
                            response.addHeader(Constants.SET_COOKIE,
                                    XssSanitizerUtil.stripXSS(
                                            cookieName + Constants.EQUAL + (String) cookiesData.get(cookieName)
                                                    + Constants.AKAMAI_PATH_HTTP_SECURE));
                        }
                    } else {

                        cookie.setValue(XssSanitizerUtil
                                .stripXSS(cookiesData.get(XssSanitizerUtil.stripXSS(cookie.getName())).toString()));
                        cookie.setPath(Constants.COOKIE_PATH);
                        cookie.setSecure(true);
                        cookie.setHttpOnly(true);
                        cookie = setExpiryTime(cookie,
                                ((Long) cookiesData.get(Constants.REMAINING_TIME)).intValue() / Constants.ONE_THOUSAND);

                        // Set cookie domain for sub domain access.
                        setCookieDomain(cookie, cookieDomain);

                        response.addCookie(cookie);
                    }
                }
            }
        }

    }

    /**
     * This method is used to updated the cookie value for the given cookie
     * name.
     * 
     * @param request
     *            HttpServletRequest for which cookie needs to be updated
     * @param response
     *            the http servlet response object
     * @param name
     *            the cookie name
     * @param value
     *            the cookie value
     * @param secureCookiesList
     *            cookie list for which secure has to be set.
     */
    public static void updateCookie(HttpServletRequest request, HttpServletResponse response, String name, String value,
            List<String> secureCookiesList) {

        if (null == request || null == request.getCookies() || null == response || null == name) {
            return;
        }

        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                cookie.setValue(XssSanitizerUtil.stripXSS(value));
                cookie.setPath(Constants.COOKIE_PATH);
                cookie.setSecure(true);
                cookie = setHttpForCookie(cookie, secureCookiesList);
                response.addCookie(cookie);
            }
        }
    }

    /**
     * This method is used to updated user related cookies
     * 
     * @param request
     *            HttpServletRequest for which cookie needs to be updated
     * @param response
     *            the http servlet response object
     * @param cookiesData
     *            the cookies data
     * @param secureCookiesList
     *            cookie list for which secure has to be set.
     */
    @SuppressWarnings("unchecked")
    public static void updateUserCookies(HttpServletRequest request, HttpServletResponse response,
            JSONObject cookiesData, List<String> secureCookiesList) {
        Map<String, String> ccnMap = cookiesData;
        if (request == null || response == null || null == ccnMap) {
            return;
        }
        Map<String, String> cookielist = getCookieMap(ccnMap);
        List<String> exclusionList = new ArrayList<String>();
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                if (cookielist.containsKey(cookie.getName())) {
                    exclusionList.add(cookie.getName());
                    cookie.setValue(XssSanitizerUtil.stripXSS(cookielist.get(cookie.getName())));
                    cookie.setPath(Constants.COOKIE_PATH);
                    cookie.setSecure(true);
                    cookie = setHttpForCookie(cookie, secureCookiesList);
                    response.addCookie(cookie);
                }
                // remove loyalty status cookie if user eligibility status has
                // changed
                if (Constants.LOYALTY_STATUS.equalsIgnoreCase(cookie.getName())
                        && !cookielist.containsKey(Constants.LOYALTY_STATUS)) {
                    cookie.setValue(XssSanitizerUtil.stripXSS(StringUtils.EMPTY));
                    cookie.setMaxAge(0);
                    cookie.setSecure(true);
                    cookie.setPath(Constants.COOKIE_PATH);
                    response.addCookie(cookie);
                }
            }
        }
        for (Map.Entry<String, String> entry : cookielist.entrySet()) {
            if (!exclusionList.contains(entry.getKey())) {
                Cookie cookie = new Cookie(entry.getKey(), XssSanitizerUtil.stripXSS(entry.getValue()));
                cookie.setPath(Constants.COOKIE_PATH);
                cookie.setSecure(true);
                cookie = setHttpForCookie(cookie, secureCookiesList);
                response.addCookie(cookie);
            }
        }
    }

    /**
     * This method will be used to return cookie Map to be update cookies
     * 
     * @param ccnMap
     *            CCN map containing CCN details
     * @return map of cookie values to be updated
     */
    private static Map<String, String> getCookieMap(Map<String, String> ccnMap) {
        String state = ccnMap.get(Constants.CCN_STATE);
        String zip = ccnMap.get(Constants.CCN_ZIP);
        String flavorID = ccnMap.get(Constants.CCN_FLAVOR_ID);
        String loyaltyEligibiltyStatus = Constants.NOT_APPLICABLE;
        String loyaltyEnrollmentStatus = Constants.NOT_APPLICABLE;
        Map<String, String> cookielist = new HashMap<String, String>();
        cookielist.put(Constants.STATE, state);
        cookielist.put(Constants.ZIP, zip);
        String contactFlag = ccnMap.get(Constants.CCN_CONTACTABILITY_FLAG);
        String channelStat = ccnMap.get(Constants.CCN_CHANNEL_STATUS);
        if (StringUtils.isNotBlank(flavorID)) {
            cookielist.put(Constants.FLAVOR_ID, flavorID);
        }
        if (StringUtils.isNotBlank(channelStat) && StringUtils.isNotBlank(contactFlag)) {
            if (Constants.DOI.equals(channelStat)) {
                channelStat = Constants.YES;
            }
            cookielist.put(Constants.DOI_STATUS, channelStat + contactFlag);

        } else {
            cookielist.put(Constants.DOI_STATUS, Constants.NO + Constants.NO);
        }
        String dm = ccnMap.get(Constants.CCN_WEB_SUBSCRIBE_CD);
        if (StringUtils.isNotBlank(dm)) {
            cookielist.put(Constants.COOKIE_DM, dm);
        }

        // update loyalty specific cookies
        String loyaltyStatus = ccnMap.get(Constants.LOYALTY_STATUS);
        if (StringUtils.isNotBlank(loyaltyStatus)) {
            cookielist.put(Constants.LOYALTY_STATUS, loyaltyStatus);

        }

        // update loyalty eligibility cookies
        if (ccnMap.containsKey(Constants.LOYALTY_ELIGIBILITY_STATUS)
                && StringUtils.isNotBlank(ccnMap.get(Constants.LOYALTY_ELIGIBILITY_STATUS))) {
            loyaltyEligibiltyStatus = ccnMap.get(Constants.LOYALTY_ELIGIBILITY_STATUS);

            if (StringUtils.isNotBlank(ccnMap.get(Constants.LOYALTY_ENROLLMENT_STATUS))) {
                loyaltyEnrollmentStatus = ccnMap.get(Constants.LOYALTY_ENROLLMENT_STATUS);

            }
        }

        cookielist.put(Constants.COOKIE_ELIGIBILITY_STATUS, loyaltyEligibiltyStatus);
        cookielist.put(Constants.COOKIE_ENROLLMENT_STATUS, loyaltyEnrollmentStatus);
        return cookielist;
    }

    /**
     * This method will be used to return client real ip address
     * 
     * @param request
     *            HttpServletRequest request
     * @return client real ip address
     */
    public static String getClientIp(HttpServletRequest request) {

        String remoteAddr = StringUtils.EMPTY;

        if (request != null) {
            remoteAddr = request.getHeader(Constants.X_FORWARDED_FOR);
            if (remoteAddr == null || StringUtils.EMPTY.equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }

        return StringUtils.substringBefore(remoteAddr, Constants.COMMA);
    }

    /**
     * This method will validate that the deeplink URL should match with the url
     * of current domain URL.
     * 
     * @param deepLinkUrl
     *            the deeplink cookie value
     * @param serverName
     *            current server name
     * @return boolean if deepLinkUrl starts with serverName returns true else
     *         false
     */
    public static boolean isValidUrl(String deepLinkUrl, String serverName) {

        String deepLinkUrlWithoutProtocol = null;
        if (null != deepLinkUrl) {
            String deepLinkUrlWithoutHttp = deepLinkUrl.startsWith(Constants.HTTP)
                    ? deepLinkUrl.substring(Constants.HTTP.length()) : StringUtils.EMPTY;
            deepLinkUrlWithoutProtocol = deepLinkUrl.startsWith(Constants.HTTPS)
                    ? deepLinkUrl.substring(Constants.HTTPS.length()) : deepLinkUrlWithoutHttp;
        }
        return null != serverName && null != deepLinkUrlWithoutProtocol
                && deepLinkUrlWithoutProtocol.startsWith(serverName.concat(Constants.ROOT_URL_PATTERN));
    }

    /**
     * This method will set the cookie with sub domain name for accessing in sub
     * domains.
     * 
     * @param cookie
     *            the cookie object
     * @param cookieDomain
     *            the cookie domain name
     */
    public static void setCookieDomain(Cookie cookie, String cookieDomain) {
        if (StringUtils.isNotBlank(cookieDomain)) {
            cookie.setDomain(cookieDomain);
        }
    }
}
