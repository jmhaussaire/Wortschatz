package com.jmhaussaire.me.wortschatz;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
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

    protected int[] id_list;
    protected int index = 0;
    protected String test_type;

    protected Word[] word_list;
    WordDAO DAO;
    TestSaveDAO tsDAO;
    TestSave save;

    //public

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);
        this.index = 0;
        getSave();
    }

    private void getSave(){
        AppDataBase database = Room.databaseBuilder(this, AppDataBase.class, "testsave")
                .allowMainThreadQueries()   //Allows room to do operation on main thread
                .build();
        this.tsDAO = database.getTestSaveDAO();
        TestSave[] ts = tsDAO.getSave();
        if (ts.length==1) {
            this.save = tsDAO.getSave()[0];
        }
        else if (ts.length==0){
            this.save = null;
        }
        else {
            Toast.makeText(this, "I think I have too many test saved", Toast.LENGTH_SHORT).show();
        }
    }


    public void goToTest(View view) {
        switch (view.getId()) {
            case R.id.continueTest:
/*                if (index==0)
                    Toast.makeText(this, "No test to continue", Toast.LENGTH_SHORT).show();
                else {
                    // TODO Continue test
                    Intent testActivity = new Intent(MainTestActivity.this, TestActivity.class);
                    testActivity.putExtra("id list",id_list);
                    testActivity.putExtra("test type",test_type);
                    testActivity.putExtra("index",index);
                    startActivityForResult(testActivity,CONTINUE_TEST_REQUEST);
                }*/
                if (save==null)
                    Toast.makeText(this, "No test to continue", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(this, "So far so good", Toast.LENGTH_SHORT).show();
                    Intent testActivity = new Intent(MainTestActivity.this, TestActivity.class);
                    this.id_list = save.getId_list();
                    this.index = save.getIndex();
                    this.test_type = save.getTest_type();
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
                        sort_type = "LIFO";
                        break;
                    case R.id.date_oldest:
                        sort_type = "FIFO";
                        break;
                    case R.id.random_smart:
                        sort_type = "smart";
                        break;
                    case R.id.random_pure:
                        sort_type = "pure";
                        break;
                }

                runningTest(test_type,sort_type);

                break;
        }
    }

    public void runningTest(String test_type, String sort_type) {
        //test_type : version or theme
        //sort_type : LIFO; FIFO; random; order ...

        // I get the Database
        AppDataBase database = Room.databaseBuilder(this, AppDataBase.class, "dico")
                .allowMainThreadQueries()   //Allows room to do operation on main thread
                .build();

        DAO = database.getWordDAO();


        // I get the list of word and sort it
        this.word_list= DAO.getWords();
        VocabActivity.sortWordList(this.word_list,sort_type,test_type); // TODO check if I need to get it
        // I get the list of indices
        this.id_list = new int[word_list.length];
        for (int i=0; i<word_list.length; i++)
            id_list[i] = (this.word_list[i].getWord_id());



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

        if (word_list.length==0){
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
                if (requestCode==CONTINUE_TEST_REQUEST) {
                    this.tsDAO.delete(this.save);
                    this.save = null;
                }
            }
            else if (resultCode == RESULT_CANCELED) {
                if (data==null)
                    Toast.makeText(this, "There might have been a problem", Toast.LENGTH_SHORT).show();
                else {
                    this.id_list = data.getIntArrayExtra("id list");
                    this.index = data.getIntExtra("final index", 0);
                    this.test_type = data.getStringExtra("test type");
                    if (save==null) {
                        save = new TestSave(this.id_list, this.index, this.test_type);
                    }
                    else {
                        save.setId_list(this.id_list);
                        save.setIndex(this.index);
                        save.setTest_type(this.test_type);
                    }
                    TestSave[] ts = this.tsDAO.getSave();
                    if (ts.length==0){
                        tsDAO.insert(this.save);
                    }
                    else tsDAO.update(this.save);

                    Toast.makeText(this, "Saved your ongoing progress. TODO", Toast.LENGTH_SHORT).show();
                    // TODO
                }
            }
        }

    }

}
