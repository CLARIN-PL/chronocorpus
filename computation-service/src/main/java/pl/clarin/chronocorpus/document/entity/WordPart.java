package pl.clarin.chronocorpus.document.entity;

import java.io.Serializable;

public class WordPart implements Serializable {

    private String base;

    private byte pos;

    private final short syllableCount = 0;

    private final short phonemeCount = 0;

    public WordPart() {
    }

    public WordPart(String base, byte pos) {
        this.base = base;
        this.pos = pos;
    }

    public String getBase() {
        return base;
    }

    public byte getPos() {
        return pos;
    }

    public short getSyllableCount() {
        return syllableCount;
    }

    public short getPhonemeCount() {
        return phonemeCount;
    }
}
