package de.alphahelix.alphalibary.storage.file.serializers;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationSerializer implements CSVSerializer<Location> {
	@Override
	public String encode(Location type) {
		return type.getX() + ":" + type.getY() + ":" + type.getZ() + ":" + type.getYaw() + ":" + type.getPitch() + ":" + type.getWorld().getName();
	}
	
	@Override
	public Location decode(String csvEncodedLine) {
		String[] info = csvEncodedLine.split(":");
		return new Location(
				Bukkit.getWorld(info[5]),
				Double.parseDouble(info[0]),
				Double.parseDouble(info[1]),
				Double.parseDouble(info[2]),
				Float.parseFloat(info[3]),
				Float.parseFloat(info[4])
		);
	}
}
