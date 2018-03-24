package de.alphahelix.alphalibary.annotations.command.exceptions;

/**
 * Used as a base class for all Exception related to a {@link de.alphahelix.alphalibary.annotations.command.Command} annotation
 *
 * @author AlphaHelix
 * @version 1.0
 * @see ArgumentException
 * @see IllegalSenderException
 * @see InvalidLenghtException
 * @see PermissionException
 * @since 1.9.2.1
 */
public class CommandException extends RuntimeException {
	
	public CommandException(String message) {
		super(message);
	}
	
	public CommandException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public CommandException(Throwable cause) {
		super(cause);
	}
	
}
