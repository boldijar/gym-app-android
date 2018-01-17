package com.gym.app.parts.notes;

import com.gym.app.data.model.Note;

import java.util.List;

/**
 * @author catalinradoiu
 * @since 2018.01.17
 */
interface NotesView {

    void setNotes(List<Note> noteList);
}
