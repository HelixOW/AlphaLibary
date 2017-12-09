package de.alphahelix.alphalibary.annotations;

import de.alphahelix.alphalibary.core.SimpleListener;

public class SimpleAnnotatedPack extends SimpleListener {

    public SimpleAnnotatedPack() {
        super();

        Annotations.COMMAND.registerCommands(this);
        Annotations.RANDOM.registerRandoms(this);
        Annotations.ITEM.registerItems(this);
    }
}
