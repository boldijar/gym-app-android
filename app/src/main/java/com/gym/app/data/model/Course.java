package com.gym.app.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Course POJO
 *
 * @author catalinradoiu
 * @since 2017.01.01
 */

public class Course {

    @SerializedName("id")
    private int mId;

    @SerializedName("name")
    private String mName;

    @SerializedName("image")
    private String mImage;

    @SerializedName("eventDate")
    private long mCourseDate;

    @SerializedName("capacity")
    private int mCapacity;

    @SerializedName("registered_users")
    private int mRegisteredUsersNumber;

    @SerializedName("trainer")
    private Trainer mTrainer;


    //Test constructor
    public Course(int mId, long mCourseDate, int mCapacity, int mRegisteredUsersNumber, Trainer mTrainer) {
        this.mId = mId;
        this.mCourseDate = mCourseDate;
        this.mCapacity = mCapacity;
        this.mRegisteredUsersNumber = mRegisteredUsersNumber;
        this.mTrainer = mTrainer;
    }

    public int getmId() {
        return mId;
    }

    public long getCourseDate() {
        return mCourseDate;
    }

    public int getCapacity() {
        return mCapacity;
    }

    public int getRegisteredUsersNumber() {
        return mRegisteredUsersNumber;
    }

    public String getImage(){
        return mImage;
    }

    public String getName(){
        return mName;
    }

    public Trainer getTrainer(){
        return mTrainer;
    }

    public void setmCapacity(int mCapacity) {
        this.mCapacity = mCapacity;
    }
}
