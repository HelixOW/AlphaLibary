package de.alphahelix.alphalibary.addons.core;

import java.io.File;

/**
 * Used to manage the {@link Addon}s
 *
 * @author AlphaHelix
 * @version 1.0
 * @see Addon
 * @since 1.9.2.1
 */
public interface AddonManager {
	
	/**
	 * Load all addons from the given directory.
	 *
	 * @param directory The directory
	 *
	 * @return All loaded addons
	 */
	Addon[] loadAddons(File directory);
	
	/**
	 * Load all addons from the default folder.
	 *
	 * @return All loaded addons
	 */
	Addon[] loadAddons();
	
	/**
	 * Gets all loaded addons
	 *
	 * @return All loaded addons
	 */
	Addon[] getAddons();
}
