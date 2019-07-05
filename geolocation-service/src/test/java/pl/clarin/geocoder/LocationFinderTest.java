package pl.clarin.geocoder;

import org.junit.Test;

import java.io.IOException;

public class LocationFinderTest {

    @Test
    public void searchTest(){

        Application a = new Application();
        LocationFinder lf = LocationFinder.getInstance();

        try {
            System.out.println(lf.query("31-456 kraków fiołkowa", 5, "pl"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
