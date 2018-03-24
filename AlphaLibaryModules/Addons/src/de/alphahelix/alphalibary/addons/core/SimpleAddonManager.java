package de.alphahelix.alphalibary.addons.core;

import de.alphahelix.alphalibary.addons.AddonCore;
import de.alphahelix.alphalibary.addons.core.exceptions.InvalidAddonException;
import org.apache.commons.lang.Validate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Pattern;

/**
 * Default implementation of the {@link AddonManager}
 *
 * @author AlphaHelix
 * @version 1.0
 * @see AddonManager
 * @since 1.9.2.1
 */
public class SimpleAddonManager implements AddonManager {
	
	private final static Pattern JAR_PATTERN = Pattern.compile("(.+?)(\\.jar)");
	
	private final File addonDir;
	private final List<Addon> addons = new ArrayList<>();
	
	public SimpleAddonManager(File addonDir) {
		Validate.notNull(addonDir, "Directory cannot be null");
		Validate.isTrue(addonDir.isDirectory(), "Directory must be a directory");
		this.addonDir = addonDir;
	}
	
	public synchronized Addon loadAddon(File file) throws InvalidAddonException {
		Addon result;
		
		if(!JAR_PATTERN.matcher(file.getName()).matches()) {
			throw new InvalidAddonException("FileHelp '" + file.getName() + "' is not a Jar FileHelp!");
		}
		
		result = AddonLoader.loadAddon(file);
		
		if(result != null) {
			this.addons.add(result);
			Validate.notNull(result.getDescription());
			AddonCore.getLogger().log(Level.INFO, result.getDescription().getName() + " v." + result.getDescription().getVersion() + " by " + result.getDescription().getAuthor() + " loaded");
		}
		
		return result;
	}
	
	@Override
	public Addon[] loadAddons(File directory) {
		Validate.notNull(directory, "Directory cannot be null");
		Validate.isTrue(directory.isDirectory(), "Directory must be a directory");
		
		List<Addon> result = new ArrayList<>();
		
		for(File file : directory.listFiles()) {
			if(!JAR_PATTERN.matcher(file.getName()).matches()) continue;
			
			try {
				result.add(this.loadAddon(file));
			} catch(InvalidAddonException e) {
				AddonCore.getLogger().log(Level.SEVERE,
						"Cannot load '" + file.getName() + "' in folder '" + directory.getPath() + "': " + e.getMessage());
			}
		}
		
		return result.toArray(new Addon[result.size()]);
	}
	
	@Override
	public Addon[] loadAddons() {
		return this.loadAddons(this.addonDir);
	}
	
	@Override
	public Addon[] getAddons() {
		return this.addons.toArray(new Addon[this.addons.size()]);
	}
}
