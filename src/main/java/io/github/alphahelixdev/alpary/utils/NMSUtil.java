package io.github.alphahelixdev.alpary.utils;

import com.mojang.authlib.GameProfile;
import io.github.alphahelixdev.alpary.reflection.nms.BlockPos;
import io.github.alphahelixdev.alpary.reflection.nms.packets.IPacket;
import io.github.alphahelixdev.helius.reflection.Reflections;
import io.github.alphahelixdev.helius.reflection.SaveField;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Array;

public class NMSUtil {

    private static final String VERSION;
    private static final Reflections REFLECTIONS = new Reflections();

    static {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        VERSION = packageName.substring(packageName.lastIndexOf(".") + 1);
    }

    public static String getNMSPrefix() {
        return "net.minecraft.server." + getVersion() + ".";
    }

    public static String getCraftBukkitPrefix() {
        return "org.bukkit.craftbukkit." + getVersion() + ".";
    }

    public static Reflections getReflections() {
        return REFLECTIONS;
    }

    public static String getVersion() {
        return VERSION;
    }

    public Class<?> getNMSClass(String name) {
        return getReflections().getClass(getNMSPrefix() + name, false);
    }

    public Class<?> getNmsClassAsArray(String name) {
        return getReflections().getClass(getNMSPrefix() + name, true);
    }

    public Class<?> getCraftBukkitClass(String name) {
        return getReflections().getClass(getCraftBukkitPrefix() + name, false);
    }

    public Class<?> getCraftBukkitClassAsArray(String name) {
        return getReflections().getClass(getCraftBukkitPrefix() + name, true);
    }

    public Object getEnumGamemode(OfflinePlayer p) {
        SaveField fInteractManager = getReflections().getDeclaredField("playerInteractManager",
                getNMSClass("EntityPlayer"));

        return getReflections().getDeclaredField("gamemode", getNMSClass("PlayerInteractManager"))
                .get(fInteractManager.get(getCraftPlayer(p)));
    }

    public Object getCraftPlayer(OfflinePlayer p) {
        return getReflections().getDeclaredMethod("getHandle", getCraftBukkitClass("entity.CraftPlayer"))
                .invoke(p, true);
    }

    public Object getCraftEntity(Entity entity) {
        return getReflections().getMethod("getHandle", getCraftBukkitClass("entity.CraftEntity")).invoke(entity, true);
    }

    public int getPing(Player p) {
        return (int) getReflections().getDeclaredField("ping", getNMSClass("EntityPlayer"))
                .get(getCraftPlayer(p));
    }

    public Object getNMSItemStack(ItemStack item) {
        return getReflections().getMethod("asNMSCopy", getCraftBukkitClass("inventory.CraftItemStack"), ItemStack.class).invoke(null, true, item);
    }

    public ItemStack getBukkitItemStack(Object nmsItem) {
        return (ItemStack) getReflections().getDeclaredMethod("asCraftMirror",
                getCraftBukkitClass("inventory.CraftItemStack"), nmsItem.getClass()).invokeStatic(nmsItem);
    }

    public int getCraftEntityID(Entity entity) {
        return (int) getReflections().getMethod("getId", getNMSClass("Entity")).invoke(getReflections().getMethod("getHandle", getCraftBukkitClass("entity.CraftEntity")).invoke(entity, true), true);
    }

    public int getNMSEntityID(Object entity) {
        return (int) getReflections().getDeclaredField("id", getNMSClass("Entity")).get(entity);
    }

    public void sendPackets(Player p, Object... packets) {
        for (Object packet : packets)
            sendPacket(p, packet);
    }

    public Object getWorldServer(World world) {
        return getReflections().getMethod("getHandle", getCraftBukkitClass("CraftWorld")).invoke(world, true);
    }

    public Object getMinecraftServer() {
        return getReflections().getMethod("getServer", getCraftBukkitClass("CraftServer")).invoke(Bukkit.getServer(), true);
    }

    public String[] fromIChatBaseComponent(Object... baseComponentArray) {
        String[] array = new String[baseComponentArray.length];

        for (int i = 0; i < array.length; i++) {
            array[i] = fromIChatBaseComponent(baseComponentArray[i]);
        }

        return array;
    }

    private String fromIChatBaseComponent(Object component) {
        return (String) getReflections().getMethod("fromComponent", getCraftBukkitClass("util.CraftChatMessage"), getNMSClass("IChatBaseComponent")).invoke(null, true, component);
    }

