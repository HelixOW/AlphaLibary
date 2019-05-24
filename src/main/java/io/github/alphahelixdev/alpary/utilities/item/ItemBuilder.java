package io.github.alphahelixdev.alpary.utilities.item;

import io.github.alphahelixdev.alpary.Alpary;
import io.github.alphahelixdev.alpary.utilities.SimpleListener;
import io.github.alphahelixdev.alpary.utilities.item.data.ItemData;
import io.github.alphahelixdev.alpary.utilities.item.data.WrongDataException;
import io.github.alphahelixdev.helius.utils.ArrayUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
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

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
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
	
	public ItemBuilder setLore(String... newLore) {
		this.getLore().clear();
		this.getLore().addAll(Arrays.asList(ArrayUtil.replaceInArray("&", "§", newLore)));
		return this;
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
	
	public ItemBuilder setMaterial(Material material) {
		this.material = material;
		return this;
	}
	
	public ItemBuilder setName(String name) {
		this.name = name.replace("&", "§");
		return this;
	}
	
	public ItemBuilder setDamage(int damage) {
		this.damage = damage;
		return this;
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
	
	public ItemBuilder setUnbreakable(boolean status) {
		this.unbreakable = status;
		return this;
	}
	
}
