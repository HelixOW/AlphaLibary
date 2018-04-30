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
import io.netty.util.internal.ConcurrentSet;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AlphaLibary extends JavaPlugin {
	
	private static final List<AlphaModule> MODULES = new ArrayList<>();
	
	private static Set<Class<?>> moduleClasses;
	private static Set<String> loadedClasses = new ConcurrentSet<>();
	private static AlphaLibary instance;
	
	public static AlphaLibary getInstance() {
		return instance;
	}
	
	public static List<AlphaModule> getModules() {
		return MODULES;
	}
	
	public static void registerModules() throws ReflectiveOperationException {
		Set<Class<?>> runSet = new ConcurrentSet<>();
		if(moduleClasses == null)
			moduleClasses = TypeFinder.findClassesImplementing(AlphaModule.class);
		
		for(Class<?> load : moduleClasses) {
			Dependency depends = load.getAnnotation(Dependency.class);
			
			if(depends == null) {
				registerModule((AlphaModule) load.newInstance());
				runSet.add(load);
				loadedClasses.add(load.getSimpleName());
			} else {
				for(String dependency : depends.dependencies()) {
					if(loadedClasses.contains(dependency)) {
						registerModule((AlphaModule) load.newInstance());
						runSet.add(load);
						loadedClasses.add(load.getSimpleName());
					}
				}
			}
		}
		
		moduleClasses.removeAll(runSet);
		
		if(moduleClasses.size() != 0)
			registerModules();
	}
	
	public static void registerModule(AlphaModule module) {
		if(!MODULES.contains(module)) {
			MODULES.add(module);
			System.out.println("Load module " + module.getClass().getSimpleName());
			
			module.load();
			module.enable();
		}
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
			else {
				try {
					loaded.getDeclaredConstructors()[0].newInstance();
				} catch(ReflectiveOperationException ignored) {
				}
			}
		}
		
		try {
			registerModules();
		} catch(ReflectiveOperationException e) {
			e.printStackTrace();
		}
		
		new PluginWatcher(this).run();
	}
}