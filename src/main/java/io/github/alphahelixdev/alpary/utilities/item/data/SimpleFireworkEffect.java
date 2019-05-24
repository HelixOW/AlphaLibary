package io.github.alphahelixdev.alpary.utilities.item.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class SimpleFireworkEffect implements Serializable {
	
	private boolean flicker = false;
	private boolean trail = false;
	
	private Type type;
	private Color[] color = new Color[]{Color.WHITE}, fades = new Color[]{Color.WHITE};
	
	/**
	 * Creates a new {@link SimpleFireworkEffect} to modify {@link org.bukkit.entity.Firework}s
	 *
	 * @param type the {@link Type} of the {@link org.bukkit.entity.Firework}
	 */
	public SimpleFireworkEffect(Type type) {
		this.setType(type);
	}
	
	public FireworkEffect build() {
		return FireworkEffect.builder().flicker(this.isFlicker()).trail(this.isTrail()).withColor(this.getColor())
				.withFade(this.getFades()).with(this.getType()).build();
	}
}