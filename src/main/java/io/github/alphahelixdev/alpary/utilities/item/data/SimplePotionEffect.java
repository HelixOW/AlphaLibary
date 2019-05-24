package io.github.alphahelixdev.alpary.utilities.item.data;

import lombok.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class SimplePotionEffect implements Serializable {
	
	private int durationInSec, amplifier;
	private PotionEffectType potionType;
	
	public PotionEffect createEffect() {
		return new PotionEffect(this.getPotionType(), this.getDurationInSec() * 20, this.getAmplifier());
	}
}
