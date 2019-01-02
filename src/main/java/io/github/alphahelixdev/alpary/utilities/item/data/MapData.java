package io.github.alphahelixdev.alpary.utilities.item.data;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;

import java.util.Objects;

public class MapData implements ItemData {

    private String locationName;
    private boolean scaling = false;
    private Color mapColor;

    public MapData(Color mapColor, String locationName) {
        this.setMapColor(mapColor);
        this.setLocationName(locationName);
    }

    @Override
    public void applyOn(ItemStack applyOn) {
        if (applyOn.getType() != Material.MAP || applyOn.getType() != Material.MAP) return;

        MapMeta meta = (MapMeta) applyOn.getItemMeta();

        meta.setColor(this.getMapColor());
        meta.setLocationName(this.getLocationName());
        meta.setScaling(this.isScaling());

        applyOn.setItemMeta(meta);
    }

    public String getLocationName() {
        return this.locationName;
    }

    public MapData setLocationName(String locationName) {
        this.locationName = locationName;
        return this;
    }

    public boolean isScaling() {
        return this.scaling;
    }

    public MapData setScaling(boolean scaling) {
        this.scaling = scaling;
        return this;
    }

    public Color getMapColor() {
        return this.mapColor;
    }

    public MapData setMapColor(Color mapColor) {
        this.mapColor = mapColor;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapData mapData = (MapData) o;
        return this.isScaling() == mapData.isScaling() &&
                Objects.equals(this.getLocationName(), mapData.getLocationName()) &&
                Objects.equals(this.getMapColor(), mapData.getMapColor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getLocationName(), this.isScaling(), this.getMapColor());
    }


    @Override
    public String toString() {
        return "MapData{" +
                "                            locationName='" + this.locationName + '\'' +
                ",                             scaling=" + this.scaling +
                ",                             mapColor=" + this.mapColor +
                '}';
    }
}
