package com.gym.app.data.model;

import android.content.Context;

public class ParkingHistory {

    private String spot;

    public ParkingHistory(String spot){
        this.spot = spot;
    }

    public String getSpot(Context mContext){
        return spot;
    }

    public void setSpot(String spot){
        this.spot = spot;
    }

    public String getSpot() {
        return spot;
    }
}
