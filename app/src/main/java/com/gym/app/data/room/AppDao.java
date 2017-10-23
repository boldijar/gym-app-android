package com.gym.app.data.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.gym.app.data.model.Event;

import java.util.List;

/**
 * @author Paul
 * @since 2017.09.25
 */

@Dao
interface AppDao {

    @Query("select * from event")
    List<Event> getEvents();
}
