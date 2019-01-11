package io.github.alphahelixdev.alpary.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

public class ArrayUtil extends io.github.alphahelixdev.helius.utils.ArrayUtil {
	
	public Location[] trim(int decimal, Location... locations) {
		return Arrays.stream(locations).map(location -> Utils.locations().trim(decimal, location)).toArray(Location[]::new);
	}
	
	public Player[] playerArrayFromString(Collection<String> playerNames) {
		return playerNames.stream().map(Bukkit::getPlayer).filter(Objects::nonNull).toArray(Player[]::new);
	}
	
	public Player[] playerArray(String... playerNames) {
		return this.playerArrayFromString(Arrays.asList(playerNames));
	}
	
	public Player[] playerArrayFromUUID(Collection<UUID> playerUUIDS) {
		return playerUUIDS.stream().map(Bukkit::getPlayer).filter(Objects::nonNull).toArray(Player[]::new);
	}
	
	public Player[] playerArray(UUID... playerUUIDS) {
		return this.playerArrayFromUUID(Arrays.asList(playerUUIDS));
	}
	
	public Vector[] vectorArrayByLocation(Collection<Location> locations) {
		return locations.stream().map(Location::toVector).toArray(Vector[]::new);
	}
	
	public Vector[] vectorArray(Location... locations) {
		return this.vectorArrayByLocation(Arrays.asList(locations));
	}
	
	public Vector[] vectorArrayByCoordinates(Collection<double[]> coordinates) {
		return coordinates.stream().map(doubles -> new Vector(doubles[0], doubles[1], doubles[2])).toArray(Vector[]::new);
	}
	
	public Vector[] vectorArray(double[]... coordinates) {
		return this.vectorArrayByCoordinates(Arrays.asList(coordinates));
	}
	
	public Location[] locationArrayByVector(World world, Collection<Vector> vectors) {
		return vectors.stream().map(vector -> new Location(world, vector.getX(), vector.getY(), vector.getZ())).toArray(Location[]::new);
	}
	
	public Location[] locationArray(World world, Vector... vectors) {
		return this.locationArrayByVector(world, Arrays.asList(vectors));
	}
	
	public Location[] locationArrayByCoordinates(World world, Collection<double[]> coordinates) {
		return coordinates.stream().map(doubles -> new Location(world, doubles[0], doubles[1], doubles[2])).toArray(Location[]::new);
	}
	
	public Location[] locationArray(World world, double[]... vectors) {
		return this.locationArrayByCoordinates(world, Arrays.asList(vectors));
	}
}