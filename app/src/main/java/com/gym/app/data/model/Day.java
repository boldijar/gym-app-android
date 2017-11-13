package com.gym.app.data.model;

/**
 * @author catalinradoiu
 * @since 2017.11.05
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

    public void setDayName(String dayName) {
        this.mDayName = dayName;
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
