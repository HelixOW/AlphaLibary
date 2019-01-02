package io.github.alphahelixdev.alpary.utilities.item.data;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PotionData implements ItemData {

    private final List<PotionEffect> toApply = new ArrayList<>();

    /**
     * Creates a new {@link PotionData} with an array of {@link SimplePotionEffect}
     *
     * @param effects an Array of {@link SimplePotionEffect}s
     */
    public PotionData(SimplePotionEffect... effects) {
        this.addEffects(effects);
    }

    @Override
    public void applyOn(ItemStack applyOn) throws WrongDataException {
        try {
            PotionMeta meta = (PotionMeta) applyOn.getItemMeta();

            for (PotionEffect effect : this.getToApply())
                meta.addCustomEffect(effect, false);

            applyOn.setItemMeta(meta);

        } catch (Exception e) {
            throw new WrongDataException(this);
        }
    }

    public PotionData addEffects(SimplePotionEffect... effects) {
        for (SimplePotionEffect effect : effects)
            this.getToApply().add(effect.createEffect());

        return this;
    }

    public List<PotionEffect> getToApply() {
        return this.toApply;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PotionData that = (PotionData) o;
        return Objects.equals(this.getToApply(), that.getToApply());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getToApply());
    }


    @Override
    public String toString() {
        return "PotionData{" +
                "                            toApply=" + this.toApply +
                '}';
    }
}
