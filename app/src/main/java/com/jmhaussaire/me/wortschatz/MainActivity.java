package com.jmhaussaire.me.wortschatz;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView theme_list ;
    //ListView version_list ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        //version_list = findViewById(R.id.version_list);


        List<HashMap<String, String>> liste = new ArrayList<HashMap<String, String>>();

        word word_1 = new word("die Katze", "le chat");
        HashMap<String, String> element1 = new HashMap<String, String>();
        element1.put("theme", word_1.getTheme());
        element1.put("version", word_1.getVersion());
        liste.add(element1);

        word_1 = new word("das Hund", "le chien");
        element1 = new HashMap<String, String>();
        element1.put("theme", word_1.getTheme());
        element1.put("version", word_1.getVersion());
        liste.add(element1);
        liste.add(element1);
        liste.add(element1);
        liste.add(element1);
        liste.add(element1);
        liste.add(element1);
        liste.add(element1);
        liste.add(element1);
        liste.add(element1);
        liste.add(element1);
        liste.add(element1);

//        word[] mon_test = new word[] {word_1, word_1, word_1, word_1};
//        List<String> first = new ArrayList<>() ;
//        List<String> second = new ArrayList<>() ;
//        for (int i=0; i<mon_test.length; i++) {
//            first.add(mon_test[i].getTheme());
//            second.add(mon_test[i].getVersion());
//        }

        ListAdapter la = new SimpleAdapter(this, liste, R.layout.list_item , new String[]{"theme", "version"},
                new int[]{R.id.text1, R.id.text2});

//        ArrayAdapter<String> aa1 = new ArrayAdapter<String>(this,R.layout.list_item,first);
//        ArrayAdapter<String> aa2 = new ArrayAdapter<String>(this,R.layout.list_item,second);



        theme_list.setAdapter(la);
        //version_list.setAdapter(aa2);
    }


    private View.OnClickListener switch_panel = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setContentView(R.layout.content_main);
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
