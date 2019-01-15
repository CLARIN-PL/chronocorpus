package pl.clarin.chronocorpus.document.control;

import com.jetstreamdb.JetstreamDBInstance;
import com.jetstreamdb.util.SizeUnit;

import java.nio.file.Path;
import java.nio.file.Paths;

public class JetstreamDB {

    public final static JetstreamDBInstance<DocumentStore> INSTANCE;

    static {
        INSTANCE = JetstreamDBInstance.New("documents-db", DocumentStore.class);
        Path databaseDirectory = Paths.get("").resolve("documents-db");
        INSTANCE.properties().setStorageChannelCount(4);
        INSTANCE.properties().setStorageDirectory(databaseDirectory.toFile());
        INSTANCE.properties().setStorageDataFileEvaluatorMaxFileSize((int) SizeUnit.GB.toBytes(2));
    }
}
