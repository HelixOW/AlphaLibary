package de.alphahelix.alphalibary.annotations.command.exceptions;

/**
 * Thrown when a argument is shorter or longer than the expected size
 *
 * @author AlphaHelix
 * @version 1.0
 * @since 1.9.2.1
 */
public class InvalidLenghtException extends CommandException {
	
	private final int expected;
	private final int given;
	
	public InvalidLenghtException(int expected, int given) {
		super((given < expected ? "Not enough" : "Too many") + " arguments. (" + given + (given < expected ? "<" : ">") + expected + ")");
		this.expected = expected;
		this.given = given;
	}
	
	public int getExpectedLength() {
		return expected;
	}
	
	public int getGivenLength() {
		return given;
	}
	
	boolean shorter() {
		return given < expected;
	}
	
	boolean longer() {
		return given > expected;
	}
}
