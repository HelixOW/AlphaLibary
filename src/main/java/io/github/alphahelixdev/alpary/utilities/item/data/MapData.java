package io.github.alphahelixdev.alpary.utilities.item.data;

import lombok.*;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class MapData implements ItemData {
	
	@NonNull
	private Color mapColor;
	@NonNull
	private String locationName;
    private boolean scaling = false;

    @Override
    public void applyOn(ItemStack applyOn) {
        if (applyOn.getType() != Material.MAP || applyOn.getType() != Material.MAP) return;

        MapMeta meta = (MapMeta) applyOn.getItemMeta();

        meta.setColor(this.getMapColor());
        meta.setLocationName(this.getLocationName());
        meta.setScaling(this.isScaling());

        applyOn.setItemMeta(meta);
    }
}
