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

package de.alphahelix.alphalibary.reflection;

import com.mojang.authlib.GameProfile;
import de.alphahelix.alphalibary.core.utils.JSONUtil;
import de.alphahelix.alphalibary.core.utils.abstracts.AbstractReflectionUtil;
import de.alphahelix.alphalibary.reflection.nms.BlockPos;
import de.alphahelix.alphalibary.reflection.nms.nbt.NBTCompound;
import de.alphahelix.alphalibary.reflection.nms.nbt.NBTList;
import de.alphahelix.alphalibary.reflection.nms.nbt.NBTType;
import de.alphahelix.alphalibary.reflection.nms.packets.IPacket;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class IReflectionUtil extends AbstractReflectionUtil {
	
	/**
	 * Gets a accessible {@link Field} out of a {@link Class}
	 *
	 * @param name  the name of the {@link Field}
	 * @param clazz the {@link Class} where the {@link Field} is located at
	 *
	 * @return a accessible {@link Field}
	 */
	public SaveField getField(String name, Class<?> clazz) {
		try {
			Field f = clazz.getField(name);
			return new SaveField(f, -1);
		} catch(Exception e) {
			e.printStackTrace();
			return new SaveField();
		}
	}
	
	public SaveField getFirstType(Class<?> type, Class<?> clazz) {
		try {
			for(Field field : clazz.getDeclaredFields()) {
				if(field.getType().equals(type)) {
					return new SaveField(field, -1);
				}
			}
			
			throw new NoSuchFieldException("Could not resolve field of type '" + type.toString() + "' in class " + clazz);
		} catch(NoSuchFieldException e) {
			e.printStackTrace();
			return new SaveField();
		}
	}
	
	public SaveField getLastType(Class<?> type, Class<?> clazz) {
		Field field = null;
		for(Field field1 : clazz.getDeclaredFields()) {
			if(field1.getType().equals(type)) {
				field = field1;
			}
		}
		
		if(field == null) {
			try {
				throw new NoSuchFieldException("Could not resolve field of type '" + type.toString() + "' in class " + clazz);
			} catch(NoSuchFieldException e) {
				e.printStackTrace();
				return new SaveField();
			}
		}
		return new SaveField(field, -1);
	}
	
	public SaveMethod getDeclaredMethod(String name, String nmsClazz, Class<?>... parameterClasses) {
		return getDeclaredMethod(name, getNmsClass(nmsClazz), parameterClasses);
	}
	
	/**
	 * Gets a private accessible {@link Method} out of a {@link Class}
	 *
	 * @param name             the name of the {@link Method}
	 * @param clazz            the {@link Class} where the {@link Method} is located at
	 * @param parameterClasses the classes of the parameters for the method
	 *
	 * @return a accessible {@link SaveMethod}
	 */
	public SaveMethod getDeclaredMethod(String name, Class<?> clazz, Class<?>... parameterClasses) {
		return getDeclaredMethod(new MethodInfo(name, clazz, parameterClasses));
	}
	
	public SaveMethod getDeclaredMethod(MethodInfo methodInfo) {
		if(ReflectiveStorage.getMethods().containsKey(methodInfo))
			return ReflectiveStorage.getMethods().get(methodInfo);
		
		try {
			SaveMethod sm = new SaveMethod(methodInfo.getType().getDeclaredMethod(methodInfo.getName(), methodInfo.getParameters()));
			
			ReflectiveStorage.getMethods().put(methodInfo, sm);
			
			return sm;
		} catch(Exception e) {
			e.printStackTrace();
			return new SaveMethod();
		}
	}
	
	public Class<?> getClass(String name, boolean asArray) {
		return getClass(new ClassInfo(name, asArray));
	}
	
	/**
	 * @return the net.minecraft.sserver version {@link String}
	 */
	public String getNmsPrefix() {
		return "net.minecraft.server." + getVersion() + ".";
	}
	
	public Class<?> getClass(ClassInfo classInfo) {
		if(ReflectiveStorage.getClasses().containsKey(classInfo))
			return ReflectiveStorage.getClasses().get(classInfo);
		
		try {
			if(classInfo.isAsArray()) {
				Class<?> arrayClazz = Array.newInstance(Class.forName(classInfo.getName()), 0).getClass();
				
				ReflectiveStorage.getClasses().put(new ClassInfo(classInfo.getName(), true), arrayClazz);
				
				return arrayClazz;
			} else {
				Class<?> clazz = Class.forName(classInfo.getName());
				
				ReflectiveStorage.getClasses().put(new ClassInfo(classInfo.getName(), false), clazz);
				
				return clazz;
			}
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Gets a NMS {@link Class}[] from the name of it
	 *
	 * @param name the name of the NMS {@link Class}[]
	 *
	 * @return the NMS {@link Class}[]
	 */
	public Class<?> getNmsClassAsArray(String name) {
		return getClass(getNmsPrefix() + name, true);
	}
	
	/**
	 * Gets a org.bukkit.craftbukkit {@link Class}[] from the name of it
	 *
	 * @param name the name of the obc {@link Class}[]
	 *
	 * @return the obc {@link Class}[]
	 */
	public Class<?> getCraftBukkitClassAsArray(String name) {
		return getClass(getCraftBukkitPrefix() + name, true);
	}
	
	/**
	 * @return the org.bukkit.craftbukkit version {@link String}
	 */
	public String getCraftBukkitPrefix() {
		return "org.bukkit.craftbukkit." + getVersion() + ".";
	}
	
	/**
	 * Gets the EnumGamemode from of a {@link OfflinePlayer}
	 *
	 * @param p the {@link OfflinePlayer} to get its EnumGamemode from
	 *
	 * @return the EnumGamemode as an {@link Object}
	 */
	public Object getEnumGamemode(OfflinePlayer p) {
		try {
			SaveField fInteractManager = getDeclaredField("playerInteractManager", "EntityPlayer");
			
			return getDeclaredField("gamemode", "PlayerInteractManager").get(fInteractManager.get(getEntityPlayer(p)));
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Gets the EntityPlayer out of a {@link OfflinePlayer}
	 *
	 * @param p the {@link OfflinePlayer} to get its EntityPlayer from
	 *
	 * @return the EntityPlayer as an {@link Object}
	 */
	public Object getEntityPlayer(OfflinePlayer p) {
		try {
			return getDeclaredMethod("getHandle", getCraftBukkitClass("entity.CraftPlayer")).invoke(p, true);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public int getEnumConstantID(Object enumConstant) {
		try {
			return (int) Enum.class.getMethod("ordinal").invoke(enumConstant);
		} catch(NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * Gets the ping from of a {@link OfflinePlayer}
	 *
	 * @param p the {@link OfflinePlayer} to get its ping from
	 *
	 * @return the ping as an {@link Object}
	 */
	public int getPing(Player p) {
		return (int) getDeclaredField("ping", "EntityPlayer").get(getEntityPlayer(p));
	}
	
	public SaveField getDeclaredField(String name, String nmsClazz) {
		return getDeclaredField(name, getNmsClass(nmsClazz));
	}
	
	public Object setNBTTag(Object nbtTag, Object nmsItem) {
		getDeclaredMethod("setTag", nmsItem.getClass(), nbtTag.getClass()).invoke(nmsItem, true, nbtTag);
		
		return nmsItem;
	}
	
	public ItemStack getBukkitItemStack(Object nmsItem) {
		return (ItemStack) getDeclaredMethod("asCraftMirror", getCraftBukkitClass("inventory.CraftItemStack"), nmsItem.getClass()).invoke(null, true, nmsItem);
	}
	
	public Object getItemRootNBTTagCompound(Object nmsItem) {
		return getDeclaredMethod("getTag", nmsItem.getClass()).invoke(nmsItem, true);
	}
	
	public Object getEntityNBTTagCompound(Object nmsEntity) {
		Object nbt = getNewNBTTag();
		
		if(nbt == null) return null;
		
		Object a = getDeclaredMethod("d", nmsEntity.getClass(), nbt.getClass()).invoke(nmsEntity, true, nbt);
		if(a == null)
			a = nbt;
		return a;
	}
	
	public Object getNewNBTTag() {
		return getDeclaredConstructor("NBTTagCompound").newInstance(true);
	}
	
	public SaveConstructor getDeclaredConstructor(String nmsClazz, Class<?>... parameterClasses) {
		return getDeclaredConstructor(getNmsClass(nmsClazz), parameterClasses);
	}
	
	public SaveConstructor getDeclaredConstructor(Class<?> clazz, Class<?>... parameterClasses) {
		return getDeclaredConstructor(new ConstructorInfo(clazz, parameterClasses));
	}
	
	public SaveConstructor getDeclaredConstructor(ConstructorInfo constructorInfo) {
		if(ReflectiveStorage.getConstructors().containsKey(constructorInfo))
			return ReflectiveStorage.getConstructors().get(constructorInfo);
		
		try {
			SaveConstructor sc = new SaveConstructor<>(constructorInfo.getType().getDeclaredConstructor(constructorInfo.getParameters()));
			
			ReflectiveStorage.getConstructors().put(constructorInfo, sc);
			
			return sc;
		} catch(Exception e) {
			e.printStackTrace();
			return new SaveConstructor();
		}
	}
	
	public Object setEntityNBTTag(Object nbtTag, Object nmsEntity) {
		getDeclaredMethod("a", getNmsClass("NBTTagCompound")).invoke(nmsEntity, true, nbtTag);
		return nmsEntity;
	}
	
	public Object getTileEntityNBTTagCompound(BlockState tile) {
		Object pos = toBlockPosition(new BlockPos() {
			@Override
			public int getX() {
				return tile.getX();
			}
			
			@Override
			public int getY() {
				return tile.getY();
			}
			
			@Override
			public int getZ() {
				return tile.getZ();
			}
		});
		Object nmsWorld = getWorldServer(tile.getWorld());
		
		if(nmsWorld == null || pos == null) return null;
		
		Object nmsTile = getDeclaredMethod("getTileEntity", nmsWorld.getClass(), pos.getClass()).invoke(nmsWorld, true, pos);
		Object tag = getNewNBTTag();
		Object a = getDeclaredMethod("save", getNmsClass("TileEntity"), getNmsClass("NBTTagCompound")).invoke(nmsTile, true, tag);
		if(a == null) a = tag;
		return a;
	}
	
	public void setTileEntityNBTTagCompound(BlockState tile, Object nbtTag) {
		Object pos = toBlockPosition(new BlockPos() {
			@Override
			public int getX() {
				return tile.getX();
			}
			
			@Override
			public int getY() {
				return tile.getY();
			}
			
			@Override
			public int getZ() {
				return tile.getZ();
			}
		});
		Object nmsWorld = getWorldServer(tile.getWorld());
		
		if(nmsWorld == null || pos == null) return;
		
		Object nmsTile = getDeclaredMethod("getTileEntity", nmsWorld.getClass(), pos.getClass()).invoke(nmsWorld, true, pos);
		
		getDeclaredMethod("a", getNmsClass("TileEntity"), getNmsClass("NBTTagCompound")).invoke(nmsTile, true, nbtTag);
	}
	
	public Object getSubNBTTagCompound(Object compound, String name) {
		return getDeclaredMethod("getCompound", compound.getClass(), String.class).invoke(compound, true, name);
	}
	
	/**
	 * Sends a Packet[] to a {@link Player}
	 *
	 * @param p       the {@link Player} to receive the Packet
	 * @param packets the Packet[] to send
	 */
	public void sendPackets(Player p, Object... packets) {
		for(Object packet : packets)
			sendPacket(p, packet);
	}
	
	/**
	 * Sends a Packet to a {@link Player}
	 *
	 * @param p      the {@link Player} to receive the Packet
	 * @param packet the Packet to send
	 */
	public void sendPacket(Player p, Object packet) {
		try {
			Object nmsPlayer = getEntityPlayer(p);
			
			if(nmsPlayer == null) return;
			
			Object con = getDeclaredField("playerConnection", nmsPlayer.getClass()).get(nmsPlayer);
			
			getMethod("sendPacket", getNmsClass("PlayerConnection"), getNmsClass("Packet")).invoke(con, true, packet);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends a Packet[] to all {@link Player}s
	 *
	 * @param packets the Packet[] to send
	 */
	public void sendPackets(Object... packets) {
		for(Player p : Bukkit.getOnlinePlayers())
			for(Object packet : packets)
				sendPacket(p, packet);
	}
	
	/**
	 * Sends a Packet[] to all {@link Player}s but one
	 *
	 * @param notFor  the {@link Player} to not send the Packet[] to
	 * @param packets the Packet[] to send
	 */
	public void sendPacketsNotFor(Player notFor, Object... packets) {
		sendPacketsNotFor(notFor.getName(), packets);
	}
	
	/**
	 * Sends a Packet[] to all {@link Player}s but one
	 *
	 * @param notFor  the {@link Player} to not send the Packet[] to
	 * @param packets the Packet[] to send
	 */
	public void sendPacketsNotFor(String notFor, Object... packets) {
		for(Player p : Bukkit.getOnlinePlayers())
			if(!p.getName().equals(notFor)) for(Object packet : packets)
				sendPacket(p, packet);
	}
	
	/**
	 * Gets the entity ID out of a {@link Entity} from its CraftEntity
	 *
	 * @param entity the {@link Entity} to get its enitity ID from
	 *
	 * @return the entity ID
	 */
	public int getEntityID(Entity entity) {
		return (int) getMethod("getId", getNmsClass("Entity")).invoke(getMethod("getHandle", getCraftBukkitClass("entity.CraftEntity")).invoke(entity, true), true);
	}
	
	/**
	 * Gets the entity ID out of a {@link Entity} from its NMS Entity
	 *
	 * @param entity the {@link Entity} to get its enitity ID from
	 *
	 * @return the entity ID
	 */
	public int getEntityID(Object entity) {
		return (int) getDeclaredField("id", "Entity").get(entity);
	}
	
	/**
	 * Gets the NMS Entity out of a {@link Entity}
	 *
	 * @param entity the {@link Entity} to get its NMS Entity from
	 *
	 * @return the NMS Entity
	 */
	public Object getCraftbukkitEntity(Entity entity) {
		return getMethod("getHandle", getCraftBukkitClass("entity.CraftEntity")).invoke(entity, true);
	}
	
	public SaveMethod getMethod(MethodInfo methodInfo) {
		if(ReflectiveStorage.getMethods().containsKey(methodInfo))
			return ReflectiveStorage.getMethods().get(methodInfo);
		
		try {
			SaveMethod sm = new SaveMethod(methodInfo.getType().getMethod(methodInfo.getName(), methodInfo.getParameters()));
			
			ReflectiveStorage.getMethods().put(methodInfo, sm);
			
			return sm;
		} catch(Exception e) {
			e.printStackTrace();
			return new SaveMethod();
		}
	}
	
	/**
	 * Gets the WorldServer out of its Bukkit {@link World}
	 *
	 * @param world the {@link World} to get its WorldServer from
	 *
	 * @return the WorldServer
	 */
	public Object getWorldServer(World world) {
		return getMethod("getHandle", getCraftBukkitClass("CraftWorld")).invoke(world, true);
	}
	
	/**
	 * Gets the MinecraftServer out of its Bukkit {@link Server}
	 *
	 * @return the MinecraftServer
	 */
	public Object getMinecraftServer() {
		return getMethod("getServer", getCraftBukkitClass("CraftServer")).invoke(Bukkit.getServer(), true);
	}
	
	/**
	 * Gets the ItemStack out of its Bukkit {@link ItemStack}
	 *
	 * @param item the {@link ItemStack} to get its ItemStack from
	 *
	 * @return the ItemStack
	 */
	public Object getNMSItemStack(ItemStack item) {
		return getMethod("asNMSCopy", getCraftBukkitClass("inventory.CraftItemStack"), ItemStack.class).invoke(null, true, item);
	}
	
	/**
	 * Gets the {@link String} out of its IChatBaseComponent
	 *
	 * @param baseComponentArray the IChatBaseComponent to get the {@link String} from
	 *
	 * @return the {@link String}
	 */
	public String[] fromIChatBaseComponent(Object... baseComponentArray) {
		
		String[] array = new String[baseComponentArray.length];
		
		for(int i = 0; i < array.length; i++) {
			array[i] = fromIChatBaseComponent(baseComponentArray[i]);
		}
		
		return array;
	}
	
	/**
	 * Gets a accessible {@link Method} out of a {@link Class}
	 *
	 * @param name             the name of the {@link Method}
	 * @param clazz            the {@link Class} where the {@link Method} is located at
	 * @param parameterClasses the classes of the parameters for the method
	 *
	 * @return a accessible {@link SaveMethod}
	 */
	public SaveMethod getMethod(String name, Class<?> clazz, Class<?>... parameterClasses) {
		return getMethod(new MethodInfo(name, clazz, parameterClasses));
	}
	
	/**
	 * Gets a OBC {@link Class} from the name of it
	 *
	 * @param name the name of the NMS {@link Class}
	 *
	 * @return the NMS {@link Class}
	 */
	public Class<?> getCraftBukkitClass(String name) {
		return getClass(getCraftBukkitPrefix() + name, false);
	}
	
	/**
	 * Gets a NMS {@link Class} from the name of it
	 *
	 * @param name the name of the NMS {@link Class}
	 *
	 * @return the NMS {@link Class}
	 */
	public Class<?> getNmsClass(String name) {
		return getClass(getNmsPrefix() + name, false);
	}
	
	/**
	 * Gets the IChatBaseComponent out of a {@link String}
	 *
	 * @param strings the {@link String} to get the IChatBaseComponent from
	 *
	 * @return the IChatBaseComponent
	 *
	 * @see ReflectionUtil#toIChatBaseComponent(String...)
	 * @deprecated
	 */
	@Deprecated
	public Object[] serializeString(String... strings) {
		Object[] array = (Object[]) Array.newInstance(getNmsClass("IChatBaseComponent"), strings.length);
		
		for(int i = 0; i < array.length; i++) {
			array[i] = toIChatBaseComponentArray(strings[i]);
		}
		
		return array;
	}
	
	public Object[] toIChatBaseComponent(String... strings) {
		Object[] array = (Object[]) Array.newInstance(getNmsClass("IChatBaseComponent"), strings.length);
		
		for(int i = 0; i < array.length; i++) {
			array[i] = toIChatBaseComponentArray(strings[i]);
		}
		
		return array;
	}
	
	/**
	 * Gets the CraftChatMessage out of a {@link String}
	 *
	 * @param s the {@link String} to get its CraftChatMessage from
	 *
	 * @return the CraftChatMessage as an {@link Object}
	 *
	 * @see ReflectionUtil#toIChatBaseComponentArray(String) (String)
	 * @deprecated
	 */
	@Deprecated
	public Object serializeString(String s) {
		try {
			Class<?> chatSerelizer = getCraftBukkitClass("util.CraftChatMessage");
			
			Method mSerelize = chatSerelizer.getMethod("fromString", String.class);
			
			return ((Object[]) mSerelize.invoke(null, s))[0];
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Object toIChatBaseComponentArray(String s) {
		SaveMethod mserialize = getDeclaredMethod("fromString", getCraftBukkitClass("util.CraftChatMessage"), String.class);
		
		return ((Object[]) mserialize.invoke(null, true, s))[0];
	}
	
	/**
	 * Gets a private accessible {@link Field} out of a {@link Class}
	 *
	 * @param name  the name of the {@link Field}
	 * @param clazz the {@link Class} where the {@link Field} is located at
	 *
	 * @return a accessible {@link Field}
	 */
	public SaveField getDeclaredField(String name, Class<?> clazz) {
		try {
			Field f = clazz.getDeclaredField(name);
			return new SaveField(f, -1);
		} catch(Exception e) {
			e.printStackTrace();
			return new SaveField();
		}
	}
	
	public GameProfile getGameProfile(Player p) {
		return (GameProfile) getDeclaredMethod("getProfile", getCraftBukkitClass("entity.CraftPlayer")).invoke(p, true);
	}
	
	public Class<?>[] getClasses(File jarFile, String pckg) {
		return getClasses(new JarInfo(jarFile, pckg));
	}
	
	public Class<?>[] getClasses(JarInfo info) {
		if(ReflectiveStorage.getJars().containsKey(info))
			return ReflectiveStorage.getJars().get(info);
		
		Set<Class<?>> classes = new HashSet<>();
		try {
			JarFile file = new JarFile(info.getJarFile());
			for(Enumeration<JarEntry> entry = file.entries(); entry.hasMoreElements(); ) {
				JarEntry jarEntry = entry.nextElement();
				String jarName = jarEntry.getName().replace('/', '.');
				
				if(info.isNeedsPckg()) {
					if(jarName.startsWith(info.getPckg()) && jarName.endsWith(".class")) {
						classes.add(getClass(jarName.substring(0, jarName.length() - 6), false));
					}
				} else if(jarName.endsWith(".class")) {
					classes.add(getClass(jarName.substring(0, jarName.length() - 6), false));
				}
			}
			file.close();
		} catch(IOException ex) {
			Bukkit.getLogger().severe("Error ocurred at getting classes, log: " + ex);
		}
		
		Class<?>[] classArray = classes.toArray(new Class[classes.size()]);
		
		ReflectiveStorage.getJars().put(info, classArray);
		
		return classArray;
	}
	
	public Class<?>[] findClassesImplementing(Class<?> implementedClazz) {
		List<Class<?>> classes = new LinkedList<>();
		
		for(Class<?> clazz : getClasses()) {
			if(implementedClazz.isAssignableFrom(clazz) && !implementedClazz.equals(clazz))
				classes.add(clazz);
		}
		
		return classes.toArray(new Class[classes.size()]);
	}
	
	public Class<?>[] getClasses() {
		Set<Class<?>> classes = new HashSet<>();
		File[] plugins = new File(".", "plugins").listFiles();
		
		if(plugins != null) {
			for(File jars : plugins) {
				if(jars.getName().endsWith(".jar")) {
					Collections.addAll(classes, getClasses(jars));
				}
			}
		}
		
		return classes.toArray(new Class[classes.size()]);
	}
	
	public Class<?>[] getClasses(File jarFile) {
		return getClasses(new JarInfo(jarFile, ""));
	}
	
	public Class<?>[] findClassesAnnotatedWith(Class<? extends Annotation> annotation) {
		List<Class<?>> classes = new LinkedList<>();
		
		for(Class<?> clazz : getClasses()) {
			if(clazz.isAnnotationPresent(annotation))
				classes.add(clazz);
		}
		
		return classes.toArray(new Class[classes.size()]);
	}
	
	public Class<?>[] findClassesNotAnnotatedWith(Class<? extends Annotation> annotation) {
		List<Class<?>> classes = new LinkedList<>();
		
		for(Class<?> clazz : getClasses()) {
			if(!clazz.isAnnotationPresent(annotation))
				classes.add(clazz);
		}
		
		return classes.toArray(new Class[classes.size()]);
	}
	
	public SaveField[] findFieldsAnnotatedWith(Class<? extends Annotation> annotation) {
		return findFieldsAnnotatedWith(annotation, getClasses());
	}
	
	public SaveField[] findFieldsAnnotatedWith(Class<? extends Annotation> annotation, Class<?>... classes) {
		List<SaveField> fields = new LinkedList<>();
		int i = 0;
		
		for(Class<?> clazz : classes) {
			for(Field f : clazz.getDeclaredFields()) {
				if(f.isAnnotationPresent(annotation)) {
					fields.add(new SaveField(f, i));
				}
				i++;
			}
			i = 0;
		}
		
		return fields.toArray(new SaveField[fields.size()]);
	}
	
	public SaveField[] findFieldsNotAnnotatedWith(Class<? extends Annotation> annotation) {
		return findFieldsNotAnnotatedWith(annotation, getClasses());
	}
	
	public SaveField[] findFieldsNotAnnotatedWith(Class<? extends Annotation> annotation, Class<?>... classes) {
		List<SaveField> fields = new LinkedList<>();
		int i = 0;
		
		for(Class<?> clazz : classes) {
			for(Field f : clazz.getDeclaredFields()) {
				if(!f.isAnnotationPresent(annotation)) {
					fields.add(new SaveField(f, i));
				}
				i++;
			}
			i = 0;
		}
		
		return fields.toArray(new SaveField[fields.size()]);
	}
	
	public SaveField[] findFieldsOfType(Class<?> type) {
		return findFieldsOfType(type, getClasses());
	}
	
	public SaveField[] findFieldsOfType(Class<?> type, Class<?>... classes) {
		List<SaveField> fields = new LinkedList<>();
		int i = 0;
		
		for(Class<?> clazz : classes) {
			for(Field f : clazz.getDeclaredFields()) {
				if(f.getType().equals(type)) {
					fields.add(new SaveField(f, i));
				}
				i++;
			}
			i = 0;
		}
		
		return fields.toArray(new SaveField[fields.size()]);
	}
	
	public SaveField findFieldAtIndex(int index, Class<?>... classes) {
		int i = 0;
		
		for(Class<?> clazz : classes) {
			for(Field f : clazz.getDeclaredFields()) {
				if(i == index)
					return new SaveField(f, i);
				i++;
			}
		}
		
		return new SaveField();
	}
	
	public SaveMethod[] findMethodsAnnotatedWith(Class<? extends Annotation> annotation) {
		return findMethodsAnnotatedWith(annotation, getClasses());
	}
	
	public SaveMethod[] findMethodsAnnotatedWith(Class<? extends Annotation> annotation, Class<?>... classes) {
		List<SaveMethod> methods = new LinkedList<>();
		
		for(Class<?> clazz : classes) {
			for(Method m : clazz.getDeclaredMethods()) {
				if(m.isAnnotationPresent(annotation))
					methods.add(new SaveMethod(m));
			}
		}
		
		return methods.toArray(new SaveMethod[methods.size()]);
	}
	
	public SaveMethod[] findMethodsNotAnnotatedWith(Class<? extends Annotation> annotation) {
		return findMethodsNotAnnotatedWith(annotation, getClasses());
	}
	
	public SaveMethod[] findMethodsNotAnnotatedWith(Class<? extends Annotation> annotation, Class<?>... classes) {
		List<SaveMethod> methods = new LinkedList<>();
		
		for(Class<?> clazz : classes) {
			for(Method m : clazz.getDeclaredMethods()) {
				if(m.isAnnotationPresent(annotation))
					methods.add(new SaveMethod(m));
			}
		}
		
		return methods.toArray(new SaveMethod[methods.size()]);
	}
	
	public SaveMethod[] findMethodsWithParameters(Class<?>... parameters) {
		return findMethodsWithParameters(parameters, getClasses());
	}
	
	public SaveMethod[] findMethodsWithParameters(Class<?>[] parameters, Class<?>... classes) {
		List<SaveMethod> methods = new LinkedList<>();
		
		for(Class<?> clazz : classes) {
			for(Method m : clazz.getDeclaredMethods()) {
				if(Arrays.equals(m.getParameterTypes(), parameters))
					methods.add(new SaveMethod(m));
			}
		}
		
		return methods.toArray(new SaveMethod[methods.size()]);
	}
	
	public SaveMethod[] findMethodsReturning(Class<?> returned) {
		return findMethodsReturning(returned, getClasses());
	}
	
	public SaveMethod[] findMethodsReturning(Class<?> returned, Class<?>... classes) {
		List<SaveMethod> methods = new LinkedList<>();
		
		for(Class<?> clazz : classes) {
			for(Method m : clazz.getDeclaredMethods()) {
				if(m.getReturnType().equals(returned))
					methods.add(new SaveMethod(m));
			}
		}
		
		return methods.toArray(new SaveMethod[methods.size()]);
	}
	
	public SaveMethod[] findMethodsNamed(String name) {
		return findMethodsNamed(name, getClasses());
	}
	
	public SaveMethod[] findMethodsNamed(String name, Class<?>... classes) {
		List<SaveMethod> methods = new LinkedList<>();
		
		for(Class<?> clazz : classes) {
			for(Method m : clazz.getDeclaredMethods()) {
				if(m.getName().equals(name))
					methods.add(new SaveMethod(m));
			}
		}
		
		return methods.toArray(new SaveMethod[methods.size()]);
	}
	
	public SaveConstructor[] findConstructorAnnotatedWith(Class<? extends Annotation> annotation) {
		return findConstructorAnnotatedWith(annotation, getClasses());
	}
	
	public SaveConstructor[] findConstructorAnnotatedWith(Class<? extends Annotation> annotation, Class<?>... classes) {
		List<SaveConstructor> constructors = new LinkedList<>();
		
		for(Class<?> clazz : classes) {
			for(Constructor<?> con : clazz.getDeclaredConstructors()) {
				if(con.isAnnotationPresent(annotation))
					constructors.add(new SaveConstructor(con));
				
			}
		}
		
		return constructors.toArray(new SaveConstructor[constructors.size()]);
	}
	
	public SaveConstructor[] findConstructorNotAnnotatedWith(Class<? extends Annotation> annotation) {
		return findConstructorNotAnnotatedWith(annotation, getClasses());
	}
	
	public SaveConstructor[] findConstructorNotAnnotatedWith(Class<? extends Annotation> annotation, Class<?>... classes) {
		List<SaveConstructor> constructors = new LinkedList<>();
		
		for(Class<?> clazz : classes) {
			for(Constructor<?> con : clazz.getDeclaredConstructors()) {
				if(!con.isAnnotationPresent(annotation))
					constructors.add(new SaveConstructor(con));
				
			}
		}
		
		return constructors.toArray(new SaveConstructor[constructors.size()]);
	}
	
	public void addNBTTagCompound(NBTCompound compound, String name) {
		if(name == null) {
			remove(compound, null);
			return;
		}
		
		Object rootTag = compound.getCoumpound();
		if(rootTag == null)
			rootTag = getNewNBTTag();
		
		if(!validCompound(compound)) return;
		
		Object tag = convertToCompound(rootTag, compound);
		
		if(tag == null) return;
		
		try {
			getDeclaredMethod("set", tag.getClass(), String.class, getNmsClass("NBTBase")).invoke(tag, true, name, getNewNBTTag());
			compound.setCompound(rootTag);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void remove(NBTCompound compound, String name) {
		Object rootTag = compound.getCoumpound();
		if(rootTag == null)
			rootTag = getNewNBTTag();
		
		if(!validCompound(compound)) return;
		Object tag = convertToCompound(rootTag, compound);
		
		if(tag == null) return;
		
		getDeclaredMethod("remove", tag.getClass(), String.class).invoke(tag, true, name);
		compound.setCompound(rootTag);
	}
	
	public boolean validCompound(NBTCompound compound) {
		Object rootTag = compound.getCoumpound();
		if(rootTag == null)
			rootTag = getNewNBTTag();
		return (convertToCompound(rootTag, compound)) != null;
	}
	
	public Object convertToCompound(Object nbtTag, NBTCompound compound) {
		Stack<String> stack = new Stack<>();
		while(compound.getParent() != null) {
			stack.add(compound.getName());
			compound = compound.getParent();
		}
		
		while(!stack.isEmpty()) {
			nbtTag = getSubNBTTagCompound(nbtTag, stack.pop());
			if(nbtTag == null)
				return null;
		}
		return nbtTag;
	}
	
	public void setString(NBTCompound compound, String name, String text) {
		if(text == null) {
			remove(compound, name);
			return;
		}
		
		Object rootTag = compound.getCoumpound();
		if(rootTag == null)
			rootTag = getNewNBTTag();
		
		if(!validCompound(compound)) return;
		
		Object tag = convertToCompound(rootTag, compound);
		
		if(tag == null) return;
		
		getDeclaredMethod("setString", tag.getClass(), String.class, String.class).invoke(tag, true, name, text);
		compound.setCompound(rootTag);
	}
	
	public String getString(NBTCompound compound, String name) {
		Object rootTag = compound.getCoumpound();
		if(rootTag == null)
			rootTag = getNewNBTTag();
		
		if(!validCompound(compound)) return null;
		
		Object tag = convertToCompound(rootTag, compound);
		
		if(tag == null) return null;
		
		return (String) getDeclaredMethod("getString", tag.getClass(), String.class).invoke(tag, true, name);
	}
	
	public Object getContent(NBTCompound compound, String name) {
		Object rootTag = compound.getCoumpound();
		if(rootTag == null)
			rootTag = getNewNBTTag();
		
		if(!validCompound(compound)) return null;
		
		Object tag = convertToCompound(rootTag, compound);
		
		if(tag == null) return null;
		
		return getDeclaredMethod("get", tag.getClass(), String.class).invoke(tag, true, name);
	}
	
	public void setInt(NBTCompound compound, String name, Integer i) {
		if(i == null) {
			remove(compound, name);
			return;
		}
		
		Object rootTag = compound.getCoumpound();
		if(rootTag == null)
			rootTag = getNewNBTTag();
		
		if(!validCompound(compound)) return;
		
		Object tag = convertToCompound(rootTag, compound);
		
		if(tag == null) return;
		
		getDeclaredMethod("setInt", tag.getClass(), String.class, int.class).invoke(tag, true, name, i);
		compound.setCompound(rootTag);
	}
	
	public Integer getInt(NBTCompound compound, String name) {
		Object rootTag = compound.getCoumpound();
		if(rootTag == null)
			rootTag = getNewNBTTag();
		
		if(!validCompound(compound)) return null;
		
		Object tag = convertToCompound(rootTag, compound);
		
		if(tag == null) return null;
		
		return (Integer) getDeclaredMethod("getInt", tag.getClass(), String.class).invoke(tag, true, name);
	}
	
	public void setByteArray(NBTCompound compound, String name, byte[] b) {
		if(b == null) {
			remove(compound, name);
			return;
		}
		
		Object rootTag = compound.getCoumpound();
		if(rootTag == null)
			rootTag = getNewNBTTag();
		
		if(!validCompound(compound)) return;
		
		Object tag = convertToCompound(rootTag, compound);
		
		if(tag == null) return;
		
		getDeclaredMethod("setByteArray", tag.getClass(), String.class, byte[].class).invoke(tag, true, name, b);
		compound.setCompound(rootTag);
	}
	
	public byte[] getByteArray(NBTCompound compound, String name) {
		Object rootTag = compound.getCoumpound();
		if(rootTag == null)
			rootTag = getNewNBTTag();
		
		if(!validCompound(compound)) return null;
		
		Object tag = convertToCompound(rootTag, compound);
		
		if(tag == null) return null;
		
		return (byte[]) getDeclaredMethod("getByteArray", tag.getClass(), String.class).invoke(tag, true, name);
	}
	
	public void setIntArray(NBTCompound compound, String name, int[] i) {
		if(i == null) {
			remove(compound, name);
			return;
		}
		
		Object rootTag = compound.getCoumpound();
		if(rootTag == null)
			rootTag = getNewNBTTag();
		
		if(!validCompound(compound)) return;
		
		Object tag = convertToCompound(rootTag, compound);
		
		if(tag == null) return;
		
		getDeclaredMethod("setIntArray", tag.getClass(), String.class, int[].class).invoke(tag, true, name, i);
		compound.setCompound(rootTag);
	}
	
	public int[] getIntArray(NBTCompound compound, String name) {
		Object rootTag = compound.getCoumpound();
		if(rootTag == null)
			rootTag = getNewNBTTag();
		
		if(!validCompound(compound)) return null;
		
		Object tag = convertToCompound(rootTag, compound);
		
		if(tag == null) return null;
		
		return (int[]) getDeclaredMethod("getIntArray", tag.getClass(), String.class).invoke(tag, true, name);
	}
	
	public void setFloat(NBTCompound compound, String name, Float f) {
		if(f == null) {
			remove(compound, name);
			return;
		}
		
		Object rootTag = compound.getCoumpound();
		if(rootTag == null)
			rootTag = getNewNBTTag();
		
		if(!validCompound(compound)) return;
		
		Object tag = convertToCompound(rootTag, compound);
		
		if(tag == null) return;
		
		getDeclaredMethod("setFloat", tag.getClass(), String.class, float.class).invoke(tag, true, name, f);
		compound.setCompound(rootTag);
	}
	
	public Float getFloat(NBTCompound compound, String name) {
		Object rootTag = compound.getCoumpound();
		if(rootTag == null)
			rootTag = getNewNBTTag();
		
		if(!validCompound(compound)) return null;
		
		Object tag = convertToCompound(rootTag, compound);
		
		if(tag == null) return null;
		
		return (Float) getDeclaredMethod("getFloat", tag.getClass(), String.class).invoke(tag, true, name);
	}
	
	public void setLong(NBTCompound compound, String name, Long l) {
		if(l == null) {
			remove(compound, name);
			return;
		}
		
		Object rootTag = compound.getCoumpound();
		if(rootTag == null)
			rootTag = getNewNBTTag();
		
		if(!validCompound(compound)) return;
		
		Object tag = convertToCompound(rootTag, compound);
		
		if(tag == null) return;
		
		getDeclaredMethod("setLong", tag.getClass(), String.class, long.class).invoke(tag, true, name, l);
		compound.setCompound(rootTag);
	}
	
	public Long getLong(NBTCompound compound, String name) {
		Object rootTag = compound.getCoumpound();
		if(rootTag == null)
			rootTag = getNewNBTTag();
		
		if(!validCompound(compound)) return null;
		
		Object tag = convertToCompound(rootTag, compound);
		
		if(tag == null) return null;
		
		return (Long) getDeclaredMethod("getLong", tag.getClass(), String.class).invoke(tag, true, name);
	}
	
	public void setShort(NBTCompound compound, String name, Short s) {
		if(s == null) {
			remove(compound, name);
			return;
		}
		
		Object rootTag = compound.getCoumpound();
		if(rootTag == null)
			rootTag = getNewNBTTag();
		
		if(!validCompound(compound)) return;
		
		Object tag = convertToCompound(rootTag, compound);
		
		if(tag == null) return;
		
		getDeclaredMethod("setShort", tag.getClass(), String.class, short.class).invoke(tag, true, name, s);
		compound.setCompound(rootTag);
	}
	
	public Short getShort(NBTCompound compound, String name) {
		Object rootTag = compound.getCoumpound();
		if(rootTag == null)
			rootTag = getNewNBTTag();
		
		if(!validCompound(compound)) return null;
		
		Object tag = convertToCompound(rootTag, compound);
		
		if(tag == null) return null;
		
		return (Short) getDeclaredMethod("getShort", tag.getClass(), String.class).invoke(tag, true, name);
	}
	
	public void setByte(NBTCompound compound, String name, Byte b) {
		if(b == null) {
			remove(compound, name);
			return;
		}
		
		Object rootTag = compound.getCoumpound();
		if(rootTag == null)
			rootTag = getNewNBTTag();
		
		if(!validCompound(compound)) return;
		
		Object tag = convertToCompound(rootTag, compound);
		
		if(tag == null) return;
		
		getDeclaredMethod("setByte", tag.getClass(), String.class, byte.class).invoke(tag, true, name, b);
		compound.setCompound(rootTag);
	}
	
	public Byte getByte(NBTCompound compound, String name) {
		Object rootTag = compound.getCoumpound();
		if(rootTag == null)
			rootTag = getNewNBTTag();
		
		if(!validCompound(compound)) return null;
		
		Object tag = convertToCompound(rootTag, compound);
		
		if(tag == null) return null;
		
		return (Byte) getDeclaredMethod("getByte", tag.getClass(), String.class).invoke(tag, true, name);
	}
	
	public void setDouble(NBTCompound compound, String name, Double d) {
		if(d == null) {
			remove(compound, name);
			return;
		}
		
		Object rootTag = compound.getCoumpound();
		if(rootTag == null)
			rootTag = getNewNBTTag();
		
		if(!validCompound(compound)) return;
		
		Object tag = convertToCompound(rootTag, compound);
		
		if(tag == null) return;
		
		getDeclaredMethod("setDouble", tag.getClass(), String.class, double.class).invoke(tag, true, name, d);
		compound.setCompound(rootTag);
	}
	
	public Double getDouble(NBTCompound compound, String name) {
		Object rootTag = compound.getCoumpound();
		if(rootTag == null)
			rootTag = getNewNBTTag();
		
		if(!validCompound(compound)) return null;
		
		Object tag = convertToCompound(rootTag, compound);
		
		if(tag == null) return null;
		
		return (Double) getDeclaredMethod("getDouble", tag.getClass(), String.class).invoke(tag, true, name);
	}
	
	public void setBoolean(NBTCompound compound, String name, Boolean b) {
		if(b == null) {
			remove(compound, name);
			return;
		}
		
		Object rootTag = compound.getCoumpound();
		if(rootTag == null)
			rootTag = getNewNBTTag();
		
		if(!validCompound(compound)) return;
		
		Object tag = convertToCompound(rootTag, compound);
		
		if(tag == null) return;
		
		getDeclaredMethod("setBoolean", tag.getClass(), String.class, boolean.class).invoke(tag, true, name, b);
		compound.setCompound(rootTag);
	}
	
	public Boolean getBoolean(NBTCompound compound, String name) {
		Object rootTag = compound.getCoumpound();
		if(rootTag == null)
			rootTag = getNewNBTTag();
		
		if(!validCompound(compound)) return null;
		
		Object tag = convertToCompound(rootTag, compound);
		
		if(tag == null) return null;
		
		return (Boolean) getDeclaredMethod("getBoolean", tag.getClass(), String.class).invoke(tag, true, name);
	}
	
	public void setObject(NBTCompound compound, String name, Object o) {
		String json = JSONUtil.toJson(o);
		setString(compound, name, json);
	}
	
	public <T> T getObject(NBTCompound compound, String name, Class<T> type) {
		String json = getString(compound, name);
		
		if(json == null) return null;
		
		return JSONUtil.getValue(json, type);
	}
	
	public void set(NBTCompound compound, String name, Object val) {
		if(val == null) {
			remove(compound, name);
			return;
		}
		
		Object rootTag = compound.getCoumpound();
		if(rootTag == null)
			rootTag = getNewNBTTag();
		
		if(!validCompound(compound)) return;
		
		Object tag = convertToCompound(rootTag, compound);
		
		if(tag == null) return;
		
		getDeclaredMethod("set", tag.getClass(), String.class, getNmsClass("NBTBase")).invoke(tag, false, name, val);
		compound.setCompound(rootTag);
	}
	
	public byte getType(NBTCompound compound, String name) {
		Object rootTag = compound.getCoumpound();
		if(rootTag == null)
			rootTag = getNewNBTTag();
		
		if(!validCompound(compound)) return 0;
		
		Object tag = convertToCompound(rootTag, compound);
		
		if(tag == null) return 0;
		
		return (byte) getDeclaredMethod("d", tag.getClass(), String.class).invoke(tag, true, name);
	}
	
	public NBTList getList(NBTCompound compound, String name, NBTType type) {
		Object rootTag = compound.getCoumpound();
		if(rootTag == null)
			rootTag = getNewNBTTag();
		
		if(!validCompound(compound)) return null;
		
		Object tag = convertToCompound(rootTag, compound);
		
		if(tag == null) return null;
		
		return new NBTList(name, compound, type, getDeclaredMethod("getList", tag.getClass(), String.class, int.class).
				invoke(tag, true, name, type.getId()));
	}
	
	public Boolean hasKey(NBTCompound compound, String name) {
		Object rootTag = compound.getCoumpound();
		if(rootTag == null)
			rootTag = getNewNBTTag();
		
		if(!validCompound(compound)) return null;
		
		Object tag = convertToCompound(rootTag, compound);
		
		if(tag == null) return null;
		
		return (Boolean) getDeclaredMethod("hasKey", tag.getClass(), String.class).invoke(tag, true, name);
	}
	
	public Set<String> getKeys(NBTCompound compound) {
		Object rootTag = compound.getCoumpound();
		if(rootTag == null)
			rootTag = getNewNBTTag();
		
		if(!validCompound(compound)) return null;
		
		Object tag = convertToCompound(rootTag, compound);
		
		if(tag == null) return null;
		
		return (Set<String>) getDeclaredMethod("c", tag.getClass()).invoke(tag, true);
	}
	
	public void sendPacket(Player p, IPacket packet) {
		sendPacket(p, packet.getPacket(true));
	}
	
	public void sendPacket(Player p, IPacket packet, boolean stackTrace) {
		sendPacket(p, packet.getPacket(stackTrace));
	}
	
	public void sendPackets(IPacket... packets) {
		for(Player p : Bukkit.getOnlinePlayers())
			for(IPacket packet : packets)
				sendPacket(p, packet.getPacket(true));
	}
	
	public void sendPackets(boolean stackTrace, IPacket... packets) {
		for(Player p : Bukkit.getOnlinePlayers())
			for(IPacket packet : packets)
				sendPacket(p, packet.getPacket(stackTrace));
	}
	
	public void sendPacketsNotFor(String notFor, IPacket... packets) {
		for(Player p : Bukkit.getOnlinePlayers())
			if(!p.getName().equals(notFor))
				for(IPacket packet : packets)
					sendPacket(p, packet.getPacket(true));
	}
	
	public void sendPacketsNotFor(Player notFor, IPacket... packets) {
		sendPacketsNotFor(notFor.getName(), packets);
	}
	
	public void sendPacketsNotFor(String notFor, boolean stackTrace, IPacket... packets) {
		for(Player p : Bukkit.getOnlinePlayers())
			if(!p.getName().equals(notFor))
				for(IPacket packet : packets)
					sendPacket(p, packet.getPacket(stackTrace));
	}
	
	public void sendPacketsNotFor(Player notFor, boolean stackTrace, IPacket... packets) {
		sendPacketsNotFor(notFor.getName(), stackTrace, packets);
	}
	
	public Object toBlockPosition(BlockPos loc) {
		return getDeclaredConstructor("BlockPosition", int.class, int.class, int.class).newInstance(true, loc.getX(), loc.getY(), loc.getZ());
	}
	
	public BlockPos fromBlockPostition(Object nmsLoc) {
		Class<?> bP = getNmsClass("BaseBlockPosition");
		
		return new BlockPos() {
			@Override
			public int getX() {
				return (int) getDeclaredField("a", bP).get(nmsLoc);
			}
			
			@Override
			public int getY() {
				return (int) getDeclaredField("b", bP).get(nmsLoc);
			}
			
			@Override
			public int getZ() {
				return (int) getDeclaredField("c", bP).get(nmsLoc);
			}
		};
	}
}