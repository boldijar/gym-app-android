package com.gym.app.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.File;

/**
 * Created by flaviuoprea on 11/12/17.
 */

public class User {
    @SerializedName("id")
    public int mId;
    @SerializedName("fullName")
    public String mFullName;
    @SerializedName("password")
    public String mPassword;
    @SerializedName("email")
    public String mEmail;
    @SerializedName("image")
    public File mImage;
    @SerializedName("isAtTheGym")
    public Boolean mIsAtTheGym;
}
