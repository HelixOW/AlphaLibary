package de.alphahelix.alphalibary.schematics;

import org.bukkit.Material;
import org.bukkit.block.Block;

import java.io.Serializable;
import java.util.ArrayList;

public class Schematic implements Serializable {

    private String name;
    private ArrayList<LocationDiff> blocks = new ArrayList<>();

    public Schematic(String name, ArrayList<LocationDiff> blocks) {
        this.name = name;
        this.blocks = blocks;
    }

    public String getName() {
        return name;
    }

    public ArrayList<LocationDiff> getBlocks() {
        return blocks;
    }

    @Override
    public String toString() {
        return "Schematic{" +
                "name='" + name + '\'' +
                ", blocks=" + blocks +
                '}';
    }

    public static class LocationDiff {

        private Material blockType;
        private byte blockData;
        private int x, y, z;

        public LocationDiff(Block block, int x, int y, int z) {
            this.blockType = block.getType();
            this.blockData = block.getData();
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Material getBlockType() {
            return blockType;
        }

        public byte getBlockData() {
            return blockData;
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

        @Override
        public String toString() {
            return "LocationDiff{" +
                    "blockType=" + blockType +
                    ", blockData=" + blockData +
                    ", x=" + x +
                    ", y=" + y +
                    ", z=" + z +
                    '}';
        }
    }
}
