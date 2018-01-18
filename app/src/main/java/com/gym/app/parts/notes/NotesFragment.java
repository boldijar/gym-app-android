package com.gym.app.parts.notes;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.gym.app.R;
import com.gym.app.data.model.Note;
import com.gym.app.parts.home.BaseHomeFragment;
import com.gym.app.view.EmptyLayout;

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

    @BindView(R.id.notes_empty_layout)
    EmptyLayout mEmptyLayout;

    private NotesPresenter mNotesPresenter;
    private NotesAdapter mNotesAdapter;
    private Snackbar mOperationSnackBar;
    private int mDeletePosition;
    private Note mNoteToDelete;
    private String mNewNoteText;
    private Snackbar.Callback mUndoDismissCallback;
    private boolean mCanDelete;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mCanDelete = true;
        initStateLayout();
        initPresenter();
        initAdapter();
        mNotesPresenter.getNotes();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_add_note, menu);
    }

    @SuppressLint("InflateParams")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.new_note_item) {
            final AlertDialog addNoteDialog = new AlertDialog.Builder(getContext())
                    .setView(getLayoutInflater().inflate(R.layout.dialog_layout_add_note, null))
                    .create();
            addNoteDialog.show();
            Button addNoteButton = addNoteDialog.findViewById(R.id.new_note_button);
            final TextInputEditText noteInput = addNoteDialog.findViewById(R.id.note_text_input);
            final TextInputLayout noteLayout = addNoteDialog.findViewById(R.id.note_text_layout);
            if (addNoteButton != null && noteInput != null && noteLayout != null) {
                addNoteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (noteInput.getText().toString().length() < 5) {
                            noteLayout.setError(getString(R.string.note_text_invalid));
                        } else {
                            mNewNoteText = noteInput.getText().toString();
                            mNotesPresenter.addNote(mNewNoteText);
                            addNoteDialog.dismiss();
                        }
                    }
                });
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setNotes(List<Note> noteList) {
        if (noteList.size() > 0) {
            mEmptyLayout.setState(EmptyLayout.State.CLEAR);
            mNotesRecyclerView.setVisibility(View.VISIBLE);
            mNotesAdapter.setNotesList(noteList);
        } else {
            mNotesRecyclerView.setVisibility(View.GONE);
            mEmptyLayout.setState(EmptyLayout.State.EMPTY_NO_BUTTON, R.string.no_notes_created);
        }
    }

    @Override
    public void setReloadState() {
        mEmptyLayout.setState(EmptyLayout.State.EMPTY, R.string.could_not_find_notes);
        mNotesRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void displayOperationStatus(MessageType messageType) {
        displaySnackBar(messageType);
    }

    @Override
    public void addNewNote(Note note) {
        mNewNoteText = null;
        mNotesAdapter.addNote(note);
        if (mNotesAdapter.getItemCount() == 1) {
            mNotesRecyclerView.setVisibility(View.VISIBLE);
            mEmptyLayout.setState(EmptyLayout.State.CLEAR);
        }
    }

    @Override
    public void noteDeleted() {
        mNoteToDelete = null;
        mCanDelete = true;
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
                if (mCanDelete) {
                    mCanDelete = false;
                    mDeletePosition = position;
                    mNoteToDelete = mNotesAdapter.getNote(position);
                    mNotesAdapter.deleteNote(mDeletePosition);
                    if (mNotesAdapter.getItemCount() == 0) {
                        mNotesRecyclerView.setVisibility(View.GONE);
                        mEmptyLayout.setState(EmptyLayout.State.EMPTY_NO_BUTTON, R.string.no_notes_created);
                    }
                    displaySnackBar(MessageType.UNDO_DELETE);
                }
            }
        });
    }

    private void initStateLayout() {
        mEmptyLayout.setState(EmptyLayout.State.LOADING);
        mEmptyLayout.setOnRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEmptyLayout.setState(EmptyLayout.State.LOADING);
                mNotesPresenter.getNotes();
            }
        });
    }

    /**
     * Handle the snackbars for error, retry and undo actions
     *
     * @param messageType the type of the snack bar that will be displayed
     */
    private void displaySnackBar(MessageType messageType) {
        if (mOperationSnackBar != null && mOperationSnackBar.isShown()) {
            mOperationSnackBar.dismiss();
            mOperationSnackBar.removeCallback(mUndoDismissCallback);
        }
        switch (messageType) {
            case UNDO_DELETE:
                mUndoDismissCallback = new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        mOperationSnackBar.removeCallback(this);
                        super.onDismissed(transientBottomBar, event);
                        mNotesPresenter.deleteNote(mNoteToDelete.getId());
                    }
                };
                mOperationSnackBar = Snackbar.make(mNotesRecyclerView, getString(R.string.note_deleted), Snackbar.LENGTH_LONG);
                mOperationSnackBar.setAction(getString(R.string.undo), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCanDelete = true;
                        mEmptyLayout.setState(EmptyLayout.State.CLEAR);
                        mOperationSnackBar.removeCallback(mUndoDismissCallback);
                        mNotesAdapter.addNote(mDeletePosition, mNoteToDelete);
                        mNotesRecyclerView.setVisibility(View.VISIBLE);
                    }
                });
                mOperationSnackBar.addCallback(mUndoDismissCallback);
                mOperationSnackBar.show();
                break;
            case ADD_ERROR:
                mOperationSnackBar = Snackbar.make(mNotesRecyclerView, getString(R.string.note_add_error), Snackbar.LENGTH_LONG);
                mOperationSnackBar.setAction(getString(R.string.retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mNotesPresenter.addNote(mNewNoteText);
                    }
                });
                mOperationSnackBar.show();
                break;
            case DELETE_ERROR:
                mCanDelete = true;
                mEmptyLayout.setState(EmptyLayout.State.CLEAR);
                mNotesAdapter.addNote(mDeletePosition, mNoteToDelete);
                if (mNotesAdapter.getItemCount() == 1) {
                    mNotesRecyclerView.setVisibility(View.VISIBLE);
                }
                mOperationSnackBar = Snackbar.make(mNotesRecyclerView, getString(R.string.note_delete_error), Snackbar.LENGTH_LONG);
                mOperationSnackBar.setAction(getString(R.string.retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCanDelete = false;
                        mNotesAdapter.deleteNote(mDeletePosition);
                        if (mNotesAdapter.getItemCount() == 0) {
                            mNotesRecyclerView.setVisibility(View.GONE);
                            mEmptyLayout.setState(EmptyLayout.State.EMPTY_NO_BUTTON, R.string.no_notes_created);
                        }
                        displaySnackBar(MessageType.UNDO_DELETE);
                    }
                });
                mOperationSnackBar.show();
                break;
        }
    }
}