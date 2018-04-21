package de.alphahelix.alphalibary.alpest;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

/**
 * Used to simulate a {@link PlayerInventory} to the server
 *
 * @author AlphaHelix
 * @version 1.0
 * @see VirtualPlayer
 * @see PlayerInventory
 * @since 1.9.2.1
 */
public class VirtualPlayerInventory implements PlayerInventory {
	
	private final Inventory inv;
	
	private ItemStack[] armorContents, extraContents;
	private ItemStack helmet, chestplate, leggins, boots, mainHand, offHand;
	private int heldItemSlot;
	
	public VirtualPlayerInventory(VirtualPlayer virtualPlayer, ItemStack[] armorContents, ItemStack[] extraContents, ItemStack helmet, ItemStack chestplate, ItemStack leggins, ItemStack boots, ItemStack mainHand, ItemStack offHand, int heldItemSlot) {
		this.inv = Bukkit.createInventory(virtualPlayer, InventoryType.PLAYER);
		this.armorContents = armorContents;
		this.extraContents = extraContents;
		this.helmet = helmet;
		this.chestplate = chestplate;
		this.leggins = leggins;
		this.boots = boots;
		this.mainHand = mainHand;
		this.offHand = offHand;
		this.heldItemSlot = heldItemSlot;
	}
	
	@Override
	public ItemStack[] getArmorContents() {
		return new ItemStack[]{helmet, chestplate, leggins, boots};
	}
	
	@Override
	public ItemStack[] getExtraContents() {
		return new ItemStack[0];
	}
	
	@Override
	public ItemStack getHelmet() {
		return helmet;
	}
	
	@Override
	public ItemStack getChestplate() {
		return chestplate;
	}
	
	@Override
	public ItemStack getLeggings() {
		return leggins;
	}
	
	@Override
	public ItemStack getBoots() {
		return boots;
	}
	
	@Override
	public void setItem(int index, ItemStack item) {
		inv.setItem(index, item);
	}
	
	@Override
	public void setBoots(ItemStack boots) {
		this.boots = boots;
	}
	
	@Override
	public ItemStack getItemInMainHand() {
		return mainHand;
	}
	
	@Override
	public void setItemInMainHand(ItemStack item) {
		this.mainHand = item;
	}
	
	@Override
	public ItemStack getItemInOffHand() {
		return offHand;
	}
	
	@Override
	public void setItemInOffHand(ItemStack item) {
		this.offHand = item;
	}
	
	@Override
	public ItemStack getItemInHand() {
		return mainHand;
	}
	
	@Override
	public void setItemInHand(ItemStack stack) {
		this.mainHand = stack;
	}
	
	@Override
	public int getHeldItemSlot() {
		return heldItemSlot;
	}
	
	@Override
	public void setHeldItemSlot(int slot) {
		this.heldItemSlot = slot;
	}
	
	@Override
	public int clear(int id, int data) {
		return 0;
	}
	
	@Override
	public HumanEntity getHolder() {
		return (HumanEntity) inv.getHolder();
	}
	
	@Override
	public void setLeggings(ItemStack leggings) {
		this.leggins = leggings;
	}
	
	@Override
	public void setChestplate(ItemStack chestplate) {
		this.chestplate = chestplate;
	}
	
	@Override
	public void setHelmet(ItemStack helmet) {
		this.helmet = helmet;
	}
	
	@Override
	public void setExtraContents(ItemStack[] items) {
		this.extraContents = items;
	}
	
	@Override
	public void setArmorContents(ItemStack[] items) {
		this.armorContents = items;
	}
	
	@Override
	public int getSize() {
		return inv.getSize();
	}
	
	@Override
	public int getMaxStackSize() {
		return inv.getMaxStackSize();
	}
	
	@Override
	public void setMaxStackSize(int size) {
		inv.setMaxStackSize(size);
	}
	
	@Override
	public String getName() {
		return inv.getName();
	}
	
	@Override
	public ItemStack getItem(int index) {
		return inv.getItem(index);
	}
	
	@Override
	public HashMap<Integer, ItemStack> addItem(ItemStack... items) throws IllegalArgumentException {
		return inv.addItem(items);
	}
	
	@Override
	public HashMap<Integer, ItemStack> removeItem(ItemStack... items) throws IllegalArgumentException {
		return inv.removeItem(items);
	}
	
	@Override
	public ItemStack[] getContents() {
		return inv.getContents();
	}
	
	@Override
	public void setContents(ItemStack[] items) throws IllegalArgumentException {
		inv.setContents(items);
	}
	
	@Override
	public ItemStack[] getStorageContents() {
		return inv.getStorageContents();
	}
	
	@Override
	public void setStorageContents(ItemStack[] items) throws IllegalArgumentException {
		inv.setStorageContents(items);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean contains(int materialId) {
		return inv.contains(materialId);
	}
	
	@Override
	public boolean contains(Material material) throws IllegalArgumentException {
		return inv.contains(material);
	}
	
	@Override
	public boolean contains(ItemStack item) {
		return inv.contains(item);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean contains(int materialId, int amount) {
		return inv.contains(materialId, amount);
	}
	
	@Override
	public boolean contains(Material material, int amount) throws IllegalArgumentException {
		return inv.contains(material, amount);
	}
	
	@Override
	public boolean contains(ItemStack item, int amount) {
		return inv.contains(item, amount);
	}
	
	@Override
	public boolean containsAtLeast(ItemStack item, int amount) {
		return inv.containsAtLeast(item, amount);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public HashMap<Integer, ? extends ItemStack> all(int materialId) {
		return inv.all(materialId);
	}
	
	@Override
	public HashMap<Integer, ? extends ItemStack> all(Material material) throws IllegalArgumentException {
		return inv.all(material);
	}
	
	@Override
	public HashMap<Integer, ? extends ItemStack> all(ItemStack item) {
		return inv.all(item);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public int first(int materialId) {
		return inv.first(materialId);
	}
	
	@Override
	public int first(Material material) throws IllegalArgumentException {
		return inv.first(material);
	}
	
	@Override
	public int first(ItemStack item) {
		return inv.first(item);
	}
	
	@Override
	public int firstEmpty() {
		return inv.firstEmpty();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void remove(int materialId) {
		inv.remove(materialId);
	}
	
	@Override
	public void remove(Material material) throws IllegalArgumentException {
		inv.remove(material);
	}
	
	@Override
	public void remove(ItemStack item) {
		inv.remove(item);
	}
	
	@Override
	public void clear(int index) {
		inv.clear(index);
	}
	
	@Override
	public void clear() {
		inv.clear();
	}
	
	@Override
	public List<HumanEntity> getViewers() {
		return inv.getViewers();
	}
	
	@Override
	public String getTitle() {
		return inv.getTitle();
	}
	
	@Override
	public InventoryType getType() {
		return inv.getType();
	}
	
	@Override
	public ListIterator<ItemStack> iterator() {
		return inv.iterator();
	}
	
	@Override
	public ListIterator<ItemStack> iterator(int index) {
		return inv.iterator(index);
	}
	
	@Override
	public Location getLocation() {
		return inv.getLocation();
	}
}
