/*
 * Copyright (C) <2017>  <AlphaHelixDev>
 *
 *       This program is free software: you can redistribute it under the
 *       terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.alphahelix.alphalibary.file;

import de.alphahelix.alphalibary.AlphaLibary;
import de.alphahelix.alphalibary.item.ItemBuilder;
import de.alphahelix.alphalibary.utils.SerializationUtil;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class SimpleFile extends YamlConfiguration {

    private static final SerializationUtil<InventoryItem> INVENTORY_ITEM_SERIALIZATION_UTIL = new SerializationUtil<>();
    private static final SerializationUtil<ItemStack[]> ITEMSTACK_ARRAY_SERIALIZATION_UTIL = new SerializationUtil<>();

    private File source = null;

    /**
     * Create a new {@link SimpleFile} inside the given path with the name 'name'
     *
     * @param path the path where the {@link File} should be created in
     * @param name the name which the {@link File} should have
     */
    public SimpleFile(String path, String name) {
        new File(path).mkdirs();
        source = new File(path, name);
        createIfNotExist();
        addValues();
    }

    /**
     * Create a new {@link SimpleFile} inside the plugin path with the name 'name'
     *
     * @param name the name which the file should have
     */
    public SimpleFile(String name) {
        source = new File(AlphaLibary.getInstance().getDataFolder().getPath(), name);
        createIfNotExist();
        addValues();
    }

    /**
     * Convert a normal {@link File} into a {@link SimpleFile}
     *
     * @param f the old File which you want to convert
     */
    public SimpleFile(File f) {
        source = f;
        createIfNotExist();
        addValues();
    }

    /**
     * Finish the setup of the {@link SimpleFile}
     */
    private void finishSetup() {
        try {
            load(source);
        } catch (Exception ignored) {

        }
    }

    /**
     * Overridden method to add new standard values to a config
     */
    public void addValues() {

    }

    /**
     * Create a new {@link SimpleFile} if it's not existing
     */
    private void createIfNotExist() {
        options().copyDefaults(true);
        if (source == null || !source.exists()) {
            try {
                assert source != null;
                source.createNewFile();
            } catch (IOException e) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        try {
                            source.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.runTaskLaterAsynchronously(AlphaLibary.getInstance(), 20);
            }
        }
        finishSetup();
    }

    /**
     * Get a colored {@link String}
     *
     * @param path the path inside this {@link SimpleFile}
     * @return the {@link String} with Colors
     */
    public String getColorString(String path) {
        if (!contains(path))
            return "";

        try {
            String toReturn = getString(path);
            return ChatColor.translateAlternateColorCodes('&', toReturn);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Get a colored {@link ArrayList} out of this {@link SimpleFile}
     *
     * @param path the path inside this {@link SimpleFile}
     * @return the {@link ArrayList} with Colors
     */
    public ArrayList<String> getColorStringList(String path) {
        if (!configContains(path)) return new ArrayList<>();
        if (!isList(path)) return new ArrayList<>();

        try {
            ArrayList<String> tR = new ArrayList<>();
            for (String str : getStringList(path)) {
                tR.add(ChatColor.translateAlternateColorCodes('&', str));
            }
            return tR;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * Save a base64 encoded {@link ItemStack}[] inside this {@link SimpleFile}
     *
     * @param path   where to save the {@link ItemStack}[]
     * @param toSave the {@link ItemStack}[] to save
     */
    public void setBase64ItemStackArray(String path, ItemStack... toSave) {
        setDefault(path, SerializationUtil.jsonToString(ITEMSTACK_ARRAY_SERIALIZATION_UTIL.serialize(toSave)));
    }

    /**
     * Save a base64 encoded {@link InventoryItem}[] inside this {@link SimpleFile}
     *
     * @param path   where to save the {@link InventoryItem}[]
     * @param toSave the {@link InventoryItem}[] to save
     */
    public void setBase64InventoryItemArray(String path, InventoryItem... toSave) {
        ArrayList<String> items = new ArrayList<>();

        for (InventoryItem item : toSave) {
            items.add(SerializationUtil.jsonToString(INVENTORY_ITEM_SERIALIZATION_UTIL.serialize(item)));
        }

        override(path, items);
    }

    /**
     * Gets a base64 encoded {@link InventoryItem}[] out of this {@link SimpleFile}
     *
     * @param path where the {@link InventoryItem}[] should be located at
     * @return the {@link InventoryItem}[] which was saved
     */
    public InventoryItem[] getBase64InventoryItemArray(String path) {
        ArrayList<InventoryItem> items = new ArrayList<>();

        for (String base64 : getStringList(path)) {
            items.add(INVENTORY_ITEM_SERIALIZATION_UTIL.deserialize(SerializationUtil.stringToJson(base64)));
        }

        return items.toArray(new InventoryItem[items.size()]);
    }

    /**
     * Gets a base64 encoded {@link ItemStack}[] out of this {@link SimpleFile}
     *
     * @param path where the {@link ItemStack}[] should be located at
     * @return the {@link ItemStack}[] which was saved
     */
    public ItemStack[] getBase64ItemStackArray(String path) {
        return ITEMSTACK_ARRAY_SERIALIZATION_UTIL.deserialize(SerializationUtil.stringToJson(getString(path)));
    }

    /**
     * Save a {@link ItemStack}[] inside this {@link SimpleFile}
     *
     * @param path   where to save the {@link ItemStack}[]
     * @param toSave the {@link ItemStack}[] to save
     */
    public void setItemStackArray(String path, ItemStack... toSave) {
        List<ItemStack> items = Arrays.asList(toSave);
        for (ItemStack saved : items) {
            setInventoryItem(path + "." + items.indexOf(saved), saved, 0);
        }
    }

    /**
     * Gets a {@link ItemStack}[] out of this {@link SimpleFile}
     *
     * @param path where the {@link ItemStack}[] should be located at
     * @return the {@link ItemStack}[] which was saved
     */
    public ItemStack[] getItemStackArray(String path) {
        ArrayList<ItemStack> items = new ArrayList<>();

        for (String id : getConfigurationSection(path).getKeys(false)) {
            items.add(getInventoryItem(path + "." + id).getItemStack());
        }
        return items.toArray(new ItemStack[items.size()]);
    }

    /**
     * Save a {@link InventoryItem} inside this {@link SimpleFile}
     *
     * @param path      where to save the {@link ItemStack}
     * @param itemStack the {@link ItemStack} to save
     * @param slot      the slot where the {@link ItemStack} is inside the {@link Inventory}
     */
    public void setInventoryItem(String path, ItemStack itemStack, int slot) {
        setDefault(path + ".name", "null");
        setMaterial(path + ".type", itemStack.getType());
        setDefault(path + ".amount", itemStack.getAmount());
        setDefault(path + ".damage", itemStack.getDurability());
        setDefault(path + ".slot", slot);
        if (getStringList(path + ".enchantments") == null)
            setMap(path + ".enchantments", new HashMap<>());
        if (getStringList(path + ".lore") == null)
            setArgumentList(path + ".lore", "");
        if (getStringList(path + ".flags") == null)
            setArgumentList(path + ".flags", "");

        if (itemStack.hasItemMeta()) {
            if (itemStack.getItemMeta().hasDisplayName()) {
                setDefault(path + ".name", itemStack.getItemMeta().getDisplayName());
            }

            if (itemStack.getItemMeta().hasLore()) {
                setArgumentList(path + ".lore", itemStack.getItemMeta().getLore().toArray(new String[itemStack.getItemMeta().getLore().size()]));
            }

            if (itemStack.getItemMeta().hasEnchants()) {
                setMap(path + ".enchantments", itemStack.getItemMeta().getEnchants());
            }

            for (ItemFlag flag : itemStack.getItemMeta().getItemFlags()) {
                addArgumentsToList(path + ".flags", flag.name());
            }
        }
    }

    /**
     * Gets a {@link InventoryItem} out of this {@link SimpleFile}
     *
     * @param path where the {@link InventoryItem} should be located at
     * @return the {@link InventoryItem} which was saved
     */
    public InventoryItem getInventoryItem(String path) {
        String name = getColorString(path + ".name");
        Material type = getMaterial(path + ".type");
        int amount = getInt(path + ".amount");
        short damage = (short) getInt(path + ".damage");
        HashMap<String, String> ench = getMap(path + ".enchantments");
        List<String> lore = getStringList(path + ".lore");
        List<String> flags = getStringList(path + ".flags");

        ItemStack stack = new ItemStack(type, amount, damage);
        ItemMeta meta = stack.getItemMeta();

        meta.setLore(lore);
        meta.setDisplayName(name);

        for (String enchantment : ench.keySet()) {
            meta.addEnchant(Enchantment.getByName(enchantment), Integer.parseInt(ench.get(enchantment)), true);
        }

        for (String flag : flags) {
            meta.addItemFlags(ItemFlag.valueOf(flag));
        }

        stack.setItemMeta(meta);

        return new InventoryItem(stack, getInt(path + ".slot"));
    }

    /**
     * Saves an array of {@link Material} names as a {@link List} inside this {@link SimpleFile}
     *
     * @param path      where to save the {@link List}
     * @param materials the name of the {@link Material}s you want to save
     */
    public void setMaterialStringList(String path, String... materials) {
        ArrayList<String> stacks = new ArrayList<>();
        Collections.addAll(stacks, materials);
        set(path, stacks);
        save();
    }

    /**
     * Gets the {@link List} with all {@link Material} names from this {@link SimpleFile}
     *
     * @param path where the {@link List} should be located at
     * @return the {@link List} with all {@link Material} names
     */
    public List<String> getMaterialStringList(String path) {
        return getStringList(path);
    }

    /**
     * Gets a {@link Material} from a 'user friendly' form inside this {@link SimpleFile}
     *
     * @param path where the {@link Material} should be located at
     * @return the saved {@link Material}
     */
    public Material getMaterial(String path) {
        return Material.getMaterial(getString(path).replace(" ", "_").toUpperCase());
    }

    /**
     * Saves a {@link Material} as a 'user friendly' form inside this {@link SimpleFile}
     *
     * @param path     the path where the {@link Material} should be located at
     * @param material the {@link Material} which should be saved
     */
    public void setMaterial(String path, Material material) {
        setDefault(path, material.name().replace("_", " ").toLowerCase());
    }

    /**
     * Saves a {@link Inventory} inside this {@link SimpleFile}
     *
     * @param path          the path where the {@link Inventory} should be located at
     * @param itemInventory the {@link Inventory} toSave
     */
    public void setItemInventory(String path, ItemInventory itemInventory) {
        override(path + ".title", itemInventory.getInventory().getTitle());
        override(path + ".size", itemInventory.getInventory().getSize());
        setBase64InventoryItemArray(path + ".content", itemInventory.getItems());
    }

    /**
     * Gets a {@link Inventory} from this {@link SimpleFile}
     *
     * @param path the path where the {@link Inventory} should be located at
     * @return the {@link Inventory} which was saved inside this {@link SimpleFile}
     */
    public Inventory getItemInventory(String path) {
        if (!isConfigurationSection(path)) return null;

        Inventory inventory = Bukkit.createInventory(null, getInt(path + ".size"), getColorString(path + ".title"));

        for (InventoryItem is : getBase64InventoryItemArray(path + ".content")) {
            if (is.getSlot() == -1) continue;
            if (is.getItemStack() != null) {
                inventory.setItem(is.getSlot(), is.getItemStack());
            }
        }

        return inventory;
    }

    /**
     * Saves a {@link Inventory} inside this {@link SimpleFile}
     *
     * @param path   where to save the {@link Inventory}
     * @param toSave the {@link Inventory} to save
     */
    public void setInventory(String path, Inventory toSave) {
        override(path + ".title", toSave.getTitle());
        override(path + ".size", toSave.getSize());
        setBase64ItemStackArray(path + ".content", toSave.getContents());
    }

    /**
     * Gets the {@link Inventory} from this {@link SimpleFile}
     *
     * @param path where the {@link Inventory} is located at
     * @return the {@link Inventory}
     */
    public Inventory getInventory(String path) {
        Inventory toReturn = Bukkit.createInventory(null, getInt(path + ".size"), getColorString(path + ".title"));

        for (ItemStack is : getBase64ItemStackArray(path + ".content")) {
            if (is != null && is.getType() != Material.AIR)
                toReturn.addItem(is);
        }

        return toReturn;
    }

    /**
     * Saves a base 64 encoded {@link Location} inside the {@link SimpleFile}
     *
     * @param path where to save the {@link Location}
     * @param loc  the {@link Location} to save
     */
    public void setBase64Location(String path, Location loc) {
        SerializationUtil<Location> serializer = new SerializationUtil<>();

        override(path, SerializationUtil.jsonToString(serializer.serialize(loc)));
    }

    /**
     * Gets a base 64 encoded {@link Location} from this {@link SimpleFile}
     *
     * @param path where the {@link Location} should be located at
     * @return the {@link Location} which is saved
     */
    public Location getBase64Location(String path) {
        SerializationUtil serializer = new SerializationUtil<>();

        if (getString(path) == null) return null;

        return (Location) serializer.deserialize(SerializationUtil.stringToJson(getString(path)));
    }

    /**
     * Saves a {@link Location} inside the {@link SimpleFile}
     *
     * @param path where to save the {@link Location}
     * @param loc  the {@link Location} to save
     */
    public void setLocation(String path, Location loc) {
        setDefault(path + ".x", loc.getX());
        setDefault(path + ".y", loc.getY());
        setDefault(path + ".z", loc.getZ());
        setDefault(path + ".yaw", loc.getYaw());
        setDefault(path + ".pitch", loc.getPitch());
        setDefault(path + ".world", loc.getWorld().getName());
    }

    /**
     * Gets a {@link Location} from this {@link SimpleFile}
     *
     * @param path          where the {@link Location} should be located at
     * @param forceGenerate should the world be loaded if it's non existing
     * @return the {@link Location} which is saved
     */
    public Location getLocation(String path, boolean forceGenerate) {

        double x = getDouble(path + ".x");
        double y = getDouble(path + ".y");
        double z = getDouble(path + ".z");
        float yaw = getInt(path + ".yaw");
        float pitch = getInt(path + ".pitch");
        World w = Bukkit.getWorld(getString(path + ".world"));

        if (forceGenerate)
            if (w == null)
                Bukkit.createWorld(new WorldCreator(getString(path + ".world")));


        return new Location(w, x, y, z, yaw, pitch);
    }

    /**
     * Saves a base 64 encoded {@link List} of arguments inside this {@link SimpleFile}
     *
     * @param path          the path where the arguments should be saved at
     * @param listArguments the arguments to save
     */
    @SafeVarargs
    private final <T> void setBase64ArgumentList(String path, T... listArguments) {
        List<String> argsAtBase64 = new ArrayList<>();
        SerializationUtil<T> serializer = new SerializationUtil<>();

        for (T arg : listArguments) {
            argsAtBase64.add(SerializationUtil.jsonToString(serializer.serialize(arg)));
        }

        override(path, argsAtBase64);
    }

    /**
     * Gets a base 64 encoded {@link ArrayList} of arguments from this {@link SimpleFile}
     *
     * @param path the path where the {@link ArrayList} should be located at
     * @param <T>  the type of the arguments
     * @return
     */
    public <T> ArrayList<T> getBase64ArgumentList(String path) {
        ArrayList<T> args = new ArrayList<>();
        SerializationUtil<T> serializer = new SerializationUtil<>();

        if (configContains(path))
            for (Object base64arg : getList(path)) {
                args.add(serializer.deserialize(SerializationUtil.stringToJson((String) base64arg)));
            }

        return args;
    }

    /**
     * Add base 64 encoded arguments inside this {@link SimpleFile}
     *
     * @param path      the path where the arguments should be saved at
     * @param arguments the arguments to add
     */
    @SafeVarargs
    public final <T> void addBase64ArgumentsToList(String path, T... arguments) {
        ArrayList<T> args = getBase64ArgumentList(path);

        Collections.addAll(args, arguments);

        setBase64ArgumentList(path, true, args.toArray());
    }

    /**
     * Remove base 64 encoded arguments from this {@link SimpleFile}
     *
     * @param path      the path where the arguments should be saved at
     * @param arguments the arguments to remove
     */
    @SafeVarargs
    public final <T> void removeBase64ArgumentsFromList(String path, T... arguments) {
        ArrayList<T> args = getBase64ArgumentList(path);

        for (T arg : arguments) {
            if (args.contains(arg)) {
                args.remove(arg);
                break;
            }
        }

        if (!args.isEmpty())
            setBase64ArgumentList(path, true, args.toArray());
        else
            override(path, null);
    }

    /**
     * Saves a {@link List} of arguments inside this {@link SimpleFile} as Strings
     *
     * @param path          the path where the arguments should be saved at
     * @param listArguments the arguments to save
     */
    private void setArgumentList(String path, String... listArguments) {
        List<String> arguments = new ArrayList<>();

        Collections.addAll(arguments, listArguments);

        override(path, arguments);
    }

    /**
     * Add arguments inside this {@link SimpleFile}
     *
     * @param path      the path where the arguments should be saved at
     * @param arguments the arguments to add
     */
    public void addArgumentsToList(String path, String... arguments) {
        List<String> args = getStringList(path);

        for (String arg : arguments) {
            if (!args.contains(arg)) args.add(arg);
        }

        setArgumentList(path, args.toArray(new String[args.size()]));
    }

    /**
     * Remove arguments from this {@link SimpleFile}
     *
     * @param path      the path where the arguments should be saved at
     * @param arguments the arguments to remove
     */
    public void removeArgumentsFromList(String path, String... arguments) {
        List<String> args = getStringList(path);

        for (String arg : arguments) {
            if (args.contains(arg)) {
                args.remove(arg);
                break;
            }
        }

        if (!args.isEmpty())
            setArgumentList(path, args.toArray(new String[args.size()]));
        else
            override(path, null);
    }

    /**
     * Save a {@link HashMap} inside this {@link SimpleFile}
     * The keys/values are saved as a {@link String}
     *
     * @param path the path where the {@link HashMap} should be saved at
     * @param map  the {@link HashMap} to save
     */
    public <K, V> void setMap(String path, Map<K, V> map) {
        ArrayList<String> keyToValue = new ArrayList<>();

        for (K k : map.keySet()) {
            keyToValue.add(k.toString() + " <:> " + map.get(k).toString());
        }

        addArgumentsToList(path, keyToValue.toArray(new String[keyToValue.size()]));
    }

    /**
     * Gets a {@link HashMap} from this {@link SimpleFile}
     *
     * @param path the path where the {@link HashMap} is located at
     * @return the {@link HashMap} saved at this location
     */
    public HashMap<String, String> getMap(String path) {
        HashMap<String, String> map = new HashMap<>();

        for (String seri : getStringList(path)) {
            String k = seri.split(" <:> ")[0];
            String v = seri.split(" <:> ")[1];

            map.put(k, v);
        }

        return map;
    }

    /**
     * Checks if this {@link SimpleFile} contains a specific {@link String}
     *
     * @param toCheck {@link String} which might be inside this {@link SimpleFile}
     * @return whether or not this {@link SimpleFile} contains the {@link String}
     */
    public boolean configContains(String toCheck) {
        boolean cContains = false;

        for (String key : getKeys(true)) {
            if (key.equalsIgnoreCase(toCheck))
                cContains = true;
        }

        return cContains;
    }

    /**
     * Save and load this {@link SimpleFile}
     */
    public void save() {
        try {
            if (source == null) return;
            save(source);
        } catch (IOException ignored) {
            ignored.printStackTrace();
        }
    }

    /**
     * Add a new value to this {@link SimpleFile}
     *
     * @param path  where the value should be saved at
     * @param value the value which you want to save
     */
    public void setDefault(String path, Object value) {
        if (path.contains("§")) {
            path = path.replaceAll("§", "&");
        }
        if (value instanceof String)
            value = ((String) value).replaceAll("§", "&");

        addDefault(path, value);

        save();
    }

    /**
     * Replaces a value inside this {@link SimpleFile}
     *
     * @param path  where the value is located at
     * @param value the new value which should be saved
     */
    public void override(String path, Object value) {
        if (value instanceof String)
            value = ((String) value).replaceAll("§", "&");

        if (configContains(path))
            set(path, value);
        else
            addDefault(path, value);
        save();
    }

    /**
     * Modified version of an {@link ItemStack} to save it inside the {@link SimpleFile}
     */
    public static class InventoryItem implements Serializable {

        private ItemStack itemStack;
        private int slot;

        public InventoryItem(ItemStack itemStack, int slot) {
            if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName() && itemStack.getItemMeta().getDisplayName().equals("_"))
                this.itemStack = new ItemBuilder(itemStack).setName(" ").build();
            else
                this.itemStack = itemStack;
            this.slot = slot;
        }

        public ItemStack getItemStack() {
            return itemStack;
        }

        public int getSlot() {
            return slot;
        }
    }

    /**
     * Modified version of an {@link Inventory} to save it inside the {@link SimpleFile}
     */
    public class ItemInventory implements Serializable {

        private Inventory inventory;
        private InventoryItem[] items;

        public ItemInventory(Inventory inventory, InventoryItem... items) {
            this.inventory = inventory;
            this.items = items;
        }

        public ItemInventory(Inventory inventory) {
            this.inventory = inventory;
            ArrayList<InventoryItem> iitems = new ArrayList<>();

            int slot = 0;

            for (ItemStack stack : inventory.getContents()) {
                if (stack != null && inventory.getItem(slot) != null) {
                    iitems.add(new InventoryItem(stack, inventory.first(stack)));
                    inventory.removeItem(inventory.getItem(slot));
                }
                slot++;
            }

            this.items = iitems.toArray(new InventoryItem[iitems.size()]);
        }

        public ItemInventory(String name, int size, InventoryItem... items) {
            this.inventory = Bukkit.createInventory(null, size, name);
            this.items = items;
        }

        public Inventory getInventory() {
            return inventory;
        }

        public InventoryItem[] getItems() {
            return items;
        }
    }
}