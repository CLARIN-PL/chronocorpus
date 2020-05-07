package pl.clarin.geocoder;

import org.ini4j.Ini;
import pl.clarin.geocoder.service.GeoNamesLocationStore;
import pl.clarin.geocoder.service.GeolocationService;
import pl.clarin.geocoder.service.MapsLocationStore;
import pl.clarin.geocoder.worker.Service;
import pl.clarin.geocoder.worker.TaskException;
import pl.clarin.geocoder.worker.Worker;

import javax.json.JsonObject;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Main extends Worker {
    private static final Logger LOGGER = Logger.getLogger(Worker.class.getName());

    @Override
    public void init() throws Exception {
    }

    @Override
    public JsonObject process(JsonObject input) throws Exception {
        Task task;
        try {
            task = new Task(input);
        } catch (RuntimeException e) {
            LOGGER.log(Level.SEVERE, "Error in code", e);
            throw new Exception("Internal error");
        }
        if (task == null && task.getPath() != null && !"".equals(task.getPath())) throw new TaskException();
        Path p = Paths.get(task.getPath());

        // limit oznacza ile potencjalnych wyników ma zostać zwrócon
        GeolocationService.getInstance().process(p, 1);
        return task.getJson();
    }

    public static void main(String[] args) {
        new Service<>(Main.class);
    }

    @Override
    public void static_init(Ini init) throws Exception {
        try {
            GeoNamesLocationStore.getInstance();
            MapsLocationStore.getInstance();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error while loading cache", e);
        }
    }
}

