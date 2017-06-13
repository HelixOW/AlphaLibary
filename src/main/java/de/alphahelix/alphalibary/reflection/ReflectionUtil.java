/*
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
 */

package de.alphahelix.alphalibary.reflection;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtil {

    private static final String version;

    static {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        version = packageName.substring(packageName.lastIndexOf("") + 1);
    }

    public static String getVersion() {
        return version;
    }

    /**
     * Gets a accessible {@link Field} out of a {@link Class}
     *
     * @param name  the name of the {@link Field}
     * @param clazz the {@link Class} where the {@link Field} is located at
     * @return a accessible {@link Field}
     */
    public static SaveField getField(String name, Class<?> clazz) {
        try {
            Field f = clazz.getField(name);
            return new SaveField(f);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets a private accessible {@link Field} out of a {@link Class}
     *
     * @param name  the name of the {@link Field}
     * @param clazz the {@link Class} where the {@link Field} is located at
     * @return a accessible {@link Field}
     */
    public static SaveField getDeclaredField(String name, Class<?> clazz) {
        try {
            Field f = clazz.getDeclaredField(name);
            return new SaveField(f);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static SaveField getFirstType(Class<?> type, Class<?> clazz) {
        try {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.getType().equals(type)) {
                    return new SaveField(field);
                }
            }

            throw new NoSuchFieldException("Could not resolve field of type '" + type.toString() + "' in class " + clazz);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static SaveField getLastType(Class<?> type, Class<?> clazz) {
        Field field = null;
        for (Field field1 : clazz.getDeclaredFields()) {
            if (field1.getType().equals(type)) {
                field = field1;
            }
        }

        if (field == null) {
            try {
                throw new NoSuchFieldException("Could not resolve field of type '" + type.toString() + "' in class " + clazz);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                return null;
            }
        }
        return new SaveField(field);
    }

    /**
     * Gets a accessible {@link Method} out of a {@link Class}
     *
     * @param name             the name of the {@link Method}
     * @param clazz            the {@link Class} where the {@link Method} is located at
     * @param parameterClasses the classes of the parameters for the method
     * @return a accessible {@link SaveMethod}
     */
    public static SaveMethod getMethod(String name, Class<?> clazz, Class<?>... parameterClasses) {
        try {
            Method m = clazz.getMethod(name, parameterClasses);
            return new SaveMethod(m);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets a private accessible {@link Method} out of a {@link Class}
     *
     * @param name             the name of the {@link Method}
     * @param clazz            the {@link Class} where the {@link Method} is located at
     * @param parameterClasses the classes of the parameters for the method
     * @return a accessible {@link SaveMethod}
     */
    public static SaveMethod getDeclaredMethod(String name, Class<?> clazz, Class<?>... parameterClasses) {
        try {
            Method m = clazz.getDeclaredMethod(name, parameterClasses);
            m.setAccessible(true);
            return new SaveMethod(m);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Class<?> getClass(String name, boolean asArray) {
        try {
            if (asArray)
                return Array.newInstance(Class.forName(name), 0).getClass();
            else
                return Class.forName(name);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @return the n.m.s.version {@link String}
     */
    public static String getNmsPrefix() {
        return "net.minecraft.server." + version + "";
    }

    /**
     * @return the o.b.c version {@link String}
     */
    public static String getCraftBukkitPrefix() {
        return "org.bukkit.craftbukkit." + version + "";
    }

    /**
     * Gets a NMS {@link Class} from the name of it
     *
     * @param name the name of the NMS {@link Class}
     * @return the NMS {@link Class}
     */
    public static Class<?> getNmsClass(String name) {
        return getClass(getNmsPrefix() + name, false);
    }

    /**
     * Gets a NMS {@link Class}[] from the name of it
     *
     * @param name the name of the NMS {@link Class}[]
     * @return the NMS {@link Class}[]
     */
    public static Class<?> getNmsClassAsArray(String name) {
        return getClass(getNmsPrefix() + name, true);
    }

    /**
     * Gets a OBC {@link Class} from the name of it
     *
     * @param name the name of the NMS {@link Class}
     * @return the NMS {@link Class}
     */
    public static Class<?> getCraftBukkitClass(String name) {
        return getClass(getCraftBukkitPrefix() + name, false);
    }

    /**
     * Gets a OBC {@link Class}[] from the name of it
     *
     * @param name the name of the NMS {@link Class}[]
     * @return the NMS {@link Class}[]
     */
    public static Class<?> getCraftBukkitClassAsArray(String name) {
        return getClass(getCraftBukkitPrefix() + name, true);
    }

    /**
     * Gets the EntityPlayer out of a {@link OfflinePlayer}
     *
     * @param p the {@link OfflinePlayer} to get its EntityPlayer from
     * @return the EntityPlayer as an {@link Object}
     */
    public static Object getEntityPlayer(OfflinePlayer p) {
        try {
            return getCraftBukkitClass("entity.CraftPlayer").getMethod("getHandle").invoke(p);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets the CraftChatMessage out of a {@link String}
     *
     * @param s the {@link String} to get its CraftChatMessage from
     * @return the CraftChatMessage as an {@link Object}
     */
    public static Object serializeString(String s) {
        try {
            Class<?> chatSerelizer = getCraftBukkitClass("util.CraftChatMessage");

            Method mSerelize = chatSerelizer.getMethod("fromString", String.class);

            return ((Object[]) mSerelize.invoke(null, s))[0];
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String fromIChatBaseComponent(Object component) {

        try {
            Class<?> chatSerelizer = getCraftBukkitClass("util.CraftChatMessage");

            Method mSerelize = chatSerelizer.getMethod("fromComponent",
                    ReflectionUtil.getNmsClass("IChatBaseComponent"));

            return (String) mSerelize.invoke(null, component);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Gets the EnumGamemode from of a {@link OfflinePlayer}
     *
     * @param p the {@link OfflinePlayer} to get its EnumGamemode from
     * @return the EnumGamemode as an {@link Object}
     */
    public static Object getEnumGamemode(OfflinePlayer p) {
        try {

            Field fInteractManager = ReflectionUtil.getNmsClass("EntityPlayer").getField("playerInteractManager");
            fInteractManager.setAccessible(true);
            Object oInteractManager = fInteractManager.get(getEntityPlayer(p));

            Field enumGamemode = ReflectionUtil.getNmsClass("PlayerInteractManager").getDeclaredField("gamemode");
            enumGamemode.setAccessible(true);

            return enumGamemode.get(oInteractManager);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets the ping from of a {@link OfflinePlayer}
     *
     * @param p the {@link OfflinePlayer} to get its ping from
     * @return the ping as an {@link Object}
     */
    public static int getPing(Player p) {
        try {
            Field ping = getNmsClass("EntityPlayer").getDeclaredField("ping");
            ping.setAccessible(true);
            return (int) ping.get(getEntityPlayer(p));
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Sends a Packet to a {@link Player}
     *
     * @param p      the {@link Player} to receive the Packet
     * @param packet the Packet to send
     */
    public static void sendPacket(Player p, Object packet) {
        try {
            Object nmsPlayer = getEntityPlayer(p);

            Object con = getDeclaredField("playerConnection", nmsPlayer.getClass()).get(nmsPlayer);

            getMethod("sendPacket", getNmsClass("PlayerConnection"), getNmsClass("Packet")).invoke(con, true, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a Packet[] to a {@link Player}
     *
     * @param p       the {@link Player} to receive the Packet
     * @param packets the Packet[] to send
     */
    public static void sendPackets(Player p, Object... packets) {
        for (Object packet : packets)
            ReflectionUtil.sendPacket(p, packet);
    }

    /**
     * Sends a Packet[] to all {@link Player}s
     *
     * @param packets the Packet[] to send
     */
    public static void sendPackets(Object... packets) {
        for (Player p : Bukkit.getOnlinePlayers())
            for (Object packet : packets)
                ReflectionUtil.sendPacket(p, packet);
    }

    /**
     * Sends a Packet[] to all {@link Player}s but one
     *
     * @param notFor  the {@link Player} to not send the Packet[] to
     * @param packets the Packet[] to send
     */
    public static void sendPacketsNotFor(String notFor, Object... packets) {
        for (Player p : Bukkit.getOnlinePlayers())
            if (!p.getName().equals(notFor))
                for (Object packet : packets)
                    ReflectionUtil.sendPacket(p, packet);
    }

    /**
     * Sends a Packet[] to all {@link Player}s but one
     *
     * @param notFor  the {@link Player} to not send the Packet[] to
     * @param packets the Packet[] to send
     */
    public static void sendPacketsNotFor(Player notFor, Object... packets) {
        for (Player p : Bukkit.getOnlinePlayers())
            if (!p.getName().equals(notFor.getName()))
                for (Object packet : packets)
                    ReflectionUtil.sendPacket(p, packet);
    }

    /**
     * Gets the entity ID out of a {@link Entity} from its CraftEntity
     *
     * @param entity the {@link Entity} to get its enitity ID from
     * @return the entity ID
     */
    public static int getEntityID(Entity entity) {
        try {
            Object entityEntity = getCraftBukkitClass("entity.CraftEntity").getMethod("getHandle").invoke(entity);

            return (int) getNmsClass("Entity").getMethod("getId").invoke(entityEntity);

        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    /**
     * Gets the entity ID out of a {@link Entity} from its NMS Entity
     *
     * @param entity the {@link Entity} to get its enitity ID from
     * @return the entity ID
     */
    public static int getEntityID(Object entity) {
        try {
            Field id = ReflectionUtil.getNmsClass("Entity").getDeclaredField("id");
            id.setAccessible(true);
            return id.getInt(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Gets the NMS Entity out of a {@link Entity}
     *
     * @param entity the {@link Entity} to get its NMS Entity from
     * @return the NMS Entity
     */
    public static Object getEntity(Entity entity) {
        try {
            return getCraftBukkitClass("entity.CraftEntity").getMethod("getHandle").invoke(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets the WorldServer out of its Bukkit {@link World}
     *
     * @param world the {@link World} to get its WorldServer from
     * @return the WorldServer
     */
    public static Object getWorldServer(World world) {
        try {
            return getCraftBukkitClass("CraftWorld").getMethod("getHandle").invoke(world);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets the MinecraftServer out of its Bukkit {@link Server}
     *
     * @param server the {@link Server} to get its MinecraftServer from
     * @return the MinecraftServer
     */
    public static Object getMinecraftServer(Server server) {
        try {
            return getCraftBukkitClass("CraftServer").getMethod("getServer").invoke(Bukkit.getServer());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets the ItemStack out of its Bukkit {@link ItemStack}
     *
     * @param item the {@link ItemStack} to get its ItemStack from
     * @return the ItemStack
     */
    public static Object getObjectNMSItemStack(ItemStack item) {
        try {
            return getCraftBukkitClass("inventory.CraftItemStack").getMethod("asNMSCopy", ItemStack.class).invoke(null,
                    item);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets the {@link String} out of its IChatBaseComponent
     *
     * @param baseComponentArray the IChatBaseComponent to get the {@link String} from
     * @return the {@link String}
     */
    public static String[] fromIChatBaseComponent(Object... baseComponentArray) {

        String[] array = new String[baseComponentArray.length];

        for (int i = 0; i < array.length; i++) {
            array[i] = fromIChatBaseComponent(baseComponentArray[i]);
        }

        return array;
    }

    /**
     * Gets the IChatBaseComponent out of a {@link String}
     *
     * @param strings the {@link String} to get the IChatBaseComponent from
     * @return the IChatBaseComponent
     */
    public static Object[] serializeString(String... strings) {

        Object[] array = (Object[]) Array.newInstance(getNmsClass("IChatBaseComponent"), strings.length);

        for (int i = 0; i < array.length; i++) {
            array[i] = serializeString(strings[i]);
        }

        return array;
    }

    public static class SaveField {

        private Field f;

        public SaveField(Field f) {
            try {
                f.setAccessible(true);
                this.f = f;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public Object get(Object instance) {
            try {
                return f.get(instance);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public Object get(Object instance, boolean stackTrace) {
            try {
                return f.get(instance);
            } catch (Exception e) {
                if (stackTrace) e.printStackTrace();
            }
            return null;
        }

        public void set(Object instance, Object value, boolean stackTrace) {
            try {
                f.set(instance, value);
            } catch (Exception e) {
                if (stackTrace) e.printStackTrace();
            }
        }
    }

    public static class SaveMethod {

        private Method m;

        public SaveMethod(Method m) {
            try {
                m.setAccessible(true);
                this.m = m;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public Object invoke(Object instance, Boolean stackTrace, Object... args) {
            try {
                return m.invoke(instance, args);
            } catch (Exception e) {
                if (stackTrace) e.printStackTrace();
            }
            return null;
        }

    }
}