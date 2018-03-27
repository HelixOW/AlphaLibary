package de.alphahelix.alphalibary.core.utils.abstracts;

import com.mojang.authlib.GameProfile;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;

public abstract class AbstractReflectionUtil {
	
	private static final String VERSION;
	public static AbstractReflectionUtil instance;
	
	static {
		String packageName = Bukkit.getServer().getClass().getPackage().getName();
		VERSION = packageName.substring(packageName.lastIndexOf(".") + 1);
	}
	
	public static String getVersion() {
		return VERSION;
	}
	
	/**
	 * Gets a accessible {@link Field} out of a {@link Class}
	 *
	 * @param name  the name of the {@link Field}
	 * @param clazz the {@link Class} where the {@link Field} is located at
	 *
	 * @return a accessible {@link Field}
	 */
	public abstract SaveField getField(String name, Class<?> clazz);
	
	public abstract SaveField getFirstType(Class<?> type, Class<?> clazz);
	
	public abstract SaveField getLastType(Class<?> type, Class<?> clazz);
	
	public abstract SaveMethod getDeclaredMethod(String name, String nmsClazz, Class<?>... parameterClasses);
	
	/**
	 * Gets a private accessible {@link Method} out of a {@link Class}
	 *
	 * @param name             the name of the {@link Method}
	 * @param clazz            the {@link Class} where the {@link Method} is located at
	 * @param parameterClasses the classes of the parameters for the method
	 *
	 * @return a accessible {@link SaveMethod}
	 */
	public abstract SaveMethod getDeclaredMethod(String name, Class<?> clazz, Class<?>... parameterClasses);
	
	public abstract SaveMethod getDeclaredMethod(MethodInfo methodInfo);
	
	public abstract Class<?> getClass(String name, boolean asArray);
	
	/**
	 * @return the net.minecraft.sserver version {@link String}
	 */
	public abstract String getNmsPrefix();
	
	public abstract Class<?> getClass(ClassInfo classInfo);
	
	/**
	 * Gets a NMS {@link Class}[] from the name of it
	 *
	 * @param name the name of the NMS {@link Class}[]
	 *
	 * @return the NMS {@link Class}[]
	 */
	public abstract Class<?> getNmsClassAsArray(String name);
	
	/**
	 * Gets a org.bukkit.craftbukkit {@link Class}[] from the name of it
	 *
	 * @param name the name of the obc {@link Class}[]
	 *
	 * @return the obc {@link Class}[]
	 */
	public abstract Class<?> getCraftBukkitClassAsArray(String name);
	
	/**
	 * @return the org.bukkit.craftbukkit version {@link String}
	 */
	public abstract String getCraftBukkitPrefix();
	
	/**
	 * Gets the EnumGamemode from of a {@link OfflinePlayer}
	 *
	 * @param p the {@link OfflinePlayer} to get its EnumGamemode from
	 *
	 * @return the EnumGamemode as an {@link Object}
	 */
	public abstract Object getEnumGamemode(OfflinePlayer p);
	
	/**
	 * Gets the EntityPlayer out of a {@link OfflinePlayer}
	 *
	 * @param p the {@link OfflinePlayer} to get its EntityPlayer from
	 *
	 * @return the EntityPlayer as an {@link Object}
	 */
	public abstract Object getEntityPlayer(OfflinePlayer p);
	
	public abstract int getEnumConstantID(Object enumConstant);
	
	/**
	 * Gets the ping from of a {@link OfflinePlayer}
	 *
	 * @param p the {@link OfflinePlayer} to get its ping from
	 *
	 * @return the ping as an {@link Object}
	 */
	public abstract int getPing(Player p);
	
	public abstract SaveField getDeclaredField(String name, String nmsClazz);
	
	public abstract Object setNBTTag(Object nbtTag, Object nmsItem);
	
	public abstract ItemStack getBukkitItemStack(Object nmsItem);
	
