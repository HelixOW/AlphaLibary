package io.github.alphahelixdev.alpary.addons.core;

import io.github.alphahelixdev.alpary.addons.AddonCore;
import io.github.alphahelixdev.alpary.addons.core.exceptions.InvalidAddonException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang.Validate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Pattern;

@Getter
@EqualsAndHashCode
@ToString
public class AddonManager {
	
	private final static Pattern JAR_PATTERN = Pattern.compile("(.+?)(\\.jar)");
	
	private final File addonDir;
	private final List<Addon> addons = new ArrayList<>();
	
	public AddonManager(File addonDir) {
		Validate.notNull(addonDir, "Directory cannot be null");
		Validate.isTrue(addonDir.isDirectory(), "Directory must be a directory");
		this.addonDir = addonDir;
	}
	
	public Addon[] loadAddons() {
		return this.loadAddons(this.addonDir);
	}
	
	public Addon[] loadAddons(File directory) {
		Validate.notNull(directory, "Directory cannot be null");
		Validate.isTrue(directory.isDirectory(), "Directory must be a directory");
		
		List<Addon> result = new ArrayList<>();
		
		for(File file : directory.listFiles()) {
			if(!AddonManager.JAR_PATTERN.matcher(file.getName()).matches()) continue;
			
			try {
				result.add(this.loadAddon(file));
			} catch(InvalidAddonException e) {
				AddonCore.getLogger().log(Level.SEVERE,
						"Cannot load '" + file.getName() + "' in folder '" + directory.getPath() + "': " + e.getMessage());
			}
		}
		
		return result.toArray(new Addon[result.size()]);
	}
	
	public synchronized Addon loadAddon(File file) throws InvalidAddonException {
		Addon result;
		
		if(!AddonManager.JAR_PATTERN.matcher(file.getName()).matches()) {
			throw new InvalidAddonException("FileHelp '" + file.getName() + "' is not a Jar FileHelp!");
		}
		
		result = AddonLoader.loadAddon(file);
		
		if(result != null) {
			this.addons.add(result);
			Validate.notNull(result.getDescription());
			AddonCore.getLogger().log(Level.INFO, result.getName() + " [v." + result.getVersion() + " by " + result.getAuthor() + "] loaded");
		}
		
		return result;
	}
	
	public Addon[] getAddons() {
		return this.addons.toArray(new Addon[0]);
	}
}
