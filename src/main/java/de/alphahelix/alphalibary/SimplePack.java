package de.alphahelix.alphalibary;

import de.alphahelix.alphalibary.annotations.Annotations;
import de.alphahelix.alphalibary.listener.SimpleLoader;

public class SimplePack implements SimpleLoader {
    public SimplePack() {
        Annotations.ITEM.registerItems(this);
        Annotations.COMMAND.registerCommands(this);
    }
}
