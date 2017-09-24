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

import com.mojang.authlib.GameProfile;
import de.alphahelix.alphalibary.nms.enums.REnumGamemode;
import de.alphahelix.alphalibary.nms.enums.REnumPlayerInfoAction;

import java.util.ArrayList;

public class PacketUtil {

    private static final Class<?> PLAYER_INFO = ReflectionUtil.getNmsClass("PacketPlayOutPlayerInfo");
    private static final Class<?> P_ID_CLAZZ = ReflectionUtil.getNmsClass("PacketPlayOutPlayerInfo$PlayerInfoData");

    /**
     * Creates a new PacketPlayOutPlayerInfoAction packet
     *
     * @param enumPlayerInfoAction the {@link REnumPlayerInfoAction} to use
     * @param gameProfile          the {@link GameProfile} to perform the action on
     * @param ping                 the ping which should be displayed inside the tablist
     * @param enumGamemode         the {@link REnumGamemode} to use
     * @param name                 the name which should be displayed inside the tablist
     * @return the PacketPlayOutPlayerInfoAction
     */
    public static Object createPlayerInfoPacket(REnumPlayerInfoAction enumPlayerInfoAction, GameProfile gameProfile, int ping, REnumGamemode enumGamemode, String name) {
        Object playOutInfo = ReflectionUtil.getDeclaredConstructor("PacketPlayOutPlayerInfo").newInstance(true);

        ReflectionUtil.getDeclaredField("a", "PacketPlayOutPlayerInfo").set(playOutInfo, enumPlayerInfoAction.getPlayerInfoAction(), true);

        Object playerInfoData = ReflectionUtil.getDeclaredConstructor(P_ID_CLAZZ,
                PLAYER_INFO, GameProfile.class, int.class, ReflectionUtil.getNmsClass("EnumGamemode"), ReflectionUtil.getNmsClass("IChatBaseComponent"))
                .newInstance(true, playOutInfo, gameProfile, ping, enumGamemode.getEnumGamemode(), ReflectionUtil.toIChatBaseComponentArray(name));

        ReflectionUtil.SaveField b = ReflectionUtil.getDeclaredField("b", "PacketPlayOutPlayerInfo");

        ArrayList<Object> array = (ArrayList<Object>) b.get(playOutInfo);

        array.add(playerInfoData);


        b.set(playOutInfo, array, true);

        return playOutInfo;
    }
}
