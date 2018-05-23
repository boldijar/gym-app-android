package com.gym.app.data.model;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ParkingHistory {

    @SerializedName("id")
    private int id;

    @SerializedName("start_datetime")
    private String start_date;

    @SerializedName("end_datetime")
    private String end_date;

    @SerializedName("park_spot")
    private ParkPlace mParkSpot;

    public ParkingHistory(){}

    public ParkPlace getParkSpot() {
        return mParkSpot;
    }


    public int getId(){
        return id;
    }

    public String getStart_date(){
        return start_date;
    }

    public String getEnd_date(){
        return end_date;
    }


    public void setId(int id){
        this.id = id;
    }



    public void setStart_date(String sd){
        this.start_date = sd;
    }

    public void setEnd_date(String end){
        this.end_date = end;
    }

    @Override
    public String toString(){
        return null;
    }
}
