package com.gym.app.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * TODO: Class description
 *
 * @author Paul
 * @since 2018.05.19
 */
public class CarBody {
    @SerializedName("car")
    public final Car mCar;

    public CarBody(Car car) {
        mCar = car;
    }
}
