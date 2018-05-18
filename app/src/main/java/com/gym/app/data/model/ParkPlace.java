package com.gym.app.data.model;

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
    @SerializedName("description")
    public String mDescription;
    @SerializedName("size")
    public String mSize;
    @SerializedName("price_per_hour")
    public int mPricePerHour;
    @SerializedName("longitude")
    public double mLongitude;
    @SerializedName("latitude")
    public double mLatitude;
    @SerializedName("address")
    public String mAddress;
    @SerializedName("name")
    public String mName;
    @SerializedName("id")
    public int mId;
    @SerializedName("user")
    public User mUser;
}
