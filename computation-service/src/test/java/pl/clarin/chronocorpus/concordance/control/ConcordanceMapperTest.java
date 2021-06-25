package pl.clarin.chronocorpus.concordance.control;

import org.hamcrest.CoreMatchers;
import org.junit.Ignore;
import org.junit.Test;
import pl.clarin.chronocorpus.concordance.entity.Concordance;
import pl.clarin.chronocorpus.document.entity.Sentence;
import pl.clarin.chronocorpus.document.entity.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConcordanceMapperTest {

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

    @Test
    @Ignore
    public void shouldSplitSentenceByRegex() {

        String s1 = "domek który, mam dom w którym pełno jest domowników a ona chodzi w podomce. Dziś, był śnieg do pasa " +
                "a wieczorem, śnieg stopniał. Armia Czerwona jest nie " +
                "zwycięzona, Armii Czerwony generał, Czerwone sztandary w Armii Czerwonej";

        String s2 = "domek który, mieć dom w który pełno być domownik a on chodzić w podomka. dziś, być śnieg do pas " +
                "a wieczór, śnieg stopnieć. armia czerwony jest nie " +
                "zwyciężyć, armia czerwony generał, czerwony sztandar w armia czerwony";

        Pattern p = Pattern.compile("\\barm\\w+ czer\\w+\\b");
        Matcher m1 = p.matcher(s2);

        List<Concordance> l = new ArrayList<>();
        while (m1.find()) {
            if(m1.start() == 0){
                l.add(new Concordance("", s2.substring(0, m1.end()).trim(), s2.substring(m1.end()).trim()));
            }else{
                l.add(new Concordance(s2.substring(0, m1.start()).trim(), s2.substring(m1.start(), m1.end()).trim(), s2.substring(m1.end()).trim()));
            }

            System.out.println(m1.start()+"-"+m1.end());
            System.out.println(m1.group());
        }

        System.out.println(s2.length());

        l.forEach(i -> {
            System.out.println(i.getLeftTokenCount()+"--"+i.getWordTokenCount()+"--"+i.getRightTokenCount());

            System.out.println(i.toJson());

            String[] z = s1.split(" ");

            String left = String.join(" ", Arrays.asList(z)
                    .subList(0, i.getLeftTokenCount()));


            String w = String.join(" ", Arrays.asList(z)
                    .subList(i.getLeftTokenCount(), i.getLeftTokenCount() + i.getWordTokenCount()));


            String right = String.join(" ", Arrays.asList(z)
                    .subList(i.getLeftTokenCount() + i.getWordTokenCount(), z.length));

            System.out.println("left:" + left);
            System.out.println("w:" + w);
            System.out.println("right:" + right);

        });

    }

}
