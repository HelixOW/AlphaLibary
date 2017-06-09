package de.alphahelix.alphalibary;

import de.alphahelix.alphalibary.arena.ArenaFile;
import de.alphahelix.alphalibary.events.ArmorChangeEvent;
import de.alphahelix.alphalibary.fakeapi.FakeAPI;
import de.alphahelix.alphalibary.netty.PacketListenerAPI;
import de.alphahelix.alphalibary.netty.handler.PacketHandler;
import de.alphahelix.alphalibary.netty.handler.PacketOptions;
import de.alphahelix.alphalibary.netty.handler.ReceivedPacket;
import de.alphahelix.alphalibary.netty.handler.SentPacket;
import de.alphahelix.alphalibary.utils.GameProfileBuilder;
import de.alphahelix.alphalibary.uuid.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class AlphaLibary extends JavaPlugin {

    private static AlphaLibary instance;
    private static GameProfileBuilder.GameProfileFile gameProfileFile;
    private static ArenaFile arenaFile;
    private static ArrayList<String> playersTotal = new ArrayList<>();
    private static ArrayList<String> playersInGame = new ArrayList<>();
    private static ArrayList<String> playersDead = new ArrayList<>();
    private static HashMap<UUID, Double> oldValues = new HashMap<>();

    public static AlphaLibary getInstance() {
        return instance;
    }

    public static GameProfileBuilder.GameProfileFile getGameProfileFile() {
        return gameProfileFile;
    }

    public static ArrayList<String> getPlayersTotal() {
        return playersTotal;
    }

    public static void addPlayerTotal(Player p) {
        playersTotal.add(p.getName());
    }

    public static void addPlayerTotal(String p) {
        playersTotal.add(p);
    }

    public static void removePlayerTotal(Player p) {
        if (playersTotal.contains(p.getName()))
            playersTotal.remove(p.getName());

        if (playersInGame.contains(p.getName()))
            playersInGame.remove(p.getName());

        if (playersDead.contains(p.getName()))
            playersDead.remove(p.getName());
    }

    public static void removePlayerTotal(String p) {
        if (playersTotal.contains(p))
            playersTotal.remove(p);

        if (playersInGame.contains(p))
            playersInGame.remove(p);

        if (playersDead.contains(p))
            playersDead.remove(p);
    }

    public static ArrayList<String> getPlayersInGame() {
        return playersInGame;
    }

    public static void addPlayerInGame(Player p) {
        playersInGame.add(p.getName());
    }

    public static void addPlayerInGame(String p) {
        playersInGame.add(p);
    }

    public static void removePlayerInGame(Player p) {
        if (playersInGame.contains(p.getName()))
            playersInGame.remove(p.getName());

        if (playersDead.contains(p.getName()))
            playersDead.remove(p.getName());
    }

    public static void removePlayerInGame(String p) {
        if (playersInGame.contains(p))
            playersInGame.remove(p);

        if (playersDead.contains(p))
            playersDead.remove(p);
    }

    public static boolean isPlayerInGame(Player p) {
        return playersInGame.contains(p.getName());
    }

    public static boolean isPlayerInGame(String p) {
        return playersInGame.contains(p);
    }

    public static ArrayList<String> getPlayersDead() {
        return playersDead;
    }

    public static void addPlayerDead(Player p) {
        playersDead.add(p.getName());
    }

    public static void addPlayerDead(String p) {
        playersDead.add(p);
    }

    public static void removePlayerDead(Player p) {
        if (playersDead.contains(p.getName()))
            playersDead.remove(p.getName());
    }

    public static void removePlayerDead(String p) {
        if (playersDead.contains(p))
            playersDead.remove(p);
    }

    public static boolean isPlayerDead(Player p) {
        return playersDead.contains(p.getName());
    }

    public static boolean isPlayerDead(String p) {
        return playersDead.contains(p);
    }

    public static ArenaFile getArenaFile() {
        return arenaFile;
    }

    @Override
    public void onLoad() {
        FakeAPI.load();
    }

    @Override
    public void onEnable() {
        instance = this;
        FakeAPI.enable();

        gameProfileFile = new GameProfileBuilder.GameProfileFile();
        arenaFile = new ArenaFile();

        File arenaFolder = new File("plugins/AlphaGameLibary/arenas");

        if (!arenaFolder.exists()) arenaFolder.mkdirs();

        PacketListenerAPI.addPacketHandler(new PacketHandler() {
            @Override
            @PacketOptions(forcePlayer = true)
            public void onSend(SentPacket packet) {
                if (packet.getPacketName().equals("PacketPlayOutUpdateAttributes")) {
                    Player p = packet.getPlayer();

                    if ((int) packet.getPacketValue("a") == p.getEntityId()) {
                        UUID id = UUIDFetcher.getUUID(p);
                        double nV = p.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                        double oV;

                        if (oldValues.containsKey(id)) {
                            if (oldValues.get(id) != nV) {
                                oV = oldValues.get(id);
                                oldValues.put(id, nV);
                                ArmorChangeEvent ace = new ArmorChangeEvent(p, oV, nV);
                                Bukkit.getPluginManager().callEvent(ace);
                            }
                        } else {
                            oldValues.put(id, nV);
                            ArmorChangeEvent ace = new ArmorChangeEvent(p, 0.0, nV);
                            Bukkit.getPluginManager().callEvent(ace);
                        }
                    }
                }
            }

            @Override
            public void onReceive(ReceivedPacket packet) {

            }
        });
    }

    @Override
    public void onDisable() {
        FakeAPI.disable();
    }
}
