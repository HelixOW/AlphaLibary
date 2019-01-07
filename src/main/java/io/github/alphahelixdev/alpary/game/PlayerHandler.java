package io.github.alphahelixdev.alpary.game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlayerHandler {

    private final Set<UUID> players = new HashSet<>();
    private final Set<UUID> alivePlayers = new HashSet<>();

    public PlayerHandler addPlayer(Player player) {
        getPlayers().add(player.getUniqueId());
        return this;
    }

    public PlayerHandler removePlayer(Player player) {
        getPlayers().remove(player.getUniqueId());
        return this;
    }

    public PlayerHandler addAlivePlayer(Player player) {
        getAlivePlayers().add(player.getUniqueId());
        return this;
    }

    public PlayerHandler removeAlivePlayer(Player player) {
        getAlivePlayers().remove(player.getUniqueId());
        return this;
    }

    public Set<UUID> getAlivePlayers() {
        return alivePlayers;
    }

    public Set<Player> getAlivePlayerSet() {
        return getAlivePlayers().stream().map(Bukkit::getPlayer).collect(Collectors.toSet());
    }

    public Set<UUID> getDeadPlayers() {
        return getPlayers().stream().filter(uuid -> !getAlivePlayers().contains(uuid)).collect(Collectors.toSet());
    }

    public Set<Player> getDeadPlayerSet() {
        return getDeadPlayers().stream().map(Bukkit::getPlayer).collect(Collectors.toSet());
    }

    public Set<UUID> getPlayers() {
        return players;
    }

    public Set<Player> getPlayerSet() {
        return getPlayers().stream().map(Bukkit::getPlayer).collect(Collectors.toSet());
    }
}
