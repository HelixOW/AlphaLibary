package io.github.alphahelixdev.alpary.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.UUID;

public class ArrayUtil {
	
	public Player[] playerArrayFromNames(Collection<String> playerNames) {
		return this.playerArray(playerNames.toArray(new String[playerNames.size()]));
	}
	
	public Player[] playerArray(String... playerNames) {
		Player[] players = new Player[playerNames.length];
		
		for(int i = 0; i < playerNames.length - 1; i++) {
			Player player = Bukkit.getPlayer(playerNames[i]);
			if(player != null)
				players[i] = player;
		}
		
		return players;
	}
	
	public Player[] playerArrayFromUUID(Collection<UUID> playerUUIDS) {
		return this.playerArray(playerUUIDS.toArray(new UUID[playerUUIDS.size()]));
	}
	
	public Player[] playerArray(UUID... playerUUIDS) {
		Player[] players = new Player[playerUUIDS.length];
		
		for(int i = 0; i < playerUUIDS.length - 1; i++) {
			Player player = Bukkit.getPlayer(playerUUIDS[i]);
			if(player != null)
				players[i] = player;
		}
		
		return players;
	}
	
	public Vector[] vectorArrayByLocation(Collection<Location> locations) {
		return this.vectorArray(locations.toArray(new Location[locations.size()]));
	}
	
	public Vector[] vectorArray(Location... locations) {
		Vector[] vectors = new Vector[locations.length];
		
		for(int i = 0; i < locations.length - 1; i++)
			vectors[i] = locations[i].toVector();
		
		return vectors;
	}
	
	public Vector[] vectorArrayByCoordinates(Collection<double[]> coordinates) {
		return this.vectorArray(coordinates.toArray(new double[coordinates.size()][3]));
	}
	
	public Vector[] vectorArray(double[]... coordinates) {
		Vector[] vectors = new Vector[coordinates.length];
		
		for(int i = 0; i < coordinates.length - 1; i++)
			vectors[i] = new Vector(coordinates[i][0], coordinates[i][1], coordinates[i][2]);
		
		return vectors;
	}
	
	public Location[] locationArrayByVector(World world, Collection<Vector> vectors) {
		return this.locationArray(world, vectors.toArray(new Vector[vectors.size()]));
	}
	
	public Location[] locationArray(World world, Vector... vectors) {
		Location[] locations = new Location[vectors.length];
		
		for(int i = 0; i < locations.length; i++)
			locations[i] = vectors[i].toLocation(world);
		
		return locations;
	}
	
	public Location[] locationArrayByCoordinates(World world, Collection<double[]> coordinates) {
		return this.locationArray(world, coordinates.toArray(new double[coordinates.size()][3]));
	}
	
	public Location[] locationArray(World world, double[]... vectors) {
		Location[] locations = new Location[vectors.length];
		
		for(int i = 0; i < locations.length; i++)
			locations[i] = new Location(world, vectors[i][0], vectors[i][1], vectors[i][2]);
		
		return locations;
	}
}
