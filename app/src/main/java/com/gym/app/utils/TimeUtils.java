package com.gym.app.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author catalinradoiu
 * @since 2017.11.18
 */

public class TimeUtils {

    public static String formatToDate(long timestamp) {
        Date date = new Date(timestamp * 1000);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dayTimeFormat = new SimpleDateFormat("kk:mm");
        return simpleDateFormat.format(date) + "\n" + dayTimeFormat.format(date);
    }
}
