package de.alphahelix.alphalibary.annotations;

import de.alphahelix.alphalibary.core.SimpleListener;

public class SimpleAnnotatedEntity extends SimpleListener {

    public SimpleAnnotatedEntity() {
        super();
        Annotations.ENTITIES.load(this);
    }
}
