package de.alphahelix.alphalibary.annotations;

import de.alphahelix.alphalibary.core.utilites.SimpleListener;

/**
 * Loads the entities inside the class
 *
 * @author AlphaHelix
 * @version 1.0
 * @since 1.9.2.1
 */
public class SimpleAnnotatedEntity extends SimpleListener {
	
	public SimpleAnnotatedEntity() {
		super();
		Annotations.ENTITIES.load(this);
	}
}
