package com.jmhaussaire.me.wortschatz;

import android.arch.persistence.room.TypeConverter;

import java.util.ArrayList;
import java.util.Date;

public class DateTypeConverter {
    @TypeConverter
    public long convertDateToLong(Date date) {
        return date.getTime();
    }

    @TypeConverter
    public Date convertLongToDate(long time) {
        return new Date(time);
    }

    @TypeConverter
    public String convertResultsToString(ArrayList<Integer> results) {
        String to_return="";
        for (int i=0; i<results.size(); i++) {
            to_return+=results.get(i);
            to_return+=" ";
        }
        return to_return;
    }

    @TypeConverter
    public ArrayList<Integer>convertStringToResults(String results) {
        String[] temp = results.split(" ");
        ArrayList<Integer> to_return = new ArrayList<Integer>();
        for (int i = 0; i< temp.length; i++){
            to_return.set(i,Integer.parseInt(temp[i]));
        }
        return to_return;
    }

}
