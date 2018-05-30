package com.scent.feedservices.Util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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

    public static String addAdvanceDaysToGivenDate(String startDate, int advanceNoOfDays,
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
        return formatter.format(cal.getTime());
    }

}
