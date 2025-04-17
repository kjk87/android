/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pplus.utils.part.utils.time;

import android.content.Context;

import com.pplus.utils.R;
import com.pplus.utils.part.logs.LogUtil;
import com.pplus.utils.part.pref.PreferenceUtil;
import com.pplus.utils.part.utils.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * <p>Date and time formatting utilities and constants.</p>
 * <p>
 * <p>Formatting is performed using the thread-safe
 * <p>
 * <p>Note that the JDK has a bug wherein calling Calendar.get(int) will override any previously
 * called Calendar.clear() calls. See LANG-755.</p>
 *
 * @version $Id: DateFormatUtils.java 1612038 2014-07-20 06:46:57Z ggregory $
 * @since 2.0
 */
public class DateFormatUtils{
    private static final String LOG_TAG = DateFormatUtils.class.getSimpleName();
    /**
     * The UTC time zone (often referred to as GMT). This is private as it is mutable.
     */
    private static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("GMT");
    /**
     * ISO 8601 formatter for date-time without time zone. The format used is {@code
     * yyyy-MM-dd'T'HH:mm:ss}.
     */
    public static final FastDateFormat ISO_DATETIME_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss");

    /**
     * ISO 8601 formatter for date-time with time zone. The format used is {@code
     * yyyy-MM-dd'T'HH:mm:ssZZ}.
     */
    public static final FastDateFormat ISO_DATETIME_TIME_ZONE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ssZZ");

    /**
     * ISO 8601 formatter for date without time zone. The format used is {@code yyyy-MM-dd}.
     */
    public static final FastDateFormat ISO_DATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd");

    /**
     * ISO 8601-like formatter for date with time zone. The format used is {@code yyyy-MM-ddZZ}.
     * This pattern does not comply with the formal ISO 8601 specification as the standard does not
     * allow a time zone  without a time.
     */
    public static final FastDateFormat ISO_DATE_TIME_ZONE_FORMAT = FastDateFormat.getInstance("yyyy-MM-ddZZ");

    /**
     * ISO 8601 formatter for time without time zone. The format used is {@code 'T'HH:mm:ss}.
     */
    public static final FastDateFormat ISO_TIME_FORMAT = FastDateFormat.getInstance("'T'HH:mm:ss");

    /**
     * ISO 8601 formatter for time with time zone. The format used is {@code 'T'HH:mm:ssZZ}.
     */
    public static final FastDateFormat ISO_TIME_TIME_ZONE_FORMAT = FastDateFormat.getInstance("'T'HH:mm:ssZZ");

    /**
     * ISO 8601-like formatter for time without time zone. The format used is {@code HH:mm:ss}. This
     * pattern does not comply with the formal ISO 8601 specification as the standard requires the
     * 'T' prefix for times.
     */
    public static final FastDateFormat ISO_TIME_NO_T_FORMAT = FastDateFormat.getInstance("HH:mm:ss");

    /**
     * ISO 8601-like formatter for time with time zone. The format used is {@code HH:mm:ssZZ}. This
     * pattern does not comply with the formal ISO 8601 specification as the standard requires the
     * 'T' prefix for times.
     */
    public static final FastDateFormat ISO_TIME_NO_T_TIME_ZONE_FORMAT = FastDateFormat.getInstance("HH:mm:ssZZ");

