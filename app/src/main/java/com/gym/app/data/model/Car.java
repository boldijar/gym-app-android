package com.gym.app.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Car {
//    @Expose
//    @SerializedName("user_id")
//    public int user_id;

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
//    @Expose
//    @SerializedName("id")
//    public int id;


//    @Override
//    public String toString() {
//        return "{" +
//                " \"model\": \"" + model  + "\"" +
//                ", \"size\": \"" + size  + "\"" +
//                ", \"plate\": \"" + plate + "\"" +
//                '}';
//    }
}
