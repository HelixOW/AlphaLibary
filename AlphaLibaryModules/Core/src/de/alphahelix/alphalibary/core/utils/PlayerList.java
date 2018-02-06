package de.alphahelix.alphalibary.core.utils;

import org.bukkit.entity.Player;

import java.util.LinkedList;


public class PlayerList extends LinkedList<String> {
    public void add(Player p) {
        add(p.getName());
    }

    public boolean has(Player p) {
        return contains(p.getName());
    }

    public boolean remove(Player p) {
        return remove(p.getName());
    }

    public void addIfNotExisting(Player p) {
        if (!has(p))
            add(p);
    }

    public Player[] getPlayers() {
        return Util.makePlayerArray(this);
    }
}
