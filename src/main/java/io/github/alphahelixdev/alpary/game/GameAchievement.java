package io.github.alphahelixdev.alpary.game;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class GameAchievement {
	
	private final String name;
	private final List<String> description = new ArrayList<>();
	private ItemStack icon;
	
	public GameAchievement(String name, ItemStack icon, Collection<String> description) {
		this.name = name;
		this.icon = icon;
		this.description.addAll(description);
	}
}
