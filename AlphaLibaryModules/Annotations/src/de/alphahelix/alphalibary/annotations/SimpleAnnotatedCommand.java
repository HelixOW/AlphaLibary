package de.alphahelix.alphalibary.annotations;

import de.alphahelix.alphalibary.core.SimpleLoader;

@SuppressWarnings("ALL")
public class SimpleAnnotatedCommand implements SimpleLoader {
    public SimpleAnnotatedCommand() {
        Annotations.COMMAND.registerCommands(this);
    }
}
