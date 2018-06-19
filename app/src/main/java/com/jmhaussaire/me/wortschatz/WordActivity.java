package com.jmhaussaire.me.wortschatz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

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

        word_type.check(R.id.verb_button);

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
                to_add = new Word(theme,version);
        }

        Intent result = new Intent();
        result.putExtra("this is the word", to_add);
        setResult(RESULT_OK, result);
        finish();
    }
}
