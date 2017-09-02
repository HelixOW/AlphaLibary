package de.alphahelix.alphalibary.item;

import de.alphahelix.alphalibary.annotations.Annotations;
import org.bukkit.event.Listener;

public class SimpleAnnotatedItemListener implements Listener {
    public SimpleAnnotatedItemListener() {
        super();
        Annotations.ITEM.registerItems(this);
    }
}
