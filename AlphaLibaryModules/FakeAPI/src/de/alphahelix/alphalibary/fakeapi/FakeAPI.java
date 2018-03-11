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

package de.alphahelix.alphalibary.fakeapi;

import com.mojang.authlib.GameProfile;
import de.alphahelix.alphalibary.core.AlphaLibary;
import de.alphahelix.alphalibary.core.utilites.UUIDFetcher;
import de.alphahelix.alphalibary.core.utilites.players.PlayerMap;
import de.alphahelix.alphalibary.fakeapi.events.FakeEntityClickEvent;
import de.alphahelix.alphalibary.fakeapi.instances.FakeEntity;
import de.alphahelix.alphalibary.fakeapi.instances.NoSuchFakeEntityException;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import de.alphahelix.alphalibary.reflection.nms.enums.REnumAction;
import de.alphahelix.alphalibary.reflection.nms.enums.REnumHand;
import de.alphahelix.alphalibary.reflection.nms.netty.PacketListenerAPI;
import de.alphahelix.alphalibary.reflection.nms.netty.handler.PacketHandler;
import de.alphahelix.alphalibary.reflection.nms.netty.handler.PacketOptions;
import de.alphahelix.alphalibary.reflection.nms.netty.handler.ReceivedPacket;
import de.alphahelix.alphalibary.reflection.nms.netty.handler.SentPacket;
import de.alphahelix.alphalibary.reflection.nms.wrappers.PlayerInfoDataWrapper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class FakeAPI {

    private static final PacketListenerAPI PACKET_LISTENER_API = new PacketListenerAPI();
    private static final PlayerMap<List<FakeEntity>> FAKE_ENTITIES = new PlayerMap<>();
    private static final Map<Integer, UUID> ENTITY_IDS = new HashMap<>();
    private static final List<String> NPCS = new ArrayList<>();

    /**
     * Gets all {@link FakeEntity}s a {@link Player} can see
     *
     * @return a {@link HashMap} with an {@link ArrayList} related to the players name
     */
    public static PlayerMap<List<FakeEntity>> getFakeEntities() {
        return FAKE_ENTITIES;
    }

    public static void addFakeEntity(Player p, FakeEntity fake) {
        List<FakeEntity> list;
        if (getFakeEntities().containsKey(p.getName())) {
            list = getFakeEntities().get(p.getName());
            list.add(fake);
        } else {
            list = new ArrayList<>();
            list.add(fake);
        }
        getFakeEntities().put(p.getName(), list);
    }

    public static void removeFakeEntity(Player p, FakeEntity fake) {
        List<FakeEntity> list;
        if (getFakeEntities().containsKey(p.getName())) {
            list = getFakeEntities().get(p.getName());
            list.remove(fake);
        } else {
            list = new ArrayList<>();
        }
        getFakeEntities().put(p.getName(), list);
    }

    public static boolean isFakeEntityInRange(Player p, int range) {
        if (!getFakeEntities().containsKey(p.getName())) return false;
        for (Block b : p.getLineOfSight(null, range)) {
            for (FakeEntity fakeEntity : getFakeEntities().get(p.getName())) {
                if ((b.getX() == fakeEntity.getCurrentlocation().getBlockX()
                        && b.getY() == fakeEntity.getCurrentlocation().getBlockY()
                        && b.getZ() == fakeEntity.getCurrentlocation().getBlockZ()
                        || (b.getX() == fakeEntity.getCurrentlocation().getBlockX()
                        && b.getY() == (fakeEntity.getCurrentlocation().getBlockY() + 1)
                        && b.getZ() == fakeEntity.getCurrentlocation().getBlockZ())))
                    return true;
            }
        }
        return false;
    }

    public static FakeEntity getLookedAtFakeEntity(Player p, int range) throws NoSuchFakeEntityException {
        if (!getFakeEntities().containsKey(p.getName())) throw new NoSuchFakeEntityException();

        for (Block b : p.getLineOfSight(null, range)) {
            for (FakeEntity fakeEntity : getFakeEntities().get(p.getName())) {
                if ((b.getX() == fakeEntity.getCurrentlocation().getBlockX()
                        && b.getY() == fakeEntity.getCurrentlocation().getBlockY()
                        && b.getZ() == fakeEntity.getCurrentlocation().getBlockZ()
                        || (b.getX() == fakeEntity.getCurrentlocation().getBlockX()
                        && b.getY() == (fakeEntity.getCurrentlocation().getBlockY() + 1)
                        && b.getZ() == fakeEntity.getCurrentlocation().getBlockZ())))
                    return fakeEntity;
            }
        }
        throw new NoSuchFakeEntityException();
    }

    public static ArrayList<FakeEntity> getFakeEntitysInRadius(Player p, double radius) {
        if (!getFakeEntities().containsKey(p.getName())) return new ArrayList<>();
        ArrayList<FakeEntity> list = new ArrayList<>();
        for (FakeEntity fakePlayer : getFakeEntities().get(p.getName())) {
            if (fakePlayer.getCurrentlocation().distanceSquared(p.getLocation()) <= radius * radius) {
                list.add(fakePlayer);
            }
        }
        return list;
    }

    public static FakeEntity getFakeEntityByObject(Player p, Object fake) throws NoSuchFakeEntityException {
        if (!getFakeEntities().containsKey(p.getName())) throw new NoSuchFakeEntityException();
        for (FakeEntity fakeEntity : getFakeEntities().get(p.getName())) {
            if (fakeEntity.getNmsEntity() == fake) return fakeEntity;
        }
        throw new NoSuchFakeEntityException();
    }

    public static FakeEntity getFakeEntityByName(Player p, String name) throws NoSuchFakeEntityException {
        if (!getFakeEntities().containsKey(p.getName())) throw new NoSuchFakeEntityException();
        for (FakeEntity fakeEntity : getFakeEntities().get(p.getName())) {
            if (ChatColor.stripColor(fakeEntity.getName()).equals(ChatColor.stripColor(name))) return fakeEntity;
        }
        throw new NoSuchFakeEntityException();
    }

    public static FakeEntity getFakeEntityByID(Player p, int entityID) throws NoSuchFakeEntityException {
        if (!getFakeEntities().containsKey(p.getName())) throw new NoSuchFakeEntityException();
        for (FakeEntity fakeEntity : getFakeEntities().get(p.getName())) {
            if (ReflectionUtil.getEntityID(fakeEntity.getNmsEntity()) == entityID) return fakeEntity;
        }
        throw new NoSuchFakeEntityException();
    }


    public static Map<Integer, UUID> getEntityIds() {
        return ENTITY_IDS;
    }

    public static List<String> getNPCS() {
        return NPCS;
    }

    public void enable(JavaPlugin plugin) {
        new FakeRegister().initAll(plugin);
        PACKET_LISTENER_API.load();
        PACKET_LISTENER_API.init();

        Bukkit.getPluginManager().registerEvents(PACKET_LISTENER_API, AlphaLibary.getInstance());


        Bukkit.getOnlinePlayers().forEach(o -> UUIDFetcher.getUUID(o, id -> getEntityIds().put(o.getEntityId(), id)));

        PacketListenerAPI.addPacketHandler(new PacketHandler() {
            @PacketOptions(forcePlayer = true)
            public void onSend(SentPacket packet) {
                if (packet.getPacketName().equals("PacketPlayOutPlayerInfo")) {
                    Object enumAction = packet.getPacketValue("a");

                    if (!enumAction.toString().equals("ADD_PLAYER")) return;

                    ArrayList<Object> newPlayerInfo = new ArrayList<>();

                    for (Object playerInfo : (ArrayList) packet.getPacketValue("b")) {

                        if (PlayerInfoDataWrapper.isUnknown(playerInfo)) {
                            newPlayerInfo.add(playerInfo);
                            continue;
                        }

                        PlayerInfoDataWrapper wrapper = PlayerInfoDataWrapper.getPlayerInfo(playerInfo);
                        OfflinePlayer p = Bukkit.getOfflinePlayer(wrapper.getProfile().getId());

                        if (getNPCS().contains(wrapper.getName())) {
                            ReflectionUtil.getDeclaredField("name", GameProfile.class).set(wrapper.getProfile(), wrapper.getName(), true);

                            newPlayerInfo.add(new PlayerInfoDataWrapper(
                                    wrapper.getProfile(), wrapper.getPing(), wrapper.getGameMode(), p.getName(), packet.getPacket()
                            ).getPlayerInfoData());
                        } else {
                            newPlayerInfo.add(playerInfo);
                        }

                    }
                    packet.setPacketValue("b", newPlayerInfo);
                }
            }

            @PacketOptions(forcePlayer = true)
            public void onReceive(ReceivedPacket packet) {
                if (packet.getPacketName().equals("PacketPlayInUseEntity")) {
                    Player p = packet.getPlayer();
                    int id = (int) packet.getPacketValue("a");
                    try {
                        FakeEntity clicked = getFakeEntityByID(p, id);

                        REnumAction action = REnumAction.values()[ReflectionUtil.getEnumConstantID(packet.getPacketValue("action"))];
                        REnumHand hand = REnumHand.MAIN_HAND;

                        if (packet.getPacketValue("d") != null)
                            hand = REnumHand.values()[ReflectionUtil.getEnumConstantID(packet.getPacketValue("d"))];

                        if (action != REnumAction.INTERACT_AT)
                            Bukkit.getPluginManager().callEvent(new FakeEntityClickEvent(p, clicked, action, hand));
                    } catch (NoSuchFakeEntityException ignore) {
                    }
                }
            }
        });
    }

    public void disable() {
        PACKET_LISTENER_API.disable();
    }
}
