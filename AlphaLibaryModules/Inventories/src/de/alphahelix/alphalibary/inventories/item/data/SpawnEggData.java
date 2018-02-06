package de.alphahelix.alphalibary.inventories.item.data;

import com.google.common.base.Objects;
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
    public void applyOn(ItemStack applyOn) {
        if (applyOn.getType() != Material.MONSTER_EGG) return;

        SpawnEggMeta meta = (SpawnEggMeta) applyOn.getItemMeta();

        meta.setSpawnedType(spawnedType);

        applyOn.setItemMeta(meta);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpawnEggData that = (SpawnEggData) o;
        return spawnedType == that.spawnedType;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(spawnedType);
    }

    @Override
    public String toString() {
        return "SpawnEggData{" +
                "spawnedType=" + spawnedType +
                '}';
    }
}
