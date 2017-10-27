package de.alphahelix.alphalibary.storage.file.serializers;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemStackSerializer implements CSVSerializer<ItemStack> {
    @Override
    public String encode(ItemStack itemStack) {
        StringBuilder stringBuilder = new StringBuilder();

        if (itemStack.getDurability() != 0) {
            stringBuilder.append(itemStack.getType().toString()).append(",").append(itemStack.getDurability()).append(" ");
        } else {
            stringBuilder.append(itemStack.getType().toString());
        }

        stringBuilder.append(itemStack.getAmount()).append(" ");

        if (itemStack.hasItemMeta()) {
            if (itemStack.getItemMeta().hasDisplayName()) {
                stringBuilder.append("name,").append(itemStack.getItemMeta().getDisplayName().replace(Character.toString(ChatColor.COLOR_CHAR), "&").replace(" ", "")).append(" ");
            }

            if (itemStack.getItemMeta().hasLore()) {
                stringBuilder.append("lore,");
                boolean firstLine = true;

                for (String line : itemStack.getItemMeta().getLore()) {
                    if (firstLine) {
                        firstLine = false;
                        stringBuilder.append(line.replace(Character.toString(ChatColor.COLOR_CHAR), "&").replace(" ", ""));
                        continue;
                    }

                    stringBuilder.append("/n").append(line.replace(Character.toString(ChatColor.COLOR_CHAR), "&").replace(" ", ""));
                }

                stringBuilder.append(" ");
            }
        }

        for (Enchantment enchantment : itemStack.getEnchantments().keySet()) {
            try {
                Enchantment.getByName(enchantment.getName());
            } catch (IllegalArgumentException e) {
                continue;
            }

            stringBuilder.append(Enchantment.getByName(enchantment.getName()).getName().replace(" ", "")).append(",").append(itemStack.getEnchantmentLevel(enchantment));
            stringBuilder.append(" ");
        }

        return stringBuilder.toString().trim();
    }

    @Override
    public ItemStack decode(String str) {
        ItemStack itemStack = new ItemStack(Material.STAINED_GLASS_PANE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        String[] parsedString = str.split(" ");

        if (parsedString[0].contains(",")) {
            itemStack.setType(Material.valueOf(parsedString[0].split(",")[0].toUpperCase()));
            itemStack.setDurability(Short.parseShort(parsedString[0].split(",")[1]));
        } else {
            itemStack.setType(Material.valueOf(parsedString[0].split(",")[0].toUpperCase()));
        }

        itemStack.setAmount(Integer.parseInt(parsedString[1]));
        ArrayUtils.remove(parsedString, 0);
        ArrayUtils.remove(parsedString, 0);

        for (String parsedInfo : parsedString) {
            if (!parsedInfo.contains(",")) {
                continue;
            }

            String key = parsedInfo.split(",")[0];
            String value = parsedInfo.split(",")[1];

            switch (key) {
                case "name": {
                    itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', value.replace("_", " ")));
                    continue;
                }

                case "lore": {
                    List<String> lore = new ArrayList<>();

                    for (String line : value.split("/n")) {
                        lore.add(ChatColor.translateAlternateColorCodes('&', line.replace("_", " ")));
                    }

                    itemMeta.setLore(lore);
                    continue;
                }

                case "protection": {
                    itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, Integer.parseInt(value), true);
                    continue;
                }
                case "fire_protection": {
                    itemMeta.addEnchant(Enchantment.PROTECTION_FIRE, Integer.parseInt(value), true);
                    continue;
                }
                case "feather_falling": {
                    itemMeta.addEnchant(Enchantment.PROTECTION_FALL, Integer.parseInt(value), true);
                    continue;
                }
                case "blast_protection": {
                    itemMeta.addEnchant(Enchantment.PROTECTION_EXPLOSIONS, Integer.parseInt(value), true);
                    continue;
                }
                case "projectile_protection": {
                    itemMeta.addEnchant(Enchantment.PROTECTION_PROJECTILE, Integer.parseInt(value), true);
                    continue;
                }
                case "respiration": {
                    itemMeta.addEnchant(Enchantment.OXYGEN, Integer.parseInt(value), true);
                    continue;
                }
                case "aqua_affinity": {
                    itemMeta.addEnchant(Enchantment.WATER_WORKER, Integer.parseInt(value), true);
                    continue;
                }
                case "thorns": {
                    itemMeta.addEnchant(Enchantment.THORNS, Integer.parseInt(value), true);
                    continue;
                }
                case "sharpness": {
                    itemMeta.addEnchant(Enchantment.DAMAGE_ALL, Integer.parseInt(value), true);
                    continue;
                }
                case "smite": {
                    itemMeta.addEnchant(Enchantment.DAMAGE_UNDEAD, Integer.parseInt(value), true);
                    continue;
                }
                case "bane_of_arthropods": {
                    itemMeta.addEnchant(Enchantment.DAMAGE_ARTHROPODS, Integer.parseInt(value), true);
                    continue;
                }
                case "knockback": {
                    itemMeta.addEnchant(Enchantment.KNOCKBACK, Integer.parseInt(value), true);
                    continue;
                }
                case "fire_aspect": {
                    itemMeta.addEnchant(Enchantment.FIRE_ASPECT, Integer.parseInt(value), true);
                    continue;
                }
                case "looting": {
                    itemMeta.addEnchant(Enchantment.LOOT_BONUS_MOBS, Integer.parseInt(value), true);
                    continue;
                }
                case "efficiency": {
                    itemMeta.addEnchant(Enchantment.DIG_SPEED, Integer.parseInt(value), true);
                    continue;
                }
                case "silk_touch": {
                    itemMeta.addEnchant(Enchantment.SILK_TOUCH, Integer.parseInt(value), true);
                    continue;
                }
                case "unbreaking": {
                    itemMeta.addEnchant(Enchantment.DURABILITY, Integer.parseInt(value), true);
                    continue;
                }
                case "power": {
                    itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, Integer.parseInt(value), true);
                    continue;
                }
                case "punch": {
                    itemMeta.addEnchant(Enchantment.ARROW_KNOCKBACK, Integer.parseInt(value), true);
                    continue;
                }
                case "flame": {
                    itemMeta.addEnchant(Enchantment.ARROW_FIRE, Integer.parseInt(value), true);
                    continue;
                }
                case "infinity": {
                    itemMeta.addEnchant(Enchantment.ARROW_INFINITE, Integer.parseInt(value), true);
                    continue;
                }
                case "luck_of_the_sea": {
                    itemMeta.addEnchant(Enchantment.LUCK, Integer.parseInt(value), true);
                    continue;
                }
                case "lured": {
                    itemMeta.addEnchant(Enchantment.LURE, Integer.parseInt(value), true);
                    continue;
                }
                case "fortune": {
                    itemMeta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, Integer.parseInt(value), true);
                    continue;
                }
                case "depth_strider": {
                    itemMeta.addEnchant(Enchantment.DEPTH_STRIDER, Integer.parseInt(value), true);
                }
            }
        }
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }
}
