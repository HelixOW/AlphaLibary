package io.github.alphahelixdev.alpary.utilities.item.data;

import io.github.alphahelixdev.alpary.Alpary;
import lombok.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class SkullData implements ItemData {

    private String ownerName;

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
}