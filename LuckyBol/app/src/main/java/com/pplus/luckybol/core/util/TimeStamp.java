package com.pplus.luckybol.core.util;

import java.util.Calendar;

/**
 * Created by Windows7-00 on 2016-11-09.
 */

public class TimeStamp {

    private static TimeStamp mInstance;
    public Calendar mStart;
    public Calendar mLap;
    private Calendar mEnd;

    public static TimeStamp getInstance() {
        if (mInstance == null) {
            mInstance = new TimeStamp();
        }
        return mInstance;
    }

    public void start() {
        mStart = Calendar.getInstance();
        mLap = Calendar.getInstance();
    }

    public String getLap() {
        Calendar curTime = Calendar.getInstance();
        long lap = curTime.getTimeInMillis() - mLap.getTimeInMillis();
        mLap = curTime;
        long min = lap / 1000 / 60;
        long sec = (lap / 1000) % 60;
        long millisec = lap % 1000;
        return String.format("%d min %d sec %d millisec", min, sec, millisec);
    }

}
