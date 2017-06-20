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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class SimpleTitle {

    private static Class<?> cEnumTitleAction = ReflectionUtil.getNmsClass("PacketPlayOutTitle$EnumTitleAction");
    private static Class<?> cIChatBaseComponent = ReflectionUtil.getNmsClass("IChatBaseComponent");

    private static Constructor<?> titleConstructor;
    private static Constructor<?> timingConstructor;

    static {
        try {
            titleConstructor = ReflectionUtil.getNmsClass("PacketPlayOutTitle").getConstructor(cEnumTitleAction, cIChatBaseComponent);
            timingConstructor = ReflectionUtil.getNmsClass("PacketPlayOutTitle").getConstructor(int.class, int.class, int.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

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
        try {
            Object pTitle = titleConstructor.newInstance(TitleAction.getNmsEnumObject(TitleAction.TITLE), de.alphahelix.alphalibary.reflection.ReflectionUtil.serializeString(title));
            Object pSubTitle = titleConstructor.newInstance(TitleAction.getNmsEnumObject(TitleAction.SUBTITLE), de.alphahelix.alphalibary.reflection.ReflectionUtil.serializeString(sub));
            Object pTimings = timingConstructor.newInstance(fadeIn * 20, stay * 20, fadeOut * 20);

            ReflectionUtil.sendPacket(p, pTimings);
            ReflectionUtil.sendPacket(p, pTitle);
            ReflectionUtil.sendPacket(p, pSubTitle);

        } catch (InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException
                | SecurityException e) {
            e.printStackTrace();
        }
    }

    public static final class TitleAction {

        public static final int TITLE = 0;
        public static final int SUBTITLE = 1;
        public static final int TIMES = 2;
        public static final int CLEAR = 3;
        public static final int RESET = 4;

        public static Object getNmsEnumObject(int action) {
            if (action < 0 || action > 4) {
                return null;
            }
            return de.alphahelix.alphalibary.reflection.ReflectionUtil.getNmsClass("PacketPlayOutTitle$EnumTitleAction").getEnumConstants()[action];
        }

    }
}
