package com.jmhaussaire.me.wortschatz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class TestActivity extends AppCompatActivity {
    Dictionary dic;
    String test_type; //version or theme
    String sort_type; //LIFO; FIFO; random; order ...
    Word current_word; //probably needed for saving ?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        /////////////////////
        // For example
        Word word_1 = new Noun("die Katze", "chat");
        current_word = word_1;
        this.test_type="theme";
        /////////////////////

        current_word.setLast_test_date(new Date(),this.test_type);

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


    @Override
    protected void onStop() {
        super.onStop();
        // Must save the data
    }

    public Word pickWordRandom(){
        Dictionary dic = this.dic;
        ArrayList<Word> word_list = dic.getWord_list();
        int index= (int) Math.random()*word_list.size();
        return word_list.get(index);
    }

    public Word pickWordRandomSmart(){
        Dictionary dic = this.dic;
        ArrayList<Word> word_list = dic.getWord_list();
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

    public void sortWordListOrder() {
        // I want to order
        // - First last_result = False
        // - First the ones that were tested the longest ago (no test is the oldest)
        // - First the ones that were entered the soonest
        ArrayList<Word> word_list = this.dic.sortWordListOrder(this.test_type);

    }

    public void reveal(View view) {
        TextView answer = findViewById(R.id.answer);
        answer.setVisibility(View.VISIBLE);
        LinearLayout ll = findViewById(R.id.result);
        ll.setVisibility(View.VISIBLE);
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

    }
}
