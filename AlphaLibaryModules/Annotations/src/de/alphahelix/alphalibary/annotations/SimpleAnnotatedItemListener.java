package de.alphahelix.alphalibary.annotations;

import org.bukkit.event.Listener;

public class SimpleAnnotatedItemListener implements Listener {

    public SimpleAnnotatedItemListener() {
        Annotations.ITEM.registerItems(this);
    }
}
