package de.alphahelix.alphalibary.annotations.item;

import de.alphahelix.alphalibary.annotations.Accessor;
import de.alphahelix.alphalibary.item.ItemBuilder;
import de.alphahelix.alphalibary.item.SkullItemBuilder;
import de.alphahelix.alphalibary.item.data.ColorData;
import de.alphahelix.alphalibary.item.data.SkullData;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;

class AnnotatedItem {

    private final Object itemClass;
    private final Field itemField;
    private final Item itemAnnotation;

    private final Skull skullAnnotation;
    private final Color colorAnnotation;

    private String[] enchantments = {};
    private ItemFlag[] itemflags = {};
    private String name = "";
    private Material material = Material.AIR;
    private int amount = 1;
    private short damage = 0;
    private String[] lore = {};
    private boolean unbreakable = false;


    AnnotatedItem(Object itemClass, Field itemField, Item itemAnnotation, Skull skullAnnotation, Color colorAnnotation) {
        this.itemClass = itemClass;
        this.itemField = itemField;
        this.itemAnnotation = itemAnnotation;
        this.skullAnnotation = skullAnnotation;
        this.colorAnnotation = colorAnnotation;

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

        for (String serializedEnchantment : enchantments) {
            String[] enchLvL = serializedEnchantment.split(":");

            builder.addEnchantment(Enchantment.getByName(enchLvL[0]), Integer.parseInt(enchLvL[1]));
        }

        builder.addItemFlags(itemflags).setAmount(amount).setDamage(damage).setUnbreakable(unbreakable).setLore(lore);

        if (skullAnnotation != null) {
            if (!skullAnnotation.owner().isEmpty())
                builder.addItemData(new SkullData(skullAnnotation.owner()));
            else {
                ItemStack skull = SkullItemBuilder.getCustomSkull(skullAnnotation.url());

                ItemBuilder builder1 = new ItemBuilder(skull);

                for (String serializedEnchantment : enchantments) {
                    String[] enchLvL = serializedEnchantment.split(":");

                    builder1.addEnchantment(Enchantment.getByName(enchLvL[0]), Integer.parseInt(enchLvL[1]));
                }

                builder1.addItemFlags(itemflags).setAmount(amount).setUnbreakable(unbreakable).setLore(lore);

                try {
                    Accessor.access(itemField).set(itemClass, builder1.build());
                } catch (ReflectiveOperationException ignored) {
                }

                return this;
            }
        }

        if (colorAnnotation != null) {
            int[] rgb = colorAnnotation.rgbColor();

            if (rgb.length == 3)
                builder.addItemData(new ColorData(rgb[0], rgb[1], rgb[2]));
        }

        try {
            Accessor.access(itemField).set(itemClass, builder.build());
        } catch (ReflectiveOperationException ignored) {
        }

        return this;
    }
}
