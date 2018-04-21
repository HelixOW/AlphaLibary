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
package de.alphahelix.alphalibary.core.utils.implementations;

import de.alphahelix.alphalibary.core.utils.ArrayUtil;
import de.alphahelix.alphalibary.core.utils.MathUtil;
import de.alphahelix.alphalibary.core.utils.abstracts.AbstractLocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;


public class ILocationUtil extends AbstractLocationUtil {
	
	public boolean isSameLocation(Location l1, Location l2) {
		return ((l1.getBlockX() == l2.getBlockX())
				&& (l1.getBlockY() == l2.getBlockY())
				&& (l1.getBlockZ() == l2.getBlockZ()));
	}
	
	public Location lookAt(Location loc, Location lookat) {
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
	
	public boolean isInside(Location loc, Location... locations) {
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
	
	public double[] getX(Location[] locations) {
		double[] x = new double[locations.length];
		
		for(int i = 0; i < locations.length; i++) {
			x[i] = locations[i].getX();
		}
		
		return x;
	}
	
	public double[] getY(Location[] locations) {
		double[] y = new double[locations.length];
		
		for(int i = 0; i < locations.length; i++) {
			y[i] = locations[i].getY();
		}
		
		return y;
	}
	
	public double[] getZ(Location[] locations) {
		double[] z = new double[locations.length];
		
		for(int i = 0; i < locations.length; i++) {
			z[i] = locations[i].getZ();
		}
		
		return z;
	}
	
	@Override
	public double[] getX(Collection<Location> locations) {
		return getX(locations.toArray(new Location[locations.size()]));
	}
	
	@Override
	public double[] getY(Collection<Location> locations) {
		return getY(locations.toArray(new Location[locations.size()]));
	}
	
	@Override
	public double[] getZ(Collection<Location> locations) {
		return getZ(locations.toArray(new Location[locations.size()]));
	}
	
	public Location getLocationBehindPlayer(Player p, int range) {
		return p.getLocation().clone().add(p.getLocation().getDirection().normalize().multiply(-1).multiply(range));
	}
	
	public Location getRandomLocation(Location player, int Xminimum, int Xmaximum, int Zminimum, int Zmaximum) {
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
	
	public EulerAngle angleToEulerAngle(int degrees) {
		return angleToEulerAngle(Math.toRadians(degrees));
	}
	
	public EulerAngle angleToEulerAngle(double radians) {
		double x = Math.cos(radians);
		double z = Math.sin(radians);
		return new EulerAngle(x, 0, z);
	}
	
	public World getRandomWorld() {
		Optional<World> optionalWorld = Bukkit.getWorlds().stream().findAny();
		if(optionalWorld.isPresent())
			return optionalWorld.get();
		throw new NoSuchElementException("Cannot find any world");
	}
	
	@Override
	public Location trim(int decimals, Location loc) {
		return new Location(loc.getWorld(), MathUtil.trim(loc.getX(), decimals), MathUtil.trim(loc.getY(), decimals), MathUtil.trim(loc.getZ(), decimals),
				(float) MathUtil.trim(loc.getYaw(), decimals), (float) MathUtil.trim(loc.getPitch(), decimals));
	}
}
