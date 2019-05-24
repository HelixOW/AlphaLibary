package io.github.alphahelixdev.alpary.game;

import io.github.alphahelixdev.alpary.game.events.KitReceiveEvent;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class GameKit {
	
	private static final Map<String, GameKit> KITS = new HashMap<>();
	
	private final String name;
	private final ItemStack[] content;
	private float price;
	private ItemStack icon;
	
	public GameKit(String name, float price, ItemStack icon, ItemStack... content) {
		this.name = name;
		this.price = price;
		this.icon = icon;
		this.content = content;
		
		KITS.put(getRawName(), this);
	}
	
	public static GameKit of(String rawName) {
		return KITS.getOrDefault(rawName, null);
	}
	
	public GameKit giveItems(Player p) {
		KitReceiveEvent kre = new KitReceiveEvent(p, this);
		
		Bukkit.getPluginManager().callEvent(kre);
		
		if (!kre.isCancelled()) {
			Arrays.stream(this.content).filter(Objects::nonNull).forEach(itemStack ->
					p.getInventory().addItem(itemStack));
		}
		return this;
	}
	
	public String getRawName() {
		return ChatColor.stripColor(this.name).replace(" ", "_");
	}
}
