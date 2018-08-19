package com.jmhaussaire.me.wortschatz;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class TestSave {
    @PrimaryKey
    @NonNull
    protected int[] id_list;
    protected int index = 0;
    protected String test_type;

    // Constructors
    TestSave(){}

    TestSave(int[] id_list, int index, String test_type ){
        this.id_list = id_list;
        this.index = index;
        this.test_type = test_type;
    };

    //Getters/Setters
    public int[] getId_list(){return this.id_list;}
    public void setId_list(int[] id_list){this.id_list = id_list;}
    public int getIndex(){return this.index;}
    public void setIndex(int index){this.index=index;}
    public String getTest_type(){return this.test_type;}
    public void setTest_type(String test_type){this.test_type=test_type;}

}
