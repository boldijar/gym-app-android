package com.gym.app.examstuff;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Single;

/**
 * Dao for database notes manipulation
 *
 * @author catalinradoiu
 * @since 2018.01.17
 */

@Dao
public interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Task> noteList);

    @Query("SELECT * FROM task")
    Single<List<Task>> getTasks();
}
