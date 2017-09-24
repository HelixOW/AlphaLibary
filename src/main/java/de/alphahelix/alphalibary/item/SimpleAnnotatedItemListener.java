package de.alphahelix.alphalibary.item;

import de.alphahelix.alphalibary.annotations.Annotations;
import de.alphahelix.alphalibary.listener.SimpleLoader;

public class SimpleAnnotatedItemListener implements SimpleLoader {
    public SimpleAnnotatedItemListener() {
        Annotations.ITEM.registerItems(this);
    }
}
