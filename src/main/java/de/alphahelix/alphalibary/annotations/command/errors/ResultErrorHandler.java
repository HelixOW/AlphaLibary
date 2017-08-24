package de.alphahelix.alphalibary.annotations.command.errors;

import de.alphahelix.alphalibary.annotations.command.exceptions.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ResultErrorHandler implements ErrorHandler {

    @Override
    public void handleCommandException(CommandException e, CommandSender sender, Command c, String[] args) {
        sender.sendMessage(getUnknown().replace("%exception%", e.getMessage()));
        throw e;
    }

    @Override
    public void handlePermissionException(PermissionException e, CommandSender sender, Command c, String[] args) {
        sender.sendMessage(getPermission().replace("%perm%", e.getPermission()));
    }

    @Override
    public void handleIllegalSenderException(IllegalSenderException e, CommandSender sender, Command c, String[] args) {
        sender.sendMessage(getIllegalSender());
    }

    @Override
    public void handleInvalidLenghtException(InvalidLenghtException e, CommandSender sender, Command c, String[] args) {
        sender.sendMessage(getInvalidLenght().replace("%exception%", e.getMessage()).replace("%command%", c.getName()).replace("%usage%", c.getUsage()));
    }

    @Override
    public void handleArgumentException(ArgumentException e, CommandSender sender, Command c, String[] args) {
        sender.sendMessage(getInvalidArgument().replace("%exception%", e.getMessage()));
    }
}
