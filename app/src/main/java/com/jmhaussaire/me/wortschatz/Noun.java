package com.jmhaussaire.me.wortschatz;

public class Noun extends Word {
    // Attribute
    static String[] articles = new String[] {"der ", "die ", "das "};
    private int gender; // 1 masc; 0 neutral; -1 fem
    private String article; // der, die, das
    private String plural; //for the plural form

    public Noun(String to_learn, String meaning) {
        super(to_learn,meaning);

        String to_learn_word = to_learn;


        for (int i=0; i<3; i++) {
            if (to_learn_word.contains(articles[i])) {
                this.article=articles[i];
                to_learn_word = to_learn_word.replace(this.article,"");
                break;
            }
        }
        this.theme = capitalizeFirstLetter(to_learn_word);
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
