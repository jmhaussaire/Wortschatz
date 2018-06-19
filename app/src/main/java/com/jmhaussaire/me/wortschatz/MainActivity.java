package com.jmhaussaire.me.wortschatz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Layout;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import junit.framework.Test;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    protected void onStop() {
        super.onStop();
        // Must save the data
    }


    public void goTo(View view) {
        switch (view.getId()) {
            case R.id.goToDic:
                Intent vocabActivity = new Intent(MainActivity.this, VocabActivity.class);
                startActivity(vocabActivity);
                break;
            case R.id.goToTest:
                Toast.makeText(this, "Not ready yet", Toast.LENGTH_SHORT).show();
//                Intent testActivity = new Intent(MainActivity.this, TestActivity.class);
//                startActivity(testActivity);
                break;
        }
    }
}
