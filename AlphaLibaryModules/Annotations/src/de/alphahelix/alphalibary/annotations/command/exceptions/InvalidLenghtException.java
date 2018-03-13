package de.alphahelix.alphalibary.annotations.command.exceptions;

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
