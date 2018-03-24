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

import de.alphahelix.alphalibary.core.utilites.locations.Shape;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.NoSuchElementException;
import java.util.Optional;


public class LocationUtil {
	
	/**
	 * Checks if two locations are equal
	 *
	 * @param l1 the first {@link Location}
	 * @param l2 the seconds {@link Location}
	 *
	 * @return are the two locations equal
	 */
	public static boolean isSameLocation(Location l1, Location l2) {
		return ((l1.getBlockX() == l2.getBlockX())
				&& (l1.getBlockY() == l2.getBlockY())
				&& (l1.getBlockZ() == l2.getBlockZ()));
	}
	
	/**
	 * Change the yaw & pitch of a {@link Location} to make it look at another {@link Location}
	 *
	 * @param loc    the {@link Location} which yaw & pitch should be modified
	 * @param lookat the {@link Location} to look at
	 *
	 * @return the new modified {@link Location}
	 */
	public static Location lookAt(Location loc, Location lookat) {
		loc = loc.clone();
		double dx = lookat.getX() - loc.getX();
		double dy = lookat.getY() - loc.getY();
		double dz = lookat.getZ() - loc.getZ();
		
		if(dx != 0) {
			if(dx < 0) {
				loc.setYaw((float) (1.5 * Math.PI));
			} else {
				loc.setYaw((float) (0.5 * Math.PI));
			}
			loc.setYaw(loc.getYaw() - (float) Math.atan(dz / dx));
		} else if(dz < 0) {
			loc.setYaw((float) Math.PI);
		}
		
		double dxz = Math.sqrt(Math.pow(dx, 2) + Math.pow(dz, 2));
		
		loc.setPitch((float) -Math.atan(dy / dxz));
		
		loc.setYaw(-loc.getYaw() * 180f / (float) Math.PI);
		loc.setPitch(loc.getPitch() * 180f / (float) Math.PI);
		
		return loc;
	}
	
	/**
	 * Checks if a {@link Location} is inside a square of two other locations
	 *
	 * @param loc       the {@link Location} to check for
	 * @param locations to check if Location is inside
	 */
	public static boolean isInside(Location loc, Location... locations) {
		double x1 = ArrayUtil.min(getX(locations));
		double y1 = ArrayUtil.min(getY(locations));
		double z1 = ArrayUtil.min(getZ(locations));
		
		double x2 = ArrayUtil.max(getX(locations));
		double y2 = ArrayUtil.max(getY(locations));
		double z2 = ArrayUtil.max(getZ(locations));
		
		return loc.getX() >= x1 && loc.getX() <= x2
				&& loc.getY() >= y1 && loc.getY() <= y2
				&& loc.getZ() >= z1 && loc.getZ() <= z2;
	}
	
	public static double[] getX(Location[] locations) {
		double[] x = new double[locations.length];
		
		for(int i = 0; i < locations.length; i++) {
			x[i] = locations[i].getX();
		}
		
		return x;
	}
	
	public static double[] getY(Location[] locations) {
		double[] y = new double[locations.length];
		
		for(int i = 0; i < locations.length; i++) {
			y[i] = locations[i].getY();
		}
		
		return y;
	}
	
	public static double[] getZ(Location[] locations) {
		double[] z = new double[locations.length];
		
		for(int i = 0; i < locations.length; i++) {
			z[i] = locations[i].getZ();
		}
		
		return z;
	}
	
	public static boolean isInShape(Location loc, Shape shape, Vector... locations) {
		
		double a = shape.area(locations);
		double a1 = shape.area(new Lo)
		
		double A = area(posA.getX(), posA.getZ(), posB.getX(), posB.getZ(), posC.getX(), posC.getZ());
		double A1 = area(x, y, posB.getX(), posB.getZ(), posC.getX(), posC.getZ());
		double A2 = area(posA.getX(), posA.getZ(), x, y, posC.getX(), posC.getZ());
		double A3 = area(posA.getX(), posA.getZ(), posB.getX(), posB.getZ(), x, y);
		
		return (A == A1 + A2 + A3);
	}
	
	/**
	 * Gets the {@link Location} behind a {@link Player}
	 *
	 * @param p     the {@link Player} to get its {@link Location}
	 * @param range the distance to the player
	 */
	public static Location getLocationBehindPlayer(Player p, int range) {
		return p.getLocation().clone().add(p.getLocation().getDirection().normalize().multiply(-1).multiply(range));
	}
	
	public static Location getRandomLocation(Location player, int Xminimum, int Xmaximum, int Zminimum, int Zmaximum) {
		try {
			World world = player.getWorld();
			double x = Double.parseDouble(
					Integer.toString(Xminimum + ((int) (Math.random() * ((Xmaximum - Xminimum) + 1))))) + 0.5d;
			double z = Double.parseDouble(
					Integer.toString(Zminimum + ((int) (Math.random() * ((Zmaximum - Zminimum) + 1))))) + 0.5d;
			player.setY(200);
			return new Location(world, x, player.getY(), z);
		} catch(NullPointerException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static EulerAngle angleToEulerAngle(int degrees) {
		return angleToEulerAngle(Math.toRadians(degrees));
	}
	
	public static EulerAngle angleToEulerAngle(double radians) {
		double x = Math.cos(radians);
		double z = Math.sin(radians);
		return new EulerAngle(x, 0, z);
	}
	
	public static World getRandomWorld() {
		Optional<World> optionalWorld = Bukkit.getWorlds().stream().findAny();
		if(optionalWorld.isPresent())
			return optionalWorld.get();
		throw new NoSuchElementException("Cannot find any world");
	}
}
