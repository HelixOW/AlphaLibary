package io.github.alphahelixdev.alpary.annotations;

import io.github.alphahelixdev.alpary.annotations.command.Command;
import io.github.alphahelixdev.alpary.annotations.command.CommandObject;
import io.github.alphahelixdev.alpary.annotations.command.Permission;
import io.github.alphahelixdev.alpary.annotations.command.errorhandlers.ErrorHandler;
import io.github.alphahelixdev.alpary.annotations.entity.Entity;
import io.github.alphahelixdev.alpary.annotations.entity.Location;
import io.github.alphahelixdev.alpary.annotations.item.*;
import io.github.alphahelixdev.alpary.annotations.item.Map;
import io.github.alphahelixdev.alpary.annotations.randoms.Random;
import io.github.alphahelixdev.alpary.commands.SimpleCommand;
import io.github.alphahelixdev.alpary.utilities.entity.EntityBuilder;
import io.github.alphahelixdev.alpary.utilities.item.ItemBuilder;
import io.github.alphahelixdev.alpary.utilities.item.data.*;
import io.github.alphahelixdev.alpary.utils.StringUtil;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.alphahelixdev.helius.reflection.SaveField;
import io.github.alphahelixdev.helius.reflection.SaveMethod;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class AnnotationHandler {
	
	private final List<CommandObject<?>> commandObjects = new ArrayList<>(Arrays.asList(
			(CommandObject<String>) commandString -> commandString,
			(CommandObject<Boolean>) commandString -> {
				List<String> trueBools = Arrays.asList("true", "on", "t");
				List<String> falseBools = Arrays.asList("false", "off", "f");
				
				if(trueBools.contains(commandString))
					return true;
				else if(falseBools.contains(commandString))
					return false;
				else
					throw new IllegalArgumentException("Can't cast " + commandString + " to a boolean!");
			},
			(CommandObject<Integer>) Integer::parseInt,
			(CommandObject<Short>) Short::parseShort,
			(CommandObject<Double>) Double::parseDouble,
			(CommandObject<Float>) Float::parseFloat,
			(CommandObject<Long>) Long::parseLong,
			(CommandObject<Byte>) Byte::parseByte,
			(CommandObject<Character>) commandString -> {
				if(commandString.length() == 1)
					return commandString.charAt(0);
				throw new IllegalArgumentException("Can't cast String with length of " + commandString.length() + " to char");
			},
			(CommandObject<UUID>) UUID::fromString
	));
	
	public void randomizeFields(Object o) {
		Arrays.stream(o.getClass().getDeclaredFields()).filter(field -> field.isAnnotationPresent(Random.class))
				.map(SaveField::new).forEach(randomField -> {
			Random r = randomField.asNormal().getAnnotation(Random.class);
			
			switch(randomField.asNormal().getType().getName().toLowerCase()) {
				case "string":
					randomField.set(o, StringUtil.generateRandomString(r.max()));
					break;
				case "double":
					randomField.set(o, ThreadLocalRandom.current().nextDouble(r.min(), r.max()));
					break;
				case "float":
					randomField.set(o, ThreadLocalRandom.current().nextDouble((r.min() < Float.MIN_VALUE ? Float.MIN_VALUE : r.min()), (r.max() > Float.MAX_VALUE ? Float.MAX_VALUE : r.max())));
					break;
				case "int":
				case "integer":
					randomField.set(o, ThreadLocalRandom.current().nextInt(r.min(), r.max()));
					break;
				case "long":
					randomField.set(o, ThreadLocalRandom.current().nextLong(r.min(), r.max()));
					break;
				case "short":
					randomField.set(o, ThreadLocalRandom.current().nextInt((r.min() < Short.MIN_VALUE ? Short.MIN_VALUE : r.min()), (r.max() > Short.MAX_VALUE ? Short.MAX_VALUE : r.max())));
					break;
				case "boolean":
					randomField.set(o, ThreadLocalRandom.current().nextBoolean());
					break;
				case "uuid":
					randomField.set(o, UUID.randomUUID());
					break;
				case "location":
					randomField.set(o, Utils.locations().getRandomLocation(Utils.locations().getRandomWorld()));
					break;
				case "material":
					randomField.set(o, Material.values()[ThreadLocalRandom.current().nextInt(Material.values().length)]);
			}
		});
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
							potion.duration()[i], PotionEffectType.getByName(potion.type()[i]), potion.amplifier()[i]));
				}
				builder.addItemData(new PotionData(effects.toArray(new SimplePotionEffect[effects.size()])));
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
	
	public void registerCommands(Object o) {
		Arrays.stream(o.getClass().getDeclaredMethods()).filter(method ->
				method.isAnnotationPresent(Command.class)).map(SaveMethod::new).filter(
				saveMethod -> saveMethod.asNormal().getParameterCount() > 0)
				.filter(saveMethod -> saveMethod.asNormal().getParameterTypes()[0].equals(CommandSender.class))
				.forEach(commandMethod -> {
					Command cmd = commandMethod.asNormal().getAnnotation(Command.class);
					Permission permission = commandMethod.asNormal().getAnnotation(Permission.class);
					ErrorHandler errorHandler;
					try {
						errorHandler = (ErrorHandler) cmd.errorHandler().getDeclaredConstructors()[0].newInstance(cmd);
					} catch(ReflectiveOperationException e) {
						e.printStackTrace();
						return;
					}
					
					new SimpleCommand((cmd.name().equals("") ? commandMethod.asNormal().getName() : cmd.name()),
							cmd.description(), cmd.alias()) {
						@Override
						public boolean execute(CommandSender cs, String label, String[] args) {
							if(permission != null) {
								if(!cs.hasPermission(permission.permission())) {
									errorHandler.noPermission(cs, label, args);
									return false;
								}
							}
							
							if(cmd.playersOnly() && !(cs instanceof Player)) {
								errorHandler.notAPlayer(cs, label, args);
								return false;
							}
							
							Object[] parsedArgs = parseArguments(cs, args);
							
							if(parsedArgs.length != commandMethod.asNormal().getParameterCount()) {
								errorHandler.wrongAmountOfArguments(cs, label, args);
								return false;
							}
							
							try {
								commandMethod.asNormal().invoke(o, parseArguments(cs, args));
							} catch(IllegalAccessException | InvocationTargetException e) {
								e.printStackTrace();
								return false;
							} catch(IllegalArgumentException e) {
								errorHandler.wrongArgument(cs, label, args);
								return false;
							}
							return true;
						}
					};
				});
	}
	
	public final Object[] parseArguments(CommandSender cs, String[] args) {
		List<Object> objects = new ArrayList<>(Collections.singletonList(cs));
		
		Arrays.stream(args).forEach(arg -> {
			for(CommandObject<?> o : getCommandObjects())
				try {
					objects.add(o.fromCommandString(arg));
				} catch(Exception e) {
					e.printStackTrace();
				}
		});
		
		return objects.toArray();
	}
	
	public List<CommandObject<?>> getCommandObjects() {
		return this.commandObjects;
	}
}
