package de.alphahelix.alphalibary.item.data;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;

public class MapData implements ItemData {

    private Color mapColor;
    private String locationName;
    private boolean scaling = false;

    public MapData(Color mapColor, String locationName) {
        this.mapColor = mapColor;
        this.locationName = locationName;
    }

    public MapData setMapColor(Color mapColor) {
        this.mapColor = mapColor;
        return this;
    }

    public MapData setLocationName(String locationName) {
        this.locationName = locationName;
        return this;
    }

    public MapData setScaling(boolean scaling) {
        this.scaling = scaling;
        return this;
    }

    @Override
    public void applyOn(ItemStack applyOn) throws WrongDataException {
        if (applyOn.getType() != Material.EMPTY_MAP || applyOn.getType() != Material.MAP) return;

        MapMeta meta = (MapMeta) applyOn.getItemMeta();

        meta.setColor(mapColor);
        meta.setLocationName(locationName);
        meta.setScaling(scaling);

        applyOn.setItemMeta(meta);
    }
}
