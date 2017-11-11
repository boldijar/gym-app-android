package com.gym.app.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

/**
 * Course POJO
 *
 * @author catalinradoiu
 * @since 2017.01.01
 */

@Entity(tableName = "courses")
public class Course {

    @SerializedName("id")
    @PrimaryKey
    @ColumnInfo(name = "id")
    private int mId;

    @SerializedName("name")
    @ColumnInfo(name = "name")
    private String mName;

    @SerializedName("image")
    @ColumnInfo(name = "image")
    private String mImage;

    @SerializedName("eventDate")
    @ColumnInfo(name = "courseDate")
    private long mCourseDate;

    @SerializedName("capacity")
    @ColumnInfo(name = "capacity")
    private int mCapacity;

    @SerializedName("registered_users")
    @ColumnInfo(name = "registeredUsers")
    private int mRegisteredUsersNumber;

    @Ignore
    @SerializedName("trainer")
    private Trainer mTrainer;

    /* Extra field used to check is the current user is registered to the current course or not
       Used only locally, in the database
       It's false by default and should be modified with its setter
    */
    @ColumnInfo(name = "isUserRegistered")
    private boolean mIsRegistered = false;

    @Override
    public boolean equals(Object obj) {
        return ((Course) obj).getId() == mId;
    }

    public Course() {
    }

    public int getId() {
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

    public String getImage() {
        return mImage;
    }

    public String getName() {
        return mName;
    }

    public Trainer getTrainer() {
        return mTrainer;
    }

    public void setCapacity(int mCapacity) {
        this.mCapacity = mCapacity;
    }

    public boolean isRegistered() {
        return mIsRegistered;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public void setImage(String mImage) {
        this.mImage = mImage;
    }

    public void setCourseDate(long mCourseDate) {
        this.mCourseDate = mCourseDate;
    }

    public void setRegisteredUsersNumber(int mRegisteredUsersNumber) {
        this.mRegisteredUsersNumber = mRegisteredUsersNumber;
    }

    public void setTrainer(Trainer mTrainer) {
        this.mTrainer = mTrainer;
    }

    public void setIsRegistered(boolean mIsRegistered) {
        this.mIsRegistered = mIsRegistered;
    }
}
