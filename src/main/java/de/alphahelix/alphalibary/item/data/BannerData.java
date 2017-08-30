package de.alphahelix.alphalibary.item.data;

import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

public class BannerData implements ItemData {

    private Pattern[] patterns;

    public BannerData(Pattern... patterns) {
        this.patterns = patterns;
    }

    @Override
    public void applyOn(ItemStack applyOn) throws WrongDataException {
        if (applyOn.getType() != Material.BANNER) return;

        BannerMeta meta = (BannerMeta) applyOn.getItemMeta();

        for (Pattern pattern : patterns)
            meta.addPattern(pattern);

        applyOn.setItemMeta(meta);
    }
}
