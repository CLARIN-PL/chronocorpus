package pl.clarin.chronocorpus;

import pl.clarin.chronocorpus.document.control.DocumentStore;

import java.util.logging.Logger;


/**
 * @author twalkow
 */
public class Progress {

    private final ProgressUpdater up;
    private long value = 0;
    private long max = 1;
    private static long globalMax = 0;
    private static final Logger LOGGER = Logger.getLogger(Progress.class.getName());

    public Progress(ProgressUpdater up) {
        this.up = up;
        if (globalMax == 0)
            globalMax = DocumentStore.getInstance().getDocuments().size();
        max = globalMax;
        //LOGGER.log(Level.INFO, "Progress max: {0}", globalMax);
        if (max < 1) max = 1;
    }

    public void initSize(long m) {
        if (m > 0)
            max = m;

    }

    public void update() {
        value++;
        double progress = value * 1.0 / max;

        if (up != null)
            up.update(progress);

    }

}
