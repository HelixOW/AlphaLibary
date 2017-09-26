package de.alphahelix.alphalibary.annotations;

import de.alphahelix.alphalibary.core.SimpleLoader;

@SuppressWarnings("ALL")
public class SimpleAnnotatedItemListener implements SimpleLoader {

    public SimpleAnnotatedItemListener() {
        Annotations.ITEM.registerItems(this);
    }
}
