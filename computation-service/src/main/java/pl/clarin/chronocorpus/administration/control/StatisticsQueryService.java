package pl.clarin.chronocorpus.administration.control;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import pl.clarin.chronocorpus.Configuration;
import pl.clarin.chronocorpus.administration.entity.Statistics;
import pl.clarin.chronocorpus.document.control.DocumentStore;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.io.*;
import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StatisticsQueryService {

    private static final Logger LOGGER = Logger.getLogger(StatisticsQueryService.class.getName());

    public static volatile StatisticsQueryService instance;
    private final Kryo kryo = new Kryo();
    private final File f = new File(Configuration.STATISTICS_FILE);

    private Statistics statistics= new Statistics();

    private StatisticsQueryService() {
        super();
        kryo.register(Statistics.class);
        kryo.register(AtomicInteger.class);
        restore();
    }

    public static StatisticsQueryService getInstance() {
        if (instance == null) {
            synchronized (StatisticsQueryService.class) {
                if (instance == null) {
                    instance = new StatisticsQueryService();
                }
            }
        }
        return instance;
    }

    public JsonObject getStatistics() {
        JsonObjectBuilder obj = Json.createObjectBuilder();
        obj.add("statistics", statistics.toJson());
        return  obj.build();
    }

    public void updateConcordanceQueryCount() {
        statistics.getConcordanceQueryCount().incrementAndGet();
        backup();
    }

    public void updateFrequencyQueryCount() {
        statistics.getFrequencyQueryCount().incrementAndGet();
        backup();
    }

    public void updateTimeSeriesQueryCount() {
        statistics.getTimeSeriesQueryCount().incrementAndGet();
        backup();
    }

    public void updateWordProfileQueryCount() {
        statistics.getWordProfileQueryCount().incrementAndGet();
        backup();
    }

    public void updateQuantityAnalysisQueryCount() {
        statistics.getQuantityAnalysisQueryCount().incrementAndGet();
        backup();
    }

    public void updateMapNamesQueryCount() {
        statistics.getMapNamesQueryCount().incrementAndGet();
        backup();
    }

    public void updateDocumentsQueryCount() {
        statistics.getDocumentsQueryCount().incrementAndGet();
        backup();
    }

    public void updateSimilarityQueryCount() {
        statistics.getSimilarityQueryCount().incrementAndGet();
        backup();
    }

    private boolean checkBackupFileExists() {
        return f.exists() && !f.isDirectory();
    }

    private void restore() {
        if (checkBackupFileExists()) {
            long start = System.currentTimeMillis();
            LOGGER.info("Restoring statistics ...");

            try (Input in = new Input(Files.newInputStream(f.toPath()))) {
                Object aNewObj = kryo.readClassAndObject(in);

                if (aNewObj instanceof Statistics) {
                    statistics = (Statistics) aNewObj;
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Restoring statistics failure", e);
            }

            long time = System.currentTimeMillis() - start;
            LOGGER.info("Restoring statistics took: " + time + "ms");
        }
    }

    private void backup() {
        try (Output out = new Output(new FileOutputStream(Configuration.STATISTICS_FILE))) {
            LOGGER.info("Saving statistics ...");
            long start = System.currentTimeMillis();
            kryo.writeClassAndObject(out, statistics);
            out.flush();
            long time = System.currentTimeMillis() - start;
            LOGGER.info("Saving statistics took: " + time + "ms");
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Statistics saving failure", e);
        }
    }
}
