package de.alphahelix.alphalibary.annotations;

import de.alphahelix.alphalibary.core.SimpleListener;

public class SimpleAnnotatedItemListener extends SimpleListener {

    public SimpleAnnotatedItemListener() {
        super();
        Annotations.ITEM.registerItems(this);
    }
}
