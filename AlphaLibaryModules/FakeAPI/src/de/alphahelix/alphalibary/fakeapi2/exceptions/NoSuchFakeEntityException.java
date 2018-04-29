package de.alphahelix.alphalibary.fakeapi2.exceptions;

public class NoSuchFakeEntityException extends NullPointerException {
	
	public NoSuchFakeEntityException() {
		super("No such Fake Entity!");
	}
	
	public NoSuchFakeEntityException(String s) {
		super("No such Fake Entity! (" + s + ")s");
	}
}
