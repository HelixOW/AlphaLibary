/*
 *
 *  * Copyright (C) <2017>  <AlphaHelixDev>
 *  *
 *  *       This program is free software: you can redistribute it under the
 *  *       terms of the GNU General Public License as published by
 *  *       the Free Software Foundation, either version 3 of the License.
 *  *
 *  *       This program is distributed in the hope that it will be useful,
 *  *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  *       GNU General Public License for more details.
 *  *
 *  *       You should have received a copy of the GNU General Public License
 *  *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package de.alphahelix.alphalibary;

import de.alphahelix.alphalibary.addons.AddonCore;
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
import java.util.HashMap;
import java.util.UUID;

public class AlphaLibary extends JavaPlugin {

    private static AlphaLibary instance;
    private static GameProfileBuilder.GameProfileFile gameProfileFile;
    private static ArenaFile arenaFile;
    private static HashMap<UUID, Double> oldValues = new HashMap<>();

    public static AlphaLibary getInstance() {
        return instance;
    }

    public static GameProfileBuilder.GameProfileFile getGameProfileFile() {
        return gameProfileFile;
    }

    public static ArenaFile getArenaFile() {
        return arenaFile;
    }

    @Override
    public void onLoad() {
        FakeAPI.load();
    }

    @Override
    public void onDisable() {
        FakeAPI.disable();
    }

    @Override
    public void onEnable() {
        instance = this;
        FakeAPI.enable();
        AddonCore.enable();

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
}