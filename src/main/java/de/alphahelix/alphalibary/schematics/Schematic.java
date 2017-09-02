package de.alphahelix.alphalibary.schematics;

import com.google.common.base.Objects;
import org.bukkit.Material;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schematic schematic = (Schematic) o;
        return Objects.equal(getName(), schematic.getName()) &&
                Objects.equal(getBlocks(), schematic.getBlocks());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName(), getBlocks());
    }

    @Override
    public String toString() {
        return "Schematic{" +
                "name='" + name + '\'' +
                ", blocks=" + blocks +
                '}';
    }

    public interface LocationDiff {
        Material getBlockType();

        MaterialData getBlockData();

        int getX();

        int getY();

        int getZ();
    }
}
