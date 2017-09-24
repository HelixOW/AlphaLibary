/*
 *     Copyright (C) <2017>  <AlphaHelixDev>
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
import org.bukkit.entity.Player;

public class SimpleTitle {

    private static final Class<?> PACKET_PLAY_OUT_TITLE_$_ENUM_TITLE_ACTION = ReflectionUtil.getNmsClass("PacketPlayOutTitle$EnumTitleAction");
    private static final Class<?> I_CHAT_BASE_COMPONENT = ReflectionUtil.getNmsClass("IChatBaseComponent");

    private static final ReflectionUtil.SaveConstructor PACKET_PLAY_OUT_TITLE = ReflectionUtil.getDeclaredConstructor("PacketPlayOutTitle", PACKET_PLAY_OUT_TITLE_$_ENUM_TITLE_ACTION, I_CHAT_BASE_COMPONENT);
    private static final ReflectionUtil.SaveConstructor PACKET_PLAY_OUT_TITLE1 = ReflectionUtil.getDeclaredConstructor("PacketPlayOutTitle", int.class, int.class, int.class);

    /**
     * Send a title and subtitle to a {@link Player}
     *
     * @param p       {@link Player} you want to send the title
     * @param title   the main title
     * @param sub     the sub title
     * @param fadeIn  fade in in seconds
     * @param stay    stay in seconds
     * @param fadeOut fade out in seconds
     */
    public static void sendTitle(Player p, String title, String sub, int fadeIn, int stay, int fadeOut) {
        Object pTitle = PACKET_PLAY_OUT_TITLE.newInstance(true, TitleAction.TITLE.getNmsEnumObject(), ReflectionUtil.toIChatBaseComponent(title));
        Object pSubTitle = PACKET_PLAY_OUT_TITLE.newInstance(true, TitleAction.SUBTITLE.getNmsEnumObject(), ReflectionUtil.toIChatBaseComponent(sub));
        Object pTimings = PACKET_PLAY_OUT_TITLE1.newInstance(true, fadeIn * 20, stay * 20, fadeOut * 20);

        ReflectionUtil.sendPacket(p, pTimings);
        ReflectionUtil.sendPacket(p, pTitle);
        ReflectionUtil.sendPacket(p, pSubTitle);
    }

    public enum TitleAction {

        TITLE,
        SUBTITLE,
        TIMES,
        CLEAR,
        RESET;

        Object getNmsEnumObject() {
            if (ordinal() < 0 || ordinal() > 4) {
                return null;
            }
            return ReflectionUtil.getNmsClass("PacketPlayOutTitle$EnumTitleAction").getEnumConstants()[ordinal()];
        }
    }
}
