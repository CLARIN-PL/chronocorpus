package pl.clarin.chronocorpus.document.entity;

import java.io.Serializable;

public class Statistic implements Serializable {

    private int verbs = 0;
    private int adjectives = 0;
    private int adverbs = 0;
    private int nouns = 0;

    public int getVerbs() {
        return verbs;
    }

    public int getValue(int pos) {
        if (pos == 1)
            return verbs;
        if (pos == 2)
            return nouns;
        if (pos == 3)
            return adverbs;
        if (pos == 4)
            return adjectives;
        return 0;
    }

    public void addValue(byte pos, int val) {
        switch (pos) {
            case 0:
                break;
            case 1:
                this.addVerb(val);
                break;
            case 2:
                this.addNoun(val);
                break;
            case 3:
                this.addAdv(val);
                break;
            case 4:
                this.addAdj(val);
                break;
        }
    }

    private void addVerb(int v) {
        this.verbs = verbs + v;
    }

    private void addAdj(int v) {
        this.adjectives = adjectives + v;
    }

    private void addAdv(int v) {
        this.adverbs = adverbs + v;
    }

    private void addNoun(int v) {
        this.nouns = nouns + v;
    }

    public int getAdjectives() {
        return adjectives;
    }

    public int getAdverbs() {
        return adverbs;
    }

    public int getNouns() {
        return nouns;
    }

}
