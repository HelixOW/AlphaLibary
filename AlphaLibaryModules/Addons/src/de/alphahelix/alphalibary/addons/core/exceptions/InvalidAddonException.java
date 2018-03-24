package de.alphahelix.alphalibary.addons.core.exceptions;

/**
 * Is thrown when a {@link de.alphahelix.alphalibary.addons.core.Addon} throws an exception
 *
 * @author AlphaHelix
 * @version 1.0
 * @see de.alphahelix.alphalibary.addons.core.Addon
 * @since 1.9.2.1
 */
public class InvalidAddonException extends Exception {
	
	private static final long serialVersionUID = -934152843129197046L;
	
	public InvalidAddonException(Throwable cause) {
		super(cause);
	}
	
	public InvalidAddonException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public InvalidAddonException(String message) {
		super(message);
	}
	
}
