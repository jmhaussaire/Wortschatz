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
        ArrayList<Integer> to_return = new ArrayList<Integer>();
        if (results.length()>0) {
            String[] temp = results.split(" ");

            for (int i = 0; i < temp.length; i++) {
                System.out.println(temp[i]);
                System.out.println(Integer.parseInt(temp[i]));
                to_return.add(Integer.parseInt(temp[i]));
            }
        }
        return to_return;
    }

}
