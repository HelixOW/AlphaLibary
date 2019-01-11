package io.github.alphahelixdev.alpary.utilities;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Objects;

public class NoInitLocation {
	
	private double x, y, z;
	private float yaw, pitch;
	private String world;
	
	public NoInitLocation(Location loc) {
		this(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch(), loc.getWorld().getName());
	}
	
	public NoInitLocation(double x, double y, double z, float yaw, float pitch, String world) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.world = world;
	}
	
	public Location realize() {
		return new Location(Bukkit.getWorld(getWorld()), getX(), getY(), getZ(), getYaw(), getPitch());
	}
	
	public String getWorld() {
		return this.world;
	}
	
	public double getX() {
		return this.x;
	}
	
	public NoInitLocation setX(double x) {
		this.x = x;
		return this;
	}
	
	public double getY() {
		return this.y;
	}
	
	public NoInitLocation setY(double y) {
		this.y = y;
		return this;
	}
	
	public double getZ() {
		return this.z;
	}
	
	public NoInitLocation setZ(double z) {
		this.z = z;
		return this;
	}
	
	public float getYaw() {
		return this.yaw;
	}
	
	public NoInitLocation setYaw(float yaw) {
		this.yaw = yaw;
		return this;
	}
	
	public float getPitch() {
		return this.pitch;
	}
	
	public NoInitLocation setPitch(float pitch) {
		this.pitch = pitch;
		return this;
	}
	
	public NoInitLocation setWorld(String world) {
		this.world = world;
		return this;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.getX(), this.getY(), this.getZ(), this.getYaw(), this.getPitch(), this.getWorld());
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		NoInitLocation that = (NoInitLocation) o;
		return Double.compare(that.getX(), getX()) == 0 &&
				Double.compare(that.getY(), getY()) == 0 &&
				Double.compare(that.getZ(), getZ()) == 0 &&
				Float.compare(that.getYaw(), getYaw()) == 0 &&
				Float.compare(that.getPitch(), getPitch()) == 0 &&
				Objects.equals(this.getWorld(), that.getWorld());
	}
	
	@Override
	public String toString() {
		return "NoInitLocation{" +
				"x=" + x +
				", y=" + y +
				", z=" + z +
				", yaw=" + yaw +
				", pitch=" + pitch +
				", world='" + world + '\'' +
				'}';
	}
}
