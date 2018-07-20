package com.jmhaussaire.me.wortschatz;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

// Need to add what add_word button does
// Need to add a context menu to remove words
// Need to add search button
public class VocabActivity extends AppCompatActivity {

    //Attributes
    protected ListView theme_list ;
    protected Toolbar tb;
    protected List<Word> word_list;
    protected ListAdapter la;
    protected String display_sort = "AZ_theme"; //AZ_theme, AZ_version, Date
    WordDAO DAO;
    //Dictionary dic;

    public final static int ADD_WORD_REQUEST = 0;
    public final static String WORD_TO_ADD = "android.intent.action.WORD_TO_ADD";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocab);
        tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setOnClickListener(switch_panel);
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        theme_list = findViewById(R.id.theme_list);

        AppDataBase database = Room.databaseBuilder(this, AppDataBase.class, "dico")
                .allowMainThreadQueries()   //Allows room to do operation on main thread
                .build();

        DAO = database.getWordDAO();
        this.word_list= DAO.getWords();
        if (word_list.size()==0) {
            Toast.makeText(getApplicationContext(),"This is just for the first time",Toast.LENGTH_LONG).show();
            Word word_1 = new Noun("die Katze", "chat");
            Word word_2 = new Noun("der Hund", "chien");
            Word word_3 = new Noun("das Ding", "truc");
            DAO.insert(word_1,word_2,word_3);
        }

        //this.dic = new Dictionary("German","English");
        //dic.addWord(word_1);
        //dic.addWord(word_2);
        //dic.addWord(word_3);

        getSupportActionBar().setTitle("GERMAN");//dic.getName()); //TO CHANGE

        displayList();
        this.theme_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(),"Im in",Toast.LENGTH_LONG).show();
                Word w = word_list.get(position);
                //Toast.makeText(getApplicationContext(),w.getword_type(),Toast.LENGTH_LONG).show();
                Intent wordActivity = new Intent(VocabActivity.this, WordActivity.class);
                wordActivity.putExtra("word to display", w);
                startActivity(wordActivity);
                return false;
                }});
    }

    public void displayList(){
        la = new SimpleAdapter(this, getHashMap(), R.layout.list_item , new String[]{"theme", "version"},
                new int[]{R.id.text1, R.id.text2});
        this.theme_list.setAdapter(la);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vocab, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.sortDate)
            this.display_sort = "Date";
        if (id == R.id.sortTheme)
            this.display_sort = "AZ_theme";
        if (id == R.id.sortVersion)
            this.display_sort = "AZ_version";

        sortWordList();
        displayList();
        return true;
        //return super.onOptionsItemSelected(item);
    }

    public void add_word(View view) {
        Intent wordActivity = new Intent(VocabActivity.this, WordActivity.class);
        startActivityForResult(wordActivity,ADD_WORD_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode== ADD_WORD_REQUEST) {
            if (resultCode==RESULT_OK){
                Word word =data.getParcelableExtra("this is the word");
                DAO.insert(word);
                this.word_list = DAO.getWords();
                sortWordList();
                displayList();
            }
        }
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
        sortWordList(this.word_list,this.display_sort);
    }

    public static void sortWordList(List<Word> word_list, String sorting_type, String test_type){
        if (sorting_type=="order")
            sortWordListOrder(word_list,test_type);
        else
            sortWordList(word_list,sorting_type);
    }

    public static void sortWordList(List<Word> word_list, String sorting_type) {
        switch (sorting_type){
            case "Date":
            case "FIFO": case "LIFO":
                sortWordListDate(word_list,sorting_type);
                break;
            case "AZ_theme":
                sortWordListTheme(word_list);

                break;
            case "AZ_version": default://smart, pure; Since it's randomly picked after, it doesn't matter how it is ordered.
                sortWordListVersion(word_list);
                break;
        }
    }

    public static void sortWordListTheme(List<Word> word_list) {

        Comparator<Word> test = new Comparator<Word>() {
            @Override
            public int compare(Word w1, Word w2) {
                Collator coll = Collator.getInstance(); //Locale.GERMANY ? use learning_language ?
                coll.setStrength(Collator.PRIMARY);
                return coll.compare(w1.getTheme(),w2.getTheme());
            }
        };

        Collections.sort(word_list,test);
    }

    public static void sortWordListVersion(List<Word> word_list) {
        Comparator<Word> test = new Comparator<Word>() {
            @Override
            public int compare(Word w1, Word w2) {
                Collator coll = Collator.getInstance(); //Locale.GERMANY ? use learning_language ?
                coll.setStrength(Collator.PRIMARY);
                return coll.compare(w1.getVersion(),w2.getVersion());
            }
        };
        Collections.sort(word_list,test);
    }

    public static void sortWordListDate(List<Word> word_list, String sorting_type) {

        Comparator<Word> test = new Comparator<Word>() {
            @Override
            public int compare(Word w1, Word w2) {
                return w1.getEntry_date().compareTo(w2.getEntry_date());
            }
        };

        if (sorting_type=="FIFO")
            Collections.sort(word_list,test);
        else if (sorting_type=="LIFO")
            Collections.sort(word_list,Collections.reverseOrder(test));
        else if (sorting_type=="Date") {
            Collections.sort(word_list, test);
        }
    }

    public static List<Word> sortWordListOrder(List<Word> word_list,final String test_type) {
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

        Collections.sort(word_list,test);
        return word_list;
        //sorting_type = "Order";
    }

}
