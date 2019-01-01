package io.github.alphahelixdev.alpary.addons;

import io.github.alphahelixdev.alpary.addons.core.AddonManager;

import java.io.File;
import java.util.logging.Level;

public class AddonCore {
	
	private static final File ADDON_FOLDER = new File("plugins/Alpary/Addons");
	private static AddonLogger logger;
	private static AddonManager addonManager;
	
	public void enable() {
		logger = new AddonLogger();
		
		if(!getAddonFolder().exists())
			getAddonFolder().mkdirs();
		
		getLogger().log(Level.INFO, "Loading addons...");
		AddonCore.addonManager = new AddonManager(getAddonFolder());
		
		AddonCore.getAddonManager().loadAddons();
		
		getLogger().log(Level.INFO, "Successfully loaded " + AddonCore.getAddonManager().getAddons().length + " Addons");
	}
	
	public static File getAddonFolder() {
		return ADDON_FOLDER;
	}
	
	public static AddonLogger getLogger() {
		return AddonCore.logger;
	}
	
	public static AddonManager getAddonManager() {
		return AddonCore.addonManager;
	}
}
