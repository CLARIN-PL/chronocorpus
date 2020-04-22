package pl.clarin.geocoder.worker;

public class TaskException extends RuntimeException {

    public TaskException(){
        super("Incorrect path or file");
    }
}
