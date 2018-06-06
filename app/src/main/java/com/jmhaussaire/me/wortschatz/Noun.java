package com.jmhaussaire.me.wortschatz;

public class Noun extends Word {
    // Attribute
    private int gender; // 1 masc; 0 neutral; -1 fem
    private String article; // der, die, das
    private String plural; //for the plural form

    public Noun(String to_learn, String meaning) {
        super(to_learn,meaning);

        String to_learn_word = to_learn;
        String[] articles = new String[] {"der ", "die ", "das "};

        for (int i=0; i<3; i++) {
            if (to_learn_word.contains(articles[i])) {
                this.article=articles[i];
                to_learn_word = to_learn_word.replace(this.article,"");
                break;
            }
        }
        this.theme = to_learn_word;
    }

    @Override
    public String printTheme() {
        return this.article.concat(this.theme);
    }
}
