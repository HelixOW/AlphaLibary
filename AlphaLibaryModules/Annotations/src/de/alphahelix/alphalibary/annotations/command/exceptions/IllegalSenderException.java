package de.alphahelix.alphalibary.annotations.command.exceptions;

/**
 * Thrown when the given sender doesn't match the required sender
 *
 * @author AlphaHelix
 * @version 1.0
 * @since 1.9.2.1
 */
public class IllegalSenderException extends CommandException {
	public IllegalSenderException(String message) {
		super(message);
	}
	
	public IllegalSenderException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public IllegalSenderException(Throwable cause) {
		super(cause);
	}
	
	public IllegalSenderException() {
		super("Wrong sender!");
	}
}
