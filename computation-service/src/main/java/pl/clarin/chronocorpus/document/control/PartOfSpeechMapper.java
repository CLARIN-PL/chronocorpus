package pl.clarin.chronocorpus.document.control;

import java.util.HashMap;
import java.util.Map;

public class PartOfSpeechMapper {

    private  Map<String, Integer> partsOfSpeech = new HashMap();

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
        partsOfSpeech.put("fin", 1);
        partsOfSpeech.put("bedzie", 1);
        partsOfSpeech.put("praet", 1);
        partsOfSpeech.put("impt", 1);
        partsOfSpeech.put("inf", 1);
        partsOfSpeech.put("pcon", 1);
        partsOfSpeech.put("pant", 1);
        partsOfSpeech.put("imps", 1);
        partsOfSpeech.put("winien", 1);
        partsOfSpeech.put("pred", 1);
        partsOfSpeech.put("pact", 1);
        partsOfSpeech.put("ppas", 1);

        //nouns
        partsOfSpeech.put("subst", 2);
        partsOfSpeech.put("depr", 2);
        partsOfSpeech.put("ger", 2);
        partsOfSpeech.put("brev", 2);

        //adjectives
        partsOfSpeech.put("adj", 4);
        partsOfSpeech.put("adja", 4);
        partsOfSpeech.put("adjp", 4);
        partsOfSpeech.put("adjc", 4);

        //adverbs
        partsOfSpeech.put("adv", 3);
    }

    public int getPartOfSpeech(String pos){
        return partsOfSpeech.getOrDefault(pos, 0);
    }

}
