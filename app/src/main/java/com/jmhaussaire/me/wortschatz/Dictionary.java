package com.jmhaussaire.me.wortschatz;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.lang.reflect.Array;
import java.text.CollationElementIterator;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@Entity
public class Dictionary {
    // Attributes
    @Ignore
    private ArrayList<Word> word_list;
    @Ignore
    private String learning_language;
    @Ignore
    private String known_language;
    @PrimaryKey
    @NonNull
    private String name;
    @Ignore //I dont need to save this info. I can always reset it to whatever. Mainly to look at what "Room" has to offer
    private String display_sort; //AZ_theme, AZ_version, Date

    // Constructor
    public Dictionary(String to_learn, String known)
    {
        this.learning_language = to_learn;
        this.known_language = known;
        this.name = learning_language;
        this.display_sort = "AZ_theme";

        word_list = new ArrayList<Word>();

    }
    public Dictionary(){
        //Default is German-English dictionary
        this.learning_language = "German";
        this.known_language = "English";
        this.name = "Default";
        this.display_sort = "AZ_theme";
        word_list = new ArrayList<Word>();
    }

    // Accessors
    public String getKnown_language() {
        return known_language;
    }
    public String getName() {
        return name;
    }
    public ArrayList<Word> getWord_list() {
        return word_list;
    }
    public String getLearning_language() {
        return learning_language;
    }
    public void setName(String name){this.name = name;}

    // Methods
    public void addWord(Word word){
        this.word_list.add(word);
        sortWordList();
    }

    public void removeWord(Word word){
        this.word_list.remove(word);
        sortWordList();
    }

    public List<HashMap<String, String>> getHashMap() {
        System.out.println("truc");
        List<HashMap<String, String>> liste = new ArrayList<HashMap<String, String>>();

        for (int i=0; i<this.word_list.size(); i++) {
            Word word = word_list.get(i);
            HashMap<String, String> element = new HashMap<String, String>();
            element.put("theme", word.printTheme());
            element.put("version", word.printVersion());
            liste.add(element);
        }

        return liste;
    }

    // All the sorting methods.
    // sorting_type = Date, AZ_*, order, FIFO, LIFO, smart, pure
    // test_type = version vs theme
    // Date (FIFO/LIFO) - for testing and display
    // Version (AZ) - for display
    // Theme (AZ) - for display
    // Order - for testing
    // Random pure - for testing
    // Random smart - for testing
    public void sortWordList() {
        sortWordList(this.display_sort);
    }

    public void sortWordList(String sorting_type, String test_type){
        if (sorting_type.equals("order"))
            sortWordListOrder(test_type);
        else
            sortWordList(sorting_type);
    }

    public void sortWordList(String sorting_type) {
        switch (sorting_type){
            case "Date": case "FIFO": case "LIFO":
                sortWordListDate(sorting_type);
                break;
            case "AZ_theme":
                sortWordListTheme();
                break;
            case "AZ_version":
                sortWordListVersion();
                break;
            default: //smart, pure; Since it's ransomly picked after, it doesn't matter how it is ordered.
                sortWordList();
                break;

        }
    }

    public void sortWordListTheme() {

        Comparator<Word> test = new Comparator<Word>() {
            @Override
            public int compare(Word w1, Word w2) {
                Collator coll = Collator.getInstance(); //Locale.GERMANY ? use learning_language ?
                coll.setStrength(Collator.PRIMARY);
                return coll.compare(w1.getTheme(),w2.getTheme());
            }
        };

        Collections.sort(this.word_list,test);
        display_sort = "AZ_theme";
    }

    public void sortWordListVersion() {

        Comparator<Word> test = new Comparator<Word>() {
            @Override
            public int compare(Word w1, Word w2) {
                Collator coll = Collator.getInstance(); //Locale.GERMANY ? use learning_language ?
                coll.setStrength(Collator.PRIMARY);
                return coll.compare(w1.getVersion(),w2.getVersion());
            }
        };

        Collections.sort(this.word_list,test);
        display_sort = "AZ_version";
    }

    public void sortWordListDate(String sorting_type) {

        Comparator<Word> test = new Comparator<Word>() {
            @Override
            public int compare(Word w1, Word w2) {
                return w1.getEntry_date().compareTo(w2.getEntry_date());
            }
        };

        if (sorting_type.equals("FIFO"))
            Collections.sort(this.word_list,test);
        else if (sorting_type.equals("LIFO"))
            Collections.sort(this.word_list,Collections.reverseOrder(test));
        else if (sorting_type.equals("Date")) {
            Collections.sort(this.word_list, test);
            this.display_sort = "Date";
        }
    }

    public ArrayList<Word> sortWordListOrder(final String test_type) {
        // I want to order
        // - First last_result = False
        // - First the ones that were tested the longest ago (no test is the oldest)
        // - First the ones that were entered the soonest
        Comparator<Word> test = new Comparator<Word>() {
            @Override
            public int compare(Word w1, Word w2) {
                Date testDate1;
                Date testDate2;
                Date entryDate1;
                Date entryDate2;

                int testResult1 = -1;
                if (!w1.getTest_results(test_type).isEmpty()){
                    testResult1=w1.getTest_results(test_type).get(w1.getTest_results(test_type).size());
                }
                int testResult2 = -1;
                if (!w2.getTest_results(test_type).isEmpty()) {
                    testResult2 = w2.getTest_results(test_type).get(w2.getTest_results(test_type).size());
                }


                testDate1= w1.getLast_test_date(test_type);
                testDate2 = w2.getLast_test_date(test_type);
                entryDate1 = w1.getEntry_date();
                entryDate2 = w2.getEntry_date();


                if (testResult1==testResult2){
                    if (testDate1.equals(testDate2))
                        return entryDate2.compareTo(entryDate1);
                    else
                        return testDate1.compareTo(testDate2);
                }
                else {
                    if (testResult1<testResult2)
                        return -1;
                    else
                        return 1;
                }
            }
        };

        Collections.sort(this.word_list,test);
        return this.word_list;
        //sorting_type = "Order";
    }

}
