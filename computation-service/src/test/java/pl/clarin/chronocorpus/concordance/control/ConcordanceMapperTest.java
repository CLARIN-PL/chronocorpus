package pl.clarin.chronocorpus.concordance.control;


import org.hamcrest.CoreMatchers;
import org.junit.Test;
import pl.clarin.chronocorpus.document.entity.Sentence;
import pl.clarin.chronocorpus.document.entity.Token;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConcordanceMapperTest {

    @Test
    public void shouldSplitSentenceByWord() {

        Sentence s = new Sentence();

        s.addWord(new Token("Dziś", "", "", (byte) 1, false));
        s.addWord(new Token("padało", "", "", (byte) 1, false));
        s.addWord(new Token("dużo", "", "", (byte) 1, false));
        s.addWord(new Token("śniegu", "", "", (byte) 1, true));
        s.addWord(new Token(",", "", "", (byte) 1,false));
        s.addWord(new Token("bo", "", "", (byte) 1,false));
        s.addWord(new Token("wieczorem", "", "", (byte) 1,false));
        s.addWord(new Token("spadł", "", "", (byte) 1,false));
        s.addWord(new Token("śnieg", "", "", (byte) 1,false));
        s.addWord(new Token("i", "", "", (byte) 1,false));
        s.addWord(new Token("było", "", "", (byte) 1,false));
        s.addWord(new Token("zimno", "", "", (byte) 1,true));
        s.addWord(new Token(".", "", "", (byte) 1,true));

        List<String> test = (Arrays.asList(ConcordanceMapper.getInstance()
                .getSentenceString("śnieg", s)));

        assertThat(test.size(), is(2));
        assertThat(test, CoreMatchers.hasItems("Dziś padało dużo śniegu, bo wieczorem spadł", "i było zimno."));

    }

    @Test
    public void shouldSplitSentenceByManyOrthWord() {

        Sentence s = new Sentence();

        s.addWord(new Token("Dziś", "", "", (byte) 1, false));
        s.addWord(new Token("był", "", "", (byte) 1, false));
        s.addWord(new Token("śnieg", "", "", (byte) 1, false));
        s.addWord(new Token("do", "", "", (byte) 1,false));
        s.addWord(new Token("pasa", "", "", (byte) 1, false));
        s.addWord(new Token("a", "", "", (byte) 1, false));
        s.addWord(new Token("wieczorem", "", "", (byte) 1, false));
        s.addWord(new Token("śnieg", "", "", (byte) 1,false));
        s.addWord(new Token("stopniał", "", "", (byte) 1, true));
        s.addWord(new Token(".", "", "", (byte) 1, false));

        List<String> test = (Arrays.asList(ConcordanceMapper.getInstance()
                .getSentenceString("śnieg", s)));

        assertThat(test.size(), is(3));
        assertThat(test, CoreMatchers.hasItems("Dziś był","do pasa a wieczorem", "stopniał."));

    }

}
