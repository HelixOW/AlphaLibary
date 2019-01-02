package io.github.alphahelixdev.alpary.utilities.item.data;

import com.google.common.base.Objects;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.ArrayList;
import java.util.List;

public class FireworkData implements ItemData {

    private final List<FireworkEffect> allEffects = new ArrayList<>();

    /**
     * Creates a new {@link FireworkData} with an array of {@link SimpleFireworkEffect}
     *
     * @param effects an Array of {@link SimpleFireworkEffect}s
     */
    public FireworkData(SimpleFireworkEffect... effects) {
        addEffects(effects);
    }

    @Override
    public void applyOn(ItemStack applyOn) {
        try {
            if (!(applyOn.getType() == Material.FIREWORK_ROCKET))
                return;


            FireworkMeta fireworkMeta = (FireworkMeta) applyOn.getItemMeta();
            fireworkMeta.addEffects(this.getAllEffects());
            applyOn.setItemMeta(fireworkMeta);
        } catch (Exception e) {
            try {
                throw new WrongDataException(this);
            } catch (WrongDataException ignored) {

            }
        }
    }

    public FireworkData addEffects(SimpleFireworkEffect... effects) {
        for (SimpleFireworkEffect effect : effects)
            this.getAllEffects().add(effect.build());
        return this;
    }

    public List<FireworkEffect> getAllEffects() {
        return allEffects;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getAllEffects());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FireworkData that = (FireworkData) o;
        return Objects.equal(this.getAllEffects(), that.getAllEffects());
    }

    @Override
    public String toString() {
        return "FireworkData{" +
                "allEffects=" + this.getAllEffects() +
                '}';
    }
}
