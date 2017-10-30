package com.gym.app.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * @author Paul
 * @since 2017.10.25
 */

@Entity
public class Product {

    @SerializedName("id")
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    public String mId;

    @ColumnInfo(name = "name")
    @SerializedName("name")
    public String mName;

    @ColumnInfo(name = "image")
    @SerializedName("image")
    public String mImage;

    @ColumnInfo(name = "description")
    @SerializedName("description")
    public String mDescription;

    @ColumnInfo(name = "price")
    @SerializedName("price")
    public String mPrice;
}
