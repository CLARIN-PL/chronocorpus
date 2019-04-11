package pl.clarin.chronocorpus.concordance.control;

import org.javatuples.Pair;
import pl.clarin.chronocorpus.concordance.entity.Concordance;
import pl.clarin.chronocorpus.concordance.entity.Concordances;
import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.document.entity.Sentence;
import pl.clarin.chronocorpus.document.entity.Token;

import java.util.*;

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

    public String[] getSentenceString(String orth, Sentence s) {
        List<String> items = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (Token w : s.getTokens()) {
            if (orth.equals(w.getOrth())) {
                items.add(sb.toString().trim());
                sb = new StringBuilder();
            } else {
                sb.append(w.getOrthWithDelimiter());
            }
        }
        items.add(sb.toString().trim());
        return items.toArray(new String[0]);
    }

    public Concordances mapSentenceToConcordances(Pair<String, Set<Property>> doc, String keyWord, Sentence s, Boolean getByBase){
        Set<String> orth = new HashSet<>();
        orth.add(keyWord);
        if(getByBase){
            orth  = s.getTokens()
                    .stream()
                    .filter(word -> word.getBase() != null && word.getBase().equals(keyWord))
                    .map(Token::getOrth)
                    .collect(Collectors.toSet());
        }

        Set<Concordance> concordances = new HashSet<>();
        orth.forEach(o -> concordances.addAll(getConcordances(o, getSentenceString(o, s))));

        return new Concordances(doc ,concordances);
    }


    private Set<Concordance> getConcordances(String orth, String[] sentence) {
        return IntStream.range(0, sentence.length-1)
                .mapToObj(i -> new Concordance(sentence[i], orth, sentence[i+1]))
                .collect(Collectors.toSet());
    }

}
