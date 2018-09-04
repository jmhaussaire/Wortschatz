package com.jmhaussaire.me.wortschatz;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class WordActivity extends AppCompatActivity {
    TextView theme_language;
    TextView version_language;
    EditText theme_word;
    EditText version_word;

    RadioGroup word_type;
    RadioGroup gender;

    int word_id = -1;

    WordDAO DAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);

        theme_language = findViewById(R.id.theme_language);
        theme_language.setText("German"); //TODO languages

        version_language = findViewById(R.id.version_language);
        version_language.setText("English"); //TODO languages

        word_type = findViewById(R.id.word_type);
        theme_word = findViewById(R.id.theme_word);
        version_word = findViewById(R.id.version_word);

        // Define the listener for when I change the word type
        RadioGroup.OnCheckedChangeListener type_listener = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View noun = findViewById(R.id.extra_noun);
                View verb = findViewById(R.id.extra_verb);

                noun.setVisibility(View.INVISIBLE);
                verb.setVisibility(View.INVISIBLE);

                switch (checkedId) {
                    case R.id.noun_button:
                        noun.setVisibility(View.VISIBLE);
                        String word = theme_word.getText().toString();
                        for (int i=0; i<Noun.articles.length; i++) {
                            String article = Noun.articles[i];

//                            if (word.contains(article)){
//                                gender.check(gender.getChildAt(i).getId());
//                                gender.setClickable(false);
//                            }

                        }

                        break;

                    case R.id.verb_button:
                        verb.setVisibility(View.VISIBLE);
                        break;
                }
            }
        };

        word_type.setOnCheckedChangeListener(type_listener);

        // Get the database of words
        AppDataBase database = Room.databaseBuilder(this, AppDataBase.class, "dico")
                .allowMainThreadQueries()   //Allows room to do operation on main thread
                .build();
        DAO = database.getWordDAO();

        // Check how I started this activity
        Intent intent = getIntent();
        int id = intent.getIntExtra("word to display",-1);
        Word word;
        if (id<0){
            word=null;
        }
        else {
            word = DAO.getWordWithId(id);
            word_id = id;
            if (word==null)
                System.out.println("houston we got a problem");
        }

        // Either Im creating a new word
        if (word==null){
            word_type.check(R.id.verb_button);
        }
        else { // Or Im looking/updating an old one
            Button delete = findViewById(R.id.delete_button);
            delete.setVisibility(View.VISIBLE);
            Button add = findViewById(R.id.add_button);
            add.setText("Update Word");

            theme_word.setText(word.printTheme());
            version_word.setText(word.printVersion());
            switch (word.getWord_type()) {
                case "Noun":
                    word_type.check(R.id.noun_button);
                    //TODO implement word type
//                    Noun noun = (Noun) word;
//                    //display_word(word); // To show all the extra information
//                    theme_word.setText(noun.printTheme());
//                    version_word.setText(noun.printVersion());
                    break;
                    case "Verb":
                        word_type.check(R.id.verb_button);
                        break;
                    case "Idiom":
                        word_type.check(R.id.idiom_button);
                        break;
                    default:
                        word_type.check(R.id.other_button);
            }

        }

        //TODO implement word type
        // If it's a noun, need to adapt the gender with the article
        RadioGroup.OnCheckedChangeListener gender_listener = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String word = theme_word.getText().toString();
                // should add the article to the word and make it unclickable
            }
        };

        //gender.setOnCheckedChangeListener(gender_listener);

    }

    public Word wordToAdd() {
        String theme = theme_word.getText().toString();
        String version = version_word.getText().toString();

        //TODO implement word type
        int button_id = word_type.getCheckedRadioButtonId();
        String word_type="";
        switch (button_id){
            case R.id.noun_button:
//                EditText plural = findViewById(R.id.plural);
//                //if (plural.getText().toString()!=""){
//                    to_add= new Noun(theme,version,plural.getText().toString());
//                    break;
                word_type="Noun";
                break;
            case R.id.verb_button:
                word_type="Verb";
                break;
            case R.id.other_button:
                word_type="Other";
                break;
            case R.id.idiom_button:
                word_type="Idiom";
                break;
        }
        Word to_add = new Word(theme, version, word_type);
        return to_add;
    }

    public void addNewWord(View view){
        Word to_add = wordToAdd();

        int id = to_add.getWord_id();
        Word test = DAO.getWordWithId(id);
        while (test!=null){
            id++;
            test  = DAO.getWordWithId(id);
        }
        to_add.setWord_id(id);
        DAO.insert(to_add);
        
        Intent result = new Intent();
        //result.putExtra("this is the word", to_add);
        setResult(RESULT_OK, result);
        finish();
    }

    public void updateWord(View view) {
        Word old_word = DAO.getWordWithId(this.word_id);
        Word to_add = wordToAdd();

        // Copy attributes
        // TODO
        to_add.setWord_id(old_word.getWord_id());
        to_add.setEntry_date(old_word.getEntry_date());
        to_add.setTest_results_theme(old_word.getTest_results_theme());
        to_add.setTest_results_version(old_word.getTest_results_version());
        to_add.setLast_test_date_version(old_word.getLast_test_date_version());
        to_add.setLast_test_date_theme(old_word.getLast_test_date_theme());

        Intent result = new Intent();
        //result.putExtra("this is the word", to_add);
        DAO.delete(old_word);
        DAO.insert(to_add);
        setResult(RESULT_OK, result);
        finish();
    }

    public void pushWord(View view) {
        if (this.word_id==-1) // Create a new word
            addNewWord(view);
        else // Update an old word
            updateWord(view);
    }

    public void deleteWord(View view) {
        Word word = DAO.getWordWithId(this.word_id);
        DAO.delete(word);
        Intent result = new Intent();
        setResult(RESULT_OK, result);
        finish();
    }

}
