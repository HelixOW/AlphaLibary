package io.github.alphahelixdev.alpary.game;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class GameAchievement {
	
	private final String name;
	private final List<String> description = new ArrayList<>();
	private ItemStack icon;
	
	public GameAchievement(String name, ItemStack icon, Collection<String> description) {
		this.name = name;
		this.icon = icon;
		this.description.addAll(description);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.getName(), this.getDescription(), this.getIcon());
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		GameAchievement that = (GameAchievement) o;
		return Objects.equals(this.getName(), that.getName()) &&
				Objects.equals(this.getDescription(), that.getDescription()) &&
				Objects.equals(this.getIcon(), that.getIcon());
	}
	
	public String getName() {
		return this.name;
	}
	
	public List<String> getDescription() {
		return this.description;
	}
	
	public ItemStack getIcon() {
		return this.icon;
	}
	
	public GameAchievement setIcon(ItemStack icon) {
		this.icon = icon;
		return this;
	}
	
	@Override
	public String toString() {
		return "GameAchievement{" +
				"name='" + name + '\'' +
				", description=" + description +
				", icon=" + icon +
				'}';
	}
}
