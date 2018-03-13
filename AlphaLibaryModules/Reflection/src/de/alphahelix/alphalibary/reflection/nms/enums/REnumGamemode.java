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

package de.alphahelix.alphalibary.reflection.nms.enums;

import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import org.bukkit.entity.Player;

import java.io.Serializable;

public enum REnumGamemode implements Serializable {
	
	NOT_SET(0),
	SURVIVAL(1),
	CREATIVE(2),
	ADVENTURE(3),
	SPECTATOR(4);
	
	private final int c;
	
	REnumGamemode(int c) {
		this.c = c;
	}
	
	public static REnumGamemode getFromPlayer(Player player) {
		switch(player.getGameMode()) {
			case ADVENTURE:
				return ADVENTURE;
			case CREATIVE:
				return CREATIVE;
			case SURVIVAL:
				return SURVIVAL;
			case SPECTATOR:
				return SPECTATOR;
			default:
				return NOT_SET;
		}
	}
	
	public Object getEnumGamemode() {
		return ReflectionUtil.getNmsClass("EnumGamemode").getEnumConstants()[c];
	}
	
	@Override
	public String toString() {
		return "REnumGamemode{" +
				"c=" + c +
				'}';
	}
}
