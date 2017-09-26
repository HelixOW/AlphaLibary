package de.alphahelix.alphalibary.core.utils;

import org.bukkit.entity.Player;

import java.util.WeakHashMap;

@SuppressWarnings("ALL")
public class PlayerMap<V> extends WeakHashMap<String, V> {
    public void put(Player p, V obj) {
        put(p.getName(), obj);
    }

    public void remove(Player p) {
        remove(p.getName());
    }

    public V get(Player p) {
        return get(p.getName());
    }

    public Player[] getKeys() {
        return Util.makePlayerArray(keySet());
    }
}
