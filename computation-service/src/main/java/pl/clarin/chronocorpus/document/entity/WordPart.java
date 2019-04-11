package pl.clarin.chronocorpus.document.entity;

import java.io.Serializable;

public class WordPart implements Serializable {

    private String base;

    private String ctag;

    private byte pos;

    private short syllableCount = 0;

    private short phonemeCount = 0;

    public WordPart() {
    }

    public WordPart(String base, String ctag, byte pos) {
        this.base = base;
        this.ctag = ctag;
        this.pos = pos;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getCtag() {
        return ctag;
    }

    public void setCtag(String ctag) {
        this.ctag = ctag;
    }

    public byte getPos() {
        return pos;
    }

    public void setPos(byte pos) {
        this.pos = pos;
    }

    public short getSyllableCount() {
        return syllableCount;
    }

    public void setSyllableCount(short syllableCount) {
        this.syllableCount = syllableCount;
    }

    public short getPhonemeCount() {
        return phonemeCount;
    }

    public void setPhonemeCount(short phonemeCount) {
        this.phonemeCount = phonemeCount;
    }
}
