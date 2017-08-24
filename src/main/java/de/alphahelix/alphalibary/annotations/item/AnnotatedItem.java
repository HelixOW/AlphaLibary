package de.alphahelix.alphalibary.annotations.item;

import de.alphahelix.alphalibary.annotations.Accessor;
import de.alphahelix.alphalibary.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

import java.lang.reflect.Field;

public class AnnotatedItem {

    private final Object itemClass;
    private final Field itemField;
    private final Item itemAnnotation;

    private String[] enchantments = {};
    private ItemFlag[] itemflags = {};
    private String name = "";
    private Material material = Material.AIR;
    private int amount = 1;
    private short damage = 0;
    private String[] lore = {};
    private boolean unbreakable = false;


    public AnnotatedItem(Object itemClass, Field itemField, Item itemAnnotation) {
        this.itemClass = itemClass;
        this.itemField = itemField;
        this.itemAnnotation = itemAnnotation;

        this.name = itemAnnotation.name();
        this.enchantments = itemAnnotation.enchantments();
        this.itemflags = itemAnnotation.itemflags();
        this.material = itemAnnotation.material();
        this.amount = itemAnnotation.amount();
        this.damage = itemAnnotation.damage();
        this.lore = itemAnnotation.lore();
        this.unbreakable = itemAnnotation.unbreakable();
    }

    public final AnnotatedItem apply() {
        ItemBuilder builder = new ItemBuilder(material);

        for (String serializedEnchantment : enchantments) {
            String[] enchLvL = serializedEnchantment.split(":");

            builder.addEnchantment(Enchantment.getByName(enchLvL[0]), Integer.parseInt(enchLvL[1]));
        }

        builder.addItemFlags(itemflags).setAmount(amount).setDamage(damage).setUnbreakable(unbreakable).setLore(lore);

        try {
            Accessor.access(itemField).set(itemClass, builder.build());
        } catch (ReflectiveOperationException ignored) {
        }

        return this;
    }
}
