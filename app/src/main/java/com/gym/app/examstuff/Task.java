package com.gym.app.examstuff;

import com.google.gson.annotations.SerializedName;

/**
 * TODO: Class description
 *
 * @author Paul
 * @since 2018.01.23
 */

public class Task {
    @SerializedName("id")
    public int mId;
    @SerializedName("text")
    public String mText;
    @SerializedName("date")
    public String mDate;
}
