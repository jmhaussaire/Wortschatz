package com.jmhaussaire.me.wortschatz;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface WordDAO {
    @Insert
    public void insert(Word... word);

    @Update
    public void update(Word... word);

    @Delete
    public void delete(Word... word);

    @Query("SELECT * FROM Word")
    public ArrayList<Word> getWords();

    @Query("SELECT * FROM Word WHERE word_id = :id")
    public Dictionary getWordWithId(String id);
}