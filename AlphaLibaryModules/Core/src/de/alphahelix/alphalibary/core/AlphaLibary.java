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

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class AlphaLibary extends JavaPlugin {

    private static AlphaLibary instance;

    public static AlphaLibary getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;

        for (Class<?> loaded : findClassesImplementing(SimpleLoader.class)) {
            try {
                Bukkit.getPluginManager().registerEvents((Listener) loaded.getDeclaredConstructors()[0].newInstance(true), this);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private Class<?>[] getClasses(File jarFile) {
        Set<Class<?>> classes = new HashSet<>();
        try {
            JarFile file = new JarFile(jarFile);
            for (Enumeration<JarEntry> entry = file.entries(); entry.hasMoreElements(); ) {
                JarEntry jarEntry = entry.nextElement();
                String jarName = jarEntry.getName().replace('/', '.');

                if (jarName.endsWith(".class")) {
                    classes.add(Class.forName(jarName.substring(0, jarName.length() - 6)));
                }
            }
            file.close();
        } catch (IOException | ClassNotFoundException ex) {
            Bukkit.getLogger().severe("Error ocurred at getting classes, log: " + ex);
        }

        return classes.toArray(new Class[classes.size()]);
    }

    private Class<?>[] getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        File[] plugins = new File(".", "plugins").listFiles();

        if (plugins != null) {
            for (File jars : plugins) {
                if (jars.getName().endsWith(".jar")) {
                    Collections.addAll(classes, getClasses(jars));
                }
            }
        }

        return classes.toArray(new Class[classes.size()]);
    }

    private Class<?>[] findClassesImplementing(Class<?> implementedClazz) {
        List<Class<?>> classes = new LinkedList<>();

        for (Class<?> clazz : getClasses()) {
            if (implementedClazz.isAssignableFrom(clazz) && !implementedClazz.equals(clazz))
                classes.add(clazz);
        }

        return classes.toArray(new Class[classes.size()]);
    }
}