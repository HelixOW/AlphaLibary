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
import de.alphahelix.alphalibary.AlphaLibary;
import de.alphahelix.alphalibary.fakeapi.events.*;
import de.alphahelix.alphalibary.fakeapi.instances.*;
import de.alphahelix.alphalibary.netty.PacketListenerAPI;
import de.alphahelix.alphalibary.netty.handler.PacketHandler;
import de.alphahelix.alphalibary.netty.handler.PacketOptions;
import de.alphahelix.alphalibary.netty.handler.ReceivedPacket;
import de.alphahelix.alphalibary.netty.handler.SentPacket;
import de.alphahelix.alphalibary.nms.PlayerInfoDataWrapper;
import de.alphahelix.alphalibary.nms.enums.REnumAction;
import de.alphahelix.alphalibary.nms.enums.REnumHand;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import de.alphahelix.alphalibary.uuid.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class FakeAPI extends AlphaLibary {

    private static final PacketListenerAPI PACKET_LISTENER_API = new PacketListenerAPI();
    private static final HashMap<String, ArrayList<FakeEntity>> FAKE_ENTITIES = new HashMap<>();
    private static final HashMap<String, ArrayList<FakePlayer>> FAKE_PLAYERS = new HashMap<>();
    private static final HashMap<String, ArrayList<FakeArmorstand>> FAKE_ARMORSTANDS = new HashMap<>();
    private static final HashMap<String, ArrayList<FakeEndercrystal>> FAKE_ENDERCRYSTALS = new HashMap<>();
    private static final HashMap<String, ArrayList<FakeItem>> FAKE_ITEMS = new HashMap<>();
    private static final HashMap<String, ArrayList<FakeMob>> FAKE_MOBS = new HashMap<>();
    private static final HashMap<String, ArrayList<FakeBigItem>> FAKE_BIG_ITEMS = new HashMap<>();
    private static final HashMap<String, ArrayList<FakeXPOrb>> FAKE_XP_ORBS = new HashMap<>();
    private static final HashMap<Integer, UUID> ENTITY_IDS = new HashMap<>();
    private static final ArrayList<String> NPCS = new ArrayList<>();

    /**
     * Gets all {@link FakeEntity}s a {@link Player} can see
     *
     * @return a {@link HashMap} with an {@link ArrayList} related to the players name
     */
    public static HashMap<String, ArrayList<FakeEntity>> getFakeEntities() {
        return FAKE_ENTITIES;
    }

    private static void addFakeEntity(Player p, FakeEntity fake) {
        ArrayList<FakeEntity> list;
        if (getFakeEntities().containsKey(p.getName())) {
            list = getFakeEntities().get(p.getName());
            list.add(fake);
        } else {
            list = new ArrayList<>();
            list.add(fake);
        }
        getFakeEntities().put(p.getName(), list);
    }

    private static void removeFakeEntity(Player p, FakeEntity fake) {
        ArrayList<FakeEntity> list;
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


    /**
     * Gets a {@link HashMap} with all {@link Player}s and which {@link FakePlayer}s they can see
     *
     * @return a {@link HashMap} with all {@link Player}s and which {@link FakePlayer}s they can see
     */
    public static HashMap<String, ArrayList<FakePlayer>> getFakePlayers() {
        return FAKE_PLAYERS;
    }

    /**
     * Adds a new {@link FakePlayer} for a specific {@link Player}
     *
     * @param p    the {@link Player} to add the {@link FakePlayer} for
     * @param fake the {@link FakePlayer} which should be added
     */
    public static void addFakePlayer(Player p, FakePlayer fake) {
        ArrayList<FakePlayer> list;
        if (getFakePlayers().containsKey(p.getName())) {
            list = getFakePlayers().get(p.getName());
            list.add(fake);
        } else {
            list = new ArrayList<>();
            list.add(fake);
        }
        getFakePlayers().put(p.getName(), list);
        addFakeEntity(p, fake);
    }

    /**
     * Removes a {@link FakePlayer} for a specific {@link Player}
     *
     * @param p    the {@link Player} to remove the {@link FakePlayer} for
     * @param fake the {@link FakePlayer} which should be removed
     */
    public static void removeFakePlayer(Player p, FakePlayer fake) {
        ArrayList<FakePlayer> list;
        if (getFakePlayers().containsKey(p.getName())) {
            list = getFakePlayers().get(p.getName());
            list.remove(fake);
        } else {
            list = new ArrayList<>();
        }
        getFakePlayers().put(p.getName(), list);
        removeFakeEntity(p, fake);
    }

    /**
     * Checks if a specific {@link Player} looks at a {@link FakePlayer} in a certain range
     *
     * @param p     the {@link Player} to check for
     * @param range the range in which it should be checked
     * @return whether or not the {@link Player} looks at a {@link FakePlayer}
     */
    public static boolean isFakePlayerInRange(Player p, int range) {
        if (!getFakePlayers().containsKey(p.getName())) return false;
        for (Block b : p.getLineOfSight(null, range)) {
            for (FakePlayer fakePlayer : getFakePlayers().get(p.getName())) {
                if ((b.getX() == fakePlayer.getCurrentlocation().getBlockX()
                        && b.getY() == fakePlayer.getCurrentlocation().getBlockY()
                        && b.getZ() == fakePlayer.getCurrentlocation().getBlockZ()
                        || (b.getX() == fakePlayer.getCurrentlocation().getBlockX()
                        && b.getY() == (fakePlayer.getCurrentlocation().getBlockY() + 1)
                        && b.getZ() == fakePlayer.getCurrentlocation().getBlockZ())))
                    return true;
            }
        }
        return false;
    }

    /**
     * Gets the {@link FakePlayer} at which the {@link Player} is currently looking at
     *
     * @param p     the {@link Player} which looks at the {@link FakePlayer}
     * @param range the range in which it should stand
     * @return the {@link FakePlayer} inside the range
     * @throws NoSuchFakeEntityException if there is nor {@link FakePlayer} in the range
     */
    public static FakePlayer getLookedAtFakePlayer(Player p, int range) throws NoSuchFakeEntityException {
        if (!getFakePlayers().containsKey(p.getName())) throw new NoSuchFakeEntityException();

        for (Block b : p.getLineOfSight(null, range)) {
            for (FakePlayer fakePlayer : getFakePlayers().get(p.getName())) {
                if ((b.getX() == fakePlayer.getCurrentlocation().getBlockX()
                        && b.getY() == fakePlayer.getCurrentlocation().getBlockY()
                        && b.getZ() == fakePlayer.getCurrentlocation().getBlockZ()
                        || (b.getX() == fakePlayer.getCurrentlocation().getBlockX()
                        && b.getY() == (fakePlayer.getCurrentlocation().getBlockY() + 1)
                        && b.getZ() == fakePlayer.getCurrentlocation().getBlockZ())))
                    return fakePlayer;
            }
        }
        throw new NoSuchFakeEntityException();
    }

    /**
     * Gets a {@link ArrayList} with all {@link FakePlayer} inside a certain radius
     *
     * @param p      the {@link Player} for which it should search for
     * @param radius the radius in which should be searched for
     * @return a {@link ArrayList} with all {@link FakePlayer} inside the radius
     */
    public static ArrayList<FakePlayer> getFakePlayersInRadius(Player p, double radius) {
        if (!getFakePlayers().containsKey(p.getName())) return new ArrayList<>();
        ArrayList<FakePlayer> list = new ArrayList<>();
        for (FakePlayer fakePlayer : getFakePlayers().get(p.getName())) {
            if (fakePlayer.getCurrentlocation().distanceSquared(p.getLocation()) <= radius * radius) {
                list.add(fakePlayer);
            }
        }
        return list;
    }

    /**
     * Gets a {@link FakePlayer} by its NMSEntity
     *
     * @param p    the {@link Player} who can see this {@link FakePlayer}
     * @param fake the NMSEntity of the {@link FakePlayer}
     * @return the {@link FakePlayer} with his NMSEntity
     * @throws NoSuchFakeEntityException when there is no {@link FakePlayer} with this {@link Object}
     */
    public static FakePlayer getFakePlayerByObject(Player p, Object fake) throws NoSuchFakeEntityException {
        if (!getFakePlayers().containsKey(p.getName())) throw new NoSuchFakeEntityException();
        for (FakePlayer fakePlayer : getFakePlayers().get(p.getName())) {
            if (fakePlayer.getNmsEntity() == fake) return fakePlayer;
        }
        throw new NoSuchFakeEntityException();
    }

    /**
     * Gets a {@link FakePlayer} by its Name
     *
     * @param p    the {@link Player} who can see this {@link FakePlayer}
     * @param name the name of the {@link FakePlayer} (can include color)
     * @return the {@link FakePlayer} with his NMSEntity
     * @throws NoSuchFakeEntityException when there is no {@link FakePlayer} with this name
     */
    public static FakePlayer getFakePlayerByName(Player p, String name) throws NoSuchFakeEntityException {
        if (!getFakePlayers().containsKey(p.getName())) throw new NoSuchFakeEntityException();
        for (FakePlayer fakePlayer : getFakePlayers().get(p.getName())) {
            if (ChatColor.stripColor(fakePlayer.getName()).equals(ChatColor.stripColor(name))) return fakePlayer;
        }
        throw new NoSuchFakeEntityException();
    }

    /**
     * Gets a {@link FakePlayer} by its EntityID
     *
     * @param p        the {@link Player} who can see this {@link FakePlayer}
     * @param entityID the entityid of the {@link FakePlayer}
     * @return the {@link FakePlayer} with his NMSEntity
     * @throws NoSuchFakeEntityException when there is no {@link FakePlayer} with this id
     */
    public static FakePlayer getFakePlayerByID(Player p, int entityID) throws NoSuchFakeEntityException {
        if (!getFakePlayers().containsKey(p.getName())) throw new NoSuchFakeEntityException();
        for (FakePlayer fakePlayer : getFakePlayers().get(p.getName())) {
            if (ReflectionUtil.getEntityID(fakePlayer.getNmsEntity()) == entityID) return fakePlayer;
        }
        throw new NoSuchFakeEntityException();
    }

    /**
     * Gets a {@link FakePlayer} by its {@link Location}
     *
     * @param p   the {@link Player} who can see this {@link FakePlayer}
     * @param loc the {@link Location} of the {@link FakePlayer}
     * @return the {@link FakePlayer} at his {@link Location}
     * @throws NoSuchFakeEntityException when there is no {@link FakePlayer} at this {@link Location}
     */
    public static FakePlayer getFakePlayerByLocation(Player p, Location loc) throws NoSuchFakeEntityException {
        if (!getFakePlayers().containsKey(p.getName())) throw new NoSuchFakeEntityException();
        for (FakePlayer fakePlayer : getFakePlayers().get(p.getName())) {
            if ((loc.getBlockX() == fakePlayer.getCurrentlocation().getBlockX()
                    && loc.getBlockY() == fakePlayer.getCurrentlocation().getBlockY()
                    && loc.getBlockZ() == fakePlayer.getCurrentlocation().getBlockZ()
                    || (loc.getBlockX() == fakePlayer.getCurrentlocation().getBlockX()
                    && loc.getBlockY() == (fakePlayer.getCurrentlocation().getBlockY() + 1)
                    && loc.getBlockZ() == fakePlayer.getCurrentlocation().getBlockZ()))) {
                return fakePlayer;
            }
        }
        throw new NoSuchFakeEntityException();
    }

    /**
     * Gets a {@link FakePlayer} by its Unique ID
     *
     * @param p    the {@link Player} who can see this {@link FakePlayer}
     * @param uuid the {@link UUID} of the {@link FakePlayer}
     * @return the {@link FakePlayer} at his {@link Location}
     * @throws NoSuchFakeEntityException when there is no {@link FakePlayer} with this {@link UUID}
     */
    public static FakePlayer getFakePlayerByID(Player p, UUID uuid) throws NoSuchFakeEntityException {
        if (!getFakePlayers().containsKey(p.getName())) throw new NoSuchFakeEntityException();
        for (FakePlayer fakePlayer : getFakePlayers().get(p.getName())) {
            if (fakePlayer.getSkinUUID() == uuid) return fakePlayer;
        }
        throw new NoSuchFakeEntityException();
    }

    /**
     * Gets a {@link HashMap} with all {@link Player}s and which {@link FakeArmorstand}s they can see
     *
     * @return a {@link HashMap} with all {@link Player}s and which {@link FakeArmorstand}s they can see
     */
    public static HashMap<String, ArrayList<FakeArmorstand>> getFakeArmorstands() {
        return FAKE_ARMORSTANDS;
    }

    /**
     * Adds a new {@link FakeArmorstand} for a specific {@link Player}
     *
     * @param p    the {@link Player} to add the {@link FakeArmorstand} for
     * @param fake the {@link FakeArmorstand} which should be added
     */
    public static void addFakeArmorstand(Player p, FakeArmorstand fake) {
        ArrayList<FakeArmorstand> list;
        if (getFakeArmorstands().containsKey(p.getName())) {
            list = getFakeArmorstands().get(p.getName());
            list.add(fake);
        } else {
            list = new ArrayList<>();
            list.add(fake);
        }
        getFakeArmorstands().put(p.getName(), list);
        addFakeEntity(p, fake);
    }

    /**
     * Removes a {@link FakeArmorstand} for a specific {@link Player}
     *
     * @param p    the {@link Player} to remove the {@link FakeArmorstand} for
     * @param fake the {@link FakeArmorstand} which should be removed
     */
    public static void removeFakeArmorstand(Player p, FakeArmorstand fake) {
        ArrayList<FakeArmorstand> list;
        if (getFakeArmorstands().containsKey(p.getName())) {
            list = getFakeArmorstands().get(p.getName());
            list.remove(fake);
        } else {
            list = new ArrayList<>();
        }
        getFakeArmorstands().put(p.getName(), list);
        removeFakeEntity(p, fake);
    }

    /**
     * Checks if a specific {@link Player} looks at a {@link FakeArmorstand} in a certain range
     *
     * @param p     the {@link Player} to check for
     * @param range the range in which it should be checked
     * @return whether or not the {@link Player} looks at a {@link FakeArmorstand}
     */
    public static boolean isFakeArmorstandInRange(Player p, int range) {
        if (!getFakeArmorstands().containsKey(p.getName())) return false;
        for (Block b : p.getLineOfSight(null, range)) {
            for (FakeArmorstand fakeArmorstand : getFakeArmorstands().get(p.getName())) {
                if ((b.getX() == fakeArmorstand.getCurrentlocation().getBlockX()
                        && b.getY() == fakeArmorstand.getCurrentlocation().getBlockY()
                        && b.getZ() == fakeArmorstand.getCurrentlocation().getBlockZ()
                        || (b.getX() == fakeArmorstand.getCurrentlocation().getBlockX()
                        && b.getY() == (fakeArmorstand.getCurrentlocation().getBlockY() + 1)
                        && b.getZ() == fakeArmorstand.getCurrentlocation().getBlockZ())))
                    return true;
            }
        }
        return false;
    }

    /**
     * Gets the {@link FakeArmorstand} at which the {@link Player} is currently looking at
     *
     * @param p     the {@link Player} which looks at the {@link FakeArmorstand}
     * @param range the range in which it should stand
     * @return the {@link FakeArmorstand} inside the range
     * @throws NoSuchFakeEntityException when there is no {@link FakeArmorstand} in range
     */
    public static FakeArmorstand getLookedAtFakeArmorstand(Player p, int range) throws NoSuchFakeEntityException {
        if (!getFakeArmorstands().containsKey(p.getName())) throw new NoSuchFakeEntityException();
        for (Block b : p.getLineOfSight(null, range)) {
            for (FakeArmorstand fakeArmorstand : getFakeArmorstands().get(p.getName())) {
                if ((b.getX() == fakeArmorstand.getCurrentlocation().getBlockX()
                        && b.getY() == fakeArmorstand.getCurrentlocation().getBlockY()
                        && b.getZ() == fakeArmorstand.getCurrentlocation().getBlockZ()
                        || (b.getX() == fakeArmorstand.getCurrentlocation().getBlockX()
                        && b.getY() == (fakeArmorstand.getCurrentlocation().getBlockY() + 1)
                        && b.getZ() == fakeArmorstand.getCurrentlocation().getBlockZ())))
                    return fakeArmorstand;
            }
        }
        throw new NoSuchFakeEntityException();
    }

    /**
     * Gets a {@link ArrayList} with all {@link FakeArmorstand} inside a certain radius
     *
     * @param p      the {@link Player} for which it should search for
     * @param radius the radius in which should be searched for
     * @return a {@link ArrayList} with all {@link FakeArmorstand} inside the radius
     */
    public static ArrayList<FakeArmorstand> getFakeArmorstandsInRadius(Player p, double radius) {
        if (!getFakeArmorstands().containsKey(p.getName())) return new ArrayList<>();
        ArrayList<FakeArmorstand> list = new ArrayList<>();
        for (FakeArmorstand fakeArmorstand : getFakeArmorstands().get(p.getName())) {
            if (fakeArmorstand.getCurrentlocation().distanceSquared(p.getLocation()) <= radius * radius) {
                list.add(fakeArmorstand);
            }
        }
        return list;
    }

    /**
     * Gets a {@link FakeArmorstand} by its NMSEntity
     *
     * @param p    the {@link Player} who can see this {@link FakeArmorstand}
     * @param fake the NMSEntity of the {@link FakeArmorstand}
     * @return the {@link FakeArmorstand} with his NMSEntity
     * @throws NoSuchFakeEntityException when there is no {@link FakeArmorstand} with that {@link Object}
     */
    public static FakeArmorstand getFakeArmorstandByObject(Player p, Object fake) throws NoSuchFakeEntityException {
        if (!getFakeArmorstands().containsKey(p.getName())) throw new NoSuchFakeEntityException();
        for (FakeArmorstand fakeArmorstand : getFakeArmorstands().get(p.getName())) {
            if (fakeArmorstand.getNmsEntity() == fake) return fakeArmorstand;
        }
        throw new NoSuchFakeEntityException();
    }

    /**
     * Gets a {@link FakeArmorstand} by its Name
     *
     * @param p    the {@link Player} who can see this {@link FakeArmorstand}
     * @param name the name of the {@link FakeArmorstand} (can include color)
     * @return the {@link FakeArmorstand} with his NMSEntity
     * @throws NoSuchFakeEntityException when there is no {@link FakeArmorstand} with this name
     */
    public static FakeArmorstand getFakeArmorstandByName(Player p, String name) throws NoSuchFakeEntityException {
        if (!getFakeArmorstands().containsKey(p.getName())) throw new NoSuchFakeEntityException();
        for (FakeArmorstand fakeArmorstand : getFakeArmorstands().get(p.getName())) {
            if (ChatColor.stripColor(fakeArmorstand.getName()).equals(ChatColor.stripColor(name)))
                return fakeArmorstand;
        }
        throw new NoSuchFakeEntityException();
    }

    /**
     * Gets a {@link FakeArmorstand} by its EntityID
     *
     * @param p        the {@link Player} who can see this {@link FakeArmorstand}
     * @param entityID the entityid of the {@link FakeArmorstand}
     * @return the {@link FakeArmorstand} with his NMSEntity
     * @throws NoSuchFakeEntityException when there is no {@link FakeArmorstand} with this id
     */
    public static FakeArmorstand getFakeArmorstandByID(Player p, int entityID) throws NoSuchFakeEntityException {
        if (!getFakeArmorstands().containsKey(p.getName())) throw new NoSuchFakeEntityException();
        for (FakeArmorstand fakeArmorstand : getFakeArmorstands().get(p.getName())) {
            if (ReflectionUtil.getEntityID(fakeArmorstand.getNmsEntity()) == entityID) return fakeArmorstand;
        }
        throw new NoSuchFakeEntityException();
    }

    /**
     * Gets a {@link FakeArmorstand} by its {@link Location}
     *
     * @param p   the {@link Player} who can see this {@link FakeArmorstand}
     * @param loc the {@link Location} of the {@link FakeArmorstand}
     * @return the {@link FakeArmorstand} at his {@link Location}
     * @throws NoSuchFakeEntityException when there is no {@link FakeArmorstand} at this {@link Location}
     */
    public static FakeArmorstand getFakeArmorstandByLocation(Player p, Location loc) throws NoSuchFakeEntityException {
        if (!getFakeArmorstands().containsKey(p.getName())) throw new NoSuchFakeEntityException();
        for (FakeArmorstand fakeArmorstand : getFakeArmorstands().get(p.getName())) {
            if ((loc.getBlockX() == fakeArmorstand.getCurrentlocation().getBlockX()
                    && loc.getBlockY() == fakeArmorstand.getCurrentlocation().getBlockY()
                    && loc.getBlockZ() == fakeArmorstand.getCurrentlocation().getBlockZ()
                    || (loc.getBlockX() == fakeArmorstand.getCurrentlocation().getBlockX()
                    && loc.getBlockY() == (fakeArmorstand.getCurrentlocation().getBlockY() + 1)
                    && loc.getBlockZ() == fakeArmorstand.getCurrentlocation().getBlockZ()))) {
                return fakeArmorstand;
            }
        }
        throw new NoSuchFakeEntityException();
    }

    /**
     * Gets a {@link HashMap} with all {@link Player}s and which {@link FakeEndercrystal}s they can see
     *
     * @return a {@link HashMap} with all {@link Player}s and which {@link FakeEndercrystal}s they can see
     */
    public static HashMap<String, ArrayList<FakeEndercrystal>> getFakeEndercrystals() {
        return FAKE_ENDERCRYSTALS;
    }

    /**
     * Adds a new {@link FakeEndercrystal} for a specific {@link Player}
     *
     * @param p    the {@link Player} to add the {@link FakeEndercrystal} for
     * @param fake the {@link FakeEndercrystal} which should be added
     */
    public static void addFakeEndercrystal(Player p, FakeEndercrystal fake) {
        ArrayList<FakeEndercrystal> list;
        if (getFakeEndercrystals().containsKey(p.getName())) {
            list = getFakeEndercrystals().get(p.getName());
            list.add(fake);
        } else {
            list = new ArrayList<>();
            list.add(fake);
        }
        getFakeEndercrystals().put(p.getName(), list);
        addFakeEntity(p, fake);
    }

    /**
     * Removes a {@link FakeEndercrystal} for a specific {@link Player}
     *
     * @param p    the {@link Player} to remove the {@link FakeEndercrystal} for
     * @param fake the {@link FakeEndercrystal} which should be removed
     */
    public static void removeFakeEndercrystal(Player p, FakeEndercrystal fake) {
        ArrayList<FakeEndercrystal> list;
        if (getFakeEndercrystals().containsKey(p.getName())) {
            list = getFakeEndercrystals().get(p.getName());
            list.remove(fake);
        } else {
            list = new ArrayList<>();
        }
        getFakeEndercrystals().put(p.getName(), list);
        removeFakeEntity(p, fake);
    }

    /**
     * Checks if a specific {@link Player} looks at a {@link FakeEndercrystal} in a certain range
     *
     * @param p     the {@link Player} to check for
     * @param range the range in which it should be checked
     * @return whether or not the {@link Player} looks at a {@link FakeEndercrystal}
     */
    public static boolean isFakeEndercrystalInRange(Player p, int range) {
        if (!getFakeEndercrystals().containsKey(p.getName())) return false;
        for (Block b : p.getLineOfSight(null, range)) {
            for (FakeEndercrystal fakeEndercrystal : getFakeEndercrystals().get(p.getName())) {
                if ((b.getX() == fakeEndercrystal.getCurrentlocation().getBlockX()
                        && b.getY() == fakeEndercrystal.getCurrentlocation().getBlockY()
                        && b.getZ() == fakeEndercrystal.getCurrentlocation().getBlockZ()
                        || (b.getX() == fakeEndercrystal.getCurrentlocation().getBlockX()
                        && b.getY() == (fakeEndercrystal.getCurrentlocation().getBlockY() + 1)
                        && b.getZ() == fakeEndercrystal.getCurrentlocation().getBlockZ())
                        || (b.getX() == fakeEndercrystal.getCurrentlocation().getBlockX()
                        && b.getY() == (fakeEndercrystal.getCurrentlocation().getBlockY() - 1)
                        && b.getZ() == fakeEndercrystal.getCurrentlocation().getBlockZ())))
                    return true;
            }
        }
        return false;
    }

    /**
     * Gets the {@link FakeEndercrystal} at which the {@link Player} is currently looking at
     *
     * @param p     the {@link Player} which looks at the {@link FakeEndercrystal}
     * @param range the range in which it should stand
     * @return the {@link FakeEndercrystal} inside the range
     * @throws NoSuchFakeEntityException when there is no {@link FakeEndercrystal} in the range
     */
    public static FakeEndercrystal getLookedAtFakeEndercrystal(Player p, int range) throws NoSuchFakeEntityException {
        if (!getFakeEndercrystals().containsKey(p.getName())) throw new NoSuchFakeEntityException();
        for (Block b : p.getLineOfSight(null, range)) {
            for (FakeEndercrystal fakeEndercrystal : getFakeEndercrystals().get(p.getName())) {
                if ((b.getX() == fakeEndercrystal.getCurrentlocation().getBlockX()
                        && b.getY() == fakeEndercrystal.getCurrentlocation().getBlockY()
                        && b.getZ() == fakeEndercrystal.getCurrentlocation().getBlockZ()
                        || (b.getX() == fakeEndercrystal.getCurrentlocation().getBlockX()
                        && b.getY() == (fakeEndercrystal.getCurrentlocation().getBlockY() - 1)
                        && b.getZ() == fakeEndercrystal.getCurrentlocation().getBlockZ())
                        || (b.getX() == fakeEndercrystal.getCurrentlocation().getBlockX()
                        && b.getY() == (fakeEndercrystal.getCurrentlocation().getBlockY() + 1)
                        && b.getZ() == fakeEndercrystal.getCurrentlocation().getBlockZ())))
                    return fakeEndercrystal;
            }
        }
        throw new NoSuchFakeEntityException();
    }

    /**
     * Gets a {@link ArrayList} with all {@link FakeEndercrystal} inside a certain radius
     *
     * @param p      the {@link Player} for which it should search for
     * @param radius the radius in which should be searched for
     * @return a {@link ArrayList} with all {@link FakeEndercrystal} inside the radius
     */
    public static ArrayList<FakeEndercrystal> getFakeEndercrystalsInRadius(Player p, double radius) {
        if (!getFakeEndercrystals().containsKey(p.getName())) return new ArrayList<>();
        ArrayList<FakeEndercrystal> list = new ArrayList<>();
        for (FakeEndercrystal fakeEndercrystal : getFakeEndercrystals().get(p.getName())) {
            if (fakeEndercrystal.getCurrentlocation().distanceSquared(p.getLocation()) <= radius * radius) {
                list.add(fakeEndercrystal);
            }
        }
        return list;
    }

    /**
     * Gets a {@link FakeEndercrystal} by its NMSEntity
     *
     * @param p    the {@link Player} who can see this {@link FakeEndercrystal}
     * @param fake the NMSEntity of the {@link FakeEndercrystal}
     * @return the {@link FakeEndercrystal} with his NMSEntity
     * @throws NoSuchFakeEntityException when there is no {@link FakeEndercrystal} with this Object
     */
    public static FakeEndercrystal getFakeEndercrystalByObject(Player p, Object fake) throws NoSuchFakeEntityException {
        if (!getFakeEndercrystals().containsKey(p.getName())) throw new NoSuchFakeEntityException();
        for (FakeEndercrystal fakeEndercrystal : getFakeEndercrystals().get(p.getName())) {
            if (fakeEndercrystal.getNmsEntity() == fake) return fakeEndercrystal;
        }
        throw new NoSuchFakeEntityException();
    }

    /**
     * Gets a {@link FakeEndercrystal} by its Name
     *
     * @param p    the {@link Player} who can see this {@link FakeEndercrystal}
     * @param name the name of the {@link FakeEndercrystal} (can include color)
     * @return the {@link FakeEndercrystal} with his NMSEntity
     * @throws NoSuchFakeEntityException when there is no {@link FakeEndercrystal} with this name
     */
    public static FakeEndercrystal getFakeEndercrystalByName(Player p, String name) throws NoSuchFakeEntityException {
        if (!getFakeEndercrystals().containsKey(p.getName())) throw new NoSuchFakeEntityException();
        for (FakeEndercrystal fakeEndercrystal : getFakeEndercrystals().get(p.getName())) {
            if (ChatColor.stripColor(fakeEndercrystal.getName()).equals(ChatColor.stripColor(name)))
                return fakeEndercrystal;
        }
        throw new NoSuchFakeEntityException();
    }

    /**
     * Gets a {@link FakeEndercrystal} by its EntityID
     *
     * @param p        the {@link Player} who can see this {@link FakeEndercrystal}
     * @param entityID the entityid of the {@link FakeEndercrystal}
     * @return the {@link FakeEndercrystal} with his NMSEntity
     * @throws NoSuchFakeEntityException when there is no {@link FakeEndercrystal} with this id
     */
    public static FakeEndercrystal getFakeEndercrystalByID(Player p, int entityID) throws NoSuchFakeEntityException {
        if (!getFakeEndercrystals().containsKey(p.getName())) throw new NoSuchFakeEntityException();
        for (FakeEndercrystal fakeEndercrystal : getFakeEndercrystals().get(p.getName())) {
            if (ReflectionUtil.getEntityID(fakeEndercrystal.getNmsEntity()) == entityID) return fakeEndercrystal;
        }
        throw new NoSuchFakeEntityException();
    }

    /**
     * Gets a {@link FakeEndercrystal} by its {@link Location}
     *
     * @param p   the {@link Player} who can see this {@link FakeEndercrystal}
     * @param loc the {@link Location} of the {@link FakeEndercrystal}
     * @return the {@link FakeEndercrystal} at his {@link Location}
     * @throws NoSuchFakeEntityException when there is no {@link FakeEndercrystal} at this {@link Location}
     */
    public static FakeEndercrystal getFakeEndercrystalByLocation(Player p, Location loc) throws NoSuchFakeEntityException {
        if (!getFakeEndercrystals().containsKey(p.getName())) throw new NoSuchFakeEntityException();
        for (FakeEndercrystal fakeEndercrystal : getFakeEndercrystals().get(p.getName())) {
            if ((loc.getBlockX() == fakeEndercrystal.getCurrentlocation().getBlockX()
                    && loc.getBlockY() == fakeEndercrystal.getCurrentlocation().getBlockY()
                    && loc.getBlockZ() == fakeEndercrystal.getCurrentlocation().getBlockZ()
                    || (loc.getBlockX() == fakeEndercrystal.getCurrentlocation().getBlockX()
                    && loc.getBlockY() == (fakeEndercrystal.getCurrentlocation().getBlockY() + 1)
                    && loc.getBlockZ() == fakeEndercrystal.getCurrentlocation().getBlockZ()))) {
                return fakeEndercrystal;
            }
        }
        throw new NoSuchFakeEntityException();
    }

    /**
     * Gets a {@link HashMap} with all {@link Player}s and which {@link FakeItem}s they can see
     *
     * @return a {@link HashMap} with all {@link Player}s and which {@link FakeItem}s they can see
     */
    public static HashMap<String, ArrayList<FakeItem>> getFakeItems() {
        return FAKE_ITEMS;
    }

    /**
     * Adds a new {@link FakeItem} for a specific {@link Player}
     *
     * @param p    the {@link Player} to add the {@link FakeItem} for
     * @param fake the {@link FakeItem} which should be added
     */
    public static void addFakeItem(Player p, FakeItem fake) {
        ArrayList<FakeItem> list;
        if (getFakeItems().containsKey(p.getName())) {
            list = getFakeItems().get(p.getName());
            list.add(fake);
        } else {
            list = new ArrayList<>();
            list.add(fake);
        }
        getFakeItems().put(p.getName(), list);
        addFakeEntity(p, fake);
    }

    /**
     * Removes a {@link FakeItem} for a specific {@link Player}
     *
     * @param p    the {@link Player} to remove the {@link FakeItem} for
     * @param fake the {@link FakeItem} which should be removed
     */
    public static void removeFakeItem(Player p, FakeItem fake) {
        ArrayList<FakeItem> list;
        if (getFakeItems().containsKey(p.getName())) {
            list = getFakeItems().get(p.getName());
            list.remove(fake);
        } else {
            list = new ArrayList<>();
        }
        getFakeItems().put(p.getName(), list);
        removeFakeEntity(p, fake);
    }

    /**
     * Checks if a specific {@link Player} looks at a {@link FakeItem} in a certain range
     *
     * @param p     the {@link Player} to check for
     * @param range the range in which it should be checked
     * @return whether or not the {@link Player} looks at a {@link FakeItem}
     */
    public static boolean isFakeItemInRange(Player p, int range) {
        if (!getFakeItems().containsKey(p.getName())) return false;
        for (Block b : p.getLineOfSight(null, range)) {
            for (FakeItem fakeItem : getFakeItems().get(p.getName())) {
                if ((b.getX() == fakeItem.getCurrentlocation().getBlockX()
                        && b.getY() == fakeItem.getCurrentlocation().getBlockY()
                        && b.getZ() == fakeItem.getCurrentlocation().getBlockZ()
                        || (b.getX() == fakeItem.getCurrentlocation().getBlockX()
                        && b.getY() == (fakeItem.getCurrentlocation().getBlockY() + 1)
                        && b.getZ() == fakeItem.getCurrentlocation().getBlockZ())))
                    return true;
            }
        }
        return false;
    }

    /**
     * Gets the {@link FakeItem} at which the {@link Player} is currently looking at
     *
     * @param p     the {@link Player} which looks at the {@link FakeItem}
     * @param range the range in which it should stand
     * @return the {@link FakeItem} inside the range
     * @throws NoSuchFakeEntityException when there is no {@link FakeItem} in the range
     */
    public static FakeItem getFakeItemInRange(Player p, int range) throws NoSuchFakeEntityException {
        if (!getFakeItems().containsKey(p.getName())) throw new NoSuchFakeEntityException();
        for (Block b : p.getLineOfSight(null, range)) {
            for (FakeItem fakeItem : getFakeItems().get(p.getName())) {
                if ((b.getX() == fakeItem.getCurrentlocation().getBlockX()
                        && b.getY() == fakeItem.getCurrentlocation().getBlockY()
                        && b.getZ() == fakeItem.getCurrentlocation().getBlockZ()
                        || (b.getX() == fakeItem.getCurrentlocation().getBlockX()
                        && b.getY() == (fakeItem.getCurrentlocation().getBlockY() + 1)
                        && b.getZ() == fakeItem.getCurrentlocation().getBlockZ())))
                    return fakeItem;
            }
        }
        throw new NoSuchFakeEntityException();
    }

    /**
     * Gets a {@link ArrayList} with all {@link FakeItem} inside a certain radius
     *
     * @param p      the {@link Player} for which it should search for
     * @param radius the radius in which should be searched for
     * @return a {@link ArrayList} with all {@link FakeItem} inside the radius
     */
    public static ArrayList<FakeItem> getFakeItemsInRadius(Player p, double radius) {
        if (!getFakeItems().containsKey(p.getName())) return new ArrayList<>();
        ArrayList<FakeItem> list = new ArrayList<>();
        for (FakeItem fakeItem : getFakeItems().get(p.getName())) {
            if (fakeItem.getCurrentlocation().distanceSquared(p.getLocation()) <= radius * radius) {
                list.add(fakeItem);
            }
        }
        return list;
    }

    /**
     * Gets a {@link FakeItem} by its NMSEntity
     *
     * @param p    the {@link Player} who can see this {@link FakeItem}
     * @param fake the NMSEntity of the {@link FakeItem}
     * @return the {@link FakeItem} with his NMSEntity
     * @throws NoSuchFakeEntityException when there is no {@link FakeItem} with this {@link Object}
     */
    public static FakeItem getFakeItemByObject(Player p, Object fake) throws NoSuchFakeEntityException {
        if (!getFakeItems().containsKey(p.getName())) throw new NoSuchFakeEntityException();
        for (FakeItem fakeItem : getFakeItems().get(p.getName())) {
            if (fakeItem.getNmsEntity() == fake) return fakeItem;
        }
        throw new NoSuchFakeEntityException();
    }

    /**
     * Gets a {@link FakeItem} by its Name
     *
     * @param p    the {@link Player} who can see this {@link FakeItem}
     * @param name the name of the {@link FakeItem} (can include color)
     * @return the {@link FakeItem} with his NMSEntity
     * @throws NoSuchFakeEntityException when there is no {@link FakeItem} with this name
     */
    public static FakeItem getFakeItemByName(Player p, String name) throws NoSuchFakeEntityException {
        if (!getFakeItems().containsKey(p.getName())) throw new NoSuchFakeEntityException();
        for (FakeItem fakeItem : getFakeItems().get(p.getName())) {
            if (ChatColor.stripColor(fakeItem.getName()).equals(ChatColor.stripColor(name))) return fakeItem;
        }
        throw new NoSuchFakeEntityException();
    }

    /**
     * Gets a {@link FakeItem} by its EntityID
     *
     * @param p        the {@link Player} who can see this {@link FakeItem}
     * @param entityID the entityid of the {@link FakeItem}
     * @return the {@link FakeItem} with his NMSEntity
     * @throws NoSuchFakeEntityException when there is no {@link FakeItem} with this id
     */
    public static FakeItem getFakeItemByID(Player p, int entityID) throws NoSuchFakeEntityException {
        if (!getFakeItems().containsKey(p.getName())) throw new NoSuchFakeEntityException();
        for (FakeItem fakeItem : getFakeItems().get(p.getName())) {
            if (ReflectionUtil.getEntityID(fakeItem.getNmsEntity()) == entityID) return fakeItem;
        }
        throw new NoSuchFakeEntityException();
    }

    /**
     * Gets a {@link FakeItem} by its {@link Location}
     *
     * @param p   the {@link Player} who can see this {@link FakeItem}
     * @param loc the {@link Location} of the {@link FakeItem}
     * @return the {@link FakeItem} at his {@link Location}
     * @throws NoSuchFakeEntityException when there is no {@link FakeItem} at this {@link Location}
     */
    public static FakeItem getFakeItemByLocation(Player p, Location loc) throws NoSuchFakeEntityException {
        if (!getFakeItems().containsKey(p.getName())) throw new NoSuchFakeEntityException();
        for (FakeItem fakeItem : getFakeItems().get(p.getName())) {
            if ((loc.getBlockX() == fakeItem.getCurrentlocation().getBlockX()
                    && loc.getBlockY() == fakeItem.getCurrentlocation().getBlockY()
                    && loc.getBlockZ() == fakeItem.getCurrentlocation().getBlockZ()
                    || (loc.getBlockX() == fakeItem.getCurrentlocation().getBlockX()
                    && loc.getBlockY() == (fakeItem.getCurrentlocation().getBlockY() + 1)
                    && loc.getBlockZ() == fakeItem.getCurrentlocation().getBlockZ()))) {
                return fakeItem;
            }
        }
        throw new NoSuchFakeEntityException();
    }

    /**
     * Gets a {@link HashMap} with all {@link Player}s and which {@link FakeMob}s they can see
     *
     * @return a {@link HashMap} with all {@link Player}s and which {@link FakeMob}s they can see
     */
    public static HashMap<String, ArrayList<FakeMob>> getFakeMobs() {
        return FAKE_MOBS;
    }

    /**
     * Adds a new {@link FakeMob} for a specific {@link Player}
     *
     * @param p    the {@link Player} to add the {@link FakeMob} for
     * @param fake the {@link FakeMob} which should be added
     */
    public static void addFakeMob(Player p, FakeMob fake) {
        ArrayList<FakeMob> list;
        if (getFakeMobs().containsKey(p.getName())) {
            list = getFakeMobs().get(p.getName());
            list.add(fake);
        } else {
            list = new ArrayList<>();
            list.add(fake);
        }
        getFakeMobs().put(p.getName(), list);
        addFakeEntity(p, fake);
    }

    /**
     * Removes a {@link FakeMob} for a specific {@link Player}
     *
     * @param p    the {@link Player} to remove the {@link FakeMob} for
     * @param fake the {@link FakeMob} which should be removed
     */
    public static void removeFakeMob(Player p, FakeMob fake) {
        ArrayList<FakeMob> list;
        if (getFakeMobs().containsKey(p.getName())) {
            list = getFakeMobs().get(p.getName());
            list.remove(fake);
        } else {
            list = new ArrayList<>();
        }
        getFakeMobs().put(p.getName(), list);
        removeFakeEntity(p, fake);
    }

    /**
     * Checks if a specific {@link Player} looks at a {@link FakeMob} in a certain range
     *
     * @param p     the {@link Player} to check for
     * @param range the range in which it should be checked
     * @return whether or not the {@link Player} looks at a {@link FakeMob}
     */
    public static boolean isFakeMobInRange(Player p, int range) {
        if (!getFakeMobs().containsKey(p.getName())) return false;
        for (Block b : p.getLineOfSight(null, range)) {
            for (FakeMob fakeMob : getFakeMobs().get(p.getName())) {
                if ((b.getX() == fakeMob.getCurrentlocation().getBlockX()
                        && b.getY() == fakeMob.getCurrentlocation().getBlockY()
                        && b.getZ() == fakeMob.getCurrentlocation().getBlockZ()
                        || (b.getX() == fakeMob.getCurrentlocation().getBlockX()
                        && b.getY() == (fakeMob.getCurrentlocation().getBlockY() + 1)
                        && b.getZ() == fakeMob.getCurrentlocation().getBlockZ())))
                    return true;
            }
        }
        return false;
    }

    /**
     * Gets the {@link FakeMob} at which the {@link Player} is currently looking at
     *
     * @param p     the {@link Player} which looks at the {@link FakeMob}
     * @param range the range in which it should stand
     * @return the {@link FakeMob} inside the range
     * @throws NoSuchFakeEntityException when there is no {@link FakeMob} in range
     */
    public static FakeMob getLookedAtFakeMob(Player p, int range) throws NoSuchFakeEntityException {
        if (!getFakeMobs().containsKey(p.getName())) throw new NoSuchFakeEntityException();
        for (Block b : p.getLineOfSight(null, range)) {
            for (FakeMob fakeMob : getFakeMobs().get(p.getName())) {
                if ((b.getX() == fakeMob.getCurrentlocation().getBlockX()
                        && b.getY() == fakeMob.getCurrentlocation().getBlockY()
                        && b.getZ() == fakeMob.getCurrentlocation().getBlockZ()
                        || (b.getX() == fakeMob.getCurrentlocation().getBlockX()
                        && b.getY() == (fakeMob.getCurrentlocation().getBlockY() + 1)
                        && b.getZ() == fakeMob.getCurrentlocation().getBlockZ())))
                    return fakeMob;
            }
        }
        throw new NoSuchFakeEntityException();
    }

    /**
     * Gets a {@link ArrayList} with all {@link FakeMob} inside a certain radius
     *
     * @param p      the {@link Player} for which it should search for
     * @param radius the radius in which should be searched for
     * @return a {@link ArrayList} with all {@link FakeMob} inside the radius
     */
    public static ArrayList<FakeMob> getFakeMobsInRadius(Player p, double radius) {
        if (!getFakeMobs().containsKey(p.getName())) return new ArrayList<>();
        ArrayList<FakeMob> list = new ArrayList<>();
        for (FakeMob fakeMob : getFakeMobs().get(p.getName())) {
            if (fakeMob.getCurrentlocation().distanceSquared(p.getLocation()) <= radius * radius) {
                list.add(fakeMob);
            }
        }
        return list;
    }

    /**
     * Gets a {@link FakeMob} by its NMSEntity
     *
     * @param p    the {@link Player} who can see this {@link FakeMob}
     * @param fake the NMSEntity of the {@link FakeMob}
     * @return the {@link FakeMob} with his NMSEntity
     * @throws NoSuchFakeEntityException when there is no {@link FakeMob} with this Object
     */
    public static FakeMob getFakeMobByObject(Player p, Object fake) throws NoSuchFakeEntityException {
        if (!getFakeMobs().containsKey(p.getName())) throw new NoSuchFakeEntityException();
        for (FakeMob fakeMob : getFakeMobs().get(p.getName())) {
            if (fakeMob.getNmsEntity() == fake) return fakeMob;
        }
        throw new NoSuchFakeEntityException();
    }

    /**
     * Gets a {@link FakeMob} by its Name
     *
     * @param p    the {@link Player} who can see this {@link FakeMob}
     * @param name the name of the {@link FakeMob} (can include color)
     * @return the {@link FakeMob} with his NMSEntity
     * @throws NoSuchFakeEntityException when there is no {@link FakeMob} with this name
     */
    public static FakeMob getFakeMobByName(Player p, String name) throws NoSuchFakeEntityException {
        if (!getFakeMobs().containsKey(p.getName())) throw new NoSuchFakeEntityException();
        for (FakeMob fakeMob : getFakeMobs().get(p.getName())) {
            if (ChatColor.stripColor(fakeMob.getName()).equals(ChatColor.stripColor(name))) return fakeMob;
        }
        throw new NoSuchFakeEntityException();
    }

    /**
     * Gets a {@link FakeMob} by its EntityID
     *
     * @param p        the {@link Player} who can see this {@link FakeMob}
     * @param entityID the entityid of the {@link FakeMob}
     * @return the {@link FakeMob} with his NMSEntity
     * @throws NoSuchFakeEntityException when there is no {@link FakeMob} with this id
     */
    public static FakeMob getFakeMobByID(Player p, int entityID) throws NoSuchFakeEntityException {
        if (!getFakeMobs().containsKey(p.getName())) throw new NoSuchFakeEntityException();
        for (FakeMob fakeMob : getFakeMobs().get(p.getName())) {
            if (ReflectionUtil.getEntityID(fakeMob.getNmsEntity()) == entityID) return fakeMob;
        }
        throw new NoSuchFakeEntityException();
    }

    /**
     * Gets a {@link FakeMob} by its {@link Location}
     *
     * @param p   the {@link Player} who can see this {@link FakeMob}
     * @param loc the {@link Location} of the {@link FakeMob}
     * @return the {@link FakeMob} at his {@link Location}
     * @throws NoSuchFakeEntityException when there is no {@link FakeMob} at this {@link Location}
     */
    public static FakeMob getFakeMobByLocation(Player p, Location loc) throws NoSuchFakeEntityException {
        if (!getFakeMobs().containsKey(p.getName())) throw new NoSuchFakeEntityException();
        for (FakeMob fakeMob : getFakeMobs().get(p.getName())) {
            if ((loc.getBlockX() == fakeMob.getCurrentlocation().getBlockX()
                    && loc.getBlockY() == fakeMob.getCurrentlocation().getBlockY()
                    && loc.getBlockZ() == fakeMob.getCurrentlocation().getBlockZ()
                    || (loc.getBlockX() == fakeMob.getCurrentlocation().getBlockX()
                    && loc.getBlockY() == (fakeMob.getCurrentlocation().getBlockY() + 1)
                    && loc.getBlockZ() == fakeMob.getCurrentlocation().getBlockZ()))) {
                return fakeMob;
            }
        }
        throw new NoSuchFakeEntityException();
    }

    /**
     * Gets a {@link HashMap} with all {@link Player}s and which {@link FakeBigItem}s they can see
     *
     * @return a {@link HashMap} with all {@link Player}s and which {@link FakeBigItem}s they can see
     */
    public static HashMap<String, ArrayList<FakeBigItem>> getFakeBigItems() {
        return FAKE_BIG_ITEMS;
    }

    /**
     * Adds a new {@link FakeBigItem} for a specific {@link Player}
     *
     * @param p    the {@link Player} to add the {@link FakeBigItem} for
     * @param fake the {@link FakeBigItem} which should be added
     */
    public static void addFakeBigItem(Player p, FakeBigItem fake) {
        ArrayList<FakeBigItem> list;
        if (getFakeBigItems().containsKey(p.getName())) {
            list = getFakeBigItems().get(p.getName());
            list.add(fake);
        } else {
            list = new ArrayList<>();
            list.add(fake);
        }
        getFakeBigItems().put(p.getName(), list);
        addFakeEntity(p, fake);
    }

    /**
     * Removes a {@link FakeBigItem} for a specific {@link Player}
     *
     * @param p    the {@link Player} to remove the {@link FakeBigItem} for
     * @param fake the {@link FakeBigItem} which should be removed
     */
    public static void removeFakeBigItem(Player p, FakeBigItem fake) {
        ArrayList<FakeBigItem> list;
        if (getFakeBigItems().containsKey(p.getName())) {
            list = getFakeBigItems().get(p.getName());
            list.remove(fake);
        } else {
            list = new ArrayList<>();
        }
        getFakeBigItems().put(p.getName(), list);
        removeFakeEntity(p, fake);
    }

    /**
     * Checks if a specific {@link Player} looks at a {@link FakeBigItem} in a certain range
     *
     * @param p     the {@link Player} to check for
     * @param range the range in which it should be checked
     * @return whether or not the {@link Player} looks at a {@link FakeBigItem}
     */
    public static boolean isFakeBigItemInRange(Player p, int range) {
        if (!getFakeBigItems().containsKey(p.getName())) return false;
        for (Block b : p.getLineOfSight(null, range)) {
            for (FakeBigItem fakeBigItem : getFakeBigItems().get(p.getName())) {
                if ((b.getX() == fakeBigItem.getCurrentlocation().getBlockX()
                        && b.getY() == fakeBigItem.getCurrentlocation().getBlockY()
                        && b.getZ() == fakeBigItem.getCurrentlocation().getBlockZ()
                        || (b.getX() == fakeBigItem.getCurrentlocation().getBlockX()
                        && b.getY() == (fakeBigItem.getCurrentlocation().getBlockY() + 1)
                        && b.getZ() == fakeBigItem.getCurrentlocation().getBlockZ())))
                    return true;
            }
        }
        return false;
    }

    /**
     * Gets the {@link FakeBigItem} at which the {@link Player} is currently looking at
     *
     * @param p     the {@link Player} which looks at the {@link FakeBigItem}
     * @param range the range in which it should stand
     * @return the {@link FakeBigItem} inside the range
     * @throws NoSuchFakeEntityException when there is no {@link FakeBigItem} in range
     */
    public static FakeBigItem getFakeBigItemInRange(Player p, int range) throws NoSuchFakeEntityException {
        if (!getFakeBigItems().containsKey(p.getName())) throw new NoSuchFakeEntityException();
        for (Block b : p.getLineOfSight(null, range)) {
            for (FakeBigItem fakeBigItem : getFakeBigItems().get(p.getName())) {
                if ((b.getX() == fakeBigItem.getCurrentlocation().getBlockX()
                        && b.getY() == fakeBigItem.getCurrentlocation().getBlockY()
                        && b.getZ() == fakeBigItem.getCurrentlocation().getBlockZ()
                        || (b.getX() == fakeBigItem.getCurrentlocation().getBlockX()
                        && b.getY() == (fakeBigItem.getCurrentlocation().getBlockY() + 1)
                        && b.getZ() == fakeBigItem.getCurrentlocation().getBlockZ())))
                    return fakeBigItem;
            }
        }
        throw new NoSuchFakeEntityException();
    }

    /**
     * Gets a {@link ArrayList} with all {@link FakeBigItem} inside a certain radius
     *
     * @param p      the {@link Player} for which it should search for
     * @param radius the radius in which should be searched for
     * @return a {@link ArrayList} with all {@link FakeBigItem} inside the radius
     */
    public static ArrayList<FakeBigItem> getFakeBigItemsInRadius(Player p, double radius) {
        if (!getFakeBigItems().containsKey(p.getName())) return new ArrayList<>();
        ArrayList<FakeBigItem> list = new ArrayList<>();
        for (FakeBigItem fakeBigItem : getFakeBigItems().get(p.getName())) {
            if (fakeBigItem.getCurrentlocation().distanceSquared(p.getLocation()) <= radius * radius) {
                list.add(fakeBigItem);
            }
        }
        return list;
    }

    /**
     * Gets a {@link FakeBigItem} by its NMSEntity
     *
     * @param p    the {@link Player} who can see this {@link FakeBigItem}
     * @param fake the NMSEntity of the {@link FakeBigItem}
     * @return the {@link FakeBigItem} with his NMSEntity
     * @throws NoSuchFakeEntityException when there is no {@link FakeBigItem} with this Object
     */
    public static FakeBigItem getFakeBigItemByObject(Player p, Object fake) throws NoSuchFakeEntityException {
        if (!getFakeBigItems().containsKey(p.getName())) throw new NoSuchFakeEntityException();
        for (FakeBigItem fakeBigItem : getFakeBigItems().get(p.getName())) {
            if (fakeBigItem.getNmsEntity() == fake) return fakeBigItem;
        }
        throw new NoSuchFakeEntityException();
    }

    /**
     * Gets a {@link FakeBigItem} by its Name
     *
     * @param p    the {@link Player} who can see this {@link FakeBigItem}
     * @param name the name of the {@link FakeBigItem} (can include color)
     * @return the {@link FakeBigItem} with his NMSEntity
     * @throws NoSuchFakeEntityException when there is no {@link FakeBigItem} with this name
     */
    public static FakeBigItem getFakeBigItemByName(Player p, String name) throws NoSuchFakeEntityException {
        if (!getFakeBigItems().containsKey(p.getName())) throw new NoSuchFakeEntityException();
        for (FakeBigItem fakeBigItem : getFakeBigItems().get(p.getName())) {
            if (ChatColor.stripColor(fakeBigItem.getName()).equals(ChatColor.stripColor(name))) return fakeBigItem;
        }
        throw new NoSuchFakeEntityException();
    }

    /**
     * Gets a {@link FakeBigItem} by its EntityID
     *
     * @param p        the {@link Player} who can see this {@link FakeBigItem}
     * @param entityID the entityid of the {@link FakeBigItem}
     * @return the {@link FakeBigItem} with his NMSEntity
     * @throws NoSuchFakeEntityException when there is no {@link FakeBigItem} with this id
     */
    public static FakeBigItem getFakeBigItemByID(Player p, int entityID) throws NoSuchFakeEntityException {
        if (!getFakeBigItems().containsKey(p.getName())) throw new NoSuchFakeEntityException();
        for (FakeBigItem fakeBigItem : getFakeBigItems().get(p.getName())) {
            if (ReflectionUtil.getEntityID(fakeBigItem.getNmsEntity()) == entityID) return fakeBigItem;
        }
        throw new NoSuchFakeEntityException();
    }

    /**
     * Gets a {@link FakeBigItem} by its {@link Location}
     *
     * @param p   the {@link Player} who can see this {@link FakeBigItem}
     * @param loc the {@link Location} of the {@link FakeBigItem}
     * @return the {@link FakeBigItem} at his {@link Location}
     * @throws NoSuchFakeEntityException when there is no {@link FakeBigItem} at this {@link Location}
     */
    public static FakeBigItem getFakeBigItemByLocation(Player p, Location loc) throws NoSuchFakeEntityException {
        if (!getFakeBigItems().containsKey(p.getName())) throw new NoSuchFakeEntityException();
        for (FakeBigItem fakeBigItem : getFakeBigItems().get(p.getName())) {
            if ((loc.getBlockX() == fakeBigItem.getCurrentlocation().getBlockX()
                    && loc.getBlockY() == fakeBigItem.getCurrentlocation().getBlockY()
                    && loc.getBlockZ() == fakeBigItem.getCurrentlocation().getBlockZ()
                    || (loc.getBlockX() == fakeBigItem.getCurrentlocation().getBlockX()
                    && loc.getBlockY() == (fakeBigItem.getCurrentlocation().getBlockY() + 1)
                    && loc.getBlockZ() == fakeBigItem.getCurrentlocation().getBlockZ()))) {
                return fakeBigItem;
            }
        }
        throw new NoSuchFakeEntityException();
    }

    /**
     * Gets a {@link HashMap} with all {@link Player}s and which {@link FakeXPOrb}s they can see
     *
     * @return a {@link HashMap} with all {@link Player}s and which {@link FakeXPOrb}s they can see
     */
    public static HashMap<String, ArrayList<FakeXPOrb>> getFakeXpOrbs() {
        return FAKE_XP_ORBS;
    }

    /**
     * Adds a new {@link FakeXPOrb} for a specific {@link Player}
     *
     * @param p    the {@link Player} to add the {@link FakeXPOrb} for
     * @param fake the {@link FakeXPOrb} which should be added
     */
    public static void addFakeXPOrb(Player p, FakeXPOrb fake) {
        ArrayList<FakeXPOrb> list;
        if (getFakeXpOrbs().containsKey(p.getName())) {
            list = getFakeXpOrbs().get(p.getName());
            list.add(fake);
        } else {
            list = new ArrayList<>();
            list.add(fake);
        }
        getFakeXpOrbs().put(p.getName(), list);
        addFakeEntity(p, fake);
    }

    /**
     * Removes a {@link FakeXPOrb} for a specific {@link Player}
     *
     * @param p    the {@link Player} to remove the {@link FakeXPOrb} for
     * @param fake the {@link FakeXPOrb} which should be removed
     */
    public static void removeFakeXPOrb(Player p, FakeXPOrb fake) {
        ArrayList<FakeXPOrb> list;
        if (getFakeXpOrbs().containsKey(p.getName())) {
            list = getFakeXpOrbs().get(p.getName());
            list.remove(fake);
        } else {
            list = new ArrayList<>();
        }
        getFakeXpOrbs().put(p.getName(), list);
        removeFakeEntity(p, fake);
    }

    /**
     * Checks if a specific {@link Player} looks at a {@link FakeXPOrb} in a certain range
     *
     * @param p     the {@link Player} to check for
     * @param range the range in which it should be checked
     * @return if a xporb is in range
     */
    public static boolean isFakeXPOrbInRange(Player p, int range) {
        if (!getFakeXpOrbs().containsKey(p.getName())) return false;
        for (Block b : p.getLineOfSight(null, range)) {
            for (FakeXPOrb fakeXPOrb : getFakeXpOrbs().get(p.getName())) {
                if ((b.getX() == fakeXPOrb.getCurrentlocation().getBlockX()
                        && b.getY() == fakeXPOrb.getCurrentlocation().getBlockY()
                        && b.getZ() == fakeXPOrb.getCurrentlocation().getBlockZ()
                        || (b.getX() == fakeXPOrb.getCurrentlocation().getBlockX()
                        && b.getY() == (fakeXPOrb.getCurrentlocation().getBlockY() + 1)
                        && b.getZ() == fakeXPOrb.getCurrentlocation().getBlockZ())))
                    return true;
            }
        }
        return false;
    }

    /**
     * Gets the {@link FakeXPOrb} at which the {@link Player} is currently looking at
     *
     * @param p     the {@link Player} which looks at the {@link FakeXPOrb}
     * @param range the range in which it should stand
     * @return the {@link FakeXPOrb} inside the range
     * @throws NoSuchFakeEntityException when there is no XP Orb in range
     */
    public static FakeXPOrb getFakeXPOrbInRange(Player p, int range) throws NoSuchFakeEntityException {
        if (!getFakeXpOrbs().containsKey(p.getName())) throw new NoSuchFakeEntityException();
        for (Block b : p.getLineOfSight(null, range)) {
            for (FakeXPOrb fakeXPOrb : getFakeXpOrbs().get(p.getName())) {
                if ((b.getX() == fakeXPOrb.getCurrentlocation().getBlockX()
                        && b.getY() == fakeXPOrb.getCurrentlocation().getBlockY()
                        && b.getZ() == fakeXPOrb.getCurrentlocation().getBlockZ()
                        || (b.getX() == fakeXPOrb.getCurrentlocation().getBlockX()
                        && b.getY() == (fakeXPOrb.getCurrentlocation().getBlockY() + 1)
                        && b.getZ() == fakeXPOrb.getCurrentlocation().getBlockZ())))
                    return fakeXPOrb;
            }
        }
        throw new NoSuchFakeEntityException();
    }

    /**
     * Gets a {@link ArrayList} with all {@link FakeXPOrb} inside a certain radius
     *
     * @param p      the {@link Player} for which it should search for
     * @param radius the radius in which should be searched for
     * @return a {@link ArrayList} with all {@link FakeXPOrb} inside the radius radius
     */
    public static ArrayList<FakeXPOrb> getFakeXPOrbsInRadius(Player p, double radius) {
        if (!getFakeXpOrbs().containsKey(p.getName())) return new ArrayList<>();
        ArrayList<FakeXPOrb> list = new ArrayList<>();
        for (FakeXPOrb fakeXPOrb : getFakeXpOrbs().get(p.getName())) {
            if (fakeXPOrb.getCurrentlocation().distanceSquared(p.getLocation()) <= radius * radius) {
                list.add(fakeXPOrb);
            }
        }
        return list;
    }

    /**
     * Gets a {@link FakeXPOrb} by its NMSEntity
     *
     * @param p    the {@link Player} who can see this {@link FakeXPOrb}
     * @param fake the NMSEntity of the {@link FakeXPOrb}
     * @return the {@link FakeXPOrb} with his NMSEntity
     * @throws NoSuchFakeEntityException when there is no xporb with this object
     */
    public static FakeXPOrb getFakeXPOrbByObject(Player p, Object fake) throws NoSuchFakeEntityException {
        if (!getFakeXpOrbs().containsKey(p.getName())) throw new NoSuchFakeEntityException();
        for (FakeXPOrb fakeXPOrb : getFakeXpOrbs().get(p.getName())) {
            if (fakeXPOrb.getNmsEntity() == fake) return fakeXPOrb;
        }
        throw new NoSuchFakeEntityException();
    }

    /**
     * Gets a {@link FakeXPOrb} by its Name
     *
     * @param p    the {@link Player} who can see this {@link FakeXPOrb}
     * @param name the name of the {@link FakeXPOrb} (can include color)
     * @return the {@link FakeXPOrb} with his NMSEntity
     * @throws NoSuchFakeEntityException when there is no {@link FakeXPOrb} with this name
     */
    public static FakeXPOrb getFakeXPOrbByName(Player p, String name) throws NoSuchFakeEntityException {
        if (!getFakeXpOrbs().containsKey(p.getName())) throw new NoSuchFakeEntityException();
        for (FakeXPOrb fakeXPOrb : getFakeXpOrbs().get(p.getName())) {
            if (ChatColor.stripColor(fakeXPOrb.getName()).equals(ChatColor.stripColor(name))) return fakeXPOrb;
        }
        throw new NoSuchFakeEntityException();
    }

    /**
     * Gets a {@link FakeXPOrb} by its EntityID
     *
     * @param p        the {@link Player} who can see this {@link FakeXPOrb}
     * @param entityID the entityid of the {@link FakeXPOrb}
     * @return the {@link FakeXPOrb} with his NMSEntity
     * @throws NoSuchFakeEntityException when there is no {@link FakeXPOrb} with this id
     */
    public static FakeXPOrb getFakeXPOrbByID(Player p, int entityID) throws NoSuchFakeEntityException {
        if (!getFakeXpOrbs().containsKey(p.getName())) throw new NoSuchFakeEntityException();
        for (FakeXPOrb fakeXPOrb : getFakeXpOrbs().get(p.getName())) {
            if (ReflectionUtil.getEntityID(fakeXPOrb.getNmsEntity()) == entityID) return fakeXPOrb;
        }
        throw new NoSuchFakeEntityException();
    }

    /**
     * Gets a {@link FakeXPOrb} by its {@link Location}
     *
     * @param p   the {@link Player} who can see this {@link FakeXPOrb}
     * @param loc the {@link Location} of the {@link FakeXPOrb}
     * @return the {@link FakeXPOrb} at his {@link Location}
     * @throws NoSuchFakeEntityException when there is no XP Orb at this {@link Location}
     */
    public static FakeXPOrb getFakeXPOrbByLocation(Player p, Location loc) throws NoSuchFakeEntityException {
        if (!getFakeXpOrbs().containsKey(p.getName())) throw new NoSuchFakeEntityException();
        for (FakeXPOrb fakeXPOrb : getFakeXpOrbs().get(p.getName())) {
            if ((loc.getBlockX() == fakeXPOrb.getCurrentlocation().getBlockX()
                    && loc.getBlockY() == fakeXPOrb.getCurrentlocation().getBlockY()
                    && loc.getBlockZ() == fakeXPOrb.getCurrentlocation().getBlockZ()
                    || (loc.getBlockX() == fakeXPOrb.getCurrentlocation().getBlockX()
                    && loc.getBlockY() == (fakeXPOrb.getCurrentlocation().getBlockY() + 1)
                    && loc.getBlockZ() == fakeXPOrb.getCurrentlocation().getBlockZ()))) {
                return fakeXPOrb;
            }
        }
        throw new NoSuchFakeEntityException();
    }


    /**
     * Wraps the floor(Abrunden) method from the net.minecraft.server MathHelper
     *
     * @param var0 the double to floor
     * @return the floored int
     */
    public static int floor(double var0) {
        int var2 = (int) var0;
        return var0 < (double) var2 ? var2 - 1 : var2;
    }

    /**
     * Converts a float into a angle
     *
     * @param v the float to convert
     * @return the converted angle as a byte
     */
    public static byte toAngle(float v) {
        return (byte) ((int) (v * 256.0F / 360.0F));
    }

    /**
     * Converts a double into a delta
     *
     * @param v the double to convert
     * @return the converted delta as a double
     */
    public static double toDelta(double v) {
        return ((v * 32) * 128);
    }


    private static int getID(Object enumConstant) {
        try {
            return (int) Enum.class.getMethod("ordinal").invoke(enumConstant);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static HashMap<Integer, UUID> getEntityIds() {
        return ENTITY_IDS;
    }

    public static ArrayList<String> getNPCS() {
        return NPCS;
    }

    /**
     * Loads the Netty Injection
     */
    public static void load() {
        PACKET_LISTENER_API.load();
    }

    /**
     * Enables the Netty Injection
     */
    public static void enable() {
        new FakeRegister().initAll();
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

                        REnumAction action = REnumAction.values()[getID(packet.getPacketValue("action"))];
                        REnumHand hand = REnumHand.MAIN_HAND;

                        if (packet.getPacketValue("d") != null)
                            hand = REnumHand.values()[getID(packet.getPacketValue("d"))];


                        if (action != REnumAction.INTERACT_AT) {

                            Bukkit.getPluginManager().callEvent(new FakeEntityClickEvent(p, clicked, action, hand));

                            if (clicked instanceof FakeArmorstand) {
                                Bukkit.getPluginManager().callEvent(new FakeArmorstandClickEvent(p, (FakeArmorstand) clicked, action, hand));
                            } else if (clicked instanceof FakeEndercrystal) {
                                Bukkit.getPluginManager().callEvent(new FakeEndercrystalClickEvent(p, (FakeEndercrystal) clicked, action, hand));
                            } else if (clicked instanceof FakeMob) {
                                Bukkit.getPluginManager().callEvent(new FakeMobClickEvent(p, (FakeMob) clicked, action, hand));
                            } else if (clicked instanceof FakePlayer) {
                                Bukkit.getPluginManager().callEvent(new FakePlayerClickEvent(p, (FakePlayer) clicked, action, hand));
                            }
                        }
                    } catch (NoSuchFakeEntityException ignore) {
                    }
                }
            }
        });
    }

    /**
     * Disables the Netty Injection
     */
    public static void disable() {
        PACKET_LISTENER_API.disable();
    }
}
