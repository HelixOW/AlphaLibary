package de.alphahelix.alphalibary.command;

import de.alphahelix.alphalibary.annotations.Annotations;
import de.alphahelix.alphalibary.listener.SimpleLoader;

public class SimpleAnnotatedCommand implements SimpleLoader {

    public SimpleAnnotatedCommand() {
        super();
        Annotations.COMMAND.registerCommands(this);
    }
}
