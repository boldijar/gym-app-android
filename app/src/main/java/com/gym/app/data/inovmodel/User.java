package com.gym.app.data.inovmodel;

import com.google.gson.annotations.SerializedName;

/**
 * TODO: Class description
 *
 * @author Paul
 * @since 2018.03.10
 */

public class User {
    @SerializedName("points")
    public int mPoints;
    @SerializedName("avatar")
    public String mAvatar;
    @SerializedName("phone")
    public String mPhone;
    @SerializedName("role")
    public String mRole;
    @SerializedName("email")
    public String mEmail;
    @SerializedName("id")
    public int mId;
    @SerializedName("first_name")
    public String mFirstName;
    @SerializedName("last_name")
    public String mLastName;
}
