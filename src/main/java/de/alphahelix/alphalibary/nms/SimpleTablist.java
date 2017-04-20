/*
 *     Copyright (C) <2016>  <AlphaHelixDev>
 *
 *     This program is free software: you can redistribute it under the
 *     terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.alphahelix.alphalibary.nms;

import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;


public class SimpleTablist {

    /**
     * Set the tablist of the {@link Player}
     *
     * @param p      you want to change the tablist for
     * @param header what stands above the players
     * @param footer what stands below the players
     */
    public static void setTablistHeaderFooter(Player p, String header, String footer) {

        if (header == null)
            header = "";
        if (footer == null)
            footer = "";

        try {
            Object headerComponent = ReflectionUtil.getNmsClass("ChatComponentText").getConstructor(String.class)
                    .newInstance(ChatColor.translateAlternateColorCodes('&', header));
            Object footerComponent = ReflectionUtil.getNmsClass("ChatComponentText").getConstructor(String.class)
                    .newInstance(ChatColor.translateAlternateColorCodes('&', footer));

            Object packetPlayOutPlayerListHeaderFooter = ReflectionUtil.getNmsClass("PacketPlayOutPlayerListHeaderFooter")
                    .getConstructor(ReflectionUtil.getNmsClass("IChatBaseComponent"))
                    .newInstance(headerComponent);

            Field f = packetPlayOutPlayerListHeaderFooter.getClass().getDeclaredField("b");
            f.setAccessible(true);
            f.set(packetPlayOutPlayerListHeaderFooter, footerComponent);

            ReflectionUtil.sendPacket(p, packetPlayOutPlayerListHeaderFooter);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
