package com.jmhaussaire.me.wortschatz;

public class Noun extends Word {
    // Attribute
    static String[] articles = new String[] {"der ", "die ", "das "};
    private int gender; // 0 masc; 1 fem; 2 neutre
    private String article; // der, die, das
    private String plural; //for the plural form
    private String feminine; //for the feminine form. For example, der Anwalt, die Anwältin

    public Noun(String to_learn, String meaning, String plural) {
        super(to_learn,meaning,"Noun");

        String to_learn_word = to_learn;


        for (int i=0; i<3; i++) {
            if (to_learn_word.contains(articles[i])) {
                this.article=articles[i];
                to_learn_word = to_learn_word.replace(this.article,"");
                this.gender=i;
                break;
            }
        }
        this.theme = capitalizeFirstLetter(to_learn_word);
        this.plural=plural;
    }

    public Noun(String to_learn, String meaning) {
        this(to_learn,meaning,"");
    }

    public String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

    @Override
    public String printTheme() {
        return this.article.concat(this.theme);
    }


}
