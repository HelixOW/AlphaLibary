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
package de.alphahelix.alphalibary.core.utils.abstracts;

import de.alphahelix.alphalibary.core.utilites.Utility;
import de.alphahelix.alphalibary.core.utils.implementations.ILocationUtil;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;

import java.util.Collection;

@Utility(implementation = ILocationUtil.class)
public abstract class AbstractLocationUtil {
	
	public static AbstractLocationUtil instance;
	
	/**
	 * Checks if two locations are equal
	 *
	 * @param l1 the first {@link Location}
	 * @param l2 the seconds {@link Location}
	 *
	 * @return are the two locations equal
	 */
	public abstract boolean isSameLocation(Location l1, Location l2);
	
	/**
	 * Change the yaw & pitch of a {@link Location} to make it look at another {@link Location}
	 *
	 * @param loc    the {@link Location} which yaw & pitch should be modified
	 * @param lookat the {@link Location} to look at
	 *
	 * @return the new modified {@link Location}
	 */
	public abstract Location lookAt(Location loc, Location lookat);
	
	/**
	 * Checks if a {@link Location} is inside a square of two other locations
	 *
	 * @param loc       the {@link Location} to check for
	 * @param locations to check if Location is inside
	 */
	public abstract boolean isInside(Location loc, Location... locations);
	
	public abstract double[] getX(Location[] locations);
	
	public abstract double[] getY(Location[] locations);
	
	public abstract double[] getZ(Location[] locations);
	
	public abstract double[] getX(Collection<Location> locations);
	
	public abstract double[] getY(Collection<Location> locations);
	
	public abstract double[] getZ(Collection<Location> locations);
	
	/**
	 * Gets the {@link Location} behind a {@link Player}
	 *
	 * @param p     the {@link Player} to get its {@link Location}
	 * @param range the distance to the player
	 */
	public abstract Location getLocationBehindPlayer(Player p, int range);
	
	public abstract Location getRandomLocation(Location player, int Xminimum, int Xmaximum, int Zminimum, int Zmaximum);
	
	public abstract EulerAngle angleToEulerAngle(int degrees);
	
	public abstract EulerAngle angleToEulerAngle(double radians);
	
	public abstract World getRandomWorld();
	
	public abstract Location trim(int decimals, Location loc);
}
