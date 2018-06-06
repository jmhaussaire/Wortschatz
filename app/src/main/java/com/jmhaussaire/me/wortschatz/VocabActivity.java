package com.jmhaussaire.me.wortschatz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// Need to add what add_word button does
// Need to add a context menu to remove words
// Need to add search button
public class VocabActivity extends AppCompatActivity {

    //Attributes
    ListView theme_list ;
    Toolbar tb;
    Dictionary dic;
    ListAdapter la;


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

        Word word_1 = new Noun("die Katze", "chat");
        try {
            java.util.concurrent.TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Word word_2 = new Noun("das Hund", "chien");
        try {
            java.util.concurrent.TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Word word_3 = new Noun("das Ding", "truc");

        this.dic = new Dictionary("German","English");
        dic.addWord(word_1);
        dic.addWord(word_2);
        dic.addWord(word_3);

        getSupportActionBar().setTitle(dic.getLearning_language());



        la = new SimpleAdapter(this, dic.getHashMap(), R.layout.list_item , new String[]{"theme", "version"},
                new int[]{R.id.text1, R.id.text2});

        theme_list.setAdapter(la);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.sortDate)
            dic.sortWordListDate();

        if (id == R.id.sortTheme)
            dic.sortWordListTheme();

        if (id == R.id.sortVersion)
            dic.sortWordListVersion();

        la = new SimpleAdapter(this, dic.getHashMap(), R.layout.list_item , new String[]{"theme", "version"},
                new int[]{R.id.text1, R.id.text2});
        theme_list.setAdapter(la);
        return true;

        //return super.onOptionsItemSelected(item);
    }

    public void add_word(View view) {
        Toast.makeText(this, "tout bon", Toast.LENGTH_SHORT).show();
    }


}
