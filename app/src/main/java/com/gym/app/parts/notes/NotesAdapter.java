package com.gym.app.parts.notes;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gym.app.R;
import com.gym.app.data.model.Note;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author catalinradoiu
 * @since 2018.01.17
 */
public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    private List<Note> mNotesList;
    private OnDeleteClickListener mOnDeleteClickListener;

    interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public NotesAdapter() {
        mNotesList = new ArrayList<>();
    }

    public void setNotesList(List<Note> notesList) {
        this.mNotesList = notesList;
        notifyDataSetChanged();
    }

    public void addNote(Note note) {
        mNotesList.add(note);
        notifyItemInserted(mNotesList.size());
    }

    public void deleteNote(int position) {
        mNotesList.remove(position);
        notifyItemRemoved(position);
    }

    public void updateNote(int position, Note note) {
        mNotesList.remove(position);
        mNotesList.add(position, note);
        notifyItemChanged(position);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        this.mOnDeleteClickListener = onDeleteClickListener;
    }

    public Note getNote(int position) {
        return mNotesList.get(position);
    }

    @Override
    public NotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NotesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note,
                parent, false), mOnDeleteClickListener);
    }

    @Override
    public void onBindViewHolder(NotesViewHolder holder, int position) {
        holder.bind(mNotesList.get(position));
    }

    @Override
    public int getItemCount() {
        return mNotesList.size();
    }

    static class NotesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.note_date)
        TextView mNoteDate;

        @BindView(R.id.note_text)
        TextView mNoteText;

        private OnDeleteClickListener mOnDeleteClickListener;

        NotesViewHolder(View itemView, final OnDeleteClickListener onDeleteClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.mOnDeleteClickListener = onDeleteClickListener;
        }

        @OnClick(R.id.delete_note_button)
        void onDeleteNoteButtonClick() {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION && mOnDeleteClickListener != null) {
                mOnDeleteClickListener.onDeleteClick(position);
            }
        }

        void bind(Note note) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.YY");
            mNoteText.setText(note.getText());
            mNoteDate.setText(itemView.getContext().getString(R.string.note_created_at,
                    dateFormat.format(new Date(note.getCreationDate() * 1000))));
        }
    }
}
