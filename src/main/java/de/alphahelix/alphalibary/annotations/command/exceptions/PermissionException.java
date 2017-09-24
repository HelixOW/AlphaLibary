package de.alphahelix.alphalibary.annotations.command.exceptions;

public class PermissionException extends CommandException {

    private final String permission;

    public PermissionException(String permission) {
        super("No permission: " + permission);
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }

}
