package com.gym.app.data.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.gym.app.data.model.Event;

/**
 * @author Paul
 * @since 2017.09.25
 */

@Database(entities = {Event.class}, version = 1)
@TypeConverters({RoomConverters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract AppDao eventsDao();
}
