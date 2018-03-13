package de.alphahelix.alphalibary.annotations.command.exceptions;

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
