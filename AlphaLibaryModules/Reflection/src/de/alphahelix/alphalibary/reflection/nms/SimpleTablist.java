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
package de.alphahelix.alphalibary.reflection.nms;

import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class SimpleTablist {
	
	private static final ReflectionUtil.SaveConstructor CHAT_COMPONENT_TEXT = ReflectionUtil.getDeclaredConstructor("ChatComponentText", String.class);
	private static final ReflectionUtil.SaveConstructor PACKET_PLAY_OUT_PLAYER_LIST_HEADER_FOOTER = ReflectionUtil.getDeclaredConstructor("PacketPlayOutPlayerListHeaderFooter");
	
	/**
	 * Set the tablist of the {@link Player}
	 *
	 * @param p      you want to change the tablist for
	 * @param header what stands above the players
	 * @param footer what stands below the players
	 */
	public static void setTablistHeaderFooter(Player p, String header, String footer) {
		if(header == null)
			header = "";
		if(footer == null)
			footer = "";
		
		Object headerComponent = CHAT_COMPONENT_TEXT.newInstance(true, ChatColor.translateAlternateColorCodes('&', header));
		Object footerComponent = CHAT_COMPONENT_TEXT.newInstance(true, ChatColor.translateAlternateColorCodes('&', footer));
		
		Object packetPlayOutPlayerListHeaderFooter = PACKET_PLAY_OUT_PLAYER_LIST_HEADER_FOOTER.newInstance(true);
		
		ReflectionUtil.SaveField h = ReflectionUtil.getDeclaredField("a", packetPlayOutPlayerListHeaderFooter.getClass());
		ReflectionUtil.SaveField f = ReflectionUtil.getDeclaredField("b", packetPlayOutPlayerListHeaderFooter.getClass());
		
		h.set(packetPlayOutPlayerListHeaderFooter, headerComponent, true);
		f.set(packetPlayOutPlayerListHeaderFooter, footerComponent, true);
		
		ReflectionUtil.sendPacket(p, packetPlayOutPlayerListHeaderFooter);
	}
}
