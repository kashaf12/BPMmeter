package com.itorch.kfstudio.www.bpmmeter;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "notes")
public class Note implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "bpm")
    private String bpm;

    @ColumnInfo(name = "label")
    private String label;

    @ColumnInfo(name = "timeanddate")
    private String timeanddate;

    @Ignore
    public Note(){

    }


    public Note(String bpm, String label, String timeanddate){
        this.bpm=bpm;
        this.label=label;
        this.timeanddate=timeanddate;
    }

    public int getId() {
        return id;
    }

    public String getBpm() {
        return bpm;
    }

    public String getLabel() {
        return label;
    }

    public String getTimeanddate() {
        return timeanddate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBpm(String bpm) {
        this.bpm = bpm;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setTimeanddate(String timeanddate) {
        this.timeanddate = timeanddate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Note)) return false;

        Note note = (Note) o;

        if (id != note.id) return false;
        return bpm != null ? bpm.equals(note.bpm) : note.bpm == null;
    }
}
