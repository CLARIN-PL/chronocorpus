package pl.clarin.chronocorpus.concordance.control;

import pl.clarin.chronocorpus.concordance.entity.Concordance;
import pl.clarin.chronocorpus.document.control.DocumentStore;
import pl.clarin.chronocorpus.document.entity.Sentence;
import pl.clarin.chronocorpus.document.entity.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ConcordanceMapper {

    public static volatile ConcordanceMapper instance;

    private ConcordanceMapper(){
    }

    public static ConcordanceMapper getInstance() {
        if (instance == null) {
            synchronized (ConcordanceMapper.class) {
                if (instance == null) {
                    instance = new ConcordanceMapper();
                }
            }
        }
        return instance;
    }

    private String[] getSentenceString(String orth, Sentence s) {
        return s.getWords().stream()
                .map(Word::GetOrthWithDelimiter)
                .collect(Collectors.joining()).split(orth);
    }

    public  List<Concordance> mapSentenceToConcordanceList(String docId, String keyWord, Sentence s, Boolean getByBase){
        String orth = keyWord;
        if(getByBase){
            orth  = s.getWords().stream().filter(word -> word.getBase().equals(keyWord)).findFirst().get().getOrth();
        }

        String[] sentence = getSentenceString(orth, s);
        return getConcordances(docId, orth, sentence);
    }


    private List<Concordance> getConcordances(String docId, String orth, String[] sentence) {
        List<Concordance> crd = new ArrayList<>();
        IntStream.range(0, sentence.length-1)
                .forEach(i -> {
                    Concordance c = new Concordance(docId, sentence[i], orth, sentence[i+1]);
                    crd.add(c);
                });
        return crd;
    }

}
