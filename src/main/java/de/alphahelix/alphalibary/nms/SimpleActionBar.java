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

import de.alphahelix.alphalibary.nms.enums.RChatMessageType;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;


public class SimpleActionBar {

    private static final Class<?> PACKET_PLAY_OUT_CHAT = ReflectionUtil.getNmsClass("PacketPlayOutChat");
    private static final Class<?> I_CHAT_BASE_COMPONENT = ReflectionUtil.getNmsClass("IChatBaseComponent");
    private static final Class<?> CHAT_COMPONENT_TEXT = ReflectionUtil.getNmsClass("ChatComponentText");

    /**
     * Sends a {@link Player} a actionbar
     *
     * @param p       to send the actionbar
     * @param message to be displayed inside the actionbar
     */
    public static void send(Player p, String message) {
        try {
            Object o = CHAT_COMPONENT_TEXT.getConstructor(String.class).newInstance(message);
            Object packetPlayOutChat = PACKET_PLAY_OUT_CHAT.getConstructor(I_CHAT_BASE_COMPONENT, ReflectionUtil.getNmsClass("ChatMessageType")).newInstance(o, RChatMessageType.GAME_INFO.getNMSChatMessageType());

            ReflectionUtil.sendPacket(p, packetPlayOutChat);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a {@link List} of {@link Player}s a actionbar
     *
     * @param players to send the actionbar
     * @param message to be displayed inside the actionbar
     */
    public static void send(List<Player> players, String message) {
        for (Player p : players) {
            send(p, message);
        }
    }

    /**
     * Send a actionbar into a {@link World}
     *
     * @param world   to send the actionbar to
     * @param message to be displayed inside the actionbar
     */
    public static void send(World world, String message) {
        for (Player p : world.getPlayers()) {
            send(p, message);
        }
    }

    /**
     * Clear the actionbar for a {@link Player}
     *
     * @param player to clear the actionbar for
     */
    private static void clear(Player player) {
        send(player, "");
    }

    /**
     * Clear the actionbar for a {@link List} of {@link org.bukkit.event.player.PlayerShearEntityEvent}
     *
     * @param players to clear the actionbar for
     */
    public static void clear(List<Player> players) {
        for (Player p : players) {
            clear(p);
        }
    }

    /**
     * Clear the actionbar inside a {@link World}
     *
     * @param world the {@link World} to clear the actionbar in
     */
    public static void clear(World world) {
        for (Player p : world.getPlayers()) {
            clear(p);
        }
    }
}
