package com.jmhaussaire.me.wortschatz;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;
import java.util.Date;

public class Word implements Parcelable {

    //Attributes
    protected String theme; //German
    protected String version; //English
    // private String word_type; // verb, noun, adj, adv, idiom

    private int n_tests =0; // total number of tests
    private int n_failed_tests =0;
    private int n_success_tests =0;
    private int n_middle_tests=0; // in case I have a middle test
    private int last_test_result=0; // good=1; middle=0; bad=-1;
    private Date entry_date; // date the word was added. For sorting.
    private Date last_test_date; // Date of the last test. For randomizing.

    // pluriel, link with verb, adj ..., pret and perfect,



    // Needed to make it parcelable
    public static final Parcelable.Creator<Word> CREATOR = new Parcelable.Creator<Word>() {
        @Override
        public Word createFromParcel(Parcel source) {
            return new Word(source);
        }
        @Override
        public Word[] newArray(int size) {
            return new Word[size];
        }
    };



    // Constructor
    public Word(String to_learn, String meaning){
        this.theme = to_learn;
        this.version = meaning;
        this.entry_date = new Date();
    }

    // Constructor from Parcel
    public Word(Parcel in){
        this.theme = in.readString();
        this.version = in.readString();
        this.entry_date = new Date();
    }



    // Getters/Setters
    public String getTheme() {
        return this.theme;
    }

    public String printTheme(){
        return this.theme;
    }

    public String getVersion() {
        return this.version;
    }

    public String printVersion() {
        return this.version;
    }

    public Date getEntry_date() {
        return entry_date;
    }


    // Methods for Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(theme);
        dest.writeString(version);
    }
}
