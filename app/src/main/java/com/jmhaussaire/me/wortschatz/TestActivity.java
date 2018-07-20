package com.jmhaussaire.me.wortschatz;

import android.app.Activity;
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
    ArrayList<Word> word_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Intent intent = getIntent(); //?

        //this.current_word = intent.getParcelableExtra("word to test");
        this.word_list = intent.getParcelableArrayListExtra("word list");
        this.test_type = intent.getStringExtra("test type");
        this.current_index = intent.getIntExtra("index",0);


        this.current_word = word_list.get(current_index);



        setTexts();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
//        Toast.makeText(this, "thats why", Toast.LENGTH_SHORT).show();
//        Intent returnIntent = new Intent();
//        returnIntent.putExtra("final word",this.current_word);//this.current_index);
//        setResult(Activity.RESULT_OK,returnIntent);
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("word list",this.word_list);//this.current_index);
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
        if (this.test_type=="theme"){
            question.setText(current_word.getTheme());
            answer.setText(current_word.getVersion());
        }
        else {//version
            question.setText(current_word.getVersion());
            answer.setText(current_word.getTheme());
        }
    }

    public Word pickWordRandom(){
        int index= (int) Math.random()*this.word_list.size();
        return this.word_list.get(index);
    }

    public Word pickWordRandomSmart(){
        ArrayList<Double> weight_list = new ArrayList<>();
        double total_weight=0;
        for (int i=0; i<word_list.size(); i++) {
            double word_weight=word_list.get(i).getWeight(this.test_type);
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
        return word_list.get(index);
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

        this.current_word.setLast_test_date(new Date(),this.test_type);
        this.current_index++;
        if (this.current_index>=word_list.size()) {
            setResult(Activity.RESULT_OK);
            finish();
        }
        else
            this.current_word= this.word_list.get(this.current_index);
        unreveal();
        setTexts();
    }
}
