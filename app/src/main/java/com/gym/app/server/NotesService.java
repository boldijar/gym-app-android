package com.gym.app.server;

import com.gym.app.data.model.Note;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Service for api requests for the notes
 *
 * @author catalinradoiu
 * @since 2018.01.17
 */

public interface NotesService {

    @GET("api/user/notes")
    Single<List<Note>> getAllNotes();

    @POST("api/user/note")
    Single<Note> createNote(@Field("text") String text, @Field("creationDate") long creationTime);

    @DELETE("api/user/note/{id}")
    Completable deleteNote(@Path("id") int id);


}
