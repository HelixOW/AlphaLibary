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

package de.alphahelix.alphalibary.core;

import de.alphahelix.alphalibary.core.type.TypeFinder;
import de.alphahelix.alphalibary.core.utilites.PluginWatcher;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class AlphaLibary extends JavaPlugin {
	
	private static final List<AlphaModule> MODULES = new ArrayList<>();
	
	private static AlphaLibary instance;
	
	public static AlphaLibary getInstance() {
		return instance;
	}
	
	public static List<AlphaModule> getModules() {
		return MODULES;
	}
	
	@Override
	public void onDisable() {
		for(AlphaModule module : MODULES)
			module.disable();
	}
	
	@Override
	public void onEnable() {
		instance = this;
		
		TypeFinder.findClassesAnnotatedWith(SimpleLoader.class, classes -> {
			for(Class<?> loaded : classes) {
				if(Listener.class.isInstance(loaded))
					try {
						Bukkit.getPluginManager().registerEvents((Listener) loaded.getDeclaredConstructors()[0].newInstance(), this);
					} catch(ReflectiveOperationException ignored) {
					}
			}
		});
		
		TypeFinder.findClassesImplementing(AlphaModule.class, classes -> {
			for(Class<?> loaded : classes) {
				try {
					registerModule((AlphaModule) loaded.newInstance());
				} catch(ReflectiveOperationException e) {
					e.printStackTrace();
				}
			}
		});
		
		for(AlphaModule module : MODULES)
			module.enable();
		
		new PluginWatcher(this).run();
	}
	
	public static void registerModule(AlphaModule module) {
		if(!MODULES.contains(module)) {
			MODULES.add(module);
			System.out.println("Load module " + module.getClass().getSimpleName());
			
			module.load();
			module.enable();
		}
	}
	
	
}