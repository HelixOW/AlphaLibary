package de.alphahelix.alphalibary.schematics;

import org.bukkit.Material;
import org.bukkit.material.MaterialData;

import java.io.Serializable;
import java.util.List;

public interface Schematic extends Serializable {

    String getName();

    List<LocationDiff> getBlocks();

    interface LocationDiff {
        Material getBlockType();

        MaterialData getBlockData();

        int getX();

        int getY();

        int getZ();
    }
}
