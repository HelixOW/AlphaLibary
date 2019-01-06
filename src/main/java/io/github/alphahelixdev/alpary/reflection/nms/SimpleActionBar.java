package io.github.alphahelixdev.alpary.reflection.nms;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Objects;

public class SimpleActionBar {

    private String message;

    public SimpleActionBar(String message) {
        this.message = message;
    }

    public void send(Player p) {
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(this.getMessage()));
    }

    public void send(World w) {
        send(w.getPlayers());
    }

    public void send() {
        send(Bukkit.getOnlinePlayers());
    }

    public void send(Collection<? extends Player> players) {
        players.forEach(this::send);
    }

    public void clear(Player p) {
        this.setMessage("");
        send(p);
    }

    public void clear(World w) {
        clear(w.getPlayers());
    }

    public void clear() {
        clear(Bukkit.getOnlinePlayers());
    }

    public void clear(Collection<? extends Player> players) {
        players.forEach(this::clear);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleActionBar that = (SimpleActionBar) o;
        return Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message);
    }

    @Override
    public String toString() {
        return "SimpleActionBar{" +
                "message='" + message + '\'' +
                '}';
    }
}
