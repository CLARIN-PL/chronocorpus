package pl.clarin.geocoder;

import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class AnnotationReaderTest {

//    @Ignore
//    @Test
//    public void testSingleAnnotationExtraction() throws Exception {
//
//        ClassLoader classLoader = getClass().getClassLoader();
//
//        try (InputStream inputStream = classLoader.getResourceAsStream("test_find_single_annotation_top9.xml")) {
//
//            AnnotationReader ar = AnnotationReader.getInstance();
//            List<Annotation> ann = ar.loadSingleDocumentAnnotations(inputStream);
//
//            assertEquals(1, ann.size());
//
//            Annotation a1 = ann.get(0);
//
//            assertEquals("s1", a1.getSentenceId());
//            assertEquals(1, a1.getChannel());
//            assertEquals("Wrocław", a1.getTokens().get(0).getBase());
//            assertEquals("Wrocławia", a1.getTokens().get(0).getOrth());
//            assertEquals("nam_loc", a1.getTokens().get(0).getType());
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testMultiAnnotationExtraction() throws Exception {
//
//        ClassLoader classLoader = getClass().getClassLoader();
//
//        try (InputStream inputStream = classLoader.getResourceAsStream("test_find_many_annotation_top9.xml")) {
//
//            AnnotationReader ar = AnnotationReader.getInstance();
//            List<Annotation> ann = ar.loadSingleDocumentAnnotations(inputStream);
//
//            assertEquals(3, ann.size());
//
//            Annotation a1 = ann.get(0);
//
//            assertEquals("s2", a1.getSentenceId());
//            assertEquals(3, a1.getChannel());
//            assertEquals("Węgry", a1.getTokens().get(0).getBase());
//            assertEquals("Węgry", a1.getTokens().get(0).getOrth());
//            assertEquals("nam_loc", a1.getTokens().get(0).getType());
//
//            Annotation a2 = ann.get(1);
//
//            assertEquals("s2", a2.getSentenceId());
//            assertEquals(2, a2.getChannel());
//            assertEquals("zielony", a2.getTokens().get(0).getBase());
//            assertEquals("góra", a2.getTokens().get(1).getBase());
//
//            assertEquals("Zielonej", a2.getTokens().get(0).getOrth());
//            assertEquals("Góry", a2.getTokens().get(1).getOrth());
//
//            assertEquals("nam_loc", a2.getTokens().get(0).getType());
//            assertEquals("nam_loc", a2.getTokens().get(1).getType());
//
//            Annotation a3 = ann.get(2);
//            assertEquals("s2", a3.getSentenceId());
//            assertEquals(1, a3.getChannel());
//            assertEquals("Pcim", a3.getTokens().get(0).getBase());
//            assertEquals("Pcimiu", a3.getTokens().get(0).getOrth());
//            assertEquals("nam_loc", a3.getTokens().get(0).getType());
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
