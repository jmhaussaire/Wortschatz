package com.jmhaussaire.me.wortschatz;

import java.util.Comparator;
import java.util.Date;

public class Word {
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

    public Word(String to_learn, String meaning){
        this.theme = to_learn;
        this.version = meaning;
        this.entry_date = new Date();
    }


    // Methods
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
}
