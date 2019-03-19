package pl.clarin.chronocorpus.document.entity;

import java.io.Serializable;

public class Word implements Serializable {

    private String base;

    private String orth;

    private String ctag;

    private byte pos;

    private short syllableCount;

    private short phonemCount;

    private boolean noSpaceAfter;

    public Word(){}

    public Word(String orth, String base, String ctag, byte pos, boolean noSpaceAfter) {
        this.base = base;
        this.orth = orth;
        this.ctag = ctag;
        this.pos = pos;
        this.noSpaceAfter = noSpaceAfter;
    }

    public String getBase() {

        return base;
    }

    public String getOrth() {

        return orth;
    }

    public String getOrthWithDelimiter(){
        if(isNoSpaceAfter()){
            return orth;
        }else return orth + " ";
    }

    public String getOrthbyBase(String base){
            return orth;
    }


    public String getCtag() {
        return ctag;
    }

    public int getBaseLetterCount() {
        return base.length();
    }

    public short getSyllableCount() {
        return syllableCount;
    }

    public short getPhonemCount() {
        return phonemCount;
    }

    public boolean isNoSpaceAfter() {
        return noSpaceAfter;
    }

    public byte getPos() {
        return pos;
    }
}
