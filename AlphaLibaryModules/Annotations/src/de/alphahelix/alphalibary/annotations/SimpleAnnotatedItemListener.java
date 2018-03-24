package de.alphahelix.alphalibary.annotations;

import de.alphahelix.alphalibary.core.utilites.SimpleListener;

/**
 * Loads the items inside the class
 *
 * @author AlphaHelix
 * @version 1.0
 * @since 1.9.2.1
 */
public class SimpleAnnotatedItemListener extends SimpleListener {
	
	public SimpleAnnotatedItemListener() {
		super();
		Annotations.ITEM.registerItems(this);
	}
}
