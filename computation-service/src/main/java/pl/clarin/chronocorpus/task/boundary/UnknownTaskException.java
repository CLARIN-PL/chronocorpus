package pl.clarin.chronocorpus.task.boundary;

public class UnknownTaskException extends RuntimeException {

    public UnknownTaskException(){
        super("Unknown task type");
    }
}
