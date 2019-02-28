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

        s.addWord(new Word("Dziś", "", "", 1, false));
        s.addWord(new Word("padało", "", "", 1, false));
        s.addWord(new Word("dużo", "", "",1, false));
        s.addWord(new Word("śniegu", "", "",1, true));
        s.addWord(new Word(",", "", "", 1,false));
        s.addWord(new Word("bo", "", "", 1,false));
        s.addWord(new Word("wieczorem", "", "", 1,false));
        s.addWord(new Word("spadł", "", "", 1,false));
        s.addWord(new Word("śnieg", "", "", 1,false));
        s.addWord(new Word("i", "", "", 1,false));
        s.addWord(new Word("było", "", "", 1,false));
        s.addWord(new Word("zimno", "", "", 1,true));
        s.addWord(new Word(".", "", "", 1,true));

        List<String> test = (Arrays.asList(ConcordanceMapper.getInstance()
                .getSentenceString("śnieg", s)));

        assertThat(test.size(), is(2));
        assertThat(test, CoreMatchers.hasItems("Dziś padało dużo śniegu, bo wieczorem spadł", "i było zimno."));

    }

    @Test
    public void shouldSplitSentenceByManyOrthWord() {

        Sentence s = new Sentence();

        s.addWord(new Word("Dziś", "", "",1, false));
        s.addWord(new Word("był", "", "",1, false));
        s.addWord(new Word("śnieg", "", "",1, false));
        s.addWord(new Word("do", "", "", 1,false));
        s.addWord(new Word("pasa", "", "",1, false));
        s.addWord(new Word("a", "", "",1, false));
        s.addWord(new Word("wieczorem", "", "",1, false));
        s.addWord(new Word("śnieg", "", "", 1,false));
        s.addWord(new Word("stopniał", "", "",1, true));
        s.addWord(new Word(".", "", "",1, false));

        List<String> test = (Arrays.asList(ConcordanceMapper.getInstance()
                .getSentenceString("śnieg", s)));

        assertThat(test.size(), is(3));
        assertThat(test, CoreMatchers.hasItems("Dziś był","do pasa a wieczorem", "stopniał."));

    }

}
