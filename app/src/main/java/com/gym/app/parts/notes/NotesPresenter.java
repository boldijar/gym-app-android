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
import io.reactivex.functions.Action;
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
        if (mSystemUtils.isNetworkUnavailable()) {
            loadNotesOffline();
        } else {
            addDisposable(mNotesService.getAllNotes()
                    .doOnSuccess(new Consumer<List<Note>>() {
                        @Override
                        public void accept(List<Note> notes) throws Exception {
                            mAppDatabase.notesDao().deleteAll();
                            mAppDatabase.notesDao().insertAll(notes);
                        }
                    })
                    .subscribeOn(Schedulers.io())
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

    void deleteNote(final int noteId) {
        addDisposable(mNotesService.deleteNote(noteId)
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        mAppDatabase.notesDao().deleteNote(noteId);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        getView().noteDeleted();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getView().displayOperationStatus(NotesView.MessageType.DELETE_ERROR);
                    }
                }));
    }

    void addNote(String note) {
        addDisposable(mNotesService.createNote(note)
                .doOnSuccess(new Consumer<Note>() {
                    @Override
                    public void accept(Note note) throws Exception {
                        mAppDatabase.notesDao().insertNote(note);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Note>() {
                    @Override
                    public void accept(Note note) throws Exception {
                        getView().addNewNote(note);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getView().displayOperationStatus(NotesView.MessageType.ADD_ERROR);
                    }
                }));
    }

    private void loadNotesOffline() {
        addDisposable(mAppDatabase.notesDao().getAllNotes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Note>>() {
                    @Override
                    public void accept(List<Note> notes) throws Exception {
                        if (notes.size() == 0){
                            getView().setReloadState();
                        } else {
                            getView().setNotes(notes);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getView().setReloadState();
                    }
                }));
    }
}