    /**
     * SMTP (and probably other) date headers. The format used is {@code EEE, dd MMM yyyy HH:mm:ss
     * Z} in US locale.
     */
    public static final FastDateFormat SMTP_DATETIME_FORMAT = FastDateFormat.getInstance("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);

    public static final FastDateFormat PPLUS_DATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");

    //-----------------------------------------------------------------------

    /**
     * <p>DateFormatUtils instances should NOT be constructed in standard programming.</p>
     * <p>
     * <p>This constructor is public to permit tools that require a JavaBean instance to
     * operate.</p>
     */
    public DateFormatUtils(){

        super();
    }

    /**
     * <p>Formats a date/time into a specific pattern using the UTC time zone.</p>
     *
     * @param millis  the date to format expressed in milliseconds
     * @param pattern the pattern to use to format the date, not null
     *
     * @return the formatted date
     */
    public static String formatUTC(final long millis, final String pattern){

        return format(new Date(millis), pattern, UTC_TIME_ZONE, null);
    }

    /**
     * <p>Formats a date/time into a specific pattern using the UTC time zone.</p>
     *
     * @param date    the date to format, not null
     * @param pattern the pattern to use to format the date, not null
     *
     * @return the formatted date
     */
    public static String formatUTC(final Date date, final String pattern){

        return format(date, pattern, UTC_TIME_ZONE, null);
    }

    /**
     * <p>Formats a date/time into a specific pattern using the UTC time zone.</p>
     *
     * @param millis  the date to format expressed in milliseconds
     * @param pattern the pattern to use to format the date, not null
     * @param locale  the locale to use, may be <code>null</code>
     *
     * @return the formatted date
     */
    public static String formatUTC(final long millis, final String pattern, final Locale locale){

        return format(new Date(millis), pattern, UTC_TIME_ZONE, locale);
    }

    /**
     * <p>Formats a date/time into a specific pattern using the UTC time zone.</p>
     *
     * @param date    the date to format, not null
     * @param pattern the pattern to use to format the date, not null
     * @param locale  the locale to use, may be <code>null</code>
     *
     * @return the formatted date
     */
    public static String formatUTC(final Date date, final String pattern, final Locale locale){

        return format(date, pattern, UTC_TIME_ZONE, locale);
    }

    /**
     * <p>Formats a date/time into a specific pattern.</p>
     *
     * @param millis  the date to format expressed in milliseconds
     * @param pattern the pattern to use to format the date, not null
     *
     * @return the formatted date
     */
    public static String format(final long millis, final String pattern){

        return format(new Date(millis), pattern, null, null);
    }

    /**
     * <p>Formats a date/time into a specific pattern.</p>
     *
     * @param date    the date to format, not null
     * @param pattern the pattern to use to format the date, not null
     *
     * @return the formatted date
     */
    public static String format(final Date date, final String pattern){

        return format(date, pattern, null, null);
    }

    /**
     * <p>Formats a calendar into a specific pattern.</p>
     *
     * @param calendar the calendar to format, not null
     * @param pattern  the pattern to use to format the calendar, not null
     *
     * @return the formatted calendar
     *
     * @see FastDateFormat#format(Calendar)
     * @since 2.4
     */
    public static String format(final Calendar calendar, final String pattern){

        return format(calendar, pattern, null, null);
    }

    /**
     * <p>Formats a date/time into a specific pattern in a time zone.</p>
     *
     * @param millis   the time expressed in milliseconds
     * @param pattern  the pattern to use to format the date, not null
     * @param timeZone the time zone  to use, may be <code>null</code>
     *
     * @return the formatted date
     */
    public static String format(final long millis, final String pattern, final TimeZone timeZone){

        return format(new Date(millis), pattern, timeZone, null);
    }

    /**
     * <p>Formats a date/time into a specific pattern in a time zone.</p>
     *
     * @param date     the date to format, not null
     * @param pattern  the pattern to use to format the date, not null
     * @param timeZone the time zone  to use, may be <code>null</code>
     *
     * @return the formatted date
     */
    public static String format(final Date date, final String pattern, final TimeZone timeZone){

        return format(date, pattern, timeZone, null);
    }

    /**
     * <p>Formats a calendar into a specific pattern in a time zone.</p>
     *
     * @param calendar the calendar to format, not null
     * @param pattern  the pattern to use to format the calendar, not null
     * @param timeZone the time zone  to use, may be <code>null</code>
     *
     * @return the formatted calendar
     *
     * @see FastDateFormat#format(Calendar)
     * @since 2.4
     */
    public static String format(final Calendar calendar, final String pattern, final TimeZone timeZone){

        return format(calendar, pattern, timeZone, null);
    }

    /**
     * <p>Formats a date/time into a specific pattern in a locale.</p>
     *
     * @param millis  the date to format expressed in milliseconds
     * @param pattern the pattern to use to format the date, not null
     * @param locale  the locale to use, may be <code>null</code>
     *
     * @return the formatted date
     */
    public static String format(final long millis, final String pattern, final Locale locale){

        return format(new Date(millis), pattern, null, locale);
    }

    /**
     * <p>Formats a date/time into a specific pattern in a locale.</p>
     *
     * @param date    the date to format, not null
     * @param pattern the pattern to use to format the date, not null
     * @param locale  the locale to use, may be <code>null</code>
     *
     * @return the formatted date
     */
    public static String format(final Date date, final String pattern, final Locale locale){

        return format(date, pattern, null, locale);
    }

    /**
     * <p>Formats a calendar into a specific pattern in a locale.</p>
     *
     * @param calendar the calendar to format, not null
     * @param pattern  the pattern to use to format the calendar, not null
     * @param locale   the locale to use, may be <code>null</code>
     *
     * @return the formatted calendar
     *
     * @see FastDateFormat#format(Calendar)
     * @since 2.4
     */
    public static String format(final Calendar calendar, final String pattern, final Locale locale){

        return format(calendar, pattern, null, locale);
    }

    /**
     * <p>Formats a date/time into a specific pattern in a time zone  and locale.</p>
     *
     * @param millis   the date to format expressed in milliseconds
     * @param pattern  the pattern to use to format the date, not null
     * @param timeZone the time zone  to use, may be <code>null</code>
     * @param locale   the locale to use, may be <code>null</code>
     *
     * @return the formatted date
     */
    public static String format(final long millis, final String pattern, final TimeZone timeZone, final Locale locale){

        return format(new Date(millis), pattern, timeZone, locale);
    }

    /**
     * <p>Formats a date/time into a specific pattern in a time zone  and locale.</p>
     *
     * @param date     the date to format, not null
     * @param pattern  the pattern to use to format the date, not null, not null
     * @param timeZone the time zone  to use, may be <code>null</code>
     * @param locale   the locale to use, may be <code>null</code>
     *
     * @return the formatted date
     */
    public static String format(final Date date, final String pattern, final TimeZone timeZone, final Locale locale){

        final FastDateFormat df = FastDateFormat.getInstance(pattern, timeZone, locale);
        return df.format(date);
    }

    /**
     * <p>Formats a calendar into a specific pattern in a time zone  and locale.</p>
     *
     * @param calendar the calendar to format, not null
     * @param pattern  the pattern to use to format the calendar, not null
     * @param timeZone the time zone  to use, may be <code>null</code>
     * @param locale   the locale to use, may be <code>null</code>
     *
     * @return the formatted calendar
     *
     * @see FastDateFormat#format(Calendar)
     * @since 2.4
     */
    public static String format(final Calendar calendar, final String pattern, final TimeZone timeZone, final Locale locale){

        final FastDateFormat df = FastDateFormat.getInstance(pattern, timeZone, locale);
        return df.format(calendar);
    }

    public static String convertDateFormat(String strDate, String pattern){

        SimpleDateFormat format = new java.text.SimpleDateFormat(PPLUS_DATE_FORMAT.getPattern(), Locale.getDefault());
        Date date = new Date();
        try {
            date = format.parse(strDate);
        } catch (ParseException e) {
            LogUtil.e(LOG_TAG, e.toString());
        }
        SimpleDateFormat format1 = new java.text.SimpleDateFormat(pattern);
        String dateString = format1.format(date);

        return dateString;
    }

    public static String convertDateFormat(FastDateFormat format, String strDate, String pattern){

        Date date = new Date();
        try {
            date = format.parse(strDate);
        } catch (ParseException e) {
            LogUtil.e(LOG_TAG, e.toString());
        }
        SimpleDateFormat format1 = new java.text.SimpleDateFormat(pattern);
        String dateString = format1.format(date);

        return dateString;
    }

    private static class TIME_MAXIMUM{

        public static final int SEC = 60;
        public static final int MIN = 60;
        public static final int HOUR = 24;
        public static final int DAY = 30;
        public static final int MONTH = 12;
    }

    public static String formatTime(int format){
        if (format < 10) {
            return "0"+format;
        } else {
            return String.valueOf(format);
        }
    }

    public static String formatPostTimeString(Context context, String strDate){

        //        FastDateFormat PPLUS_DATE_FORMAT
        //                = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.s");
        //        2016-10-05 20:11:22.0
        if(StringUtils.isEmpty(strDate)) {
            return "";
        }

        SimpleDateFormat format = new java.text.SimpleDateFormat(PPLUS_DATE_FORMAT.getPattern(), Locale.getDefault());
        Date date = new Date();

        try {
            date = format.parse(strDate);
        } catch (ParseException e) {
            LogUtil.e(LOG_TAG, e.toString());
        }

        long curTime = System.currentTimeMillis();
        long regTime = date.getTime();
        long diffTime = (curTime - regTime) / 1000;

        String msg = null;
        if(diffTime < TIME_MAXIMUM.SEC) {
            // sec
            msg = context.getString(R.string.just_before);
        } else if((diffTime /= TIME_MAXIMUM.SEC) < TIME_MAXIMUM.MIN) {
            // min
            msg = diffTime + context.getString(R.string.minutes_ago);
        } else if((diffTime /= TIME_MAXIMUM.MIN) < TIME_MAXIMUM.HOUR) {
            // hour
            msg = (diffTime) + context.getString(R.string.hours_ago);
        } else if((diffTime /= TIME_MAXIMUM.HOUR) < TIME_MAXIMUM.DAY) {
            // day
            msg = new SimpleDateFormat(context.getString(R.string.month_day)).format(date);
            //            msg = (diffTime) + "일 전";
        } else{
            if(new SimpleDateFormat("yyyy").format(date).equals(new SimpleDateFormat("yyyy").format(new Date(curTime)))){
                msg = new SimpleDateFormat("MM.dd").format(date);
            }else{
                msg = new SimpleDateFormat("yyyy.MM.dd").format(date);
            }

        }
//        if((diffTime /= TIME_MAXIMUM.DAY) < TIME_MAXIMUM.MONTH) {
//            // day
//            //            msg = (diffTime) + "달 전";
//            msg = new SimpleDateFormat("MM.dd").format(date);
//        } else {
//            //            msg = (diffTime) + "년 전";
//            msg = new SimpleDateFormat("yyyy.MM.dd").format(date);
//        }

        return msg;
    }

    public static String formatPromotionWinsDate(String dateTime) {

        if(StringUtils.isEmpty(dateTime)){
            return "";
        }

        SimpleDateFormat format = new java.text.SimpleDateFormat(PPLUS_DATE_FORMAT.getPattern(), Locale.getDefault());
        Date srcDate = new Date();
        try {
            srcDate = format.parse(dateTime);

        } catch (ParseException e) {
            LogUtil.e(LOG_TAG, e.toString());
        }

        String destDate = new SimpleDateFormat("yyyy.MM.dd HH:mm").format(srcDate);

        return destDate;
    }

    public static String formatPromotionTimes(String start, String end){

        if(StringUtils.isEmpty(start)) {
            return "";
        }

        SimpleDateFormat format = new java.text.SimpleDateFormat(PPLUS_DATE_FORMAT.getPattern(), Locale.getDefault());
        Date startDate = new Date();
        Date endDate = new Date();
        try {
            startDate = format.parse(start);
            endDate = format.parse(end);

        } catch (ParseException e) {
            LogUtil.e(LOG_TAG, e.toString());
        }

        String startDateString = new SimpleDateFormat("MM-dd HH:mm").format(startDate);
        String endDateString = new SimpleDateFormat("MM-dd HH:mm").format(endDate);
        StringBuffer msg = new StringBuffer(startDateString);
        msg.append(" ~ ").append(endDateString);

        return msg.toString();
    }

    public static String formatPromotionTimes(String start, String end , String returnFormat){

        if(StringUtils.isEmpty(start)) {
            return "";
        }

        SimpleDateFormat format = new java.text.SimpleDateFormat(PPLUS_DATE_FORMAT.getPattern(), Locale.getDefault());
        Date startDate = new Date();
        Date endDate = new Date();
        try {
            startDate = format.parse(start);
            endDate = format.parse(end);

        } catch (ParseException e) {
            LogUtil.e(LOG_TAG, e.toString());
        }

        String startDateString = new SimpleDateFormat(returnFormat).format(startDate);
        String endDateString = new SimpleDateFormat(returnFormat).format(endDate);
        StringBuffer msg = new StringBuffer(startDateString);
        msg.append(" ~ ").append(endDateString);

        return msg.toString();
    }

    public static String formatPromotionDate(String start, String returnFormat){

        if(StringUtils.isEmpty(start)) {
            return "";
        }

        SimpleDateFormat format = new java.text.SimpleDateFormat(ISO_DATE_FORMAT.getPattern());
        Date date = new Date();
        try {
            date = format.parse(start);

        } catch (ParseException e) {
            LogUtil.e(LOG_TAG, e.toString());
        }

        String formatDate = new SimpleDateFormat(returnFormat).format(date);

        return formatDate;
    }

    public static String formatPromotionTime(String start, String returnFormat){

        if(StringUtils.isEmpty(start)) {
            return "";
        }

        SimpleDateFormat format = new java.text.SimpleDateFormat(ISO_TIME_NO_T_FORMAT.getPattern());
        Date date = new Date();
        try {
            date = format.parse(start);

        } catch (ParseException e) {
            LogUtil.e(LOG_TAG, e.toString());
        }

        String formatDate = new SimpleDateFormat(returnFormat).format(date);

        return formatDate;
    }

}
