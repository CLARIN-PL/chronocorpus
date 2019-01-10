package pl.clarin.chronocorpus.document.entity;

public class Word {

    private String base;

    private String orth;

    private short pos;

    private short letterCount;

    private short syllableCount;

    private short phonemCount;

    public Word(String base, String orth, short pos) {
        this.base = base;
        this.orth = orth;
        this.pos = pos;
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

    public short getPos() {
        return pos;
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
}
