package de.alphahelix.alphalibary.annotations.command.exceptions;

/**
 * Thrown when the sender doesn't have the right permission to execute the command
 *
 * @author AlphaHelix
 * @version 1.0
 * @since 1.9.2.1
 */
public class PermissionException extends CommandException {
	
	private final String permission;
	
	public PermissionException(String permission) {
		super("No permission : " + permission);
		this.permission = permission;
	}
	
	public String getPermission() {
		return permission;
	}
	
}
