package io.github.alphahelixdev.alpary.annotations.command.errorhandlers;

import io.github.alphahelixdev.alpary.annotations.command.Command;
import org.bukkit.command.CommandSender;

public abstract class ErrorHandler {
	
	private Command command;
	
	public ErrorHandler(Command command) {
		this.command = command;
	}
	
	public Command getCommand() {
		return this.command;
	}
	
	public abstract void noPermission(CommandSender cs, String label, String[] args);
	
	public abstract void notAPlayer(CommandSender cs, String label, String[] args);
	
	public abstract void wrongAmountOfArguments(CommandSender cs, String label, String[] args);
	
	public abstract void wrongArgument(CommandSender cs, String label, String[] args);
}