    public Object[] toIChatBaseComponent(String... strings) {
        Object[] array = (Object[]) Array.newInstance(getNMSClass("IChatBaseComponent"), strings.length);

        for (int i = 0; i < array.length; i++) {
            array[i] = toIChatBaseComponentArray(strings[i]);
        }

        return array;
    }

    public Object toIChatBaseComponentArray(String s) {
        return getReflections().getDeclaredMethod("fromString", getCraftBukkitClass("util.CraftChatMessage"), String.class).invoke(null, true, s);
    }

    public GameProfile getGameProfile(Player p) {
        return (GameProfile) getReflections().getDeclaredMethod("getProfile", getCraftBukkitClass("entity.CraftPlayer")).invoke(p, true);
    }

    public Object toBlockPosition(BlockPos loc) {
        return getReflections().getDeclaredConstructor(getNMSClass("BlockPosition"), int.class, int.class, int.class).newInstance(true, loc.getX(), loc.getY(), loc.getZ());
    }

    public BlockPos fromBlockPostition(Object nmsLoc) {
        Class<?> bP = getNMSClass("BaseBlockPosition");

        return new BlockPos((int) getReflections().getDeclaredField("a", bP).get(nmsLoc), (int) getReflections().getDeclaredField("b", bP).get(nmsLoc), (Integer) getReflections().getDeclaredField("c", bP).get(nmsLoc));
    }

    public Object getNMSEnumConstant(String nmsClass, int position) {
        return getNMSClass(nmsClass).getEnumConstants()[position];
    }

    public Object getCraftEnumConstant(String craftClass, int position) {
        return getCraftBukkitClass(craftClass).getEnumConstants()[position];
    }

    public void sendPacket(Player p, Object packet) {
        Object nmsPlayer = getCraftPlayer(p);

        if (nmsPlayer == null) return;

        Object con = getReflections().getDeclaredField("playerConnection", nmsPlayer.getClass()).get(nmsPlayer);

        getReflections().getMethod("sendPacket", getNMSClass("PlayerConnection"),
                getNMSClass("Packet")).invoke(con, true, packet);
    }

    public void sendPackets(Object... packets) {
        for (Player p : Bukkit.getOnlinePlayers())
            for (Object packet : packets)
                sendPacket(p, packet);
    }

    public void sendPacketsNotFor(Player notFor, Object... packets) {
        sendPacketsNotFor(notFor.getName(), packets);
    }

    public void sendPacketsNotFor(String notFor, Object... packets) {
        Bukkit.getOnlinePlayers().stream().filter(o -> !o.getName().equals(notFor)).forEach(p -> {
            for (Object packet : packets)
                sendPacket(p, packet);
        });
    }

    public void sendPacket(Player p, IPacket packet) {
        sendPacket(p, packet.getPacket(true));
    }

    public void sendPacket(Player p, IPacket packet, boolean stackTrace) {
        sendPacket(p, packet.getPacket(stackTrace));
    }

    public void sendPackets(IPacket... packets) {
        for (Player p : Bukkit.getOnlinePlayers())
            for (IPacket packet : packets)
                sendPacket(p, packet.getPacket(true));
    }

    public void sendPackets(boolean stackTrace, IPacket... packets) {
        for (Player p : Bukkit.getOnlinePlayers())
            for (IPacket packet : packets)
                sendPacket(p, packet.getPacket(stackTrace));
    }

    public void sendPacketsNotFor(String notFor, IPacket... packets) {
        Bukkit.getOnlinePlayers().stream().filter(o -> !o.getName().equals(notFor)).forEach(p -> {
            for (IPacket packet : packets)
                sendPacket(p, packet.getPacket(true));
        });
    }

    public void sendPacketsNotFor(Player notFor, IPacket... packets) {
        sendPacketsNotFor(notFor.getName(), packets);
    }

    public void sendPacketsNotFor(String notFor, boolean stackTrace, IPacket... packets) {
        Bukkit.getOnlinePlayers().stream().filter(o -> !o.getName().equals(notFor)).forEach(p -> {
            for (IPacket packet : packets)
                sendPacket(p, packet.getPacket(stackTrace));
        });
    }

    public void sendPacketsNotFor(Player notFor, boolean stackTrace, IPacket... packets) {
        sendPacketsNotFor(notFor.getName(), stackTrace, packets);
    }
}
