package com.gym.app.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author catalinradoiu
 * @since 2017.11.18
 */

public class TimeUtils {

    private static final int ONE_HOUR_TIMESTAMP = 3600 * 1000;

    @SuppressLint("SimpleDateFormat")
    public static String formatToDate(long timestamp) {
        Date date = new Date(timestamp * 1000);
        Date endTime = new Date(timestamp * 1000 + ONE_HOUR_TIMESTAMP);
        SimpleDateFormat dayNameFormat = new SimpleDateFormat("EEEE");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");
        SimpleDateFormat dayTimeFormat = new SimpleDateFormat("kk:mm");
        return dayNameFormat.format(date) + ",  " + simpleDateFormat.format(date)
                + "\n" + dayTimeFormat.format(date) + "-" + dayTimeFormat.format(endTime);
    }
}
