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

package de.alphahelix.alphalibary.inventories.item;

import com.mojang.authlib.GameProfile;
import de.alphahelix.alphalibary.core.utils.SkinChangeUtil;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.Serializable;
import java.lang.reflect.Field;

public class SkullItemBuilder {

    private static final Base64 BASE_64 = new Base64();

    public static ItemStack getCustomSkull(String url) {
        GameProfile profile = SkinChangeUtil.changeSkin(url);

        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        ItemMeta headMeta = head.getItemMeta();
        Class<?> headMetaClass = headMeta.getClass();

        try {
            Field f = headMetaClass.getDeclaredField("profile");
            f.setAccessible(true);

            f.set(headMeta, profile);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        head.setItemMeta(headMeta);
        return head;
    }

    public static ItemStack getPlayerSkull(String name) {
        ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
        meta.setOwner(name);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public enum Skulls implements Serializable {
        ARROW_LEFT("MHF_ArrowLeft"),
        ARROW_RIGHT("MHF_ArrowRight"),
        ARROW_UP("MHF_ArrowUp"),
        ARROW_DOWN("MHF_ArrowDown"),
        QUESTION("MHF_Question"),
        EXCLAMATION("MHF_Exclamation"),
        CAMERA("FHG_Cam"),

        ZOMBIE_PIGMAN("MHF_PigZombie"),
        PIG("MHF_Pig"),
        SHEEP("MHF_Sheep"),
        BLAZE("MHF_Blaze"),
        CHICKEN("MHF_Chicken"),
        COW("MHF_Cow"),
        SLIME("MHF_Slime"),
        SPIDER("MHF_Spider"),
        SQUID("MHF_Squid"),
        VILLAGER("MHF_Villager"),
        OCELOT("MHF_Ocelot"),
        HEROBRINE("MHF_Herobrine"),
        LAVA_SLIME("MHF_LavaSlime"),
        MOOSHROOM("MHF_MushroomCow"),
        GOLEM("MHF_Golem"),
        GHAST("MHF_Ghast"),
        ENDERMAN("MHF_Enderman"),
        CAVE_SPIDER("MHF_CaveSpider"),

        CACTUS("MHF_Cactus"),
        CAKE("MHF_Cake"),
        CHEST("MHF_Chest"),
        MELON("MHF_Melon"),
        LOG("MHF_OakLog"),
        PUMPKIN("MHF_Pumpkin"),
        TNT("MHF_TNT"),
        DYNAMITE("MHF_TNT2");

        private final String id;

        Skulls(String id) {
            this.id = id;
        }

        public ItemStack getSkull() {
            ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
            meta.setOwner(id);
            itemStack.setItemMeta(meta);
            return itemStack;
        }

        @Override
        public String toString() {
            return "Skulls{" +
                    "id='" + id + '\'' +
                    '}';
        }
    }
}
