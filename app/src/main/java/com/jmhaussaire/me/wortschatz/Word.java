package com.jmhaussaire.me.wortschatz;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.icu.util.DateInterval;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.time.Duration;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

@Entity
public class Word { //implements Parcelable {
    public static int WORD_COUNT =0; // Count instances to set the id

    //Attributes
    @PrimaryKey
    @NonNull
    protected int word_id; // Could be useful
    protected String theme; //German
    protected String version; //English
    private Date entry_date; // date the word was added. For sorting.
    private String word_type; // Verb, Noun, Adj, Adv, Idiom.

    private ArrayList<Integer> test_results_theme;
    private ArrayList<Integer> test_results_version;
    private Date last_test_date_version; // Date of the last test. For sorting.
    private Date last_test_date_theme; // Date of the last test. For sorting.

    @Ignore
    private double weight_version=1; //Knowing the former attributes, the corresponding weight
    @Ignore
    private double weight_theme=1; //Knowing the former attributes, the corresponding weight

    // pluriel, link with verb, adj ..., pret and perfect,



    // Needed to make it parcelable
//    public static final Parcelable.Creator<Word> CREATOR = new Parcelable.Creator<Word>() {
//        @Override
//        public Word createFromParcel(Parcel source) {
//            return new Word(source);
//        }
//        @Override
//        public Word[] newArray(int size) {
//            return new Word[size];
//        }
//    };

    // Constructor from Parcel
//    public Word(Parcel in){
//        this(in.readString(),in.readString(),in.readString());
////        this.word_id = WORD_COUNT;
////        WORD_COUNT++;
////        this.theme = in.readString();
////        this.version = in.readString();
////        this.word_type = in.readString();
////        this.entry_date = new Date();
////        this.last_test_date_theme = new Date(0); // 1970-01-01
////        this.last_test_date_version = new Date(0);// 1970-01-01
////        this.test_results_theme = new ArrayList<Integer>();
////        this.test_results_version = new ArrayList<Integer>();
//    }


    // Constructor
    public Word(String to_learn, String meaning, String word_type){
        this.word_id = WORD_COUNT;
        WORD_COUNT++;
        this.theme = to_learn;
        this.version = meaning;
        this.word_type = word_type;
        this.entry_date = new Date();
        this.last_test_date_theme = new Date(0); // 1970-01-01
        this.last_test_date_version = new Date(0);// 1970-01-01
        this.test_results_theme = new ArrayList<Integer>();
        this.test_results_version = new ArrayList<Integer>();
    }

    public Word(){
        this("","","");
    }

    public Word(Word word){
        this.word_id = word.getWord_id();
        this.theme = word.getTheme();
        this.version = word.getVersion();
        this.word_type = word.getWord_type();
        this.entry_date = word.getEntry_date();
        this.last_test_date_theme = word.getLast_test_date_theme();
        this.last_test_date_version = word.getLast_test_date_version();
        this.test_results_theme = word.getTest_results_theme();
        this.test_results_version = word.getTest_results_version();
    }




    // Getters/Setters

    @NonNull
    public int getWord_id() {
        return word_id;
    }
    public void setWord_id(int id) {
        this.word_id = id;
    }
    public String getTheme() {
        return this.theme;
    }
    public void setTheme(String theme) {this.theme = theme;}
    public String printTheme(){
        return this.theme;
    }
    public String getVersion() {
        return this.version;
    }
    public void setVersion(String version) {this.version= version;}
    public String printVersion() {
        return this.version;
    }
    public String getWord_type() {return this.word_type;}
    public void setWord_type(String type) {this.word_type=type;}
    public Date getEntry_date() {
        return entry_date;
    }
    public void setEntry_date(Date date) {this.entry_date=date;};
    public Date getLast_test_date_version() {
        return last_test_date_version;
    }
    public void setLast_test_date_version(Date date) {
        this.last_test_date_version=date;
    }
    public Date getLast_test_date_theme() { return last_test_date_theme;  }
    public void setLast_test_date_theme(Date date) {
        this.last_test_date_theme=date;
    }
    public Date getLast_test_date(String type){
        if (type == "theme"){
            return getLast_test_date_theme();
        }
        else if (type == "version"){
            return getLast_test_date_version();
        }
        else {
            System.out.println("Not the right type");
            return new Date(0);
        }
    }
    public void setLast_test_date(Date date, String type){
        if (type == "theme"){
            this.last_test_date_theme = date;
        }
        else if (type == "version"){
            this.last_test_date_version = date;
        }
        else {
            System.out.println("Not the right type");
        }
    }
    public double getWeight_version() {
        updateWeightVersion();
        return this.weight_version;
    }
    public double getWeight_theme() {
        updateWeightTheme();
        return this.weight_theme;
    }
    public double getWeight(String type) {
        if (type == "theme"){
            return getWeight_theme();
        }
        else if (type == "version"){
            return getWeight_version();
        }
        else {
            System.out.println("Not the right type");
            return 0;
        }
    }
    public ArrayList<Integer> getTest_results_theme() {
        return test_results_theme;
    }
    public void setTest_results_theme(ArrayList<Integer> res) {
        this.test_results_theme=res;
    }
    public ArrayList<Integer> getTest_results_version() {
        return test_results_version;
    }
    public void setTest_results_version(ArrayList<Integer> res) {
        this.test_results_version=res;
    }
    public ArrayList<Integer> getTest_results(String type) {
        if (type == "theme"){
            return getTest_results_theme();
        }
        else if (type == "version"){
            return getTest_results_version();
        }
        else {
            System.out.println("Not the right type");
            return new ArrayList<>();
        }
    }
    public void appendTest_results(Integer result, String type){
        if (type == "theme"){
            this.test_results_theme.add(result);
        }
        else if (type == "version"){
            this.test_results_version.add(result);
        }
        else {
            System.out.println("Not the right type");
            return;
        }
    }


//    // Methods for Parcelable
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(theme);
//        dest.writeString(version);
//        dest.writeString(word_type);
//    }



    private void updateWeightTheme() {
        double new_weight = computeWeight(test_results_theme, this.last_test_date_theme);
        this.weight_theme = new_weight;
    }

    private void updateWeightVersion() {
        double new_weight = computeWeight(this.test_results_version, this.last_test_date_version);
        this.weight_version = new_weight;
    }

    // Returns a weight between 0 and 1. The bigger the weight, the more likely the word is to be picked.
    private double computeWeight(ArrayList<Integer> test_results, Date last_test_date) {
        double weight=0;
        int test_size=test_results.size();
        if (test_size==0)
            weight=1;
        else if (test_results.get(test_size-1)==-1)
            weight=1;
        else {
            // If I got it right a lot in a row, I don't need it anymore
            int good_in_row = 1;
            boolean until=true;
            while (until) {
                good_in_row += test_results.get(test_size-good_in_row);
                until = good_in_row>0;
            }
            double weight_1 = 1/good_in_row;

            Date today = new Date();
            Calendar cal = Calendar.getInstance();

            // If it's old, I don't need it anymore ?
//            cal.setTime(entry_date);
//            cal.add(Calendar.DATE,45);
//            Date twoWeeks = cal.getTime();

            // If I haven't tested it in a while, I should check it again.
            int mult=1;
            for (int k=1;k<10; k++)
            {
                cal.setTime(last_test_date);
                cal.add(Calendar.DATE,k*15+30);
                Date later = cal.getTime();
                if (today.after(later)) mult += 1;
            }
            weight = Math.min(Math.max(weight*mult,0.1),1);
        }
        return weight;
    }





}
