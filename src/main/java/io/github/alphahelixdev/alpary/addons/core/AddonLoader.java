package io.github.alphahelixdev.alpary.addons.core;

import io.github.alphahelixdev.alpary.addons.AddonCore;
import io.github.alphahelixdev.alpary.addons.core.exceptions.InvalidAddonException;
import org.apache.commons.lang.Validate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class AddonLoader {
	
	public static Addon loadAddon(File file) throws InvalidAddonException {
		Validate.notNull(file, "FileHelp cannot be null");
		
		if(!file.exists())
			throw new InvalidAddonException(new FileNotFoundException(String.valueOf(file.getPath()) + " does not exist"));
		
		File dataFolder = new File(AddonCore.getAddonFolder() + File.separator + file.getName());
		
		if(!dataFolder.exists())
			dataFolder.mkdir();
		
		AddonClassLoader loader;
		
		try {
			loader = new AddonClassLoader(AddonLoader.class.getClassLoader(), file, dataFolder);
			
			loader.close();
			
			return loader.addon;
		} catch(IOException e) {
			throw new InvalidAddonException(e);
		}
	}
}
