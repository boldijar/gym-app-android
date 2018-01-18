package com.gym.app.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

/**
 * @author catalinradoiu
 * @since 2018.01.17
 */

@Entity(tableName = "notes")
public class Note {

    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    private int id;

    @ColumnInfo(name = "text")
    @SerializedName("text")
    private String text;

    @ColumnInfo(name = "creationDate")
    @SerializedName("creationDate")
    private long creationDate;

    public Note(int id, String text, long creationDate) {
        this.id = id;
        this.text = text;
        this.creationDate = creationDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }
}
