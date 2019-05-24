package io.github.alphahelixdev.alpary.utilities.item.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

@Getter
@EqualsAndHashCode
@ToString
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
}
