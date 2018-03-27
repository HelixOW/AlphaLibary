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
package de.alphahelix.alphalibary.core.utils;

import de.alphahelix.alphalibary.core.utils.abstracts.AbstractLocationUtil;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;


public interface LocationUtil {
	
	/**
	 * Checks if two locations are equal
	 *
	 * @param l1 the first {@link Location}
	 * @param l2 the seconds {@link Location}
	 *
	 * @return are the two locations equal
	 */
	static boolean isSameLocation(Location l1, Location l2) {
		return AbstractLocationUtil.instance.isSameLocation(l1, l2);
	}
	
	/**
	 * Change the yaw & pitch of a {@link Location} to make it look at another {@link Location}
	 *
	 * @param loc    the {@link Location} which yaw & pitch should be modified
	 * @param lookat the {@link Location} to look at
	 *
	 * @return the new modified {@link Location}
	 */
	static Location lookAt(Location loc, Location lookat) {
		return AbstractLocationUtil.instance.lookAt(loc, lookat);
	}
	
	/**
	 * Checks if a {@link Location} is inside a square of two other locations
	 *
	 * @param loc       the {@link Location} to check for
	 * @param locations to check if Location is inside
	 */
	static boolean isInside(Location loc, Location... locations) {
		return AbstractLocationUtil.instance.isInside(loc, locations);
	}
	
	static double[] getX(Location[] locations) {
		return AbstractLocationUtil.instance.getX(locations);
	}
	
	static double[] getY(Location[] locations) {
		return AbstractLocationUtil.instance.getY(locations);
	}
	
	static double[] getZ(Location[] locations) {
		return AbstractLocationUtil.instance.getZ(locations);
	}
	
	/**
	 * Gets the {@link Location} behind a {@link Player}
	 *
	 * @param p     the {@link Player} to get its {@link Location}
	 * @param range the distance to the player
	 */
	static Location getLocationBehindPlayer(Player p, int range) {
		return AbstractLocationUtil.instance.getLocationBehindPlayer(p, range);
	}
	
	static Location getRandomLocation(Location player, int Xminimum, int Xmaximum, int Zminimum, int Zmaximum) {
		return AbstractLocationUtil.instance.getRandomLocation(player, Xminimum, Xmaximum, Zminimum, Zmaximum);
	}
	
	static EulerAngle angleToEulerAngle(int degrees) {
		return AbstractLocationUtil.instance.angleToEulerAngle(degrees);
	}
	
	static EulerAngle angleToEulerAngle(double radians) {
		return AbstractLocationUtil.instance.angleToEulerAngle(radians);
	}
	
	static World getRandomWorld() {
		return AbstractLocationUtil.instance.getRandomWorld();
	}
}
