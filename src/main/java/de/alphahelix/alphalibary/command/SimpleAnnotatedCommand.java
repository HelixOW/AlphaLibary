package de.alphahelix.alphalibary.command;

import de.alphahelix.alphalibary.annotations.Annotations;
import org.bukkit.event.Listener;

public class SimpleAnnotatedCommand implements Listener {
    public SimpleAnnotatedCommand() {
        super();
        Annotations.COMMAND.registerCommands(this);
    }
}
