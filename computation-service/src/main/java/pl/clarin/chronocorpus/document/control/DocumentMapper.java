package pl.clarin.chronocorpus.document.control;

import pl.clarin.chronocorpus.document.entity.Sentence;
import pl.clarin.chronocorpus.document.entity.Word;
import java.util.stream.Collectors;

public class DocumentMapper {

    public static volatile DocumentMapper instance;

    private DocumentMapper(){
    }

    public static DocumentMapper getInstance() {
        if (instance == null) {
            synchronized (DocumentMapper.class) {
                if (instance == null) {
                    instance = new DocumentMapper();
                }
            }
        }
        return instance;
    }

    public String getDocumentContent(Sentence s) {
      return s.getWords().stream()
                .map(Word::getOrthWithDelimiter)
                .collect(Collectors.joining());
    }
}
