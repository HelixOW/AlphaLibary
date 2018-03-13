package de.alphahelix.alphalibary.inventories.inventory;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AdvancedInventory implements Cloneable {
	
	private static final Map<String, Map<Integer, AdvancedInventory>> ALL_PAGES = new HashMap<>();
	
	private final String id, title;
	private final int rows;
	private int page;
	private Map<Integer, ClickItem> items = new HashMap<>();
	
	private Inventory bukkitInventory;
	
	private AdvancedInventory(String id, String title, int rows, int page, Inventory bukkitInventory, Map<Integer, ClickItem> items) {
		this(id, title, rows);
		this.page = page;
		this.bukkitInventory = bukkitInventory;
		this.items = items;
	}
	
	public AdvancedInventory(String id, String title, int rows) {
		this.title = title;
		this.id = id;
		this.rows = rows;
		bukkitInventory = Bukkit.createInventory(new IDHolder(id), rows * 9, title);
		
		if(ALL_PAGES.containsKey(id)) {
			Map<Integer, AdvancedInventory> pages = ALL_PAGES.get(id);
			
			this.page = pages.keySet().size() + 1;
			
			pages.put(page, this);
			ALL_PAGES.put(id, pages);
		} else {
			this.page = 1;
			
			ALL_PAGES.put(id, Maps.newHashMap(ImmutableMap.<Integer, AdvancedInventory>builder()
					.put(page, this)
					.build()));
		}
	}
	
	public AdvancedInventory addPage() {
		return new AdvancedInventory(this.id, this.title, this.rows);
	}
	
	public String getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public int getRows() {
		return rows;
	}
	
	public int getPage() {
		return page;
	}
	
	public AdvancedInventory firstPage() {
		return ALL_PAGES.get(id).get(1);
	}
	
	public boolean isFirst() {
		return this.page == 1;
	}
	
	public AdvancedInventory lastPage() {
		return ALL_PAGES.get(id).get(ALL_PAGES.get(id).keySet().size());
	}
	
	public boolean isLast() {
		return this.page == ALL_PAGES.get(id).keySet().size();
	}
	
	public AdvancedInventory nextPage() {
		if(hasNextPage())
			return ALL_PAGES.get(id).get(page + 1);
		return ALL_PAGES.get(id).get(page);
	}
	
	public boolean hasNextPage() {
		return ALL_PAGES.get(id).containsKey(page + 1);
	}
	
	public AdvancedInventory previousPage() {
		if(hasPreviousPage())
			return ALL_PAGES.get(id).get(page - 1);
		return ALL_PAGES.get(id).get(page);
	}
	
	public boolean hasPreviousPage() {
		return ALL_PAGES.get(id).containsKey(page - 1);
	}

    /*
         0 1 2 3 4 5 6 7 8
      1 #- - - - - - - - -#
      2 #- - - - - - - - -#
      3 #- - - - - - - - -#
     */
	
	public AdvancedInventory fillFrame(ClickItem item) {
		fillLeftRight(item);
		fillTopBottom(item);
		return this;
	}
	
	public AdvancedInventory fillLeftRight(ClickItem item) {
		fillColumn(1, item);
		fillColumn(9, item);
		return this;
	}
	
	public AdvancedInventory fillTopBottom(ClickItem item) {
		fillRow(1, item);
		fillRow(rows, item);
		return this;
	}
	
	public AdvancedInventory fillColumn(int column, ClickItem item) {
		return fillColumnToRow(column, -1, item);
	}
	
	public AdvancedInventory fillRow(int row, ClickItem item) {
		for(int slot = ((row - 1) * 9); slot < ((row - 1) * 9) + 8; slot++) {
			setItem(slot, item);
		}
		return this;
	}
	
	public AdvancedInventory fillColumnToRow(int column, int row, ClickItem item) {
		for(int r = 0; r <= rows - 1; r++) {
			if(r == row) break;
			setItem(r * 9 + (column - 1), item);
		}
		return this;
	}
	
	public AdvancedInventory setItem(int slot, ClickItem item) {
		bukkitInventory.setItem(slot, item.getItemStack());
		items.put(slot, item);
		return this;
	}

    /*
    slot = row * 9 + column
    row = slot / 9 + column
    column = slot / row - 9
     */
	
	public AdvancedInventory fillRowToColumn(int row, int column, ClickItem item) {
		for(int slot = ((row - 1) * 9); slot < ((row - 1) * 9) + 8; slot++) {
			int c = (slot / row) - 8;
			if(c == column) break;
			setItem(slot, item);
		}
		return this;
	}
	
	public AdvancedInventory fillRectangle(int startRow, int row, int startColumn, int column, ClickItem item) {
		for(int r = 0; r <= rows - 1; r++) {
			if(!(r >= startRow && r <= row)) continue;
			for(int c = 0; c <= 8; c++) {
				if(!(c >= startColumn && c <= column)) continue;
				setItem(r, c, item);
			}
		}
		return this;
	}
	
	public AdvancedInventory setItem(int row, int column, ClickItem item) {
		return setItem((row - 1) * 9 + (column - 1), item);
	}
	
	public AdvancedInventory fill(ClickItem item) {
		for(int slot = 0; slot <= bukkitInventory.getSize() - 1; slot++) {
			setItem(slot, item);
		}
		return this;
	}
	
	public Optional<ClickItem> getItem(int row, int column) {
		return getItem(row * column);
	}
	
	public Optional<ClickItem> getItem(int slot) {
		return Optional.ofNullable(items.get(slot));
	}
	
	public Inventory getBukkitInventory() {
		return bukkitInventory;
	}
	
	public AdvancedInventory open(Player player) {
		InventoryManager.getInventoryManager().openInventory(player, this);
		player.openInventory(bukkitInventory);
		return this;
	}
	
	@Override
	public AdvancedInventory clone() {
		return new AdvancedInventory(id, title, rows, page, bukkitInventory, items);
	}
}
