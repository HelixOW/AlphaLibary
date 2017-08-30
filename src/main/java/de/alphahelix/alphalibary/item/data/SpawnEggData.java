package de.alphahelix.alphalibary.item.data;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SpawnEggMeta;

public class SpawnEggData implements ItemData {

    EntityType spawnedType;

    public SpawnEggData(EntityType spawnedType) {
        this.spawnedType = spawnedType;
    }

    public SpawnEggData setSpawnedType(EntityType spawnedType) {
        this.spawnedType = spawnedType;
        return this;
    }

    @Override
    public void applyOn(ItemStack applyOn) throws WrongDataException {
        if (applyOn.getType() != Material.MONSTER_EGG) return;

        SpawnEggMeta meta = (SpawnEggMeta) applyOn.getItemMeta();

        meta.setSpawnedType(spawnedType);

        applyOn.setItemMeta(meta);
    }
}
