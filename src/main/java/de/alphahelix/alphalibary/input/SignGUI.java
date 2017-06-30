/*
 *
 *  * Copyright (C) <2017>  <AlphaHelixDev>
 *  *
 *  *       This program is free software: you can redistribute it under the
 *  *       terms of the GNU General Public License as published by
 *  *       the Free Software Foundation, either version 3 of the License.
 *  *
 *  *       This program is distributed in the hope that it will be useful,
 *  *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  *       GNU General Public License for more details.
 *  *
 *  *       You should have received a copy of the GNU General Public License
 *  *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package de.alphahelix.alphalibary.input;

import de.alphahelix.alphalibary.nms.BlockPos;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import org.bukkit.entity.Player;

public class SignGUI extends InputGUI {

	@Override
	public void openGUI(Player p) {
		try {

			BlockPos s = new BlockPos() {
				@Override
				public int getX() {
					return 0;
				}

				@Override
				public int getY() {
					return 0;
				}

				@Override
				public int getZ() {
					return 0;
				}
			};

			ReflectionUtil.sendPacket(p, getPacketPlayOutOpenSignEditor().newInstance(ReflectionUtil.toBlockPosition(s)));

			getOpenGUIS().add(p.getName());
		} catch(ReflectiveOperationException e) {
			e.printStackTrace();
		}
	}
}
