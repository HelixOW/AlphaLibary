package de.alphahelix.alphalibary.command;

import de.alphahelix.alphalibary.annotations.Annotations;
import de.alphahelix.alphalibary.listener.SimpleListener;

public class SimpleAnnotatedCommand extends SimpleListener {

    public SimpleAnnotatedCommand() {
        super();
        Annotations.COMMAND.registerCommands(this);
    }
}
