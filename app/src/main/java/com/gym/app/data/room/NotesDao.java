package com.gym.app.data.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.gym.app.data.model.Note;

import java.util.List;

import io.reactivex.Single;

/**
 * Dao for database notes manipulation
 *
 * @author catalinradoiu
 * @since 2018.01.17
 */

@Dao
public interface NotesDao {

    @Insert
    void insertAll(List<Note> noteList);

    @Insert
    void insertNote(Note note);

    @Delete
    void deleteNote(Note note);

    @Query("DELETE FROM notes")
    void deleteAll();

    @Query("SELECT * FROM notes")
    Single<List<Note>> getAllNotes();
}
