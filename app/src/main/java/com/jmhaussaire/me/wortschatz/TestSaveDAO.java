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
public interface TestSaveDAO {
    @Insert
    public void insert(TestSave save);

    @Update
    public void update(TestSave save);

    @Delete
    public void delete(TestSave save);

    @Query("SELECT * FROM TestSave")
    public TestSave[]  getSave();

}