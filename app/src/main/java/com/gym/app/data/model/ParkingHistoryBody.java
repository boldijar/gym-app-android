package com.gym.app.data.model;

import com.google.gson.annotations.SerializedName;

public class ParkingHistoryBody {
    @SerializedName("booking")
    public final ParkingHistory mParkingHistory;

    public ParkingHistoryBody(ParkingHistory ph){
        this.mParkingHistory = ph;
    }

}
