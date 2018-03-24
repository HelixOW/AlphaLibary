package de.alphahelix.alphalibary.annotations;

import de.alphahelix.alphalibary.core.utilites.SimpleListener;

/**
 * Loads the randoms inside the class
 *
 * @author AlphaHelix
 * @version 1.0
 * @since 1.9.2.1
 */
public class SimpleAnnotatedRandom extends SimpleListener {
	
	public SimpleAnnotatedRandom() {
		super();
		Annotations.RANDOM.registerRandoms(this);
	}
}
