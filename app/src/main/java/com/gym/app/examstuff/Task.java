package com.gym.app.examstuff;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * TODO: Class description
 *
 * @author Paul
 * @since 2018.01.23
 */

@Entity
public class Task {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    @SerializedName("id")
    public int mId;
    @SerializedName("text")
    @ColumnInfo(name = "text")
    public String mText;
    @ColumnInfo(name = "date")
    @SerializedName("date")
    public String mDate;
}
