/*
 *
 *  * Copyright (C) <2017>  <AlphaHelixDev>
 *  *
 *  *       This program is free software: you can redistribute it under the
 *  *       terms of the GNU General Public License as published by
 *  *       the Free Software Foundation, either version 3 of the License.
 *  *
 *  *       This program is distributed in the hope that it will be useful,
 *  *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  *       GNU General Public License for more details.
 *  *
 *  *       You should have received a copy of the GNU General Public License
 *  *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package de.alphahelix.alphalibary.reflection;

import de.alphahelix.alphalibary.nbt.NBTCompound;
import de.alphahelix.alphalibary.nbt.NBTList;
import de.alphahelix.alphalibary.nbt.NBTType;
import de.alphahelix.alphalibary.nms.BlockPos;
import de.alphahelix.alphalibary.nms.packets.IPacket;
import de.alphahelix.alphalibary.utils.JSONUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.*;
import java.util.Set;
import java.util.Stack;

public class ReflectionUtil {

    private static final String version;

    static {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        version = packageName.substring(packageName.lastIndexOf(".") + 1);
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
     * Gets a private accessible {@link Method} out of a {@link Class}
     *
     * @param name             the name of the {@link Method}
     * @param clazz            the {@link Class} where the {@link Method} is located at
     * @param parameterClasses the classes of the parameters for the method
     * @return a accessible {@link SaveMethod}
     */
    public static SaveMethod getDeclaredMethod(String name, Class<?> clazz, Class<?>... parameterClasses) {
        try {
            return new SaveMethod(clazz.getDeclaredMethod(name, parameterClasses));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static SaveMethod getDeclaredMethod(String name, String nmsClazz, Class<?>... parameterClasses) {
        try {
            return new SaveMethod(getNmsClass(nmsClazz).getDeclaredMethod(name, parameterClasses));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static SaveConstructor getDeclaredConstructor(Class<?> clazz, Class<?>... parameterClasses) {
        try {
            return new SaveConstructor(clazz.getDeclaredConstructor(parameterClasses));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static SaveConstructor getDeclaredConstructor(String nmsClazz, Class<?>... parameterClasses) {
        try {
            return new SaveConstructor(getNmsClass(nmsClazz).getDeclaredConstructor(parameterClasses));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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

    private static Class<?> getClass(String name, boolean asArray) {
        try {
            if (asArray) return Array.newInstance(Class.forName(name), 0).getClass();
            else return Class.forName(name);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @return the n.m.s.version {@link String}
     */
    public static String getNmsPrefix() {
        return "net.minecraft.server." + version + ".";
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
     * @return the o.b.c version {@link String}
     */
    public static String getCraftBukkitPrefix() {
        return "org.bukkit.craftbukkit." + version + ".";
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
     * Gets a NMS {@link Class} from the name of it
     *
     * @param name the name of the NMS {@link Class}
     * @return the NMS {@link Class}
     */
    public static Class<?> getNmsClass(String name) {
        return getClass(getNmsPrefix() + name, false);
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
     * Gets a OBC {@link Class} from the name of it
     *
     * @param name the name of the NMS {@link Class}
     * @return the NMS {@link Class}
     */
    public static Class<?> getCraftBukkitClass(String name) {
        return getClass(getCraftBukkitPrefix() + name, false);
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

    public static Object getNewNBTTag() {
        try {
            return getNmsClass("NBTTagCompound").newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object setNBTTag(Object nbtTag, Object nmsItem) {
        getDeclaredMethod("setTag", nmsItem.getClass(), nbtTag.getClass()).invoke(nmsItem, true, nbtTag);

        return nmsItem;
    }

    public static ItemStack getBukkitItemStack(Object nmsItem) {
        return (ItemStack) getDeclaredMethod("asCraftMirror", getCraftBukkitClass("inventory.CraftItemStack"), nmsItem.getClass()).invoke(null, true, nmsItem);
    }

    public static Object getItemRootNBTTagCompound(Object nmsItem) {
        return getDeclaredMethod("getTag", nmsItem.getClass()).invoke(nmsItem, true);
    }

    public static Object getEntityNBTTagCompound(Object nmsEntity) {
        try {
            Object nbt = getNmsClass("NBTTagCompound").newInstance();
            Object a = getDeclaredMethod("d", nmsEntity.getClass(), nbt.getClass()).invoke(nmsEntity, true, nbt);
            if (a == null)
                a = nbt;
            return a;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object setEntityNBTTag(Object nbtTag, Object nmsEntity) {
        getDeclaredMethod("a", getNmsClass("NBTTagCompound")).invoke(nmsEntity, true, nbtTag);
        return nmsEntity;
    }

    public static Object getTileEntityNBTTagCompound(BlockState tile) {
        try {
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
            Object nmsTile = getDeclaredMethod("getTileEntity", nmsWorld.getClass(), pos.getClass()).invoke(nmsWorld, true, pos);
            Object tag = getNmsClass("NBTTagCompound").newInstance();
            Object a = getDeclaredMethod("save", getNmsClass("TileEntity"), getNmsClass("NBTTagCompound")).invoke(nmsTile, true, tag);
            if (a == null) a = tag;
            return a;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setTileEntityNBTTagCompound(BlockState tile, Object nbtTag) {
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
        Object nmsTile = getDeclaredMethod("getTileEntity", nmsWorld.getClass(), pos.getClass()).invoke(nmsWorld, true, pos);

        getDeclaredMethod("a", getNmsClass("TileEntity"), getNmsClass("NBTTagCompound")).invoke(nmsTile, true, nbtTag);
    }

    public static Object getSubNBTTagCompound(Object compound, String name) {
        return getDeclaredMethod("getCompound", compound.getClass(), String.class).invoke(compound, true, name);
    }

    public static void addNBTTagCompound(NBTCompound compound, String name) {
        if (name == null) {
            remove(compound, name);
            return;
        }

        Object rootTag = compound.getCoumpound();
        if (rootTag == null)
            rootTag = getNewNBTTag();

        if (!validCompound(compound)) return;

        Object tag = convertToCompound(rootTag, compound);

        try {
            getDeclaredMethod("set", tag.getClass(), String.class, getNmsClass("NBTBase")).invoke(tag, true, name, getNewNBTTag());
            compound.setCompound(rootTag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void remove(NBTCompound compound, String name) {
        Object rootTag = compound.getCoumpound();
        if (rootTag == null)
            rootTag = getNewNBTTag();

        if (!validCompound(compound)) return;
        Object tag = convertToCompound(rootTag, compound);

        getDeclaredMethod("remove", tag.getClass(), String.class).invoke(tag, true, name);
        compound.setCompound(rootTag);
    }

    public static boolean validCompound(NBTCompound compound) {
        Object rootTag = compound.getCoumpound();
        if (rootTag == null)
            rootTag = getNewNBTTag();
        return (convertToCompound(rootTag, compound)) != null;
    }

    public static Object convertToCompound(Object nbtTag, NBTCompound compound) {
        Stack<String> stack = new Stack<>();
        while (compound.getParent() != null) {
            stack.add(compound.getName());
            compound = compound.getParent();
        }

        while (!stack.isEmpty()) {
            nbtTag = getSubNBTTagCompound(nbtTag, stack.pop());
            if (nbtTag == null)
                return null;
        }
        return nbtTag;
    }

    public static void setString(NBTCompound compound, String name, String text) {
        if (text == null) {
            remove(compound, name);
            return;
        }

        Object rootTag = compound.getCoumpound();
        if (rootTag == null)
            rootTag = getNewNBTTag();

        if (!validCompound(compound)) return;

        Object tag = convertToCompound(rootTag, compound);

        getDeclaredMethod("setString", tag.getClass(), String.class, String.class).invoke(tag, true, name, text);
        compound.setCompound(rootTag);
    }

    public static String getString(NBTCompound compound, String name) {
        Object rootTag = compound.getCoumpound();
        if (rootTag == null)
            rootTag = getNewNBTTag();

        if (!validCompound(compound)) return null;

        Object tag = convertToCompound(rootTag, compound);

        return (String) getDeclaredMethod("getString", tag.getClass(), String.class).invoke(tag, true, name);
    }

    public static Object getContent(NBTCompound compound, String name) {
        Object rootTag = compound.getCoumpound();
        if (rootTag == null)
            rootTag = getNewNBTTag();

        if (!validCompound(compound)) return null;

        Object tag = convertToCompound(rootTag, compound);

        return getDeclaredMethod("get", tag.getClass(), String.class).invoke(tag, true, name);
    }

    public static void setInt(NBTCompound compound, String name, Integer i) {
        if (i == null) {
            remove(compound, name);
            return;
        }

        Object rootTag = compound.getCoumpound();
        if (rootTag == null)
            rootTag = getNewNBTTag();

        if (!validCompound(compound)) return;

        Object tag = convertToCompound(rootTag, compound);

        getDeclaredMethod("setInt", tag.getClass(), String.class, int.class).invoke(tag, true, name, i);
        compound.setCompound(rootTag);
    }

    public static Integer getInt(NBTCompound compound, String name) {
        Object rootTag = compound.getCoumpound();
        if (rootTag == null)
            rootTag = getNewNBTTag();

        if (!validCompound(compound)) return null;

        Object tag = convertToCompound(rootTag, compound);

        return (Integer) getDeclaredMethod("getInt", tag.getClass(), String.class).invoke(tag, true, name);
    }

    public static void setByteArray(NBTCompound compound, String name, byte[] b) {
        if (b == null) {
            remove(compound, name);
            return;
        }

        Object rootTag = compound.getCoumpound();
        if (rootTag == null)
            rootTag = getNewNBTTag();

        if (!validCompound(compound)) return;

        Object tag = convertToCompound(rootTag, compound);

        getDeclaredMethod("setByteArray", tag.getClass(), String.class, byte[].class).invoke(tag, true, name, b);
        compound.setCompound(rootTag);
    }

    public static byte[] getByteArray(NBTCompound compound, String name) {
        Object rootTag = compound.getCoumpound();
        if (rootTag == null)
            rootTag = getNewNBTTag();

        if (!validCompound(compound)) return null;

        Object tag = convertToCompound(rootTag, compound);

        return (byte[]) getDeclaredMethod("getByteArray", tag.getClass(), String.class).invoke(tag, true, name);
    }

    public static void setIntArray(NBTCompound compound, String name, int[] i) {
        if (i == null) {
            remove(compound, name);
            return;
        }

        Object rootTag = compound.getCoumpound();
        if (rootTag == null)
            rootTag = getNewNBTTag();

        if (!validCompound(compound)) return;

        Object tag = convertToCompound(rootTag, compound);

        getDeclaredMethod("setIntArray", tag.getClass(), String.class, int[].class).invoke(tag, true, name, i);
        compound.setCompound(rootTag);
    }

    public static int[] getIntArray(NBTCompound compound, String name) {
        Object rootTag = compound.getCoumpound();
        if (rootTag == null)
            rootTag = getNewNBTTag();

        if (!validCompound(compound)) return null;

        Object tag = convertToCompound(rootTag, compound);

        return (int[]) getDeclaredMethod("getIntArray", tag.getClass(), String.class).invoke(tag, true, name);
    }

    public static void setFloat(NBTCompound compound, String name, Float f) {
        if (f == null) {
            remove(compound, name);
            return;
        }

        Object rootTag = compound.getCoumpound();
        if (rootTag == null)
            rootTag = getNewNBTTag();

        if (!validCompound(compound)) return;

        Object tag = convertToCompound(rootTag, compound);

        getDeclaredMethod("setFloat", tag.getClass(), String.class, float.class).invoke(tag, true, name, f);
        compound.setCompound(rootTag);
    }

    public static Float getFloat(NBTCompound compound, String name) {
        Object rootTag = compound.getCoumpound();
        if (rootTag == null)
            rootTag = getNewNBTTag();

        if (!validCompound(compound)) return null;

        Object tag = convertToCompound(rootTag, compound);

        return (Float) getDeclaredMethod("getFloat", tag.getClass(), String.class).invoke(tag, true, name);
    }

    public static void setLong(NBTCompound compound, String name, Long l) {
        if (l == null) {
            remove(compound, name);
            return;
        }

        Object rootTag = compound.getCoumpound();
        if (rootTag == null)
            rootTag = getNewNBTTag();

        if (!validCompound(compound)) return;

        Object tag = convertToCompound(rootTag, compound);

        getDeclaredMethod("setLong", tag.getClass(), String.class, long.class).invoke(tag, true, name, l);
        compound.setCompound(rootTag);
    }

    public static Long getLong(NBTCompound compound, String name) {
        Object rootTag = compound.getCoumpound();
        if (rootTag == null)
            rootTag = getNewNBTTag();

        if (!validCompound(compound)) return null;

        Object tag = convertToCompound(rootTag, compound);

        return (Long) getDeclaredMethod("getLong", tag.getClass(), String.class).invoke(tag, true, name);
    }

    public static void setShort(NBTCompound compound, String name, Short s) {
        if (s == null) {
            remove(compound, name);
            return;
        }

        Object rootTag = compound.getCoumpound();
        if (rootTag == null)
            rootTag = getNewNBTTag();

        if (!validCompound(compound)) return;

        Object tag = convertToCompound(rootTag, compound);

        getDeclaredMethod("setShort", tag.getClass(), String.class, short.class).invoke(tag, true, name, s);
        compound.setCompound(rootTag);
    }

    public static Short getShort(NBTCompound compound, String name) {
        Object rootTag = compound.getCoumpound();
        if (rootTag == null)
            rootTag = getNewNBTTag();

        if (!validCompound(compound)) return null;

        Object tag = convertToCompound(rootTag, compound);

        return (Short) getDeclaredMethod("getShort", tag.getClass(), String.class).invoke(tag, true, name);
    }

    public static void setByte(NBTCompound compound, String name, Byte b) {
        if (b == null) {
            remove(compound, name);
            return;
        }

        Object rootTag = compound.getCoumpound();
        if (rootTag == null)
            rootTag = getNewNBTTag();

        if (!validCompound(compound)) return;

        Object tag = convertToCompound(rootTag, compound);

        getDeclaredMethod("setByte", tag.getClass(), String.class, byte.class).invoke(tag, true, name, b);
        compound.setCompound(rootTag);
    }

    public static Byte getByte(NBTCompound compound, String name) {
        Object rootTag = compound.getCoumpound();
        if (rootTag == null)
            rootTag = getNewNBTTag();

        if (!validCompound(compound)) return null;

        Object tag = convertToCompound(rootTag, compound);

        return (Byte) getDeclaredMethod("getByte", tag.getClass(), String.class).invoke(tag, true, name);
    }

    public static void setDouble(NBTCompound compound, String name, Double d) {
        if (d == null) {
            remove(compound, name);
            return;
        }

        Object rootTag = compound.getCoumpound();
        if (rootTag == null)
            rootTag = getNewNBTTag();

        if (!validCompound(compound)) return;

        Object tag = convertToCompound(rootTag, compound);

        getDeclaredMethod("setDouble", tag.getClass(), String.class, double.class).invoke(tag, true, name, d);
        compound.setCompound(rootTag);
    }

    public static Double getDouble(NBTCompound compound, String name) {
        Object rootTag = compound.getCoumpound();
        if (rootTag == null)
            rootTag = getNewNBTTag();

        if (!validCompound(compound)) return null;

        Object tag = convertToCompound(rootTag, compound);

        return (Double) getDeclaredMethod("getDouble", tag.getClass(), String.class).invoke(tag, true, name);
    }

    public static void setBoolean(NBTCompound compound, String name, Boolean b) {
        if (b == null) {
            remove(compound, name);
            return;
        }

        Object rootTag = compound.getCoumpound();
        if (rootTag == null)
            rootTag = getNewNBTTag();

        if (!validCompound(compound)) return;

        Object tag = convertToCompound(rootTag, compound);

        getDeclaredMethod("setBoolean", tag.getClass(), String.class, boolean.class).invoke(tag, true, name, b);
        compound.setCompound(rootTag);
    }

    public static Boolean getBoolean(NBTCompound compound, String name) {
        Object rootTag = compound.getCoumpound();
        if (rootTag == null)
            rootTag = getNewNBTTag();

        if (!validCompound(compound)) return null;

        Object tag = convertToCompound(rootTag, compound);

        return (Boolean) getDeclaredMethod("getBoolean", tag.getClass(), String.class).invoke(tag, true, name);
    }

    public static void setObject(NBTCompound compound, String name, Object o) {
        String json = JSONUtil.toJson(o);
        setString(compound, name, json);
    }

    public static <T> T getObject(NBTCompound compound, String name, Class<T> type) {
        String json = getString(compound, name);

        if (json == null) return null;

        return JSONUtil.getValue(json, type);
    }

    public static void set(NBTCompound compound, String name, Object val) {
        if (val == null) {
            remove(compound, name);
            return;
        }

        Object rootTag = compound.getCoumpound();
        if (rootTag == null)
            rootTag = getNewNBTTag();

        if (!validCompound(compound)) return;

        Object tag = convertToCompound(rootTag, compound);

        getDeclaredMethod("set", tag.getClass(), String.class, getNmsClass("NBTBase")).invoke(tag, false, name, val);
        compound.setCompound(rootTag);
    }

    public static byte getType(NBTCompound compound, String name) {
        Object rootTag = compound.getCoumpound();
        if (rootTag == null)
            rootTag = getNewNBTTag();

        if (!validCompound(compound)) return 0;

        Object tag = convertToCompound(rootTag, compound);

        return (byte) getDeclaredMethod("d", tag.getClass(), String.class).invoke(tag, true, name);
    }

    public static NBTList getList(NBTCompound compound, String name, NBTType type) {
        Object rootTag = compound.getCoumpound();
        if (rootTag == null)
            rootTag = getNewNBTTag();

        if (!validCompound(compound)) return null;

        Object tag = convertToCompound(rootTag, compound);

        return new NBTList(name, compound, type, getDeclaredMethod("getList", tag.getClass(), String.class, int.class).
                invoke(tag, true, name, type.getId()));
    }

    public static Boolean hasKey(NBTCompound compound, String name) {
        Object rootTag = compound.getCoumpound();
        if (rootTag == null)
            rootTag = getNewNBTTag();

        if (!validCompound(compound)) return null;

        Object tag = convertToCompound(rootTag, compound);

        return (Boolean) getDeclaredMethod("hasKey", tag.getClass(), String.class).invoke(tag, true, name);
    }

    public static Set<String> getKeys(NBTCompound compound) {
        Object rootTag = compound.getCoumpound();
        if (rootTag == null)
            rootTag = getNewNBTTag();

        if (!validCompound(compound)) return null;

        Object tag = convertToCompound(rootTag, compound);

        return (Set<String>) getDeclaredMethod("c", tag.getClass()).invoke(tag, true);
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

    public static void sendPacket(Player p, IPacket packet) {
        try {
            Object nmsPlayer = getEntityPlayer(p);

            Object con = getDeclaredField("playerConnection", nmsPlayer.getClass()).get(nmsPlayer);

            getMethod("sendPacket", getNmsClass("PlayerConnection"), getNmsClass("Packet")).invoke(con, true, packet.getPacket(false));
        } catch (Exception e) {
            e.printStackTrace();
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

    public static SaveField getDeclaredField(String name, String nmsClazz) {
        try {
            Field f = getNmsClass(nmsClazz).getDeclaredField(name);
            return new SaveField(f);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
            if (!p.getName().equals(notFor)) for (Object packet : packets)
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
            if (!p.getName().equals(notFor.getName())) for (Object packet : packets)
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
    public static Object getCraftbukkitEntity(Entity entity) {
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
    public static Object getNMSItemStack(ItemStack item) {
        try {
            return getCraftBukkitClass("inventory.CraftItemStack").getMethod("asNMSCopy", ItemStack.class).invoke(null, item);
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

    private static String fromIChatBaseComponent(Object component) {

        try {
            Class<?> chatSerelizer = getCraftBukkitClass("util.CraftChatMessage");

            Method mSerelize = chatSerelizer.getMethod("fromComponent", ReflectionUtil.getNmsClass("IChatBaseComponent"));

            return (String) mSerelize.invoke(null, component);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

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

    public static Object toBlockPosition(BlockPos loc) {
        try {
            return getNmsClass("BlockPosition").getConstructor(int.class, int.class, int.class).newInstance(loc.getX(), loc.getY(), loc.getZ());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BlockPos fromBlockPostition(Object nmsLoc) {
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

    public static class SaveConstructor {

        private Constructor<?> c;

        public SaveConstructor(Constructor<?> c) {
            try {
                c.setAccessible(true);
                this.c = c;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public Object newInstance(Boolean stackTrace, Object... args) {
            try {
                return c.newInstance(args);
            } catch (Exception e) {
                if (stackTrace) e.printStackTrace();
            }
            return null;
        }

    }
}