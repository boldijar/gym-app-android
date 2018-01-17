package com.gym.app.parts.notes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
    private Snackbar mOperationSnackBar;
    private int mDeletePosition;
    private Note mNoteToDelete;
    private String mNewNoteText;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initPresenter();
        initAdapter();
        mNotesPresenter.getNotes();
    }

    @Override
    public void setNotes(List<Note> noteList) {
        mNotesAdapter.setNotesList(noteList);
    }

    @Override
    public void displayOperationStatus(MessageType messageType) {
        displaySnackBar(messageType);
    }

    @Override
    public void addNewNote(Note note) {
        mNotesAdapter.addNote(note);
        displaySnackBar(MessageType.ADD_SUCCESS);
    }

    @Override
    public void noteDeleted() {
        mNoteToDelete = null;
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

    private void initAdapter() {
        mNotesAdapter = new NotesAdapter();
        mNotesRecyclerView.setAdapter(mNotesAdapter);
        mNotesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mNotesAdapter.setOnDeleteClickListener(new NotesAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int position) {
                mDeletePosition = position;
                mNoteToDelete = mNotesAdapter.getNote(position);
                mNotesAdapter.deleteNote(mDeletePosition);
                displaySnackBar(MessageType.UNDO_DELETE);
            }
        });
    }

    private void displaySnackBar(MessageType messageType) {
        if (mOperationSnackBar != null && mOperationSnackBar.isShown()) {
            mOperationSnackBar.dismiss();
        }
        switch (messageType) {
            case UNDO_DELETE:
                mOperationSnackBar = Snackbar.make(mNotesRecyclerView, getString(R.string.note_deleted), Snackbar.LENGTH_LONG);
                mOperationSnackBar.setAction(getString(R.string.undo), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mNotesAdapter.addNote(mDeletePosition, mNotesAdapter.getNote(mDeletePosition));
                    }
                });
                mOperationSnackBar.addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        mNotesPresenter.deleteNote(mNoteToDelete.getId());
                        mOperationSnackBar.removeCallback(this);
                    }
                });
                mOperationSnackBar.show();
                break;
            case ADD_ERROR:
                mOperationSnackBar = Snackbar.make(mNotesRecyclerView, getString(R.string.note_add_error), Snackbar.LENGTH_LONG);
                mOperationSnackBar.setAction(getString(R.string.retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                mOperationSnackBar.show();
                break;
            case ADD_SUCCESS:
                mOperationSnackBar = Snackbar.make(mNotesRecyclerView, getString(R.string.note_add_success), Snackbar.LENGTH_LONG);
                mOperationSnackBar.show();
                break;
            case DELETE_ERROR:
                mNotesAdapter.addNote(mDeletePosition, mNoteToDelete);
                mOperationSnackBar = Snackbar.make(mNotesRecyclerView, getString(R.string.note_delete_error), Snackbar.LENGTH_LONG);
                mOperationSnackBar.setAction(getString(R.string.retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mNotesPresenter.deleteNote(mNoteToDelete.getId());
                    }
                });
                mOperationSnackBar.show();
                break;
        }
    }
}
