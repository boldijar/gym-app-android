package com.gym.app.parts.notes;

import com.gym.app.data.model.Note;

import java.util.List;

/**
 * @author catalinradoiu
 * @since 2018.01.17
 */
interface NotesView {

    enum MessageType {
        UNDO_DELETE, ADD_ERROR, ADD_SUCCESS, DELETE_ERROR
    }

    void setNotes(List<Note> noteList);

    void displayOperationStatus(MessageType messageType);

    void addNewNote(Note note);

    void noteDeleted();
}
