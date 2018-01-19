package com.gym.app.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
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

    @SerializedName("registeredUsers")
    @ColumnInfo(name = "registeredUsers")
    private int mRegisteredUsersNumber;

    @Embedded
    @SerializedName("trainer")
    private Trainer mTrainer;

    @SerializedName("amRegistered")
    @ColumnInfo(name = "isUserRegistered")
    private int mIsRegistered = 0;

    /*  Extra field used to check if the current user ( trainer ) owns the course or not
        User only locally, in the database
        It's false by default and should be modified with its setter
     */
    @ColumnInfo(name = "trained")
    private int mIsTrained = 0;

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Course && ((Course) obj).getId() == mId;
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

    public void setCapacity(int capacity) {
        this.mCapacity = capacity;
    }

    public int isRegistered() {
        return mIsRegistered;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public void setImage(String image) {
        this.mImage = image;
    }

    public void setCourseDate(long courseDate) {
        this.mCourseDate = courseDate;
    }

    public void setRegisteredUsersNumber(int mRegisteredUsersNumber) {
        this.mRegisteredUsersNumber = mRegisteredUsersNumber;
    }

    public void setTrainer(Trainer trainer) {
        this.mTrainer = trainer;
    }

    public void setIsRegistered(int isRegistered) {
        this.mIsRegistered = isRegistered;
    }

    public void setIsTrained(int isTrained) {
        this.mIsTrained = isTrained;
    }

    public int getIsTrained() {
        return mIsTrained;
    }
}
