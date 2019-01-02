package io.github.alphahelixdev.alpary.utilities.item;

import io.github.alphahelixdev.alpary.Alpary;
import io.github.alphahelixdev.alpary.utilities.SimpleListener;
import io.github.alphahelixdev.alpary.utilities.item.data.ItemData;
import io.github.alphahelixdev.alpary.utilities.item.data.WrongDataException;
import io.github.alphahelixdev.helius.utils.ArrayUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;


public class ItemBuilder extends SimpleListener implements Serializable {
	
	private static final ItemComparator COMPARATOR = new ItemComparator();
	private final List<ItemFlag> itemflags = new ArrayList<>();
	private final List<ItemData> itemData = new ArrayList<>();
	private final List<String> lore = new ArrayList<>();
	private final Map<Enchantment, Integer> enchantments = new HashMap<>();
	
	private Material material;
	private int amount, damage;
	private String name = "";
	private boolean unbreakable = false;
	private Consumer<PlayerInteractEvent> triggerEventConsumer = e -> {
	};
	
	public ItemBuilder(String material) {
		this(Material.getMaterial(material.toUpperCase()));
	}
	
	public ItemBuilder(Material material) {
		this(new ItemStack(material));
	}
	
	public ItemBuilder(ItemStack is) {
		super(Alpary.getInstance());
		this.setMaterial(is.getType());
		this.setAmount(is.getAmount());
		
		if(is.hasItemMeta()) {
			ItemMeta meta = is.getItemMeta();
			if(meta.hasLore()) this.getLore().addAll(meta.getLore());
			if(meta.hasDisplayName()) this.setName(meta.getDisplayName());
			if(meta.hasEnchants()) this.getEnchantments().putAll(meta.getEnchants());
			if(meta instanceof Damageable) this.setDamage(((Damageable) meta).getDamage());
			
			this.getItemflags().addAll(meta.getItemFlags());
		}
	}
	
	public List<String> getLore() {
		return this.lore;
	}
	
	public ItemBuilder setLore(String... newLore) {
		this.getLore().clear();
		this.getLore().addAll(Arrays.asList(ArrayUtil.replaceInArray("&", "§", newLore)));
		return this;
	}
	
	public Map<Enchantment, Integer> getEnchantments() {
		return this.enchantments;
	}
	
	public List<ItemFlag> getItemflags() {
		return this.itemflags;
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if(e.getItem() == null) return;
		if(COMPARATOR.compare(e.getItem(), this.build()) == 0)
			this.getTriggerEventConsumer().accept(e);
	}
	
	public ItemStack build() {
		ItemStack s = new ItemStack(this.getMaterial());
		s.setAmount(this.getAmount());
		ItemMeta m = s.getItemMeta();
		
		this.getItemflags().forEach(m::addItemFlags);
		
		m.setDisplayName(this.getName());
		m.setLore(this.getLore());
		s.setItemMeta(m);
		
		if(m instanceof Damageable)
			((Damageable) m).setDamage(this.getDamage());
		
		this.enchantments.forEach(s::addUnsafeEnchantment);
		
		this.getItemData().forEach(id -> {
			try {
				id.applyOn(s);
			} catch(WrongDataException e) {
				e.printStackTrace();
			}
		});
		
		return s;
	}
	
	public Consumer<PlayerInteractEvent> getTriggerEventConsumer() {
		return this.triggerEventConsumer;
	}
	
	public Material getMaterial() {
		return this.material;
	}
	
	public ItemBuilder setMaterial(Material material) {
		this.material = material;
		return this;
	}
	
	public int getAmount() {
		return this.amount;
	}
	
	public String getName() {
		return this.name;
	}
	
	public ItemBuilder setName(String name) {
		this.name = name.replace("&", "§");
		return this;
	}
	
	public int getDamage() {
		return this.damage;
	}
	
	public ItemBuilder setDamage(int damage) {
		this.damage = damage;
		return this;
	}
	
	public List<ItemData> getItemData() {
		return this.itemData;
	}
	
	public ItemBuilder setAmount(int amount) {
		this.amount = amount;
		return this;
	}
	
	public ItemBuilder setTriggerEventConsumer(Consumer<PlayerInteractEvent> triggerEventConsumer) {
		this.triggerEventConsumer = triggerEventConsumer;
		return this;
	}
	
	public ItemBuilder addEnchantment(Enchantment e, int level) {
		this.getEnchantments().put(e, level);
		return this;
	}
	
	public ItemBuilder addItemData(ItemData data) {
		this.getItemData().add(data);
		return this;
	}
	
	public ItemBuilder setGlow() {
		this.getEnchantments().put(Enchantment.ARROW_DAMAGE, 1);
		this.getItemflags().add(ItemFlag.HIDE_ENCHANTS);
		return this;
	}
	
	public ItemBuilder addItemFlags(ItemFlag... flagsToAdd) {
		Collections.addAll(this.getItemflags(), flagsToAdd);
		return this;
	}
	
	public ItemBuilder removeItemFlags(ItemFlag... flagsToRemove) {
		this.getItemflags().removeAll(Arrays.asList(flagsToRemove));
		return this;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.getItemflags(), this.getItemData(), this.getLore(), this.getEnchantments(), this.getMaterial(), this.getAmount(), this.getDamage(), this.getName(), this.isUnbreakable(), this.getTriggerEventConsumer());
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		ItemBuilder that = (ItemBuilder) o;
		return this.getAmount() == that.getAmount() &&
				this.getDamage() == that.getDamage() &&
				this.isUnbreakable() == that.isUnbreakable() &&
				Objects.equals(this.getItemflags(), that.getItemflags()) &&
				Objects.equals(this.getItemData(), that.getItemData()) &&
				Objects.equals(this.getLore(), that.getLore()) &&
				Objects.equals(this.getEnchantments(), that.getEnchantments()) &&
				this.getMaterial() == that.getMaterial() &&
				Objects.equals(this.getName(), that.getName()) &&
				Objects.equals(this.getTriggerEventConsumer(), that.getTriggerEventConsumer());
	}
	
	public boolean isUnbreakable() {
		return this.unbreakable;
	}
	
	public ItemBuilder setUnbreakable(boolean status) {
		this.unbreakable = status;
		return this;
	}
	
	@Override
	public String toString() {
		return "ItemBuilder{" +
				"itemflags=" + itemflags +
				", itemData=" + itemData +
				", lore=" + lore +
				", enchantments=" + enchantments +
				", material=" + material +
				", amount=" + amount +
				", damage=" + damage +
				", name='" + name + '\'' +
				", unbreakable=" + unbreakable +
				", triggerEventConsumer=" + triggerEventConsumer +
				"} " + super.toString();
	}
}
