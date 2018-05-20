package com.gym.app.data.model;

import com.google.gson.annotations.SerializedName;
import com.gym.app.data.inovmodel.User;

/**
 * TODO: Class description
 *
 * @author Paul
 * @since 2018.03.10
 */

public class ParkPlaceBody {
    @SerializedName("park_spot")
    public final ParkPlace mPark;

    public ParkPlaceBody(ParkPlace park) {
        mPark = park;
    }
}