package de.alphahelix.alphalibary.addons.core.exceptions;

public class InvalidAddonException extends Exception {

    private static final long serialVersionUID = -934152843129197046L;

    public InvalidAddonException(Throwable cause) {
        super(cause);
    }

    public InvalidAddonException() {
    }

    public InvalidAddonException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidAddonException(String message) {
        super(message);
    }

}