	public abstract Object getItemRootNBTTagCompound(Object nmsItem);
	
	public abstract Object getEntityNBTTagCompound(Object nmsEntity);
	
	public abstract Object getNewNBTTag();
	
	public abstract SaveConstructor getDeclaredConstructor(String nmsClazz, Class<?>... parameterClasses);
	
	public abstract SaveConstructor getDeclaredConstructor(Class<?> clazz, Class<?>... parameterClasses);
	
	public abstract SaveConstructor getDeclaredConstructor(ConstructorInfo constructorInfo);
	
	public abstract Object setEntityNBTTag(Object nbtTag, Object nmsEntity);
	
	public abstract Object getTileEntityNBTTagCompound(BlockState tile);
	
	public abstract void setTileEntityNBTTagCompound(BlockState tile, Object nbtTag);
	
	public abstract Object getSubNBTTagCompound(Object compound, String name);
	
	/**
	 * Sends a Packet[] to a {@link Player}
	 *
	 * @param p       the {@link Player} to receive the Packet
	 * @param packets the Packet[] to send
	 */
	public abstract void sendPackets(Player p, Object... packets);
	
	/**
	 * Sends a Packet to a {@link Player}
	 *
	 * @param p      the {@link Player} to receive the Packet
	 * @param packet the Packet to send
	 */
	public abstract void sendPacket(Player p, Object packet);
	
	/**
	 * Sends a Packet[] to all {@link Player}s
	 *
	 * @param packets the Packet[] to send
	 */
	public abstract void sendPackets(Object... packets);
	
	/**
	 * Sends a Packet[] to all {@link Player}s but one
	 *
	 * @param notFor  the {@link Player} to not send the Packet[] to
	 * @param packets the Packet[] to send
	 */
	public abstract void sendPacketsNotFor(Player notFor, Object... packets);
	
	/**
	 * Sends a Packet[] to all {@link Player}s but one
	 *
	 * @param notFor  the {@link Player} to not send the Packet[] to
	 * @param packets the Packet[] to send
	 */
	public abstract void sendPacketsNotFor(String notFor, Object... packets);
	
	/**
	 * Gets the entity ID out of a {@link Entity} from its CraftEntity
	 *
	 * @param entity the {@link Entity} to get its enitity ID from
	 *
	 * @return the entity ID
	 */
	public abstract int getEntityID(Entity entity);
	
	/**
	 * Gets the entity ID out of a {@link Entity} from its NMS Entity
	 *
	 * @param entity the {@link Entity} to get its enitity ID from
	 *
	 * @return the entity ID
	 */
	public abstract int getEntityID(Object entity);
	
	/**
	 * Gets the NMS Entity out of a {@link Entity}
	 *
	 * @param entity the {@link Entity} to get its NMS Entity from
	 *
	 * @return the NMS Entity
	 */
	public abstract Object getCraftbukkitEntity(Entity entity);
	
	public abstract SaveMethod getMethod(MethodInfo methodInfo);
	
	/**
	 * Gets the WorldServer out of its Bukkit {@link World}
	 *
	 * @param world the {@link World} to get its WorldServer from
	 *
	 * @return the WorldServer
	 */
	public abstract Object getWorldServer(World world);
	
	/**
	 * Gets the MinecraftServer out of its Bukkit {@link Server}
	 *
	 * @return the MinecraftServer
	 */
	public abstract Object getMinecraftServer();
	
	/**
	 * Gets the ItemStack out of its Bukkit {@link ItemStack}
	 *
	 * @param item the {@link ItemStack} to get its ItemStack from
	 *
	 * @return the ItemStack
	 */
	public abstract Object getNMSItemStack(ItemStack item);
	
	/**
	 * Gets the {@link String} out of its IChatBaseComponent
	 *
	 * @param baseComponentArray the IChatBaseComponent to get the {@link String} from
	 *
	 * @return the {@link String}
	 */
	public abstract String[] fromIChatBaseComponent(Object... baseComponentArray);
	
