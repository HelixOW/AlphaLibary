package io.github.alphahelixdev.alpary.utilities;

import lombok.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class NoInitLocation {
	
	private double x, y, z;
	private float yaw, pitch;
	private String world;
	
	public NoInitLocation(Location loc) {
		this(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch(), loc.getWorld().getName());
	}
	
	public Location realize() {
		return new Location(Bukkit.getWorld(getWorld()), getX(), getY(), getZ(), getYaw(), getPitch());
	}
}
