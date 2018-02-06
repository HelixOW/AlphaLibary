package de.alphahelix.alphalibary.storage.file2;

import de.alphahelix.alphalibary.core.utils.SerializationUtil;
import de.alphahelix.alphalibary.inventories.ItemInventory;
import de.alphahelix.alphalibary.inventories.item.InventoryItem;
import de.alphahelix.alphalibary.storage.IDataStorage;
import de.alphahelix.alphalibary.storage.file.AbstractFile;
import de.alphahelix.alphalibary.storage.file2.yaml.YamlConfig;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.function.Consumer;

public class SimpleFile extends AbstractFile implements IDataStorage {

    private final YamlConfig cfg;

    public SimpleFile(String path, String name) {
        super(path, name);

        cfg = YamlConfig.loadFromFile(this);

        addValues();
    }

    public SimpleFile(JavaPlugin plugin, String name) {
        this(plugin.getDataFolder().getAbsolutePath(), name);
    }

    public SimpleFile(File parent, String child) {
        this(parent.getAbsolutePath(), child);
    }

    public SimpleFile(URI uri) {
        this(new File(uri.getPath()).getParent(), new File(uri.getPath()).getName());
    }

    public SimpleFile(AbstractFile file) {
        this(file.getParent(), file.getName());
    }

    /**
     * Overridden method to add new standard values to a config
     */
    public void addValues() {
    }

    protected YamlConfig getCfg() {
        return cfg;
    }

    public boolean contains(Object path) {
        return cfg.contains(path.toString());
    }

    public boolean isList(Object path) {
        return cfg.isList(path.toString());
    }

