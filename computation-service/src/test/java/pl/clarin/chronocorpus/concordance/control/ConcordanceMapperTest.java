package pl.clarin.chronocorpus.concordance.control;


import org.hamcrest.CoreMatchers;
import org.junit.Test;
import pl.clarin.chronocorpus.document.entity.Sentence;
import pl.clarin.chronocorpus.document.entity.Word;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConcordanceMapperTest {

    @Test
    public void shouldSplitSentenceByWord() {

        Sentence s = new Sentence();

        s.addWord(new Word("Dziś", "", "", (byte) 1, false));
        s.addWord(new Word("padało", "", "", (byte) 1, false));
        s.addWord(new Word("dużo", "", "", (byte) 1, false));
        s.addWord(new Word("śniegu", "", "", (byte) 1, true));
        s.addWord(new Word(",", "", "", (byte) 1,false));
        s.addWord(new Word("bo", "", "", (byte) 1,false));
        s.addWord(new Word("wieczorem", "", "", (byte) 1,false));
        s.addWord(new Word("spadł", "", "", (byte) 1,false));
        s.addWord(new Word("śnieg", "", "", (byte) 1,false));
        s.addWord(new Word("i", "", "", (byte) 1,false));
        s.addWord(new Word("było", "", "", (byte) 1,false));
        s.addWord(new Word("zimno", "", "", (byte) 1,true));
        s.addWord(new Word(".", "", "", (byte) 1,true));

        List<String> test = (Arrays.asList(ConcordanceMapper.getInstance()
                .getSentenceString("śnieg", s)));

        assertThat(test.size(), is(2));
        assertThat(test, CoreMatchers.hasItems("Dziś padało dużo śniegu, bo wieczorem spadł", "i było zimno."));

    }

    @Test
    public void shouldSplitSentenceByManyOrthWord() {

        Sentence s = new Sentence();

        s.addWord(new Word("Dziś", "", "", (byte) 1, false));
        s.addWord(new Word("był", "", "", (byte) 1, false));
        s.addWord(new Word("śnieg", "", "", (byte) 1, false));
        s.addWord(new Word("do", "", "", (byte) 1,false));
        s.addWord(new Word("pasa", "", "", (byte) 1, false));
        s.addWord(new Word("a", "", "", (byte) 1, false));
        s.addWord(new Word("wieczorem", "", "", (byte) 1, false));
        s.addWord(new Word("śnieg", "", "", (byte) 1,false));
        s.addWord(new Word("stopniał", "", "", (byte) 1, true));
        s.addWord(new Word(".", "", "", (byte) 1, false));

        List<String> test = (Arrays.asList(ConcordanceMapper.getInstance()
                .getSentenceString("śnieg", s)));

        assertThat(test.size(), is(3));
        assertThat(test, CoreMatchers.hasItems("Dziś był","do pasa a wieczorem", "stopniał."));

    }

}
