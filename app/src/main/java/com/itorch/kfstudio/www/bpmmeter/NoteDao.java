package com.itorch.kfstudio.www.bpmmeter;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.itorch.kfstudio.www.bpmmeter.Note;

import java.util.List;

import io.reactivex.Maybe;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM notes ORDER BY timeanddate DESC")
    Maybe<List<Note>> getAll();

    @Query("SELECT AVG(bpm) FROM notes")
    Maybe<Float> avgUsers();

    @Query("SELECT MIN(bpm) FROM notes")
    Maybe<Integer> minUsers();

    @Query("SELECT MAX(bpm) FROM notes")
    Maybe<Integer> maxUsers();

    @Insert
    void insertAll(Note... notes);

    @Delete
    void delete(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note... note);

}