package de.alphahelix.alphalibary.annotations;

import de.alphahelix.alphalibary.core.utilites.SimpleListener;

public class SimpleAnnotatedCommand extends SimpleListener {
    public SimpleAnnotatedCommand() {
        super();
        Annotations.COMMAND.registerCommands(this);
    }
}
