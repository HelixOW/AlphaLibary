package de.alphahelix.alphalibary.item;

import de.alphahelix.alphalibary.annotations.Annotations;
import de.alphahelix.alphalibary.listener.SimpleListener;

public class SimpleAnnotatedItemListener extends SimpleListener {
    public SimpleAnnotatedItemListener() {
        super();
        Annotations.ITEM.registerItems(this);
    }
}
