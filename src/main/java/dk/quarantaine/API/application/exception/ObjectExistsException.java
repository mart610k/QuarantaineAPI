package dk.quarantaine.api.application.exception;

public class ObjectExistsException extends Exception{
    public ObjectExistsException(String message) {
        super(message);
    }
}
