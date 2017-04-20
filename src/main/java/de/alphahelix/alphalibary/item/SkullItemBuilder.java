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

package de.alphahelix.alphalibary.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.UUID;

public class SkullItemBuilder {

    private static Class<?> skullMetaClass, tileEntityClass, blockPositionClass;
    private static int mcVersion;

    static {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        mcVersion = Integer.parseInt(version.replaceAll("[^0-9]", ""));

        skullMetaClass = ReflectionUtil.getCraftBukkitClass("inventory.CraftMetaSkull");
        tileEntityClass = ReflectionUtil.getNmsClass("TileEntitySkull");
        if (mcVersion > 174) {
            blockPositionClass = ReflectionUtil.getNmsClass("BlockPosition");
        }
    }

    /**
     * @param skinURL the URL to the skin-image (full skin)
     * @return The {@link ItemStack} (SKULL_ITEM) with the given look (skin-image)
     */
    public static ItemStack getSkull(String skinURL) {
        return getSkull(skinURL, 1);
    }

    /**
     * @param skinURL The URL to the skin-image (full skin)
     * @param amount  The amount of skulls (for {@link ItemStack})
     * @return The {@link ItemStack} (SKULL_ITEM) with the given look (skin-image)
     */
    public static ItemStack getSkull(String skinURL, int amount) {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, amount, (short) 3);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        try {
            Field profileField = skullMetaClass.getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, getProfile(skinURL));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        skull.setItemMeta(meta);
        return skull;
    }

    /**
     * @param gameProfile the {@link GameProfile} of the player s skin
     * @return the {@link ItemStack} (SKULL_ITEM) with the given look (skin-image)
     */
    public static ItemStack getSkull(GameProfile gameProfile) {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        try {
            Field profileField = skullMetaClass.getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, gameProfile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        skull.setItemMeta(meta);
        return skull;
    }

    /**
     * @param loc     the {@link Location} to place the skull
     * @param skinURL the URL to the skin-image
     * @return If the {@link Block} at the given {@link Location} was replaced with a {@link org.bukkit.block.Skull}
     */
    public static boolean setBlock(Location loc, String skinURL) {
        return setBlock(loc.getBlock(), skinURL);
    }

    /**
     * @param block   The {@link Block} to set to a {@link org.bukkit.block.Skull}
     * @param skinURL The URL to the skin-image
     * @return If the {@link Block} at the given {@link Location} was replaced with a {@link org.bukkit.block.Skull}
     */
    public static boolean setBlock(Block block, String skinURL) {
        boolean flag = block.getType() == Material.SKULL;
        if (!flag) {
            block.setType(Material.SKULL);
        }
        try {
            Object nmsWorld = block.getWorld().getClass().getMethod("getHandle").invoke(block.getWorld());
            Object tileEntity;
            if (mcVersion <= 174) {
                Method getTileEntity = nmsWorld.getClass().getMethod("getTileEntity", int.class, int.class, int.class);
                tileEntity = tileEntityClass.cast(getTileEntity.invoke(nmsWorld, block.getX(), block.getY(), block.getZ()));
            } else {
                Method getTileEntity = nmsWorld.getClass().getMethod("getTileEntity", blockPositionClass);
                tileEntity =
                        tileEntityClass.cast(getTileEntity.invoke(nmsWorld,
                                getBlockPositionFor(block.getX(), block.getY(), block.getZ())));
            }
            tileEntityClass.getMethod("setGameProfile", GameProfile.class).invoke(tileEntity, getProfile(skinURL));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return !flag;
    }

    private static GameProfile getProfile(String skinURL) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        String base64encoded = Base64Coder.encodeString("{textures:{SKIN:{url:\"" + skinURL + "\"}}}");
        Property property = new Property("textures", base64encoded);
        profile.getProperties().put("textures", property);
        return profile;
    }

    private static Object getBlockPositionFor(int x, int y, int z) {
        Object blockPosition = null;
        try {
            Constructor<?> cons = blockPositionClass.getConstructor(int.class, int.class, int.class);
            blockPosition = cons.newInstance(x, y, z);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return blockPosition;
    }

}
