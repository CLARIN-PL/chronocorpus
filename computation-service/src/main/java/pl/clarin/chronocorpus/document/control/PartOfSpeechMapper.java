package pl.clarin.chronocorpus.document.control;

import java.util.HashMap;
import java.util.Map;

public class PartOfSpeechMapper {

    private  Map<String, Byte> partsOfSpeech = new HashMap<>();

    public static volatile PartOfSpeechMapper instance;

    public static PartOfSpeechMapper getInstance() {
        if (instance == null) {
            synchronized (PartOfSpeechMapper.class) {
                if (instance == null) {
                    instance = new PartOfSpeechMapper();
                }
            }
        }
        return instance;
    }


    private PartOfSpeechMapper() {
        //verbs
        partsOfSpeech.put("fin", (byte) 1);
        partsOfSpeech.put("bedzie", (byte) 1);
        partsOfSpeech.put("praet", (byte) 1);
        partsOfSpeech.put("impt", (byte) 1);
        partsOfSpeech.put("inf", (byte) 1);
        partsOfSpeech.put("pcon", (byte) 1);
        partsOfSpeech.put("pant", (byte) 1);
        partsOfSpeech.put("imps", (byte) 1);
        partsOfSpeech.put("winien", (byte) 1);
        partsOfSpeech.put("pred", (byte) 1);
        partsOfSpeech.put("pact", (byte) 1);
        partsOfSpeech.put("ppas", (byte) 1);

        //nouns
        partsOfSpeech.put("subst", (byte) 2);
        partsOfSpeech.put("depr", (byte) 2);
        partsOfSpeech.put("ger", (byte) 2);
        partsOfSpeech.put("brev", (byte) 2);

        //adjectives
        partsOfSpeech.put("adj", (byte) 4);
        partsOfSpeech.put("adja", (byte) 4);
        partsOfSpeech.put("adjp", (byte) 4);
        partsOfSpeech.put("adjc", (byte) 4);

        //adverbs
        partsOfSpeech.put("adv", (byte) 3);
    }

    public byte getPartOfSpeech(String pos){
        return partsOfSpeech.getOrDefault(pos,(byte) 0);
    }

}
