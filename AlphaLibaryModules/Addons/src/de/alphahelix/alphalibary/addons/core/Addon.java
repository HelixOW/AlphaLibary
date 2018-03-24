/*
 *
 * Copyright (C) <2017>  <AlphaHelixDev>
 *
 *       This program is free software: you can redistribute it under the
 *       terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package de.alphahelix.alphalibary.addons.core;

import java.io.File;

/**
 * Needs to be extended, to let the {@link de.alphahelix.alphalibary.core.AlphaLibary} realize it's a Addon
 *
 * @author AlphaHelix
 * @since 1.9.2.1
 * @version 1.0
 * @see de.alphahelix.alphalibary.core.AlphaLibary
 */
public abstract class Addon {
	
	private File dataFolder;
	private AddonDescriptionFile description;
	private ClassLoader loader;
	
	/**
	 * Inits / reinits the values
	 *
	 * @param classLoader the {@link AddonClassLoader} to use
	 * @param dataFolder  the folder in which all File should be stored
	 * @param description the addon.yml file
	 */
	final void init(AddonClassLoader classLoader, File dataFolder, AddonDescriptionFile description) {
		this.dataFolder = dataFolder;
		this.description = description;
		this.loader = classLoader;
		onEnable();
	}
	
	/**
	 * Is called when the {@link de.alphahelix.alphalibary.core.AlphaLibary} loads the {@link Addon}
	 */
	public abstract void onEnable();
	
	public File getDataFolder() {
		return dataFolder;
	}
	
	public AddonDescriptionFile getDescription() {
		return description;
	}
	
	public ClassLoader getLoader() {
		return loader;
	}
}
