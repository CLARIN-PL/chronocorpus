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
            word = new WordPart(base, pos);
        }
    }

    public boolean isInterp(){
        return word == null;
    }

    public String getBase() {
        if(word != null)
            return word.getBase();
        return null;
    }

    public String getBaseWithDelimiter() {
        if(word != null)
            if(isNoSpaceAfter()){
                return word.getBase();
            } else return word.getBase() + " ";
        return getOrthWithDelimiter();
    }

    public String getOrth() {
        return orth;
    }

    public String getOrthWithDelimiter(){
        if(isNoSpaceAfter()){
            return orth;
        }else return orth + " ";
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
        if(word != null)
            return word.getPos();
        return 0;
    }
}
