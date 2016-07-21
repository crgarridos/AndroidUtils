package com.ylly.android.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by cristian on 11/01/2016
 */
public abstract class DateUtils {

    public static final String DATE_FORMAT_DEFAULT = "dd/MM/yyyy";

    /**
     * @return the days between the two dates
     */
    public static double getDaysBetweenDates(Date theEarlierDate, Date theLaterDate) {
        double result = Double.POSITIVE_INFINITY;
        if (theEarlierDate != null && theLaterDate != null) {
            final long MILLISECONDS_PER_DAY = 1000 * 60 * 60 * 24;
            Calendar aCal = Calendar.getInstance();
            aCal.setTime(theEarlierDate);
            long aFromOffset = aCal.get(Calendar.DST_OFFSET);
            aCal.setTime(theLaterDate);
            long aToOffset = aCal.get(Calendar.DST_OFFSET);
            long aDayDiffInMili = (theLaterDate.getTime() + aToOffset) - (theEarlierDate.getTime() + aFromOffset);
            result = ((double) aDayDiffInMili / MILLISECONDS_PER_DAY);
        }
        return result;
    }

    /* @return get he current time in milis*/
    public static long getTime() {
        return Calendar.getInstance().getTimeInMillis();
    }

    /** Parse a date for "{@value #DATE_FORMAT_DEFAULT}" mask
     * @see #parse(String, String) */
    public static Date parse(String date) {
        return parse(DATE_FORMAT_DEFAULT, date);
    }


    /** parse a date string for a given mask
     * @param dateMask the mask used to parse
     * @param dateToParse the date string
     * @return the equvalent date null, if the mask doesn't apply*/
    public static Date parse(String dateMask, String dateToParse) {
        try {
            return new SimpleDateFormat(dateMask, Locale.getDefault()).parse(dateToParse);
        } catch (ParseException e) {
            return null;
        }
    }


    public static String formatDate(Date date, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
        return sdf.format(date);
    }

    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_DEFAULT, Locale.getDefault());
        return sdf.format(date);
    }
}
