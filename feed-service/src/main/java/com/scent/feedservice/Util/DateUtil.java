package com.scent.feedservice.Util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static com.scent.feedservice.Util.Constants.EMPTY;

public class DateUtil {
    private static final Logger LOG = LogManager.getLogger(DateUtil.class);
    /**
     * no arg constructor
     */
    private DateUtil() {
        // do nothing
        super();
    }

    public static String formatDate(String dateIn, String datePatternIn, String datePatternOut) {
        String dateOut = dateIn;
        try {
            SimpleDateFormat sdfIn = new SimpleDateFormat(datePatternIn, Locale.getDefault(Locale.Category.FORMAT));
            SimpleDateFormat sdfOut = new SimpleDateFormat(datePatternOut, Locale.getDefault(Locale.Category.FORMAT));
            Date date = sdfIn.parse(dateIn);
            dateOut = sdfOut.format(date);
        } catch (ParseException e) {
            LoggerUtil.error(LOG,
                    String.format("ParseException in formatDate for dateIn: %s, datePatternIn: %s, datePatternOut: %s",
                            dateIn, datePatternIn, datePatternOut),
                    e);
        }

        return dateOut;
    }

    public static Date getFormatDate(String dateIn, String datePatternIn, String datePatternOut) {
        Date dateOut = null;
        try {
            String date = formatDate(dateIn, datePatternIn, datePatternOut);
            SimpleDateFormat sdfOut = new SimpleDateFormat(datePatternOut, Locale.getDefault(Locale.Category.FORMAT));
            dateOut = sdfOut.parse(date);
        } catch (ParseException e) {
            LoggerUtil.error(LOG,
                    String.format("ParseException in formatDate for dateIn: %s, datePatternIn: %s, datePatternOut: %s",
                            dateIn, datePatternIn, datePatternOut),
                    e);
        }

        return dateOut;
    }

    public static String getCurrentDate(String datePattern, String timeZoneId) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(timeZoneId));
        SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern, Locale.getDefault(Locale.Category.FORMAT));
        dateFormatter.setCalendar(calendar);
        return dateFormatter.format(calendar.getTime());
    }

    public static String getDateAfter(String datePattern, String timeZoneId, int amount) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(timeZoneId));
        SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern, Locale.getDefault(Locale.Category.FORMAT));
        calendar.add(Calendar.MONTH, +amount);
        return dateFormatter.format(calendar.getTime());
    }

    public static Date addAdvanceDaysToGivenDate(String startDate, int advanceNoOfDays,
                                                   String inputDateTimePattern, String timeZoneId) {
        SimpleDateFormat formatter = new SimpleDateFormat(inputDateTimePattern,
                Locale.getDefault(Locale.Category.FORMAT));
        TimeZone timezone = TimeZone.getTimeZone(timeZoneId);
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(timezone);
        try {
            Date date = formatter.parse(startDate);
            cal.setTime(date);
            cal.add(Calendar.DATE, advanceNoOfDays);
        } catch (ParseException e) {
            LoggerUtil.error(LOG,
                    String.format("ParseException in addDaysToGivenDate for date string: [%s]", startDate), e);
        }
        return cal.getTime();
    }

    public static Date updateHourToExpiryDate(Date dateIn, int hour, String timeZoneId) {
        TimeZone timezone = TimeZone.getTimeZone(timeZoneId);
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(timezone);
        cal.setTime(dateIn);
        cal.add(Calendar.HOUR_OF_DAY, hour);
        return cal.getTime();
    }

    /**
     * This method is used to return the time in milliseconds for the given
     * date/time. It accepts date/time format for creating the date, and time
     * zone for creating the calendar instance.
     *
     * @param dateTime
     *            input dateTime
     * @param dateTimePattern
     *            pattern of input dateTime
     * @param timeZoneId
     *            time zone of input dateTime
     * @return dateTime converted into milliseconds
     */
    public static long getTimeInMillis(String dateTime, String dateTimePattern, String timeZoneId) {
        TimeZone timezone = TimeZone.getTimeZone(timeZoneId);
        Calendar calendar = Calendar.getInstance(timezone);

        try {
            SimpleDateFormat formatter = new SimpleDateFormat(dateTimePattern,
                    Locale.getDefault(Locale.Category.FORMAT));
            formatter.setCalendar(calendar);
            calendar.setTime(formatter.parse(dateTime));
        } catch (ParseException e) {
//            LoggerUtil.error(LOG, String.format(Constants.LOG_PARSE_EXCEPTION, dateTime, dateTimePattern, timeZoneId),
//                    e);
        }

        return calendar.getTimeInMillis();
    }
    /**
     * This method is used to return the get date in (February 13, Wednesday)
     * format for the given time zone id. *
     *
     * @param dateTime
     *            input dateTime
     *
     * @param inputPattern
     *            the input date pattern.
     * @param inputTimezone
     *            the input date TimeZone id.
     * @param outputPattern
     *            the output date pattern.
     * @param outputTimezone
     *            the output date TimeZone id.
     * @return currentDate the current date in given format.
     */
    public static String convertDateToTimeZone(String dateTime, String inputPattern, String inputTimezone,
                                          String outputPattern, String outputTimezone) {
        Calendar calendarIn = Calendar.getInstance(TimeZone.getTimeZone(inputTimezone));
        Calendar calendarOut = Calendar.getInstance(TimeZone.getTimeZone(outputTimezone));
        String dateOut = EMPTY;
        try {
            SimpleDateFormat formatterIn = new SimpleDateFormat(inputPattern,
                    Locale.getDefault(Locale.Category.FORMAT));
            SimpleDateFormat formatterOut = new SimpleDateFormat(outputPattern,
                    Locale.getDefault(Locale.Category.FORMAT));
            formatterIn.setCalendar(calendarIn);
            formatterOut.setCalendar(calendarOut);
            Date date = formatterIn.parse(dateTime);
            dateOut = formatterOut.format(date);
        } catch (ParseException e) {
//            LoggerUtil.error(LOG, String.format(Constants.LOG_PARSE_EXCEPTION, dateTime, inputPattern, inputTimezone),
//                    e);
        }
        return dateOut;
    }

    public static String getCurrentYear(String dateTime, String dateTimePattern, String timeZoneId) {
        TimeZone timezone = TimeZone.getTimeZone(timeZoneId);
        Calendar calendar = Calendar.getInstance(timezone);
        String year = EMPTY;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(dateTimePattern,
                    Locale.getDefault(Locale.Category.FORMAT));
            formatter.setCalendar(calendar);
            calendar.setTime(formatter.parse(dateTime));
            formatter = new SimpleDateFormat(Constants.YEAR_FORMAT, Locale.getDefault(Locale.Category.FORMAT));
            year = formatter.format(calendar.getTime());
        } catch (ParseException e) {
//            LoggerUtil.error(LOG, String.format(Constants.LOG_PARSE_EXCEPTION, dateTime, dateTimePattern, timeZoneId),
//                    e);
        }
        return year;

    }


}
