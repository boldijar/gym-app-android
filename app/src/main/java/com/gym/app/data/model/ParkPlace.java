package com.gym.app.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.gym.app.data.inovmodel.User;

/**
 * TODO: Class description
 *
 * @author Paul
 * @since 2018.03.10
 */

public class ParkPlace {
    @SerializedName("user_id")
    public int mUserId;

    @Expose
    @SerializedName("description")
    public String mDescription;

    @Expose
    @SerializedName("size")
    public String mSize;

    @Expose
    @SerializedName("price_per_hour")
    public int mPricePerHour;

    @Expose
    @SerializedName("longitude")
    public double mLongitude;

    @Expose
    @SerializedName("latitude")
    public double mLatitude;

    @Expose
    @SerializedName("address")
    public String mAddress;

    @SerializedName("name")
    @Expose
    public String mName;

    @SerializedName("id")
    public int mId;

    @Expose
    @SerializedName("user")
    public User mUser;
}
