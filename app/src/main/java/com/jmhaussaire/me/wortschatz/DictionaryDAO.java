package com.jmhaussaire.me.wortschatz;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DictionaryDAO {
    @Insert
    public void insert(Dictionary... dic);

    @Update
    public void update(Dictionary... dic);

    @Delete
    public void delete(Dictionary dic);

    @Query("SELECT * FROM Dictionary")
    public List<Dictionary> getDics();

    @Query("SELECT * FROM Dictionary WHERE name = :name")
    public Dictionary getDicWithId(String name);
}