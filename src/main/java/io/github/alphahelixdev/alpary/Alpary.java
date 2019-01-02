package io.github.alphahelixdev.alpary;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.alphahelixdev.alpary.addons.AddonCore;
import io.github.alphahelixdev.alpary.annotations.item.Item;
import io.github.alphahelixdev.alpary.annotations.item.ItemColor;
import io.github.alphahelixdev.alpary.annotations.randoms.Random;
import io.github.alphahelixdev.alpary.utilities.BukkitListener;
import io.github.alphahelixdev.alpary.utilities.UUIDFetcher;
import io.github.alphahelixdev.alpary.utils.StringUtil;
import io.github.alphahelixdev.helius.Helius;
import io.github.alphahelixdev.helius.reflection.SaveField;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Alpary extends JavaPlugin {

    private static Alpary instance;
    private final Reflections reflections = new Reflections();
    private final GsonBuilder gsonBuilder = new GsonBuilder();
    private UUIDFetcher uuidFetcher;

    public static Alpary getInstance() {
        return Alpary.instance;
    }

    @Override
    public void onEnable() {
        Helius.main(new String[]{});
        Alpary.instance = this;

        this.uuidFetcher = new UUIDFetcher();
        new AddonCore().enable();

        this.reflections.getTypesAnnotatedWith(BukkitListener.class).stream().filter(Listener.class::isAssignableFrom).forEach(listenerClass -> {
            try {
                Bukkit.getPluginManager().registerEvents((Listener) listenerClass.getDeclaredConstructor().newInstance(), this);
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        });
    }

    public Reflections getReflections() {
        return this.reflections;
    }

    public GsonBuilder gsonBuilder() {
        return this.gsonBuilder;
    }

    public Gson gson() {
        return this.gsonBuilder.create();
    }

    public UUIDFetcher uuidFetcher() {
        return this.uuidFetcher;
    }

    public void randomizeFields(Object o) {
        Arrays.stream(o.getClass().getDeclaredFields()).filter(field -> field.isAnnotationPresent(Random.class))
                .map(SaveField::new).forEach(randomField -> {
            Random r = randomField.asNormal().getAnnotation(Random.class);

            switch (randomField.asNormal().getType().getName().toLowerCase()) {
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
                case "UUID":
                    randomField.set(o, UUID.randomUUID());
                    break;
            }
        });
    }

    public void setItemFields(Object o) {
        Arrays.stream(o.getClass().getDeclaredFields()).filter(field -> field.isAnnotationPresent(Item.class))
                .map(SaveField::new).forEach(itemField -> {
            Item item = itemField.asNormal().getAnnotation(Item.class);
            ItemStack created = new ItemStack(item.material(), item.amount());
            ItemMeta meta = created.getItemMeta();

            if (item.itemflags().length != 0)
                meta.addItemFlags(item.itemflags());

            if (!item.name().equals(""))
                meta.setDisplayName(item.name());

            if (item.damage() != 0 && meta instanceof Damageable)
                ((Damageable) meta).setDamage(item.damage());

            if (item.lore().length != 0)
                meta.setLore(Arrays.asList(item.lore()));

            meta.setUnbreakable(item.unbreakable());

            if (itemField.asNormal().isAnnotationPresent(ItemColor.class)) {

            }

        });
    }
}