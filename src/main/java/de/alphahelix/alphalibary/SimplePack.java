package de.alphahelix.alphalibary;

import de.alphahelix.alphalibary.annotations.Annotations;
import de.alphahelix.alphalibary.listener.SimpleListener;

public class SimplePack extends SimpleListener {
    public SimplePack() {
        super();
        Annotations.ITEM.registerItems(this);
        Annotations.COMMAND.registerCommands(this);
    }
}
