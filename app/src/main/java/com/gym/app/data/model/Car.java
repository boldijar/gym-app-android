package com.gym.app.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Car {
    @SerializedName("id")
    private int id;

    @SerializedName("user_id")
    private int user_id;

    @Expose
    @SerializedName("model")
    private String model;

    @Expose
    @SerializedName("size")
    private String size;

    @Expose
    @SerializedName("plate")
    private String plate;


    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    //    @Override
//    public String toString() {
//        return "{" +
//                " \"model\": \"" + model  + "\"" +
//                ", \"size\": \"" + size  + "\"" +
//                ", \"plate\": \"" + plate + "\"" +
//                '}';
//    }
}
