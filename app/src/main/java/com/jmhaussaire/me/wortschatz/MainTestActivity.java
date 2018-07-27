package com.jmhaussaire.me.wortschatz;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainTestActivity extends AppCompatActivity {
    public final static int CONTINUE_TEST_REQUEST = 1;
    public final static int TEST_WORD_REQUEST = 2;


    protected List<Word> word_list;
    protected int[] id_list;
    protected int index = 0;
    protected String test_type;

    WordDAO DAO;

    //public

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);
        this.index = 0;
    }

    public void goToTest(View view) {
        switch (view.getId()) {
            case R.id.continueTest:
                if (index==0)
                    Toast.makeText(this, "No test to continue", Toast.LENGTH_SHORT).show();
                else {
                    // TODO Continue test
                    Intent testActivity = new Intent(MainTestActivity.this, TestActivity.class);
                    testActivity.putExtra("id list",id_list);
                    testActivity.putExtra("test type",test_type);
                    testActivity.putExtra("index",index);
                    startActivityForResult(testActivity,CONTINUE_TEST_REQUEST);

                }
                break;
            case R.id.startNewTest:
                // Need to get the value of the radio buttons to know what kind of test I am running.
                String sort_type = null;
                RadioGroup rgTestType = findViewById(R.id.test_type);
                switch (rgTestType.getCheckedRadioButtonId()){
                    case R.id.version_button:
                        this.test_type = "version";
                        break;
                    case R.id.theme_button:
                        this.test_type = "theme";
                        break;
                }
                RadioGroup rgSortType = findViewById(R.id.test_order);
                switch (rgSortType.getCheckedRadioButtonId()){
                    case R.id.ordered:
                        sort_type = "order";
                        break;
                    case R.id.date_newest:
                        test_type = "LIFO";
                        break;
                    case R.id.date_oldest:
                        test_type = "FIFO";
                        break;
                    case R.id.random_smart:
                        test_type = "smart";
                        break;
                    case R.id.random_pure:
                        test_type = "pure";
                        break;
                }

                runningTest(test_type,sort_type);

                break;
        }
    }

    public void runningTest(String test_type, String sort_type) {
        //test_type : version or theme
        //sort_type : LIFO; FIFO; random; order ...

        // I have a dictionary.
        //////////////////////
        // Example
        /////////////////
//        Word word_1 = new Noun("die Katze", "chat");
//        Word word_2 = new Noun("der Hund", "chien");
//        Word word_3 = new Noun("das Ding", "truc");
//
//        Dictionary dic = new Dictionary("German","English");
//        dic.addWord(word_1);
//        dic.addWord(word_2);
//        dic.addWord(word_3);
        //////////////////////////////////


        // I get the Database
        AppDataBase database = Room.databaseBuilder(this, AppDataBase.class, "dico")
                .allowMainThreadQueries()   //Allows room to do operation on main thread
                .build();

        DAO = database.getWordDAO();


        // I get the list of word and sort it
        this.word_list= DAO.getWords();
        VocabActivity.sortWordList(this.word_list,sort_type,test_type); // TODO check if I need to get it
        // I get the list of indices
        this.id_list = new int[word_list.size()];
        for (int i=0; i<word_list.size(); i++)
            id_list[i] = (this.word_list.get(i).getWord_id());



/*
        for (int i=0; i<word_list.size(); i++) {
            Word word = word_list.get(i);
            Intent testActivity = new Intent(MainTestActivity.this, TestActivity.class);
            testActivity.putExtra("word to test",word);
            testActivity.putExtra("test type",test_type);
            //startActivity(testActivity);
            startActivityForResult(testActivity,TEST_WORD_REQUEST);

        }
*/

        if (word_list.size()==0){
            Toast.makeText(this, "Fill in the dictionary first !", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent testActivity = new Intent(MainTestActivity.this, TestActivity.class);
            testActivity.putExtra("id list", id_list);
            testActivity.putExtra("test type", test_type);
            testActivity.putExtra("index", this.index);
            startActivityForResult(testActivity, TEST_WORD_REQUEST);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode== TEST_WORD_REQUEST ) | (requestCode==CONTINUE_TEST_REQUEST)  ){
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "You are done with this test", Toast.LENGTH_SHORT).show();
                this.index = 0;
            }
            else if (resultCode == RESULT_CANCELED) {
                if (data==null)
                    Toast.makeText(this, "There might have been a problem", Toast.LENGTH_SHORT).show();
                else {
                    this.id_list = data.getIntArrayExtra("id list");
                    this.index = data.getIntExtra("final index", 0);
                    this.test_type = data.getStringExtra("test type");
                    Toast.makeText(this, "Saved your ongoing progress. TODO", Toast.LENGTH_SHORT).show();
                    // TODO
                }
            }
        }

    }

}
