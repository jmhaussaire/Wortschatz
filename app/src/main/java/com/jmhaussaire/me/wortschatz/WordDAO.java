package com.jmhaussaire.me.wortschatz;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.Date;
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
    public List<Word> getWords();

    @Query("SELECT word_id FROM Word")
    public List<Integer> getIds();

    @Query("SELECT * FROM Word WHERE word_id = :id")
    public Word getWordWithId(int id);

    @Query("UPDATE Word SET last_test_date_version = :date WHERE word_id = :id ")
    public void updateTestDateVersion(int id, Date date);

    @Query("UPDATE Word SET last_test_date_theme = :date WHERE word_id = :id ")
    public void updateTestDateTheme(int id, Date date);

    @Query("UPDATE Word SET test_results_theme= :results WHERE word_id = :id ")
    public void updateResultTheme(int id, ArrayList<Integer> results);

    @Query("UPDATE Word SET test_results_version= :results WHERE word_id = :id ")
    public void updateResultVersion(int id, ArrayList<Integer> results);
}