package io.github.alphahelixdev.alpary.utilities.menu;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class Menu {
	
	private final String title;
	private final int size;
	private final MenuManager menuManager = new MenuManager();
	private final Map<Integer, MenuElement> elements = new HashMap<>();
	
	public MenuElement getElement(int slot) {
		return this.getElements().getOrDefault(slot, null);
	}
	
	public Menu addElement(int slot, MenuElement element) {
		this.getElements().put(slot, element);
		return this;
	}
	
	public void open(Player p) {
		this.open(p, p);
	}
	
	public void open(Player p, InventoryHolder holder) {
		Inventory inv = Bukkit.createInventory(holder, this.getSize(), this.getTitle());
		
		this.getElements().forEach((integer, element) -> inv.setItem(integer, element.getIcon(p)));
		
		this.getMenuManager().toggleMenu(p, this);
		
		p.openInventory(inv);
	}
	
	public List<MenuElement> getMenuElements() {
		return new ArrayList<>(this.getElements().values());
	}
}