	public String fromIChatBaseComponent(Object component) {
		return (String) getMethod("fromComponent", getCraftBukkitClass("util.CraftChatMessage"), getNmsClass("IChatBaseComponent")).invoke(null, true, component);
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
	public abstract SaveMethod getMethod(String name, Class<?> clazz, Class<?>... parameterClasses);
	
	/**
	 * Gets a OBC {@link Class} from the name of it
	 *
	 * @param name the name of the NMS {@link Class}
	 *
	 * @return the NMS {@link Class}
	 */
	public abstract Class<?> getCraftBukkitClass(String name);
	
	/**
	 * Gets a NMS {@link Class} from the name of it
	 *
	 * @param name the name of the NMS {@link Class}
	 *
	 * @return the NMS {@link Class}
	 */
	public abstract Class<?> getNmsClass(String name);
	
	/**
	 * Gets the IChatBaseComponent out of a {@link String}
	 *
	 * @param strings the {@link String} to get the IChatBaseComponent from
	 *
	 * @return the IChatBaseComponent
	 *
	 * @see AbstractReflectionUtil#toIChatBaseComponent(String...)
	 * @deprecated
	 */
	@Deprecated
	public abstract Object[] serializeString(String... strings);
	
	public abstract Object[] toIChatBaseComponent(String... strings);
	
	/**
	 * Gets the CraftChatMessage out of a {@link String}
	 *
	 * @param s the {@link String} to get its CraftChatMessage from
	 *
	 * @return the CraftChatMessage as an {@link Object}
	 *
	 * @see AbstractReflectionUtil#toIChatBaseComponentArray(String) (String)
	 * @deprecated
	 */
	@Deprecated
	public abstract Object serializeString(String s);
	
	public abstract Object toIChatBaseComponentArray(String s);
	
	/**
	 * Gets a private accessible {@link Field} out of a {@link Class}
	 *
	 * @param name  the name of the {@link Field}
	 * @param clazz the {@link Class} where the {@link Field} is located at
	 *
	 * @return a accessible {@link Field}
	 */
	public abstract SaveField getDeclaredField(String name, Class<?> clazz);
	
	public abstract GameProfile getGameProfile(Player p);
	
	public abstract Class<?>[] getClasses(File jarFile, String pckg);
	
	public abstract Class<?>[] getClasses(JarInfo info);
	
	public abstract Class<?>[] findClassesImplementing(Class<?> implementedClazz);
	
	public abstract Class<?>[] getClasses();
	
	public abstract Class<?>[] getClasses(File jarFile);
	
	public abstract Class<?>[] findClassesAnnotatedWith(Class<? extends Annotation> annotation);
	
	public abstract Class<?>[] findClassesNotAnnotatedWith(Class<? extends Annotation> annotation);
	
	public abstract SaveField[] findFieldsAnnotatedWith(Class<? extends Annotation> annotation);
	
	public abstract SaveField[] findFieldsAnnotatedWith(Class<? extends Annotation> annotation, Class<?>... classes);
	
	public abstract SaveField[] findFieldsNotAnnotatedWith(Class<? extends Annotation> annotation);
	
	public abstract SaveField[] findFieldsNotAnnotatedWith(Class<? extends Annotation> annotation, Class<?>... classes);
	
	public abstract SaveField[] findFieldsOfType(Class<?> type);
	
	public abstract SaveField[] findFieldsOfType(Class<?> type, Class<?>... classes);
	
	public abstract SaveField findFieldAtIndex(int index, Class<?>... classes);
	
	public abstract SaveMethod[] findMethodsAnnotatedWith(Class<? extends Annotation> annotation);
	
	public abstract SaveMethod[] findMethodsAnnotatedWith(Class<? extends Annotation> annotation, Class<?>... classes);
	
	public abstract SaveMethod[] findMethodsNotAnnotatedWith(Class<? extends Annotation> annotation);
	
	public abstract SaveMethod[] findMethodsNotAnnotatedWith(Class<? extends Annotation> annotation, Class<?>... classes);
	
	public abstract SaveMethod[] findMethodsWithParameters(Class<?>... parameters);
	
	public abstract SaveMethod[] findMethodsWithParameters(Class<?>[] parameters, Class<?>... classes);
	
	public abstract SaveMethod[] findMethodsReturning(Class<?> returned);
	
	public abstract SaveMethod[] findMethodsReturning(Class<?> returned, Class<?>... classes);
	
	public abstract SaveMethod[] findMethodsNamed(String name);
	
	public abstract SaveMethod[] findMethodsNamed(String name, Class<?>... classes);
	
	public abstract SaveConstructor[] findConstructorAnnotatedWith(Class<? extends Annotation> annotation);
	
	public abstract SaveConstructor[] findConstructorAnnotatedWith(Class<? extends Annotation> annotation, Class<?>... classes);
	
	public abstract SaveConstructor[] findConstructorNotAnnotatedWith(Class<? extends Annotation> annotation);
	
	public abstract SaveConstructor[] findConstructorNotAnnotatedWith(Class<? extends Annotation> annotation, Class<?>... classes);
	
	public static class SaveField {
		
		private Field f;
		private int index;
		
		public SaveField(Field f) {
			this(f, -1);
		}
		
		public SaveField(Field f, int index) {
			try {
				f.setAccessible(true);
				this.f = f;
			} catch(Exception e) {
				e.printStackTrace();
			}
			this.index = index;
		}
		
		public SaveField() {
		}
		
		public SaveField removeFinal() {
			try {
				if(Modifier.isFinal(field().getModifiers()))
					field().setInt(field(), field().getModifiers() & ~Modifier.FINAL);
			} catch(IllegalAccessException e) {
				e.printStackTrace();
			}
			return this;
		}
		
		public Field field() {
			return f;
		}
		
		public Object get(Object instance) {
			if(f == null) return new Object();
			try {
				return f.get(instance);
			} catch(Exception e) {
				e.printStackTrace();
			}
			return new Object();
		}
		
		public Object get(Object instance, boolean stackTrace) {
			if(f == null) return new Object();
			try {
				return f.get(instance);
			} catch(Exception e) {
				if(stackTrace) e.printStackTrace();
			}
			return new Object();
		}
		
		public void set(Object instance, Object value, boolean stackTrace) {
			if(f == null) return;
			try {
				f.set(instance, value);
			} catch(Exception e) {
				if(stackTrace) e.printStackTrace();
			}
		}
		
		public int index() {
			return index;
		}
	}
	
	public static class SaveMethod {
		
		private Method m;
		
		public SaveMethod(Method m) {
			try {
				m.setAccessible(true);
				this.m = m;
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		public SaveMethod() {
		}
		
		public Object invoke(Object instance, Boolean stackTrace, Object... args) {
			if(m == null) return new Object();
			try {
				return m.invoke(instance, args);
			} catch(Exception e) {
				if(stackTrace) e.printStackTrace();
			}
			return new Object();
		}
		
		public Method method() {
			return m;
		}
	}
	
	public static class SaveConstructor<T> {
		
		private T type;
		private Constructor<T> c;
		
		public SaveConstructor(Constructor<T> c) {
			try {
				c.setAccessible(true);
				this.c = c;
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		public SaveConstructor(Class<T> c) {
			try {
				Constructor<T> con = c.getDeclaredConstructor();
				con.setAccessible(true);
				this.c = con;
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		public SaveConstructor() {
		}
		
		public T newInstance(Boolean stackTrace, Object... args) {
			if(c == null) return null;
			try {
				return c.newInstance(args);
			} catch(Exception e) {
				if(stackTrace) e.printStackTrace();
			}
			return null;
		}
		
		public Constructor<T> constructor() {
			return c;
		}
	}
	
	public static class FieldInfo {
		
		private final String name;
		private final Class<?> type;
		
		public FieldInfo(String name, Class<?> type) {
			this.name = name;
			this.type = type;
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(getName(), getType());
		}
		
		public String getName() {
			return name;
		}
		
		public Class<?> getType() {
			return type;
		}
		
		@Override
		public boolean equals(Object o) {
			if(this == o) return true;
			if(o == null || getClass() != o.getClass()) return false;
			FieldInfo fieldInfo = (FieldInfo) o;
			return Objects.equals(getName(), fieldInfo.getName()) &&
					Objects.equals(getType(), fieldInfo.getType());
		}
		
		
	}
	
	public static class MethodInfo {
		
		private final String name;
		private final Class<?> type;
		private final Class<?>[] parameters;
		
		public MethodInfo(String name, Class<?> type, Class<?>[] parameters) {
			this.name = name;
			this.type = type;
			this.parameters = parameters;
		}
		
		public String getName() {
			return name;
		}
		
		public Class<?> getType() {
			return type;
		}
		
		public Class<?>[] getParameters() {
			return parameters;
		}
		
		@Override
		public boolean equals(Object o) {
			if(this == o) return true;
			if(o == null || getClass() != o.getClass()) return false;
			MethodInfo that = (MethodInfo) o;
			return Objects.equals(getName(), that.getName()) &&
					Objects.equals(getType(), that.getType()) &&
					Arrays.equals(getParameters(), that.getParameters());
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(getName(), getType(), getParameters());
		}
	}
	
	public static class ConstructorInfo {
		
		private final Class<?> type;
		private final Class<?>[] parameters;
		
		public ConstructorInfo(Class<?> type, Class<?>[] parameters) {
			this.type = type;
			this.parameters = parameters;
		}
		
		public Class<?> getType() {
			return type;
		}
		
		public Class<?>[] getParameters() {
			return parameters;
		}
		
		@Override
		public boolean equals(Object o) {
			if(this == o) return true;
			if(o == null || getClass() != o.getClass()) return false;
			ConstructorInfo that = (ConstructorInfo) o;
			return Objects.equals(getType(), that.getType()) &&
					Arrays.equals(getParameters(), that.getParameters());
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(getType(), getParameters());
		}
	}
	
	public static class ClassInfo {
		
		private final String name;
		private final boolean asArray;
		
		public ClassInfo(String name, boolean asArray) {
			this.name = name;
			this.asArray = asArray;
		}
		
		public String getName() {
			return name;
		}
		
		public boolean isAsArray() {
			return asArray;
		}
		
		@Override
		public boolean equals(Object o) {
			if(this == o) return true;
			if(o == null || getClass() != o.getClass()) return false;
			ClassInfo classInfo = (ClassInfo) o;
			return isAsArray() == classInfo.isAsArray() &&
					Objects.equals(getName(), classInfo.getName());
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(getName(), isAsArray());
		}
	}
	
	public static class JarInfo {
		
		private final File jarFile;
		private final String pckg;
		private final boolean needsPckg;
		
		public JarInfo(File jarFile, String pckg) {
			this.jarFile = jarFile;
			this.pckg = pckg;
			this.needsPckg = pckg.trim().isEmpty();
		}
		
		public File getJarFile() {
			return jarFile;
		}
		
		public String getPckg() {
			return pckg;
		}
		
		public boolean isNeedsPckg() {
			return needsPckg;
		}
		
		@Override
		public boolean equals(Object o) {
			if(this == o) return true;
			if(o == null || getClass() != o.getClass()) return false;
			JarInfo jarInfo = (JarInfo) o;
			return isNeedsPckg() == jarInfo.isNeedsPckg() &&
					Objects.equals(getJarFile(), jarInfo.getJarFile()) &&
					Objects.equals(getPckg(), jarInfo.getPckg());
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(getJarFile(), getPckg(), isNeedsPckg());
		}
	}
}
