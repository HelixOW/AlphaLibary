/*
 *     Copyright (C) <2016>  <AlphaHelixDev>
 *
 *     This program is free software: you can redistribute it under the
 *     terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.alphahelix.alphalibary.item;

import de.alphahelix.alphalibary.item.data.ItemData;
import de.alphahelix.alphalibary.item.data.WrongDataException;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.Map.Entry;

public class ItemBuilder {

    private Map<Enchantment, Integer> enchantments = new HashMap<>();
    private ArrayList<ItemFlag> itemflags = new ArrayList<>();
    private ArrayList<ItemData> itemData = new ArrayList<>();
    private String name = "";
    private Material material = Material.AIR;
    private int amount = 1;
    private short damage = 0;
    private List<String> lore = new ArrayList<>();
    private boolean unbreakable = false;

    /**
     * Create a new {@link ItemStack} with the given {@link Material}
     *
     * @param material the {@link Material} of the {@link ItemStack}
     */
    public ItemBuilder(Material material) {
        this.material = material;
    }

    public ItemBuilder(ItemStack is) {
        material = is.getType();
        amount = is.getAmount();
        damage = is.getDurability();
        if (is.hasItemMeta()) {
            if (is.getItemMeta().hasLore()) lore = is.getItemMeta().getLore();
            if (is.getItemMeta().hasDisplayName()) name = is.getItemMeta().getDisplayName();
            if (is.getItemMeta().hasEnchants()) enchantments = is.getEnchantments();
            itemflags.addAll(is.getItemMeta().getItemFlags());
        }
    }

    public ItemBuilder(String material) {
        this.material = Material.getMaterial(material.toUpperCase());
    }

    /**
     * Add a new {@link Enchantment} to the {@link ItemStack}
     *
     * @param e     the {@link Enchantment} which the {@link ItemStack} should have then
     * @param level the level of this {@link Enchantment}
     * @return this {@link ItemBuilder}
     */
    public ItemBuilder addEnchantment(Enchantment e, int level) {
        enchantments.put(e, level);
        return this;
    }

    /**
     * Adds a {@link ItemData} to the {@link ItemStack}
     *
     * @param data the {@link ItemData} to add
     * @return this {@link ItemBuilder}
     */
    public ItemBuilder addItemData(ItemData data) {
        itemData.add(data);
        return this;
    }

    /**
     * Gets the all current {@link ItemData}s
     *
     * @return {@link ArrayList} with all current {@link ItemData}
     */
    public ArrayList<ItemData> getAllData() {
        return itemData;
    }

    /**
     * Add a gloweffect to the {@link ItemStack}
     *
     * @return this {@link ItemBuilder}
     */
    public ItemBuilder setGlow() {
        enchantments.put(Enchantment.ARROW_DAMAGE, 1);
        itemflags.add(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    /**
     * Add new {@link ItemFlag}s to the {@link ItemStack}
     *
     * @param flagsToAdd the {@link ItemFlag}s you want to add
     * @return this {@link ItemBuilder}
     */
    public ItemBuilder addItemFlags(ItemFlag... flagsToAdd) {
        Collections.addAll(itemflags, flagsToAdd);
        return this;
    }

    /**
     * Remove {@link ItemFlag}s from the {@link ItemStack}
     *
     * @param flagsToRemove the {@link ItemFlag}s you want to remove
     * @return this {@link ItemBuilder}
     */
    public ItemBuilder removeItemFlags(ItemFlag... flagsToRemove) {
        for (ItemFlag iFlag : flagsToRemove) {
            itemflags.remove(iFlag);
        }
        return this;
    }

    /**
     * Gets a {@link ArrayList} with all current {@link ItemFlag}s for this {@link ItemStack}
     *
     * @return a {@link ArrayList} with all current {@link ItemFlag}s
     */
    public ArrayList<ItemFlag> getAllItemflags() {
        return itemflags;
    }

    /**
     * Get all {@link Enchantment}s this {@link ItemStack} has
     *
     * @return all {@link Enchantment}s of this {@link ItemStack}
     */
    public Map<Enchantment, Integer> getAllEnchantments() {
        return enchantments;
    }

    /**
     * Get the custom name of this {@link ItemStack}
     *
     * @return the custom name of this {@link ItemStack}
     */
    public String getName() {
        return name;
    }

    /**
     * Set a custom name for the {@link ItemStack}
     *
     * @param name The new custom name of the {@link ItemStack}
     * @return this {@link ItemBuilder}
     */
    public ItemBuilder setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Get the {@link Material} of this {@link ItemStack}
     *
     * @return the {@link Material} of this {@link ItemStack}
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Get the amount of this {@link ItemStack}
     *
     * @return the amount of this {@link ItemStack}
     */
    public int getAmount() {
        return amount;
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
        return damage;
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
        return lore;
    }

    /**
     * Set a custom lore for the {@link ItemStack}
     *
     * @param newLore the new custom lore of the {@link ItemStack}
     * @return this {@link ItemBuilder}
     */
    public ItemBuilder setLore(String... newLore) {
        this.lore = Arrays.asList(newLore);
        return this;
    }

    /**
     * Get the breakstatus of this {@link ItemStack}
     *
     * @return whether or not this {@link ItemStack} can break
     */
    public boolean isUnbreakable() {
        return unbreakable;
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

    /**
     * Get the final {@link ItemStack} with all the attributes you have been adding
     *
     * @return the {@link ItemStack} out of this {@link ItemBuilder}
     */
    public ItemStack build() {
        ItemStack s = new ItemStack(material);
        s.setAmount(amount);
        s.setDurability(damage);
        ItemMeta m = s.getItemMeta();

        for (ItemFlag iflag : itemflags)
            m.addItemFlags(iflag);
        m.setDisplayName(name);
        m.setLore(lore);
        s.setItemMeta(m);
        for (Entry<Enchantment, Integer> temp : enchantments.entrySet())
            s.addUnsafeEnchantment(temp.getKey(), temp.getValue());

        for (ItemData id : getAllData())
            try {
                id.applyOn(s);
            } catch (WrongDataException e) {
                e.printStackTrace();
            }

        return s;
    }
}
