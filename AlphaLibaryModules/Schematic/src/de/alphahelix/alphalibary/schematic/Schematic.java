package de.alphahelix.alphalibary.schematic;

import com.google.common.base.Objects;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.material.MaterialData;

import java.io.Serializable;
import java.util.List;

public class Schematic implements Serializable {
	
	private String name;
	private List<LocationDiff> blocks;
	
	public Schematic(String name, List<LocationDiff> blocks) {
		this.name = name;
		this.blocks = blocks;
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(getName(), getBlocks());
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		Schematic schematic = (Schematic) o;
		return Objects.equal(getName(), schematic.getName()) &&
				Objects.equal(getBlocks(), schematic.getBlocks());
	}
	
	@Override
	public String toString() {
		return "Schematic{" +
				"name='" + name + '\'' +
				", players=" + blocks +
				'}';
	}
	
	public String getName() {
		return name;
	}
	
	public Schematic setName(String name) {
		this.name = name;
		return this;
	}
	
	public List<LocationDiff> getBlocks() {
		return blocks;
	}
	
	public Schematic setBlocks(List<LocationDiff> blocks) {
		this.blocks = blocks;
		return this;
	}
	
	public static class LocationDiff {
		private final Material blockType;
		private final MaterialData blockData;
		private final int blockPower, x, y, z;
		
		public LocationDiff(Block b, Location location) {
			this.blockType = b.getType();
			this.blockData = b.getState().getData();
			this.blockPower = b.getBlockPower();
			this.x = b.getX() - location.getBlockX();
			this.y = b.getY() - location.getBlockY();
			this.z = b.getZ() - location.getBlockZ();
		}
		
		public Material getBlockType() {
			return blockType;
		}
		
		public MaterialData getBlockData() {
			return blockData;
		}
		
		public int getBlockPower() {
			return blockPower;
		}
		
		public int getX() {
			return x;
		}
		
		public int getY() {
			return y;
		}
		
		public int getZ() {
			return z;
		}
	}
}
