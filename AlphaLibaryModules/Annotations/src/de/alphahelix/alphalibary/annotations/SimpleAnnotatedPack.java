package de.alphahelix.alphalibary.annotations;

import de.alphahelix.alphalibary.core.utilites.SimpleListener;

/**
 * Loads all annotations inside the class
 *
 * @author AlphaHelix
 * @version 1.0
 * @since 1.9.2.1
 */
public class SimpleAnnotatedPack extends SimpleListener {
	
	public SimpleAnnotatedPack() {
		super();
		Annotations.loadAll(this);
	}
}
