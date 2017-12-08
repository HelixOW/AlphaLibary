package de.alphahelix.alphalibary.annotations;

import org.bukkit.event.Listener;

public class SimpleAnnotatedCommand implements Listener {
    public SimpleAnnotatedCommand() {
        Annotations.COMMAND.registerCommands(this);
    }
}
