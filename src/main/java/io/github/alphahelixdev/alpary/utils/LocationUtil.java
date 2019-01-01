package io.github.alphahelixdev.alpary.utils;

import io.github.alphahelixdev.helius.utils.MathUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class LocationUtil {

    public Location lookAt(Location loc, Location lookat) {
		loc = loc.clone();
		double dx = lookat.getX() - loc.getX();
		double dy = lookat.getY() - loc.getY();
		double dz = lookat.getZ() - loc.getZ();
		
		if(dx != 0) {
			if(dx < 0)
				loc.setYaw((float) (1.5 * Math.PI));
			else
				loc.setYaw((float) (0.5 * Math.PI));
			
			loc.setYaw(loc.getYaw() - (float) Math.atan(dz / dx));
		} else if(dz < 0)
			loc.setYaw((float) Math.PI);
		
		double dxz = Math.sqrt((dx * dx) + (dz * dz));
		
		loc.setPitch((float) -Math.atan(dy / dxz));
		
		loc.setYaw(-loc.getYaw() * 180f / (float) Math.PI);
		loc.setPitch(loc.getPitch() * 180f / (float) Math.PI);
		
		return loc;
	}

    public List<Double> getX(Collection<Location> locations) {
		return locations.stream().map(Location::getX).collect(Collectors.toList());
	}

    public List<Double> getY(Collection<Location> locations) {
		return locations.stream().map(Location::getY).collect(Collectors.toList());
	}

    public List<Double> getZ(Collection<Location> locations) {
		return locations.stream().map(Location::getZ).collect(Collectors.toList());
	}

    public Location getLocationBehindPlayer(Player p, int range) {
		return p.getLocation().clone().add(p.getLocation().getDirection().normalize().multiply(-1).multiply(range));
	}

    public Location getRandomLocation(Location player, double minX, double maxX, double minZ, double maxZ, double y) {
		World world = player.getWorld();
		
		double x = minX + ((Math.random() * ((maxX - minX) + 1))) + 0.5d;
		double z = minZ + ((Math.random() * ((maxZ - minZ) + 1))) + 0.5d;
		player.setY(y);
		
		return new Location(world, x, player.getY(), z);
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
		return Bukkit.getWorlds().stream().findAny().orElseThrow(() -> new NoSuchElementException("Can't find any world"));
	}
	
	public boolean isSameBlockLocation(Location l1, Location l2) {
		return ((l1.getBlockX() == l2.getBlockX())
				&& (l1.getBlockY() == l2.getBlockY())
				&& (l1.getBlockZ() == l2.getBlockZ()));
	}

    public Location trim(int decimals, Location loc) {
        return new Location(loc.getWorld(), MathUtil.trim(loc.getX(), decimals), MathUtil.trim(loc.getY(), decimals), MathUtil.trim(loc.getZ(), decimals),
                (float) MathUtil.trim(loc.getYaw(), decimals), (float) MathUtil.trim(loc.getPitch(), decimals));
    }
}
