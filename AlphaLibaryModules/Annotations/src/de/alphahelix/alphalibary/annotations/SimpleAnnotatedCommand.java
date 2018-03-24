package de.alphahelix.alphalibary.annotations;

import de.alphahelix.alphalibary.core.utilites.SimpleListener;

/**
 * Loads the commands inside the class
 *
 * @author AlphaHelix
 * @version 1.0
 * @since 1.9.2.1
 */
public class SimpleAnnotatedCommand extends SimpleListener {
	public SimpleAnnotatedCommand() {
		super();
		Annotations.COMMAND.registerCommands(this);
	}
}
