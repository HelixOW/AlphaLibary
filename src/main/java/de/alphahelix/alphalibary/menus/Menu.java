package de.alphahelix.alphalibary.menus;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Menu {

    private final String title;
    private final int size;
    private HashMap<Integer, MenuElement> elements = new HashMap<>();
    private MenuManager menuManager;

    public Menu(String title, int size) {
        this.title = title;
        this.size = size;
        this.elements = new HashMap<>();

        menuManager = new MenuManager();
    }

    public String getTitle() {
        return title;
    }

    public int getSize() {
        return size;
    }

    public MenuManager getMenuManager() {
        return menuManager;
    }

    public List<MenuElement> getElements() {
        List<MenuElement> elementList = new ArrayList<>();
        for (Map.Entry<Integer, MenuElement> entry : this.elements.entrySet()) {
            elementList.add(entry.getValue());
        }
        return elementList;
    }

    public MenuElement getElement(int slot) {
        if (elements.containsKey(slot))
            return elements.get(slot);
        return null;
    }

    public Menu addElement(int slot, MenuElement element) {
        elements.put(slot, element);
        return this;
    }

    public void open(Player p) {
        Inventory inv = Bukkit.createInventory(p, getSize(), getTitle());

        for (Map.Entry<Integer, MenuElement> element : this.elements.entrySet()) {
            inv.setItem(element.getKey(), element.getValue().getIcon(p));
        }

        getMenuManager().setMenuOpened(p, this);

        p.openInventory(inv);
    }
}
