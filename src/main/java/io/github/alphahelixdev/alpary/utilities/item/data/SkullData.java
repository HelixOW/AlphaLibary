package io.github.alphahelixdev.alpary.utilities.item.data;

import io.github.alphahelixdev.alpary.Alpary;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Objects;

public class SkullData implements ItemData {

    private String ownerName;

    /**
     * Creates a {@link org.bukkit.block.Skull} with a special skin
     *
     * @param name of the owner
     */
    public SkullData(String name) {
        this.setOwnerName(name);
    }

    @Override
    public void applyOn(ItemStack applyOn) {
        try {
	        if(applyOn.getType() != Material.PLAYER_HEAD)
                return;
	
	        SkullMeta skullMeta = (SkullMeta) applyOn.getItemMeta();
	        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(Alpary.getInstance().uuidFetcher().getUUID(this.getOwnerName())));
            applyOn.setItemMeta(skullMeta);

        } catch (Exception e) {
            try {
                throw new WrongDataException(this);
            } catch (WrongDataException ignored) {

            }
        }
    }

    public String getOwnerName() {
        return this.ownerName;
    }

    public SkullData setOwnerName(String ownerName) {
        this.ownerName = ownerName;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SkullData skullData = (SkullData) o;
        return Objects.equals(this.getOwnerName(), skullData.getOwnerName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getOwnerName());
    }


    @Override
    public String toString() {
        return "SkullData{" +
                "                            ownerName='" + this.ownerName + '\'' +
                '}';
    }
}