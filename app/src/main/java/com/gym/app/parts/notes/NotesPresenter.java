package com.gym.app.parts.notes;

import com.gym.app.data.SystemUtils;
import com.gym.app.data.model.Note;
import com.gym.app.data.room.AppDatabase;
import com.gym.app.di.InjectionHelper;
import com.gym.app.presenter.Presenter;
import com.gym.app.server.NotesService;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author catalinradoiu
 * @since 2018.01.17
 */
public class NotesPresenter extends Presenter<NotesView> {

    @Inject
    NotesService mNotesService;

    @Inject
    AppDatabase mAppDatabase;

    @Inject
    SystemUtils mSystemUtils;

    NotesPresenter(NotesView view) {
        super(view);
        InjectionHelper.getApplicationComponent().inject(this);
    }

    void getNotes() {
        addDisposable(mNotesService.getAllNotes().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Note>>() {
                    @Override
                    public void accept(List<Note> notes) throws Exception {
                        getView().setNotes(notes);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
    }
}
