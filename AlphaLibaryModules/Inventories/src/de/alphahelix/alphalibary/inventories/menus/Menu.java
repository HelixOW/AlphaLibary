package de.alphahelix.alphalibary.inventories.menus;

import com.google.common.base.Objects;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Menu implements Serializable {
	
	private final String title;
	private final int size;
	private final MenuManager menuManager;
	private HashMap<Integer, MenuElement> elements;
	
	public Menu(String title, int size) {
		this.title = title;
		this.size = size;
		this.elements = new HashMap<>();
		
		menuManager = new MenuManager();
	}
	
	public MenuElement getElement(int slot) {
		if(elements.containsKey(slot))
			return elements.get(slot);
		return null;
	}
	
	public Menu addElement(int slot, MenuElement element) {
		elements.put(slot, element);
		return this;
	}
	
	public void open(Player p) {
		open(p, p);
	}
	
	public void open(Player p, InventoryHolder holder) {
		Inventory inv = Bukkit.createInventory(holder, getSize(), getTitle());
		
		for(Map.Entry<Integer, MenuElement> element : this.elements.entrySet()) {
			inv.setItem(element.getKey(), element.getValue().getIcon(p));
		}
		
		getMenuManager().setMenuOpened(p, this);
		
		p.openInventory(inv);
	}
	
	public int getSize() {
		return size;
	}
	
	public String getTitle() {
		return title;
	}
	
	public MenuManager getMenuManager() {
		return menuManager;
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(getTitle(), getSize(), getElements(), getMenuManager());
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		Menu menu = (Menu) o;
		return getSize() == menu.getSize() &&
				Objects.equal(getTitle(), menu.getTitle()) &&
				Objects.equal(getElements(), menu.getElements()) &&
				Objects.equal(getMenuManager(), menu.getMenuManager());
	}
	
	@Override
	public String toString() {
		return "Menu{" +
				"title='" + title + '\'' +
				", size=" + size +
				", elements=" + elements +
				", menuManager=" + menuManager +
				'}';
	}
	
	public List<MenuElement> getElements() {
		List<MenuElement> elementList = new ArrayList<>();
		for(Map.Entry<Integer, MenuElement> entry : this.elements.entrySet()) {
			elementList.add(entry.getValue());
		}
		return elementList;
	}
}
