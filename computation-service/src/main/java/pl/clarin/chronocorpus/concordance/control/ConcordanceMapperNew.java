package pl.clarin.chronocorpus.concordance.control;

import org.javatuples.Pair;
import pl.clarin.chronocorpus.concordance.entity.Concordance;
import pl.clarin.chronocorpus.concordance.entity.Concordances;
import pl.clarin.chronocorpus.document.entity.Property;
import pl.clarin.chronocorpus.document.entity.Sentence;
import pl.clarin.chronocorpus.document.entity.Token;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ConcordanceMapperNew {

    public static volatile ConcordanceMapperNew instance;

    private ConcordanceMapperNew() {
    }

    public static ConcordanceMapperNew getInstance() {
        if (instance == null) {
            synchronized (ConcordanceMapperNew.class) {
                if (instance == null) {
                    instance = new ConcordanceMapperNew();
                }
            }
        }
        return instance;
    }

    public Concordances mapSentenceToConcordances(Pair<Integer, Set<Property>> doc, Pattern pattern, Sentence s, Boolean byBase) {
        Set<Concordance> concordances = new HashSet<>();
        if (byBase) {
            concordances.addAll(getConcordancesByBase(pattern, s));
        } else {
            concordances.addAll(getConcordances(pattern, s));
        }
        return new Concordances(doc, concordances);
    }


    public Set<Concordance> getConcordances(Pattern p, Sentence sentence) {
        String sent = sentence.getSentence();
        Matcher matcher = p.matcher(sent);
        Set<Concordance> concordances = new HashSet<>();
        while (matcher.find()) {
            if (matcher.start() == 0) {
                concordances.add(new Concordance("", sent.substring(0, matcher.end()), sent.substring(matcher.end())));
            } else {
                concordances.add(new Concordance(sent.substring(0, matcher.start()), sent.substring(matcher.start(), matcher.end()), sent.substring(matcher.end())));
            }
        }
        return concordances;
    }


    public Set<Concordance> getConcordancesByBase(Pattern p, Sentence sentence) {
        String inBase = sentence.getSentenceInBaseForm();
        Matcher matcher = p.matcher(inBase);

        Set<Concordance> concordances = new HashSet<>();
        String left = "";
        String word;

        while (matcher.find()) {
            Concordance c;

            if (matcher.start() == 0) {
                word = inBase.substring(0, matcher.end());
            } else {
                left = inBase.substring(0, matcher.start());
                word = inBase.substring(matcher.start(), matcher.end());
            }

            int leftCount = 0;

            String test = "";

            if (!left.equals(test)) {
                for (Token t : sentence.getTokens()) {
                    test = test + t.getBaseWithDelimiter();
                    leftCount++;
                    if (left.equals(test)) break;
                }
            }

            if (leftCount != 0) {
                List<Token> tk = sentence.getTokens()
                        .subList(0, leftCount);
                left = tk.stream()
                        .map(Token::getOrthWithDelimiter)
                        .collect(Collectors.joining());
            }

            int wCount = 0;
            test = "";
            for (Token t : sentence.getTokens().subList(leftCount, sentence.getTokens().size())) {
                test = test + t.getBaseWithDelimiter();
                wCount++;
                if (word.equals(test.trim())) break;
            }

            List<Token> tk = sentence.getTokens()
                        .subList(leftCount, leftCount+wCount);
             word = tk.stream()
                        .map(Token::getOrthWithDelimiter)
                        .collect(Collectors.joining());
            tk = sentence.getTokens()
                    .subList(leftCount + wCount, sentence.getTokens().size());
            String right = tk.stream()
                    .map(Token::getOrthWithDelimiter)
                    .collect(Collectors.joining());

            c = new Concordance(left, word, right);

            concordances.add(c);
        }
        return concordances;
    }


}
