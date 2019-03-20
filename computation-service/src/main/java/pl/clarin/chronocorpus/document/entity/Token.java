package pl.clarin.chronocorpus.document.entity;

import java.io.Serializable;

public class Token implements Serializable {

    private String orth;

    private WordPart word;

    private boolean noSpaceAfter;

    public Token(){}

    public Token(String orth, String base, String ctag, byte pos, boolean noSpaceAfter) {
        this.orth = orth;
        this.noSpaceAfter = noSpaceAfter;

        if(!"interp".equals(ctag)){
            word = new WordPart(base, ctag, pos);
        }
    }

    public boolean isInterp(){
        return word == null;
    }

    public String getBase() {
        return word.getBase();
    }

    public String getOrth() {
        return orth;
    }

    public String getOrthWithDelimiter(){
        if(isNoSpaceAfter()){
            return orth;
        }else return orth + " ";
    }


    public String getCtag() {
        return word.getCtag();
    }

    public short getSyllableCount() {
        return word.getSyllableCount();
    }

    public short getPhonemeCount() {
        return word.getPhonemeCount();
    }

    public boolean isNoSpaceAfter() {
        return noSpaceAfter;
    }

    public byte getPos() {
        return word.getPos();
    }
}
