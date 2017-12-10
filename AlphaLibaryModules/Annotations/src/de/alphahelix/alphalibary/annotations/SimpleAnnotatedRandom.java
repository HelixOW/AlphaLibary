package de.alphahelix.alphalibary.annotations;

import de.alphahelix.alphalibary.core.SimpleListener;

public class SimpleAnnotatedRandom extends SimpleListener {

    public SimpleAnnotatedRandom() {
        super();
        Annotations.RANDOM.registerRandoms(this);
    }
}
