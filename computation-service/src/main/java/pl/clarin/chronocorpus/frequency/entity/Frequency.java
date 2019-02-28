package pl.clarin.chronocorpus.frequency.entity;

import java.util.concurrent.atomic.AtomicInteger;

public class Frequency {

    private String word;

    private String partOfSpeech;

    private AtomicInteger count = new AtomicInteger();

    public Frequency(String word, String partOfSpeech) {
        this.word = word;
        this.partOfSpeech = partOfSpeech;
    }

    public void increase(){

    }
}
