package com.gym.app.data.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.gym.app.data.model.Course;
import com.gym.app.data.model.Note;
import com.gym.app.data.model.Product;
import com.gym.app.examstuff.Task;
import com.gym.app.examstuff.TaskDao;

/**
 * @author Paul
 * @since 2017.09.25
 */

@Database(entities = {Product.class, Course.class, Note.class, Task.class}, version = 8)
@TypeConverters({RoomConverters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract AppDao getDao();

    public abstract CoursesDao getCoursesDao();

    public abstract NotesDao notesDao();

    public abstract TaskDao taskDao();
}
