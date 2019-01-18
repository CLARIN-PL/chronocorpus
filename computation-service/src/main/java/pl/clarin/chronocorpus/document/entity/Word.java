package pl.clarin.chronocorpus.document.entity;

import java.io.Serializable;

public class Word implements Serializable {

    private String base;

    private String orth;

    private String ctag;

    private short letterCount;

    private short syllableCount;

    private short phonemCount;

    public Word(){}

    public Word(String orth, String base, String ctag) {
        this.base = base;
        this.orth = orth;
        this.ctag = ctag;
    }

    public Word withLetterCount(short cnt){
        this.letterCount = cnt;
        return this;
    }

    public Word withSyllableCount(short cnt){
        this.letterCount = cnt;
        return this;
    }

    public Word withPhonemCount(short cnt){
        this.letterCount = cnt;
        return this;
    }
    public String getBase() {

        return base;
    }

    public String getOrth() {

        return orth;
    }

    public String getOrthbyBase(String base){
            return orth;
    }


    public String getCtag() {
        return ctag;
    }

    public short getLetterCount() {
        return letterCount;
    }

    public short getSyllableCount() {
        return syllableCount;
    }

    public short getPhonemCount() {
        return phonemCount;
    }

    @Override
    public String toString() {
        return "Word{" +
                "orth='" + orth + '\'' +", base='" + base + '\'' +
                '}';
    }
}
