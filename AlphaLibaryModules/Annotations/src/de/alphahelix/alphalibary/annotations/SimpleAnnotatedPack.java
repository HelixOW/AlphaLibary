package de.alphahelix.alphalibary.annotations;

import de.alphahelix.alphalibary.core.SimpleListener;

public class SimpleAnnotatedPack extends SimpleListener {

    public SimpleAnnotatedPack() {
        super();
        Annotations.loadAll(this);
    }
}