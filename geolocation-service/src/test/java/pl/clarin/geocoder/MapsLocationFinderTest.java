package pl.clarin.geocoder;

import org.junit.Test;
import pl.clarin.geocoder.service.MapsLocationFinder;

import java.io.IOException;

public class MapsLocationFinderTest {

    @Test
    public void searchTest(){

        Application a = new Application();
        MapsLocationFinder lf = MapsLocationFinder.getInstance();

        try {
            System.out.println(lf.query("31-456 kraków fiołkowa", 5, "pl"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
