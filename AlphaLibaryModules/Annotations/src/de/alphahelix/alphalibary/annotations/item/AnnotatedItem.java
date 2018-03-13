package de.alphahelix.alphalibary.annotations.item;

import de.alphahelix.alphalibary.annotations.Accessor;
import de.alphahelix.alphalibary.inventories.item.ItemBuilder;
import de.alphahelix.alphalibary.inventories.item.SkullItemBuilder;
import de.alphahelix.alphalibary.inventories.item.data.*;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Field;
import java.util.*;

class AnnotatedItem {
	
	private final Object itemClass;
	private final Field itemField;
	
	private final Skull skullAnnotation;
	private final Color colorAnnotation;
	private final Banner bannerAnnotation;
	private final Map mapAnnotation;
	private final Potion potionAnnotation;
	private final SpawnEgg spawnEggAnnotation;
	
	private final String name;
	private final String[] enchantments;
	private final ItemFlag[] itemflags;
	private final Material material;
	private final int amount;
	private final short damage;
	private final String[] lore;
	private final boolean unbreakable;
	
	
	AnnotatedItem(Object itemClass, Field itemField, Item itemAnnotation, Skull skullAnnotation, Color colorAnnotation, Banner bannerAnnotation, Map mapAnnotation, Potion potionAnnotation, SpawnEgg spawnEggAnnotation) {
		this.itemClass = itemClass;
		this.itemField = itemField;
		this.skullAnnotation = skullAnnotation;
		this.colorAnnotation = colorAnnotation;
		this.bannerAnnotation = bannerAnnotation;
		this.mapAnnotation = mapAnnotation;
		this.potionAnnotation = potionAnnotation;
		this.spawnEggAnnotation = spawnEggAnnotation;
		
		this.name = itemAnnotation.name();
		this.enchantments = itemAnnotation.enchantments();
		this.itemflags = itemAnnotation.itemflags();
		this.material = itemAnnotation.material();
		this.amount = itemAnnotation.amount();
		this.damage = itemAnnotation.damage();
		this.lore = itemAnnotation.lore();
		this.unbreakable = itemAnnotation.unbreakable();
	}
	
	final AnnotatedItem apply() {
		ItemBuilder builder = new ItemBuilder(material);
		
		for(String serializedEnchantment : enchantments) {
			String[] enchLvL = serializedEnchantment.split(":");
			
			builder.addEnchantment(Enchantment.getByName(enchLvL[0]), Integer.parseInt(enchLvL[1]));
		}
		
		builder.addItemFlags(itemflags).setAmount(amount).setName(name).setDamage(damage).setUnbreakable(unbreakable).setLore(lore);
		
		if(skullAnnotation != null) {
			if(!skullAnnotation.owner().isEmpty())
				builder.addItemData(new SkullData(skullAnnotation.owner()));
			else {
				ItemStack skull = SkullItemBuilder.getCustomSkull(skullAnnotation.url());
				
				ItemBuilder builder1 = new ItemBuilder(skull);
				
				for(String serializedEnchantment : enchantments) {
					String[] enchLvL = serializedEnchantment.split(":");
					
					builder1.addEnchantment(Enchantment.getByName(enchLvL[0]), Integer.parseInt(enchLvL[1]));
				}
				
				builder1.addItemFlags(itemflags).setAmount(amount).setUnbreakable(unbreakable).setLore(lore);
				
				try {
					Accessor.access(itemField).set(itemClass, builder1.build());
				} catch(ReflectiveOperationException ignored) {
				}
				
				return this;
			}
		}
		
		if(colorAnnotation != null) {
			int[] rgb = colorAnnotation.rgbColor();
			
			if(rgb.length == 3)
				builder.addItemData(new ColorData(rgb[0], rgb[1], rgb[2]));
		}
		
		if(bannerAnnotation != null) {
			DyeColor[] colors = bannerAnnotation.color();
			PatternType[] types = bannerAnnotation.type();
			Set<Pattern> patterns = new HashSet<>();
			
			if(colors.length == types.length) {
				for(int i = 0; i < colors.length; i++) {
					patterns.add(new Pattern(colors[i], types[i]));
				}
				
				builder.addItemData(new BannerData(patterns.toArray(new Pattern[patterns.size()])));
			}
		}
		
		if(mapAnnotation != null) {
			org.bukkit.Color c = org.bukkit.Color.fromRGB(mapAnnotation.color()[0], mapAnnotation.color()[1], mapAnnotation.color()[2]);
			String locationName = mapAnnotation.locationName();
			boolean scaling = mapAnnotation.scaling();
			
			builder.addItemData(new MapData(c, locationName).setScaling(scaling));
		}
		
		if(potionAnnotation != null) {
			int[] dura = potionAnnotation.duration();
			int[] amp = potionAnnotation.amplifier();
			String[] types = potionAnnotation.type();
			
			List<PotionEffectType> effectTypes = new LinkedList<>();
			Set<SimplePotionEffect> effectSet = new HashSet<>();
			
			if((dura.length == amp.length) && (dura.length == types.length)) {
				Arrays.stream(types).forEach(s -> effectTypes.add(PotionEffectType.getByName(s)));
				
				for(int i = 0; i < dura.length; i++) {
					effectSet.add(new SimplePotionEffect(dura[i], effectTypes.get(i), amp[i]));
				}
				
				builder.addItemData(new PotionData(effectSet.toArray(new SimplePotionEffect[effectSet.size()])));
			}
		}
		
		if(spawnEggAnnotation != null)
			builder.addItemData(new SpawnEggData(spawnEggAnnotation.spawned()));
		
		try {
			Accessor.access(itemField).set(itemClass, builder.build());
		} catch(ReflectiveOperationException ignored) {
		}
		
		return this;
	}
}
