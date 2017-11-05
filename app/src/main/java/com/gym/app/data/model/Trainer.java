package com.gym.app.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Trainer POJO class
 *
 * @author catalinradoiu
 * @since 2017.01.01
 */

public class Trainer {

    @SerializedName("id")
    private int mId;

    @SerializedName("fullName")
    private String mFullName;

    @SerializedName("email")
    private String mEmail;

    @SerializedName("picture")
    private String mPictureUrl;


    //Test constructor
    public Trainer(int mId, String mFullName, String mEmail, String mPictureUrl) {
        this.mId = mId;
        this.mFullName = mFullName;
        this.mEmail = mEmail;
        this.mPictureUrl = mPictureUrl;
    }

    public int getId() {
        return mId;
    }

    public String getFullName() {
        return mFullName;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getPictureUrl() {
        return mPictureUrl;
    }
}
