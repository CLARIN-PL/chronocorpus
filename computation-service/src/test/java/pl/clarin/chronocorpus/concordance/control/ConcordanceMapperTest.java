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

        s.addWord(new Word("Dziś", "", "", false));
        s.addWord(new Word("padało", "", "", false));
        s.addWord(new Word("dużo", "", "", false));
        s.addWord(new Word("śniegu", "", "", true));
        s.addWord(new Word(",", "", "", false));
        s.addWord(new Word("bo", "", "", false));
        s.addWord(new Word("wieczorem", "", "", false));
        s.addWord(new Word("spadł", "", "", false));
        s.addWord(new Word("śnieg", "", "", false));
        s.addWord(new Word("i", "", "", false));
        s.addWord(new Word("było", "", "", false));
        s.addWord(new Word("zimno", "", "", true));
        s.addWord(new Word(".", "", "", true));

        List<String> test = (Arrays.asList(ConcordanceMapper.getInstance()
                .getSentenceString("śnieg", s)));

        assertThat(test.size(), is(2));
        assertThat(test, CoreMatchers.hasItems("Dziś padało dużo śniegu, bo wieczorem spadł", "i było zimno."));

    }

    @Test
    public void shouldSplitSentenceByManyOrthWord() {

        Sentence s = new Sentence();

        s.addWord(new Word("Dziś", "", "", false));
        s.addWord(new Word("był", "", "", false));
        s.addWord(new Word("śnieg", "", "", false));
        s.addWord(new Word("do", "", "", false));
        s.addWord(new Word("pasa", "", "", false));
        s.addWord(new Word("a", "", "", false));
        s.addWord(new Word("wieczorem", "", "", false));
        s.addWord(new Word("śnieg", "", "", false));
        s.addWord(new Word("stopniał", "", "", true));
        s.addWord(new Word(".", "", "", false));

        List<String> test = (Arrays.asList(ConcordanceMapper.getInstance()
                .getSentenceString("śnieg", s)));

        assertThat(test.size(), is(3));
        assertThat(test, CoreMatchers.hasItems("Dziś był","do pasa a wieczorem", "stopniał."));

    }

}
