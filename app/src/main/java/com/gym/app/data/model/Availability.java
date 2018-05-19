package com.gym.app.data.model;

import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Class description
 *
 * @author Paul
 * @since 2018.05.19
 */
public class Availability {
    @SerializedName("schedule")
    public String mSchedule;
    @SerializedName("description")
    public String mDescription;
    @SerializedName("end_datetime")
    public Timestamp mEndDatetime;
    @SerializedName("start_datetime")
    public Timestamp mStartDatetime;
    @SerializedName("updated_at")
    public String mUpdatedAt;
    @SerializedName("created_at")
    public String mCreatedAt;
    @SerializedName("id")
    public int mId;
    @SerializedName("park_spot_id")
    public int mParkSpotId;
    @SerializedName("park_spot")
    public ParkPlace mParkPlace;

    public static Map createAvailabilityBody(String schedule) {
        HashMap map = new HashMap();
        HashMap availability = new HashMap();
        availability.put("schedule", schedule);
        map.put("availability", availability);
        return map;
    }
}
