package com.gym.app.data.room;

import android.arch.persistence.room.TypeConverter;

import java.sql.Timestamp;

/**
 * @author Paul
 * @since 2017.09.25
 */

public class RoomConverters {

    @TypeConverter
    public static Timestamp longToTimestamp(Long value) {
        if (value == null) {
            return null;
        }
        return new Timestamp(value);

    }

    @TypeConverter
    public static Long timestampToLong(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        return timestamp.getTime();
    }

}
