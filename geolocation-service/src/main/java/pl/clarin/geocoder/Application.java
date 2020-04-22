package pl.clarin.geocoder;

import org.ini4j.Ini;
import pl.clarin.geocoder.service.GeolocationService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Application {

    private static final Logger LOGGER = Logger.getLogger(Application.class.getName());

    public static void main(String... args) {

        new Application();
        Path p = Paths.get("/home/tnaskret/Dane/test/ch/");
        GeolocationService.getInstance().process(p, 1);

    }

    public Application() {
        loadConfiguration();
    }

    private void loadConfiguration() {
        try {
            Ini ini = new Ini(new File("config.ini"));
            Configuration.init(ini);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Problems with init file", ex);
        }
    }
}