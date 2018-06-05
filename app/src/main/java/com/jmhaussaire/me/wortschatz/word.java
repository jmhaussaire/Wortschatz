package com.jmhaussaire.me.wortschatz;

import java.util.Date;

public class word {
    //Attributes
    private String theme;
    private String version;
    private int n_tests =0;
    private int n_failed_tests =0;
    private int n_success_tests =0;
    private int n_middle_tests=0;
    private String last_test_result="";
    private Date entry_date;
    private Date last_test_date;

    public word(String unknown, String meaning){
        this.theme = unknown;
        this.version = meaning;
        this.entry_date = new Date();
    }

    // Methods
    public String getTheme() {
        return this.theme;
    }

    public String getVersion() {
        return this.version;
    }

}
