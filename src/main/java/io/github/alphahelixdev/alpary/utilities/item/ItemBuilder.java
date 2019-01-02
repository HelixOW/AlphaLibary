package io.github.alphahelixdev.alpary.utilities.item;

import io.github.alphahelixdev.alpary.Alpary;
import io.github.alphahelixdev.alpary.utilities.SimpleListener;
import io.github.alphahelixdev.alpary.utilities.item.data.ItemData;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
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

    public ItemBuilder(Material material) {
        this(new ItemStack(material));
    }

    public ItemBuilder(ItemStack is) {
        super(Alpary.getInstance());
        this.setMaterial(is.getType());
        this.setAmount(is.getAmount());
        this.setDamage(is.getDurability());

        if (is.hasItemMeta()) {
            ItemMeta meta = is.getItemMeta();
            if (meta.hasLore()) this.getLore().addAll(meta.getLore());
            if (meta.hasDisplayName()) this.setName(meta.getDisplayName());
            if (meta.hasEnchants()) this.getEnchantments().putAll(meta.getEnchants());

            this.getItemflags().addAll(meta.getItemFlags());
        }
    }

    public ItemBuilder(String material) {
        this(Material.getMaterial(material.toUpperCase()));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getItem() == null) return;
        if (COMPARATOR.compare(e.getItem(), this.build()) == 0)
            this.getTriggerEventConsumer().accept(e);
    }

    /**
     * Get the final {@link ItemStack} with all the attributes you have been adding
     *
     * @return the {@link ItemStack} out of this {@link ItemBuilder}
     */
    public ItemStack build() {
        ItemStack s = new ItemStack(this.getMaterial());
        s.setAmount(this.getAmount());
        s.setDurability(this.getDamage());
        ItemMeta m = s.getItemMeta();

        for (ItemFlag iflag : this.getItemflags())
            m.addItemFlags(iflag);
        m.setDisplayName(this.getName());
        m.setLore(this.getLore());
        s.setItemMeta(m);
        for (Map.Entry<Enchantment, Integer> temp : this.getEnchantments().entrySet())
            s.addUnsafeEnchantment(temp.getKey(), temp.getValue());

        for (ItemData id : this.getItemData())
            try {
                id.applyOn(s);
            } catch (WrongDataException e) {
                e.printStackTrace();
            }

        return s;
    }

    /**
     * Add a new {@link Enchantment} to the {@link ItemStack}
     *
     * @param e     the {@link Enchantment} which the {@link ItemStack} should have then
     * @param level the level of this {@link Enchantment}
     * @return this {@link ItemBuilder}
     */
    public ItemBuilder addEnchantment(Enchantment e, int level) {
        this.getEnchantments().put(e, level);
        return this;
    }

    /**
     * Adds a {@link ItemData} to the {@link ItemStack}
     *
     * @param data the {@link ItemData} to add
     * @return this {@link ItemBuilder}
     */
    public ItemBuilder addItemData(ItemData data) {
        this.getItemData().add(data);
        return this;
    }

    /**
     * Add a gloweffect to the {@link ItemStack}
     *
     * @return this {@link ItemBuilder}
     */
    public ItemBuilder setGlow() {
        this.getEnchantments().put(Enchantment.ARROW_DAMAGE, 1);
        this.getItemflags().add(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    /**
     * Add new {@link ItemFlag}s to the {@link ItemStack}
     *
     * @param flagsToAdd the {@link ItemFlag}s you want to add
     * @return this {@link ItemBuilder}
     */
    public ItemBuilder addItemFlags(ItemFlag... flagsToAdd) {
        Collections.addAll(this.getItemflags(), flagsToAdd);
        return this;
    }

    /**
     * Remove {@link ItemFlag}s from the {@link ItemStack}
     *
     * @param flagsToRemove the {@link ItemFlag}s you want to remove
     * @return this {@link ItemBuilder}
     */
    public ItemBuilder removeItemFlags(ItemFlag... flagsToRemove) {
        this.getItemflags().removeAll(Arrays.asList(flagsToRemove));
        return this;
    }

    /**
     * Get the custom name of this {@link ItemStack}
     *
     * @return the custom name of this {@link ItemStack}
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set a custom name for the {@link ItemStack}
     *
     * @param name The new custom name of the {@link ItemStack}
     * @return this {@link ItemBuilder}
     */
    public ItemBuilder setName(String name) {
        this.name = name.replace("&", "§");
        return this;
    }

    /**
     * Get the {@link Material} of this {@link ItemStack}
     *
     * @return the {@link Material} of this {@link ItemStack}
     */
    public Material getMaterial() {
        return this.material;
    }

    public ItemBuilder setMaterial(Material material) {
        this.material = material;
        return this;
    }

    /**
     * Get the amount of this {@link ItemStack}
     *
     * @return the amount of this {@link ItemStack}
     */
    public int getAmount() {
        return this.amount;
    }

    /**
     * Set a custom amount for the {@link ItemStack}
     *
     * @param amount the new amount
     * @return this {@link ItemBuilder}
     */
    public ItemBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    /**
     * Get the durability of this {@link ItemStack}
     *
     * @return the durability of this {@link ItemStack}
     */
    public short getDamage() {
        return this.damage;
    }

    /**
     * Set the durability for the {@link ItemStack}
     *
     * @param damage the durability of the {@link ItemStack}
     * @return this {@link ItemBuilder}
     */
    public ItemBuilder setDamage(short damage) {
        this.damage = damage;
        return this;
    }

    /**
     * Get the lore of this {@link ItemStack}
     *
     * @return the lore of this {@link ItemStack}
     */
    public List<String> getLore() {
        return this.lore;
    }

    /**
     * Set a custom lore for the {@link ItemStack}
     *
     * @param newLore the new custom lore of the {@link ItemStack}
     * @return this {@link ItemBuilder}
     */
    public ItemBuilder setLore(String... newLore) {
        this.getLore().clear();
        this.getLore().addAll(Arrays.asList(ArrayUtil.replaceInArray("&", "§", newLore)));
        return this;
    }

    /**
     * Get the breakstatus of this {@link ItemStack}
     *
     * @return whether or not this {@link ItemStack} can break
     */
    public boolean isUnbreakable() {
        return this.unbreakable;
    }

    /**
     * Set the Status if a item can break or not
     *
     * @param status if the item can break or not
     * @return this {@link ItemBuilder}
     */
    public ItemBuilder setUnbreakable(boolean status) {
        this.unbreakable = status;
        return this;
    }

    public List<ItemFlag> getItemflags() {
        return this.itemflags;
    }

    public List<ItemData> getItemData() {
        return this.itemData;
    }

    public Map<Enchantment, Integer> getEnchantments() {
        return this.enchantments;
    }

    public Consumer<PlayerInteractEvent> getTriggerEventConsumer() {
        return this.triggerEventConsumer;
    }

    public ItemBuilder setTriggerEventConsumer(Consumer<PlayerInteractEvent> triggerEventConsumer) {
        this.triggerEventConsumer = triggerEventConsumer;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemBuilder builder = (ItemBuilder) o;
        return this.getAmount() == builder.getAmount() &&
                this.getDamage() == builder.getDamage() &&
                this.isUnbreakable() == builder.isUnbreakable() &&
                Objects.equals(this.getName(), builder.getName()) &&
                Objects.equals(this.getItemflags(), builder.getItemflags()) &&
                Objects.equals(this.getItemData(), builder.getItemData()) &&
                Objects.equals(this.getLore(), builder.getLore()) &&
                Objects.equals(this.getEnchantments(), builder.getEnchantments()) &&
                Objects.equals(this.getTriggerEventConsumer(), builder.getTriggerEventConsumer()) &&
                this.getMaterial() == builder.getMaterial();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getName(), this.getAmount(), this.getDamage(), this.isUnbreakable(), this.getItemflags(), this.getItemData(), this.getLore(), this.getEnchantments(), this.getTriggerEventConsumer(), this.getMaterial());
    }


    @Override
    public String toString() {
        return "ItemBuilder{" +
                "                            name='" + this.name + '\'' +
                ",                             amount=" + this.amount +
                ",                             damage=" + this.damage +
                ",                             unbreakable=" + this.unbreakable +
                ",                             itemflags=" + this.itemflags +
                ",                             itemData=" + this.itemData +
                ",                             lore=" + this.lore +
                ",                             enchantments=" + this.enchantments +
                ",                             triggerEventConsumer=" + this.triggerEventConsumer +
                ",                             material=" + this.material +
                '}';
    }
}
