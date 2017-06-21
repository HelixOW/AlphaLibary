package de.alphahelix.alphalibary.addons.core.exceptions;

public class InvalidAddonDescriptionException extends Exception {

    private static final long serialVersionUID = 3311082639471227619L;

    public InvalidAddonDescriptionException(Throwable cause, String message) {
        super(message, cause);
    }

    public InvalidAddonDescriptionException(Throwable cause) {
        super("Invalid addon.yml", cause);
    }

    public InvalidAddonDescriptionException(String message) {
        super(message);
    }

    public InvalidAddonDescriptionException() {
        super("Invalid addon.yml");
    }

}
