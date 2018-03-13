package de.alphahelix.alphalibary.annotations.command.errors;

import de.alphahelix.alphalibary.annotations.command.exceptions.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface ErrorHandler {
	
	default String getUnknown() {
		return "§cUnknown exception, please report it to the author! (%exception%)";
	}
	
	default String getPermission() {
		return "§cYou don't have the permission: %perm%";
	}
	
	default String getIllegalSender() {
		return "Only players can execute this command!";
	}
	
	default String getInvalidLenght() {
		return "§c %exception% " + System.lineSeparator() + "§cUse§8: /%command% %usage%";
	}
	
	default String getInvalidArgument() {
		return "§c %exception%";
	}
	
	default void handleCommandException(CommandException e, CommandSender sender, Command c, String[] args) {
	}
	
	default void handlePermissionException(PermissionException e, CommandSender sender, Command c, String[] args) {
	}
	
	default void handleIllegalSenderException(IllegalSenderException e, CommandSender sender, Command c, String[] args) {
	}
	
	default void handleInvalidLenghtException(InvalidLenghtException e, CommandSender sender, Command c, String[] args) {
	}
	
	default void handleArgumentException(ArgumentException e, CommandSender sender, Command c, String[] args) {
	}
}
