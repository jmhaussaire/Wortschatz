package com.jmhaussaire.me.wortschatz;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class TestActivity extends AppCompatActivity {
    String test_type; //version or theme
    Word current_word; //probably needed for saving ?
    int current_index= 0;
    int[] word_list;

    WordDAO DAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        // I get the Database
        AppDataBase database = Room.databaseBuilder(this, AppDataBase.class, "dico")
                .allowMainThreadQueries()   //Allows room to do operation on main thread
                .build();

        DAO = database.getWordDAO();

        Intent intent = getIntent();

        //this.current_word = intent.getParcelableExtra("word to test");
        //this.word_list = intent.getParcelableArrayListExtra("word list");
        // TODO
        this.word_list = intent.getIntArrayExtra("id list");
        this.test_type = intent.getStringExtra("test type");
        this.current_index = intent.getIntExtra("index",0);

        this.current_word = DAO.getWordWithId(word_list[current_index]);

        setTexts();
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("id list",this.word_list);//this.current_index);
        returnIntent.putExtra("final index",this.current_index);
        returnIntent.putExtra("test type",test_type);
        setResult(Activity.RESULT_CANCELED,returnIntent);
        finish();
    }

    //    @Override
//    protected void onPause() {
//        super.onPause();
//        Intent returnIntent = new Intent();
//        returnIntent.putExtra("final word",8);//this.current_index);
//        setResult(Activity.RESULT_CANCELED,returnIntent);
//    }




    protected void setTexts(){
        TextView question = findViewById(R.id.question);
        TextView answer = findViewById(R.id.answer);
        if (this.test_type.equals("version")){
            question.setText(current_word.getTheme());
            answer.setText(current_word.getVersion());
        }
        else {//theme
            question.setText(current_word.getVersion());
            answer.setText(current_word.getTheme());
        }
    }

    public int pickWordRandom(){
        int index= (int) Math.random()*this.word_list.length;
        return this.word_list[index];
    }

    public int pickWordRandomSmart(){
        ArrayList<Double> weight_list = new ArrayList<>();
        double total_weight=0;
        for (int i=0; i<word_list.length; i++) {
            Word word = DAO.getWordWithId(word_list[i]);
            double word_weight= word.getWeight(this.test_type);
            weight_list.add(word_weight);
            total_weight+=word_weight;
        }

        double rand= Math.random()*total_weight;
        int index=-1;
        int i=0;
        while (index<0) {
            if (weight_list.get(i)<rand) index=i;
            else rand-=weight_list.get(i);
            i++;
        }
        return word_list[index];
    }

    public void reveal(View view) {
        TextView answer = findViewById(R.id.answer);
        answer.setVisibility(View.VISIBLE);
        LinearLayout ll = findViewById(R.id.result);
        ll.setVisibility(View.VISIBLE);
    }

    public void unreveal(){
        TextView answer = findViewById(R.id.answer);
        answer.setVisibility(View.INVISIBLE);
        LinearLayout ll = findViewById(R.id.result);
        ll.setVisibility(View.INVISIBLE);
    }

    public void sendAnswer(View view) {
        if (view.getId()==R.id.true_answer) {
            current_word.appendTest_results(1,this.test_type);
        }
        if (view.getId()==R.id.false_answer) {
            current_word.appendTest_results(-1,this.test_type);
        }
        if (view.getId()==R.id.middle_answer) {
            current_word.appendTest_results(0,this.test_type);
        }

        current_word.setLast_test_date(new Date(),this.test_type);

        DAO.update(current_word);

        this.current_index++;
        if (this.current_index>=word_list.length) {
            setResult(Activity.RESULT_OK);
            finish();
        }
        else
            this.current_word= DAO.getWordWithId(this.word_list[this.current_index]);
        unreveal();
        setTexts();
    }
}
