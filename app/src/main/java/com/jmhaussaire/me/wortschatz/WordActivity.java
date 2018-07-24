package com.jmhaussaire.me.wortschatz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class WordActivity extends AppCompatActivity {
    TextView theme_language;
    TextView version_language;
    EditText theme_word;
    EditText version_word;

    RadioGroup word_type;
    RadioGroup gender;

    Dictionary dic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);

        this.dic = new Dictionary("German","English");

        theme_language = findViewById(R.id.theme_language);
        theme_language.setText(dic.getLearning_language());

        version_language = findViewById(R.id.version_language);
        version_language.setText(dic.getKnown_language());

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


        // Check how I started this activity
        Intent intent = getIntent();
        Word word = intent.getParcelableExtra("word to display");
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


    @Override
    protected void onStop() {
        super.onStop();
        // Must save the data
    }

    public void pushWord(View view) {
        int id = word_type.getCheckedRadioButtonId();

        String theme = theme_word.getText().toString();
        String version = version_word.getText().toString();

        Word to_add;

        switch (id){
            case R.id.noun_button:
                EditText plural = findViewById(R.id.plural);
                //if (plural.getText().toString()!=""){
                    to_add= new Noun(theme,version,plural.getText().toString());
                    break;
                //}
            //case verb
            //case other
            //case idiom
            default:
                to_add = new Word(theme,version,"Verb");
        }

        Intent result = new Intent();
        //result.putExtra("this is the word", to_add);
        //TODO, but maybe database insert should word out
        setResult(RESULT_OK, result);
        finish();
    }

    public void deleteWord(View view) {
        // Need to make this
    }

}
