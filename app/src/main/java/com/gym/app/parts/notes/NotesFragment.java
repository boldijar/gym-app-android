package com.gym.app.parts.notes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gym.app.R;
import com.gym.app.data.model.Note;
import com.gym.app.parts.home.BaseHomeFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author catalinradoiu
 * @since 2018.01.17
 */
public class NotesFragment extends BaseHomeFragment implements NotesView {

    @BindView(R.id.notes_recycler_view)
    RecyclerView mNotesRecyclerView;

    private NotesPresenter mNotesPresenter;
    private NotesAdapter mNotesAdapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initPresenter();
        initNotesList();
    }

    @Override
    public void setNotes(List<Note> noteList) {
        mNotesAdapter.setNotesList(noteList);
    }

    @Override
    protected int getTitle() {
        return R.string.notes;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_notes;
    }

    private void initPresenter() {
        mNotesPresenter = new NotesPresenter(this);
    }

    private void initNotesList() {
        mNotesAdapter = new NotesAdapter();
        mNotesRecyclerView.setAdapter(mNotesAdapter);
        mNotesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mNotesPresenter.getNotes();
    }
}
