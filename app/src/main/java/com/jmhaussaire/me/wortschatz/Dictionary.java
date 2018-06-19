package com.jmhaussaire.me.wortschatz;

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

public class Dictionary {
    // Attributes
    ArrayList<Word> word_list;

    private String learning_language;
    private String known_language;
    private String name;

    private String sorting_type; //AZ_theme, AZ_version, Date

    // Constructor
    public Dictionary(String to_learn, String known)
    {
        this.learning_language = to_learn;
        this.known_language = known;
        this.name = learning_language;
        this.sorting_type = "AZ_theme";

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

    public void sortWordList() {
        sortWordList(this.sorting_type);
    }

    public void sortWordList(String type) {
        switch (type){
            case "Date":
                sortWordListDate();
                break;
            case "AZ_theme":
                sortWordListTheme();
                break;
            case "AZ_version":
                sortWordListVersion();
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
        sorting_type = "AZ_theme";
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
        sorting_type = "AZ_version";
    }

    public void sortWordListDate() {

        Comparator<Word> test = new Comparator<Word>() {
            @Override
            public int compare(Word w1, Word w2) {
                System.out.println("here");
                System.out.println(w1.getEntry_date());
                System.out.println(w2.getEntry_date());
                System.out.println(w1.getEntry_date().compareTo(w2.getEntry_date()));
                return w1.getEntry_date().compareTo(w2.getEntry_date());
            }
        };

        Collections.sort(this.word_list,test);
        sorting_type = "Date";
    }
}
