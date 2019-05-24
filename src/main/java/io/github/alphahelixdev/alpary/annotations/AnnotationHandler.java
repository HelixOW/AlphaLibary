package io.github.alphahelixdev.alpary.annotations;

import io.github.alphahelixdev.alpary.Alpary;
import io.github.alphahelixdev.alpary.annotations.entity.Entity;
import io.github.alphahelixdev.alpary.annotations.entity.Location;
import io.github.alphahelixdev.alpary.annotations.item.*;
import io.github.alphahelixdev.alpary.utilities.entity.EntityBuilder;
import io.github.alphahelixdev.alpary.utilities.item.ItemBuilder;
import io.github.alphahelixdev.alpary.utilities.item.data.*;
import io.github.whoisalphahelix.helix.Helix;
import io.github.whoisalphahelix.helix.reflection.SaveField;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;

@ToString
public class AnnotationHandler extends io.github.whoisalphahelix.helix.handlers.AnnotationHandler {
	
	public AnnotationHandler(Helix helix) {
		super(helix);
	}
	
	public void setItemFields(Object o) {
		Arrays.stream(o.getClass().getDeclaredFields()).filter(field -> field.isAnnotationPresent(Item.class))
				.map(SaveField::new).forEach(itemField -> {
			Item item = itemField.asNormal().getAnnotation(Item.class);
			ItemBuilder builder = new ItemBuilder(new ItemStack(item.material(), item.amount()));
			
			if(item.itemflags().length != 0)
				builder.addItemFlags(item.itemflags());
			
			if(!item.name().equals(""))
				builder.setName(item.name());
			
			if(item.damage() != 0)
				builder.setDamage(item.damage());
			
			if(item.lore().length != 0)
				builder.setLore(item.lore());
			
			builder.setUnbreakable(item.unbreakable());
			
			if(itemField.asNormal().isAnnotationPresent(ItemColor.class)) {
				ItemColor itemColor = itemField.asNormal().getAnnotation(ItemColor.class);
				builder.addItemData(new ColorData(itemColor.red(), itemColor.blue(), itemColor.green()));
			}
			
			if(itemField.asNormal().isAnnotationPresent(ItemEnchantment.class)) {
				ItemEnchantment itemEnchantment = itemField.asNormal().getAnnotation(ItemEnchantment.class);
				builder.addEnchantment(Enchantment.getByKey(NamespacedKey.minecraft(itemEnchantment.name())), itemEnchantment.level());
			}
			
			if(itemField.asNormal().isAnnotationPresent(Map.class)) {
				Map map = itemField.asNormal().getAnnotation(Map.class);
				builder.addItemData(new MapData(Color.fromRGB(map.red(), map.green(), map.blue()), map.locationName()));
			}
			
			if(itemField.asNormal().isAnnotationPresent(Potion.class)) {
				Potion potion = itemField.asNormal().getAnnotation(Potion.class);
				java.util.List<SimplePotionEffect> effects = new ArrayList<>();
				for(int i = 0; i < potion.type().length - 1; i++) {
					effects.add(new SimplePotionEffect(
							potion.duration()[i], potion.amplifier()[i], PotionEffectType.getByName(potion.type()[i])
					));
				}
				builder.addItemData(new PotionData(effects.toArray(new SimplePotionEffect[0])));
			}
			
			if(itemField.asNormal().isAnnotationPresent(Skull.class)) {
				Skull skull = itemField.asNormal().getAnnotation(Skull.class);
				builder.addItemData(new SkullData(skull.owner()));
			}
			
			itemField.set(o, builder.build());
		});
	}
	
	public void setEntityFields(Object o) {
		Arrays.stream(o.getClass().getDeclaredFields()).filter(field -> field.isAnnotationPresent(Entity.class))
				.filter(field -> field.isAnnotationPresent(Location.class)).map(SaveField::new).forEach(entityField -> {
			Entity entity = entityField.asNormal().getAnnotation(Entity.class);
			Location location = entityField.asNormal().getAnnotation(Location.class);
			EntityBuilder builder = new EntityBuilder(entity.typeClazz());
			org.bukkit.Location loc = new org.bukkit.Location(Bukkit.getWorld(location.world()), location.x(),
					location.y(), location.z());
			
			if(!entity.name().equals(""))
				builder.setName(entity.name());
			
			builder.setHealth(entity.health())
					.setMove(entity.moveable())
					.setItemPickup(entity.pickUpItem())
					.setGlowing(entity.glowing())
					.setGravity(entity.gravity())
					.setInvincible(entity.invincible())
					.setAgeLock(entity.ageLock())
					.setAge(entity.age());
			
			entityField.set(o, builder.spawn(loc));
		});
	}
	
	public void registerListeners() {
		Alpary.getInstance().reflections().getTypesAnnotatedWith(BukkitListener.class).stream().filter(Listener.class::isAssignableFrom)
				.filter(aClass -> {
					try {
						return aClass.getDeclaredConstructor() != null;
					} catch(NoSuchMethodException e) {
						e.printStackTrace();
						return false;
					}
				}).forEach(listenerClass -> {
			try {
				Bukkit.getPluginManager().registerEvents((Listener) listenerClass.getDeclaredConstructor()
						.newInstance(), Alpary.getInstance());
			} catch(ReflectiveOperationException e) {
				e.printStackTrace();
			}
		});
	}
}
