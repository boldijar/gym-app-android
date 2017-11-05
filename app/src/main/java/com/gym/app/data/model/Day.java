package com.gym.app.data.model;

/**
 * Created by catalinradoiu on 03/11/2017.
 */

public class Day {

    private String mDayName;
    private long mStartTime;
    private long mEndTime;

    public Day(String dayName, long startTime, long endTime) {
        this.mDayName = dayName;
        this.mStartTime = startTime;
        this.mEndTime = endTime;
    }

    public String getDayName() {
        return mDayName;
    }

    public long getStartTime() {
        return mStartTime;
    }

    public long getEndTime() {
        return mEndTime;
    }
}
