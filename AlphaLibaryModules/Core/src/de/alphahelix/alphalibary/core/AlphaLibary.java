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
import de.alphahelix.alphalibary.core.utilites.Utility;
import de.alphahelix.alphalibary.core.utils.abstracts.AbstractReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
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
		
		for(Class<?> loaded : TypeFinder.findClassesAnnotatedWith(SimpleLoader.class)) {
			if(Listener.class.isInstance(loaded))
				try {
					Bukkit.getPluginManager().registerEvents((Listener) loaded.getDeclaredConstructors()[0].newInstance(), this);
				} catch(ReflectiveOperationException ignored) {
				}
		}
		
		for(Class<?> loaded : TypeFinder.findClassesImplementing(AlphaModule.class)) {
			try {
				registerModule((AlphaModule) loaded.newInstance());
			} catch(ReflectiveOperationException ignored) {
			}
		}
		
		for(Class<?> utilities : TypeFinder.findClassesAnnotatedWith(Utility.class)) {
			registerUtil(utilities, utilities.getAnnotation(Utility.class).implementation());
		}
		
		new PluginWatcher(this).run();
		
		System.out.println(AbstractReflectionUtil.instance.getNmsPrefix());
	}
	
	public static void registerModule(AlphaModule module) {
		if(!MODULES.contains(module)) {
			MODULES.add(module);
			System.out.println("Load module " + module.getClass().getSimpleName());
			
			module.load();
			module.enable();
		}
	}
	
	public static void registerUtil(Class<?> util, Class<?> implementation) {
		try {
			Field inst = util.getField("instance");
			
			inst.setAccessible(true);
			
			inst.set(null, implementation.newInstance());
		} catch(ReflectiveOperationException ignored) {
		}
	}
}