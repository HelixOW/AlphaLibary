package de.alphahelix.alphalibary.addons;


import de.alphahelix.alphalibary.addons.core.AddonManager;
import de.alphahelix.alphalibary.addons.core.SimpleAddonManager;
import de.alphahelix.alphalibary.core.AlphaModule;

import java.io.File;
import java.util.logging.Level;

/**
 * Used to load the {@link de.alphahelix.alphalibary.addons.core.Addon}s when the {@link de.alphahelix.alphalibary.core.AlphaLibary} is loaded
 *
 * @author AlphaHelix
 * @version 1.0
 * @see de.alphahelix.alphalibary.addons.core.Addon
 * @see de.alphahelix.alphalibary.core.AlphaLibary
 * @since 1.9.2.1
 */
public class AddonCore implements AlphaModule {
	
	private static File addonFolder;
	private static AddonLogger logger;
	private static AddonManager addonManager;
	
	public static AddonManager getAddonManager() {
		return addonManager;
	}
	
	@Override
	public void enable() {
		logger = new AddonLogger();
		
		addonFolder = new File("plugins/AlphaLibary/Addons");
		if(!addonFolder.exists())
			addonFolder.mkdirs();
		
		getLogger().log(Level.INFO, "Loading addons...");
		addonManager = new SimpleAddonManager(getAddonFolder());
		addonManager.loadAddons();
		
		getLogger().log(Level.INFO, "Successfully loaded " + addonManager.getAddons().length + " Addons");
	}
	
	public static AddonLogger getLogger() {
		return logger;
	}
	
	public static File getAddonFolder() {
		return addonFolder;
	}
}
