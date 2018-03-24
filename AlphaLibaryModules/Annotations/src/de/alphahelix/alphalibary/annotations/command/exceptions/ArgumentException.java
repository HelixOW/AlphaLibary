package de.alphahelix.alphalibary.annotations.command.exceptions;

/**
 * Thrown when a doesn't match the actual expected type
 *
 * @author AlphaHelix
 * @version 1.0
 * @since 1.9.2.1
 */
public class ArgumentException extends CommandException {
	
	private String argument;
	private Class<?> type;
	
	public ArgumentException(String message) {
		super(message);
	}
	
	public ArgumentException(String message, String argument, Class<?> type) {
		super(message);
		this.argument = argument;
		this.type = type;
	}
	
	public ArgumentException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ArgumentException(String message, Throwable cause, String argument, Class<?> type) {
		super(message, cause);
		this.argument = argument;
		this.type = type;
	}
	
	public ArgumentException(Throwable cause) {
		super(cause);
	}
	
	public ArgumentException(Throwable cause, String argument, Class<?> type) {
		super(cause);
		this.argument = argument;
		this.type = type;
	}
	
	public String getArgument() {
		return argument;
	}
	
	public Class<?> getParameterType() {
		return type;
	}
}
