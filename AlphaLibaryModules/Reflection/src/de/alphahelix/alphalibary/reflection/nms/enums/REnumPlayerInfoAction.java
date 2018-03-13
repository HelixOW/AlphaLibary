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
package de.alphahelix.alphalibary.reflection.nms.enums;

import de.alphahelix.alphalibary.reflection.ReflectionUtil;

import java.io.Serializable;

public enum REnumPlayerInfoAction implements Serializable {
	
	ADD_PLAYER(0), UPDATE_GAME_MODE(1), UPDATE_LATENCY(2), UPDATE_DISPLAY_NAME(3), REMOVE_PLAYER(4);
	
	private final int index;
	
	REnumPlayerInfoAction(int enumIndex) {
		this.index = enumIndex;
	}
	
	public Object getPlayerInfoAction() {
		return ReflectionUtil.getNmsClass("PacketPlayOutPlayerInfo$EnumPlayerInfoAction").getEnumConstants()[index];
	}
	
	@Override
	public String toString() {
		return "REnumPlayerInfoAction{" +
				"index=" + index +
				'}';
	}
}
