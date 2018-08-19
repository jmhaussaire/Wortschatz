package com.jmhaussaire.me.wortschatz;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

@Database(entities = {Word.class, TestSave.class}, version = 1)
@TypeConverters({DateTypeConverter.class})
public abstract class AppDataBase extends RoomDatabase {
    public abstract WordDAO getWordDAO();
    public abstract TestSaveDAO getTestSaveDAO();
}
