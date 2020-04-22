package pl.clarin.geocoder.worker;

import org.ini4j.Ini;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Service<W extends Worker> {
    private static final Logger LOGGER = Logger.getLogger(Service.class.getName());
    Ini init;

    public Service(Class<W> classW) {

        try {
            init = new Ini(new File("config.ini"));
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Problems with init file", ex);
            return;
        }

        LOGGER.log(Level.INFO, "Starting tool: " + init.get("service", "tool"));

        int worker_number = init.get("tool", "workers_number", Integer.class);
        List<W> workers = new ArrayList<>();
        try {

            for (int i = 0; i < worker_number; i++) {
                W worker;
                worker = classW.newInstance();
                worker.service_init(init);
                workers.add(worker);
                worker.start();
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Problems with initalising workers", ex);
            return;
        }

    }
}