    @Override
    public void setValue(Object path, Object value) {
        cfg.set(path.toString(), value);

        try {
            cfg.save(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <T> T getValue(Object path) {
        return (T) cfg.get(path.toString());
    }

    @Override
    public void setDefaultValue(Object path, Object value) {
        if (!contains(path))
            setValue(path, value);
    }

    @Override
    public void removeValue(Object path) {
        setValue(path, null);
    }

    @Override
    public <T> void getValue(Object path, Class<T> definy, Consumer<T> callback) {
        callback.accept(getValue(path));
    }

    @Override
    public void getKeys(Consumer<List<String>> callback) {
        callback.accept(new ArrayList<>(getKeys()));
    }

    @Override
    public <T> void getValues(Class<T> definy, Consumer<List<T>> callback) {
        callback.accept((List<T>) cfg.getKeys());
    }

    @Override
    public void hasValue(Object path, Consumer<Boolean> callback) {
        callback.accept(contains(path));
    }

    public String[] getKeys(String path) {
        return path.substring(path.indexOf(".") + 1).split("\\.");
    }

    public Set<String> getKeys() {
        Set<String> keys = new HashSet<>();
        for (String key : cfg.getKeys()) {
            keys.add(key.substring(0, key.indexOf(".")));
        }

        return keys;
    }


    public String getString(String path) {
        return (String) getValue(path);
    }

    public int getInt(String path) {
        return Integer.valueOf(getValue(path));
    }

    public short getShort(String path) {
        return Short.valueOf(getValue(path));
    }

    public double getDouble(String path) {
        return Double.valueOf(getValue(path));
    }

    public float getFloat(String path) {
        return Float.valueOf(getValue(path));
    }

    public boolean getBoolean(String path) {
        return Boolean.valueOf(getValue(path));
    }

    public char getChar(String path) {
        return getValue(path);
    }

    public byte getByte(String path) {
        return Byte.valueOf(getValue(path));
    }

    public long getLong(String path) {
        return Long.valueOf(getValue(path));
    }

    /**
     * Get a colored {@link String}
     *
     * @param path the path inside this {@link de.alphahelix.alphalibary.storage.file.SimpleFile}
     * @return the {@link String} with Colors
     */
    public String getColorString(String path) {
        if (!contains(path))
            return "";

        try {
            String toReturn = getValue(path);
            return ChatColor.translateAlternateColorCodes('&', toReturn);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Get a colored {@link ArrayList} out of this {@link de.alphahelix.alphalibary.storage.file.SimpleFile}
     *
     * @param path the path inside this {@link de.alphahelix.alphalibary.storage.file.SimpleFile}
     * @return the {@link ArrayList} with Colors
     */
    public List<String> getColorStringList(String path) {
        if (!contains(path)) return new ArrayList<>();
        if (!isList(path)) return new ArrayList<>();

        try {
            List<String> tR = new ArrayList<>();
            for (String str : (List<String>) getValue(path)) {
                tR.add(ChatColor.translateAlternateColorCodes('&', str));
            }
            return tR;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * Save a base64 encoded {@link ItemStack}[] inside this {@link de.alphahelix.alphalibary.storage.file.SimpleFile}
     *
     * @param path   where to save the {@link ItemStack}[]
     * @param toSave the {@link ItemStack}[] to save
     */
    public void setBase64ItemStackArray(String path, ItemStack... toSave) {
        setDefault(path, SerializationUtil.encodeBase64(toSave));
    }

    /**
     * Save a base64 encoded {@link InventoryItem}[] inside this {@link de.alphahelix.alphalibary.storage.file.SimpleFile}
     *
     * @param path   where to save the {@link InventoryItem}[]
     * @param toSave the {@link InventoryItem}[] to save
     */
    public void setBase64InventoryItemArray(String path, InventoryItem... toSave) {
        ArrayList<String> items = new ArrayList<>();

        for (InventoryItem item : toSave) {
            items.add(SerializationUtil.encodeBase64(item));
        }

        setValue(path, items);
    }

    /**
     * Gets a base64 encoded {@link InventoryItem}[] out of this {@link de.alphahelix.alphalibary.storage.file.SimpleFile}
     *
     * @param path where the {@link InventoryItem}[] should be located at
     * @return the {@link InventoryItem}[] which was saved
     */
    public InventoryItem[] getBase64InventoryItemArray(String path) {
        ArrayList<InventoryItem> items = new ArrayList<>();

        for (String base64 : (List<String>) getValue(path)) {
            items.add(SerializationUtil.decodeBase64(base64, InventoryItem.class));
        }

        return items.toArray(new InventoryItem[items.size()]);
    }

    /**
     * Gets a base64 encoded {@link ItemStack}[] out of this {@link de.alphahelix.alphalibary.storage.file.SimpleFile}
     *
     * @param path where the {@link ItemStack}[] should be located at
     * @return the {@link ItemStack}[] which was saved
     */
    public ItemStack[] getBase64ItemStackArray(String path) {
        return SerializationUtil.decodeBase64(getValue(path), ItemStack[].class);
    }

    /**
     * Save a {@link ItemStack}[] inside this {@link de.alphahelix.alphalibary.storage.file.SimpleFile}
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
     * Gets a {@link ItemStack}[] out of this {@link de.alphahelix.alphalibary.storage.file.SimpleFile}
     *
     * @param path where the {@link ItemStack}[] should be located at
     * @return the {@link ItemStack}[] which was saved
     */
    public ItemStack[] getItemStackArray(String path) {
        ArrayList<ItemStack> items = new ArrayList<>();

        for (String id : getKeys()) {
            items.add(getInventoryItem(path + "." + id).getItemStack());
        }
        return items.toArray(new ItemStack[items.size()]);
    }

    /**
     * Save a {@link InventoryItem} inside this {@link de.alphahelix.alphalibary.storage.file.SimpleFile}
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
        if (getValue(path + ".enchantments") == null)
            setMap(path + ".enchantments", new HashMap<>());
        if (getValue(path + ".lore") == null)
            setArgumentList(path + ".lore", "");
        if (getValue(path + ".flags") == null)
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
     * Gets a {@link InventoryItem} out of this {@link de.alphahelix.alphalibary.storage.file.SimpleFile}
     *
     * @param path where the {@link InventoryItem} should be located at
     * @return the {@link InventoryItem} which was saved
     */
    public InventoryItem getInventoryItem(String path) {

        String name = getColorString(path + ".name");
        Material type = getMaterial(path + ".type");
        int amount = getValue(path + ".amount");
        short damage = getValue(path + ".damage");
        Map<String, String> ench = getMap(path + ".enchantments");
        List<String> lore = getValue(path + ".lore");
        List<String> flags = getValue(path + ".flags");

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

        return new InventoryItem(stack, getValue(path + ".slot"));
    }

    /**
     * Saves an array of {@link Material} names as a {@link List} inside this {@link de.alphahelix.alphalibary.storage.file.SimpleFile}
     *
     * @param path      where to save the {@link List}
     * @param materials the name of the {@link Material}s you want to save
     */
    public void setMaterialStringList(String path, String... materials) {
        List<String> stacks = new ArrayList<>();
        Collections.addAll(stacks, materials);
        setValue(path, stacks);
    }

    /**
     * Gets the {@link List} with all {@link Material} names from this {@link de.alphahelix.alphalibary.storage.file.SimpleFile}
     *
     * @param path where the {@link List} should be located at
     * @return the {@link List} with all {@link Material} names
     */
    public List<String> getMaterialStringList(String path) {
        return (List<String>) getValue(path);
    }

    /**
     * Gets a {@link Material} from a 'user friendly' form inside this {@link de.alphahelix.alphalibary.storage.file.SimpleFile}
     *
     * @param path where the {@link Material} should be located at
     * @return the saved {@link Material}
     */
    public Material getMaterial(String path) {
        return Material.getMaterial(((String) getValue(path)).replace(" ", "_").toUpperCase());
    }

    /**
     * Saves a {@link Material} as a 'user friendly' form inside this {@link de.alphahelix.alphalibary.storage.file.SimpleFile}
     *
     * @param path     the path where the {@link Material} should be located at
     * @param material the {@link Material} which should be saved
     */
    public void setMaterial(String path, Material material) {
        setValue(path, material.name().replace("_", " ").toLowerCase());
    }

    /**
     * Saves a {@link Inventory} inside this {@link de.alphahelix.alphalibary.storage.file.SimpleFile}
     *
     * @param path          the path where the {@link Inventory} should be located at
     * @param itemInventory the {@link Inventory} toSave
     */
    public void setItemInventory(String path, ItemInventory itemInventory) {
        setValue(path + ".title", itemInventory.getInventory().getTitle());
        setValue(path + ".size", itemInventory.getInventory().getSize());
        setBase64InventoryItemArray(path + ".content", itemInventory.getItems());
    }

    /**
     * Gets a {@link Inventory} from this {@link de.alphahelix.alphalibary.storage.file.SimpleFile}
     *
     * @param path the path where the {@link Inventory} should be located at
     * @return the {@link Inventory} which was saved inside this {@link de.alphahelix.alphalibary.storage.file.SimpleFile}
     */
    public Inventory getItemInventory(String path) {
        if (!path.contains(".")) return null;

        Inventory inventory = Bukkit.createInventory(null, (Integer) getValue(path + ".size"), getColorString(path + ".title"));

        for (InventoryItem is : getBase64InventoryItemArray(path + ".content")) {
            if (is.getSlot() == -1) continue;
            if (is.getItemStack() != null) {
                inventory.setItem(is.getSlot(), is.getItemStack());
            }
        }

        return inventory;
    }

    /**
     * Saves a {@link Inventory} inside this {@link de.alphahelix.alphalibary.storage.file.SimpleFile}
     *
     * @param path   where to save the {@link Inventory}
     * @param toSave the {@link Inventory} to save
     */
    public void setInventory(String path, Inventory toSave) {
        setValue(path + ".title", toSave.getTitle());
        setValue(path + ".size", toSave.getSize());
        setBase64ItemStackArray(path + ".content", toSave.getContents());
    }

    /**
     * Gets the {@link Inventory} from this {@link de.alphahelix.alphalibary.storage.file.SimpleFile}
     *
     * @param path where the {@link Inventory} is located at
     * @return the {@link Inventory}
     */
    public Inventory getInventory(String path) {
        Inventory toReturn = Bukkit.createInventory(null, (Integer) getValue(path + ".size"), getColorString(path + ".title"));

        for (ItemStack is : getBase64ItemStackArray(path + ".content")) {
            if (is != null && is.getType() != Material.AIR)
                toReturn.addItem(is);
        }

        return toReturn;
    }

    /**
     * Saves a base 64 encoded {@link Location} inside the {@link de.alphahelix.alphalibary.storage.file.SimpleFile}
     *
     * @param path where to save the {@link Location}
     * @param loc  the {@link Location} to save
     */
    public void setBase64Location(String path, Location loc) {
        setValue(path, SerializationUtil.encodeBase64(loc));
    }

    /**
     * Gets a base 64 encoded {@link Location} from this {@link de.alphahelix.alphalibary.storage.file.SimpleFile}
     *
     * @param path where the {@link Location} should be located at
     * @return the {@link Location} which is saved
     */
    public Location getBase64Location(String path) {
        if (getValue(path) == null) return null;

        return SerializationUtil.decodeBase64(getValue(path), Location.class);
    }

    /**
     * Saves a {@link Location} inside the {@link de.alphahelix.alphalibary.storage.file.SimpleFile}
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
     * Gets a {@link Location} from this {@link de.alphahelix.alphalibary.storage.file.SimpleFile}
     *
     * @param path          where the {@link Location} should be located at
     * @param forceGenerate should the world be loaded if it's non existing
     * @return the {@link Location} which is saved
     */
    public Location getLocation(String path, boolean forceGenerate) {

        double x = getValue(path + ".x");
        double y = getValue(path + ".y");
        double z = getValue(path + ".z");
        float yaw = getValue(path + ".yaw");
        float pitch = getValue(path + ".pitch");
        World w = Bukkit.getWorld((String) getValue(path + ".world"));

        if (forceGenerate)
            if (w == null)
                Bukkit.createWorld(new WorldCreator(getString(path + ".world")));


        return new Location(w, x, y, z, yaw, pitch);
    }

    /**
     * Saves a base 64 encoded {@link List} of arguments inside this {@link de.alphahelix.alphalibary.storage.file.SimpleFile}
     *
     * @param path          the path where the arguments should be saved at
     * @param listArguments the arguments to save
     */
    @SafeVarargs
    private final <T> void setBase64ArgumentList(String path, T... listArguments) {
        List<String> argsAtBase64 = new ArrayList<>();

        for (T arg : listArguments) {
            argsAtBase64.add(SerializationUtil.encodeBase64(arg));
        }

        setValue(path, argsAtBase64);
    }

    /**
     * Gets a base 64 encoded {@link ArrayList} of arguments from this {@link de.alphahelix.alphalibary.storage.file.SimpleFile}
     *
     * @param path the path where the {@link ArrayList} should be located at
     * @param <T>  the type of the arguments
     */
    public <T> List<T> getBase64ArgumentList(String path, Class<T> objectClazz) {
        List<T> args = new ArrayList<>();

        if (contains(path))
            for (String base64arg : (List<String>) getValue(path)) {
                args.add(SerializationUtil.decodeBase64(base64arg, objectClazz));
            }

        return args;
    }

    /**
     * Add base 64 encoded arguments inside this {@link de.alphahelix.alphalibary.storage.file.SimpleFile}
     *
     * @param path      the path where the arguments should be saved at
     * @param arguments the arguments to add
     */
    @SafeVarargs
    public final <T> void addBase64ArgumentsToList(String path, T... arguments) {
        List<T> args = this.getBase64ArgumentList(path, (Class<T>) arguments.getClass().getComponentType());

        Collections.addAll(args, arguments);

        setBase64ArgumentList(path, args.toArray());
    }

    /**
     * Remove base 64 encoded arguments from this {@link de.alphahelix.alphalibary.storage.file.SimpleFile}
     *
     * @param path      the path where the arguments should be saved at
     * @param arguments the arguments to remove
     */
    @SafeVarargs
    public final <T> void removeBase64ArgumentsFromList(String path, T... arguments) {
        List<T> args = getBase64ArgumentList(path, (Class<T>) arguments.getClass().getComponentType());

        for (T arg : arguments) {
            if (args.contains(arg)) {
                args.remove(arg);
                break;
            }
        }

        if (!args.isEmpty())
            setBase64ArgumentList(path, args.toArray());
        else
            setValue(path, null);
    }

    /**
     * Saves a {@link List} of arguments inside this {@link de.alphahelix.alphalibary.storage.file.SimpleFile} as Strings
     *
     * @param path          the path where the arguments should be saved at
     * @param listArguments the arguments to save
     */
    private void setArgumentList(String path, String... listArguments) {
        List<String> arguments = new ArrayList<>();

        Collections.addAll(arguments, listArguments);

        setValue(path, arguments);
    }

    /**
     * Add arguments inside this {@link de.alphahelix.alphalibary.storage.file.SimpleFile}
     *
     * @param path      the path where the arguments should be saved at
     * @param arguments the arguments to add
     */
    public void addArgumentsToList(String path, String... arguments) {
        List<String> args;

        if (!contains(path))
            args = new ArrayList<>();
        else
            args = getValue(path);

        for (String arg : arguments) {
            if (!args.contains(arg)) args.add(arg);
        }

        setArgumentList(path, args.toArray(new String[args.size()]));
    }

    /**
     * Remove arguments from this {@link de.alphahelix.alphalibary.storage.file.SimpleFile}
     *
     * @param path      the path where the arguments should be saved at
     * @param arguments the arguments to remove
     */
    public void removeArgumentsFromList(String path, String... arguments) {
        List<String> args = getValue(path);

        for (String arg : arguments) {
            if (args.contains(arg)) {
                args.remove(arg);
                break;
            }
        }

        if (!args.isEmpty())
            setArgumentList(path, args.toArray(new String[args.size()]));
        else
            setValue(path, null);
    }

    /**
     * Save a {@link HashMap} inside this {@link de.alphahelix.alphalibary.storage.file.SimpleFile}
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
     * Gets a {@link HashMap} from this {@link de.alphahelix.alphalibary.storage.file.SimpleFile}
     *
     * @param path the path where the {@link HashMap} is located at
     * @return the {@link HashMap} saved at this location
     */
    public HashMap<String, String> getMap(String path) {
        HashMap<String, String> map = new HashMap<>();

        for (String seri : (List<String>) getValue(path)) {
            String k = seri.split(" <:> ")[0];
            String v = seri.split(" <:> ")[1];

            map.put(k, v);
        }

        return map;
    }

    /**
     * Add a new value to this {@link de.alphahelix.alphalibary.storage.file.SimpleFile}
     *
     * @param path  where the value should be saved at
     * @param value the value which you want to save
     */
    public void setDefault(String path, Object value) {
        if (contains(path))
            return;

        if (path.contains("§")) {
            path = path.replaceAll("§", "&");
        }

        if (value instanceof String)
            value = ((String) value).replaceAll("§", "&");

        setValue(path, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SimpleFile that = (SimpleFile) o;
        return Objects.equals(cfg, that.cfg);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), cfg);
    }

    @Override
    public String toString() {
        return "SimpleFile{" +
                "cfg=" + cfg +
                "} " + super.toString();
    }
}
