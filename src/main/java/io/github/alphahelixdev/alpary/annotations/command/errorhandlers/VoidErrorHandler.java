package io.github.alphahelixdev.alpary.annotations.command.errorhandlers;

import org.bukkit.command.CommandSender;

public class VoidErrorHandler implements ErrorHandler {
	
	@Override
	public void noPermission(CommandSender cs, String label, String[] args) {
	
	}
	
	@Override
	public void notAPlayer(CommandSender cs, String label, String[] args) {
	
	}
	
	@Override
	public void wrongAmountOfArguments(CommandSender cs, String label, String[] args) {
	
	}
	
	@Override
	public void wrongArgument(CommandSender cs, String label, String[] args) {
	
	}
}
