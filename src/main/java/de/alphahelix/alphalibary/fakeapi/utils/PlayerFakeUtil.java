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

package de.alphahelix.alphalibary.fakeapi.utils;

import com.mojang.authlib.GameProfile;
import de.alphahelix.alphalibary.AlphaLibary;
import de.alphahelix.alphalibary.fakeapi.FakeAPI;
import de.alphahelix.alphalibary.fakeapi.FakeRegister;
import de.alphahelix.alphalibary.fakeapi.instances.FakeEntity;
import de.alphahelix.alphalibary.fakeapi.instances.FakePlayer;
import de.alphahelix.alphalibary.item.SkullItemBuilder;
import de.alphahelix.alphalibary.nms.enums.REnumEquipSlot;
import de.alphahelix.alphalibary.nms.enums.REnumGamemode;
import de.alphahelix.alphalibary.nms.enums.REnumPlayerInfoAction;
import de.alphahelix.alphalibary.nms.packets.*;
import de.alphahelix.alphalibary.nms.wrappers.EntityWrapper;
import de.alphahelix.alphalibary.reflection.PacketUtil;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import de.alphahelix.alphalibary.utils.GameProfileBuilder;
import de.alphahelix.alphalibary.utils.LocationUtil;
import de.alphahelix.alphalibary.uuid.UUIDFetcher;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.UUID;

public class PlayerFakeUtil {

    private static final HashMap<String, BukkitTask> FOLLOW_MAP = new HashMap<>();
    private static final HashMap<String, BukkitTask> STARE_MAP = new HashMap<>();
    private static final HashMap<String, BukkitTask> SPLIT_MAP = new HashMap<>();

    private static final ReflectionUtil.SaveConstructor ENTITY_PLAYER =
            ReflectionUtil.getDeclaredConstructor("EntityPlayer", ReflectionUtil.getNmsClass("MinecraftServer"),
                    ReflectionUtil.getNmsClass("WorldServer"),
                    GameProfile.class,
                    ReflectionUtil.getNmsClass("PlayerInteractManager"));

    /**
     * Spawns in a {@link FakePlayer} for the {@link Player}
     *
     * @param p          the {@link Player} to spawn the {@link FakePlayer} for
     * @param loc        {@link Location} where the {@link FakePlayer} should be spawned at
     * @param skin       the {@link OfflinePlayer} which has the skin for the {@link FakePlayer}
     * @param customName of the {@link FakePlayer} inside the file and above his head
     */
    public static void spawnPlayer(Player p, Location loc, OfflinePlayer skin, String customName, SpawnCallback<FakePlayer> callback) {
        spawnTemporaryPlayer(p, loc, skin, customName, entity -> {
            FakeRegister.getPlayerLocationsFile().addPlayerToFile(entity);
            callback.done(entity);
        });
    }

    /**
     * Spawns in a {@link FakePlayer} for the {@link Player}
     *
     * @param p          the {@link Player} to spawn the {@link FakePlayer} for
     * @param loc        {@link Location} where the {@link FakePlayer} should be spawned at
     * @param skin       the {@link OfflinePlayer} which has the skin for the {@link FakePlayer}
     * @param customName of the {@link FakePlayer} inside the file and above his head
     * @see PlayerFakeUtil#spawnPlayer(Player, Location, OfflinePlayer, String, SpawnCallback)
     * @deprecated not async {@link UUID} retrieving
     */
    @Deprecated
    public static FakePlayer spawnPlayer(Player p, Location loc, OfflinePlayer skin, String customName) {
        FakePlayer player = spawnTemporaryPlayer(p, loc, skin, customName);

        if (player != null)
            FakeRegister.getPlayerLocationsFile().addPlayerToFile(player);

        return player;
    }

    /**
     * @see PlayerFakeUtil#spawnPlayer(Player, Location, UUID, String, SpawnCallback)
     * @deprecated not async {@link UUID} retrieving
     */
    @Deprecated
    public static FakePlayer spawnPlayer(Player p, Location loc, UUID skin, String name) {
        FakePlayer player = spawnTemporaryPlayer(p, loc, skin, name);

        if (player != null)
            FakeRegister.getPlayerLocationsFile().addPlayerToFile(player);

        return player;
    }


    public static void spawnPlayer(Player p, Location loc, UUID skin, String name, SpawnCallback<FakePlayer> callback) {
        spawnTemporaryPlayer(p, loc, skin, name, entity -> {
            FakeRegister.getPlayerLocationsFile().addPlayerToFile(entity);
            callback.done(entity);
        });
    }

    public static FakePlayer spawnPlayer(Player p, Location loc, GameProfile skin, String name) {
        FakePlayer tR = spawnTemporaryPlayer(p, loc, skin, name);

        if (tR == null)
            return null;

        FakeRegister.getPlayerLocationsFile().addPlayerToFile(tR);

        return tR;
    }

    /**
     * Spawns in a temporary {@link FakePlayer} (disappears after rejoin) for the {@link Player}
     *
     * @param p          the {@link Player} to spawn the {@link FakePlayer} for
     * @param loc        {@link Location} where the {@link FakePlayer} should be spawned at
     * @param skin       the {@link OfflinePlayer} which has the skin for the {@link FakePlayer}
     * @param customName of the {@link FakePlayer} inside the file and above his head
     */
    public static void spawnTemporaryPlayer(Player p, Location loc, OfflinePlayer skin, String customName, SpawnCallback<FakePlayer> callback) {
        UUIDFetcher.getUUID(skin, id ->
                GameProfileBuilder.fetch(id, gameProfile -> callback.done(spawnTemporaryPlayer(p, loc, gameProfile, customName)))
        );
    }

    /**
     * Spawns in a temporary {@link FakePlayer} (disappears after rejoin) for the {@link Player}
     *
     * @param p          the {@link Player} to spawn the {@link FakePlayer} for
     * @param loc        {@link Location} where the {@link FakePlayer} should be spawned at
     * @param skin       the {@link OfflinePlayer} which has the skin for the {@link FakePlayer}
     * @param customName of the {@link FakePlayer} inside the file and above his head
     * @see PlayerFakeUtil#spawnTemporaryPlayer(Player, Location, OfflinePlayer, String, SpawnCallback)
     * @deprecated not async UUID retrieving
     */
    @Deprecated
    public static FakePlayer spawnTemporaryPlayer(Player p, Location loc, OfflinePlayer skin, String customName) {
        return spawnTemporaryPlayer(p, loc, GameProfileBuilder.fetch(UUIDFetcher.getUUID(skin)), customName);
    }

    public static void spawnTemporaryPlayer(Player p, Location loc, UUID skin, String customName, SpawnCallback<FakePlayer> callback) {
        GameProfileBuilder.fetch(skin, gameProfile -> callback.done(spawnTemporaryPlayer(p, loc, gameProfile, customName)));
    }

    /**
     * @see PlayerFakeUtil#spawnTemporaryPlayer(Player, Location, UUID, String, SpawnCallback)
     * @deprecated not async UUID retrieving
     */
    @Deprecated
    public static FakePlayer spawnTemporaryPlayer(Player p, Location loc, UUID skin, String customName) {
        return spawnTemporaryPlayer(p, loc, GameProfileBuilder.fetch(skin), customName);
    }

    public static FakePlayer spawnTemporaryPlayer(final Player p, final Location loc, GameProfile skin, final String customName) {
        GameProfile gameProfile = skin;

        Object npc = ENTITY_PLAYER.newInstance(false,
                ReflectionUtil.getMinecraftServer(),
                ReflectionUtil.getWorldServer(loc.getWorld()),
                gameProfile,
                ReflectionUtil.getDeclaredConstructor("PlayerInteractManager", ReflectionUtil.getNmsClass("World"))
                        .newInstance(false, ReflectionUtil.getWorldServer(loc.getWorld())));
        EntityWrapper e = new EntityWrapper(npc);

        e.setLocation(loc);

        FakeAPI.getNPCS().add(customName);

        new BukkitRunnable() {
            @Override
            public void run() {
                ReflectionUtil.sendPacket(p, PacketUtil.createPlayerInfoPacket(
                        REnumPlayerInfoAction.ADD_PLAYER,
                        gameProfile,
                        0,
                        REnumGamemode.SURVIVAL,
                        customName));

                ReflectionUtil.sendPacket(p, new PPONamedEntitySpawn(npc));
                ReflectionUtil.sendPacket(p, new PPOEntityLook(e.getEntityID(), loc.getYaw(), loc.getPitch(), true));

                ReflectionUtil.sendPacket(p, PacketUtil.createPlayerInfoPacket(
                        REnumPlayerInfoAction.REMOVE_PLAYER,
                        gameProfile,
                        0,
                        REnumGamemode.SURVIVAL,
                        customName));
            }
        }.runTaskLater(AlphaLibary.getInstance(), 4);

        FakePlayer fakePlayer = new FakePlayer(loc, customName, skin.getId(), npc);

        FakeAPI.addFakePlayer(p, fakePlayer);

        return fakePlayer;
    }

    /**
     * Removes a {@link FakePlayer} for on {@link Player} from the {@link org.bukkit.World}
     *
     * @param p   the {@link Player} to destroy the {@link FakePlayer} for
     * @param npc the {@link FakePlayer} to remove
     */
    public static void removePlayer(Player p, FakePlayer npc) {
        ReflectionUtil.sendPacket(p, new PPOEntityDestroy(ReflectionUtil.getEntityID(npc.getNmsEntity())));

        FakeAPI.removeFakePlayer(p, npc);
    }

    /**
     * Moves the given {@link FakePlayer}
     *
     * @param p     the {@link Player} to move the {@link FakePlayer} for
     * @param x     blocks in x direction
     * @param y     blocks in y direction
     * @param z     blocks in z direction
     * @param yaw   new yaw
     * @param pitch new pitch
     * @param npc   the {@link FakePlayer} which should be moved
     */
    public static void movePlayer(Player p, double x, double y, double z, float yaw, float pitch, FakePlayer npc) {
        Location old = npc.getCurrentlocation();
        Location ne = old.clone().add(x, y, z);

        ReflectionUtil.sendPacket(p, new PPORelEntityMove(
                ReflectionUtil.getEntityID(npc.getNmsEntity()),
                old.getX() - ne.getX(),
                old.getY() - ne.getY(),
                old.getZ() - ne.getZ(),
                false
        ));

        ReflectionUtil.sendPacket(p, new PPOEntityLook(ReflectionUtil.getEntityID(npc.getNmsEntity()),
                yaw, pitch, false));

        npc.setCurrentlocation(npc.getCurrentlocation().add(x, y, z));
    }

    /**
     * Teleports a {@link FakePlayer} to a specific {@link Location} for the given {@link Player}
     *
     * @param p   the {@link Player} to teleport the {@link FakePlayer} for
     * @param loc the {@link Location} to teleport the {@link FakePlayer} to
     * @param npc the {@link FakePlayer} which should be teleported
     */
    public static void teleportPlayer(Player p, Location loc, FakePlayer npc) {
        try {
            Field x = ReflectionUtil.getNmsClass("Entity").getField("locX"), y = ReflectionUtil.getNmsClass("Entity").getField("locY"), z = ReflectionUtil.getNmsClass("Entity").getField("locZ"), yaw = ReflectionUtil.getNmsClass("Entity").getField("yaw"), pitch = ReflectionUtil.getNmsClass("Entity").getField("pitch");
            Object a = npc.getNmsEntity();

            x.setAccessible(true);
            y.setAccessible(true);
            z.setAccessible(true);
            yaw.setAccessible(true);
            pitch.setAccessible(true);

            x.set(a, loc.getX());
            y.set(a, loc.getY());
            z.set(a, loc.getZ());
            yaw.set(a, loc.getYaw());
            pitch.set(a, loc.getPitch());

            ReflectionUtil.sendPacket(p, new PPOEntityTeleport(a));
            ReflectionUtil.sendPacket(p, new PPOEntityLook(ReflectionUtil.getEntityID(a), loc.getYaw(), loc.getPitch(), false));

            npc.setCurrentlocation(loc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Equip a {@link FakePlayer} with a {@link ItemStack} for the {@link Player}
     *
     * @param p    the {@link Player} to equip the {@link FakePlayer} for
     * @param npc  the {@link FakePlayer} which should get equipped
     * @param item the {@link ItemStack} which the {@link FakePlayer} should receive
     * @param slot the {@link REnumEquipSlot} where the {@link ItemStack} should be placed at
     */
    public static void equipPlayer(Player p, FakePlayer npc, ItemStack item, REnumEquipSlot slot) {
        ReflectionUtil.sendPacket(p, new PPOEntityEquipment(
                ReflectionUtil.getEntityID(npc.getNmsEntity()), item, slot
        ));
    }

    /**
     * Check if a {@link FakePlayer} follows a {@link Player}
     *
     * @param toCheck the {@link Player} to check if he has a {@link FakePlayer} which follows him
     * @return if the {@link Player} has a {@link FakePlayer} which followes him
     */
    public static boolean hasFollower(Player toCheck) {
        return FOLLOW_MAP.containsKey(toCheck.getName());
    }

    /**
     * Make a {@link FakePlayer} follow a specific {@link Player}, which only the {@link Player}  can see
     *
     * @param p        the {@link Player} to see the following {@link FakePlayer}
     * @param toFollow the {@link Player} which the {@link FakePlayer} should follow
     * @param npc      the {@link FakePlayer} which should follow the {@link Player}
     */
    public static void followPlayer(final Player p, final Player toFollow, final FakePlayer npc) {
        FOLLOW_MAP.put(p.getName(), new BukkitRunnable() {
            @Override
            public void run() {
                teleportPlayer(p, p.getLocation(), npc);
            }
        }.runTaskTimer(AlphaLibary.getInstance(), 0, 1));
    }

    /**
     * Make a {@link FakePlayer} unfollow his {@link Player}
     *
     * @param p the {@link Player} who shouldn't be followed anylonger
     */
    public static void unFollowPlayer(Player p) {
        if (FOLLOW_MAP.containsKey(p.getName())) {
            FOLLOW_MAP.get(p.getName()).cancel();
            FOLLOW_MAP.remove(p.getName());
        }
    }

    /**
     * Make a {@link FakePlayer} look at a specific Player, which another specific {@link Player} can see
     *
     * @param p        the {@link Player} to see the following watch
     * @param toLookAt the {@link Player} to look at
     * @param npc      the {@link FakePlayer} who should watch the {@link Player}
     */
    public static void lookAtPlayer(Player p, Player toLookAt, FakePlayer npc) {
        ReflectionUtil.sendPacket(p, new PPOEntityLook(
                ReflectionUtil.getEntityID(npc.getNmsEntity()),
                LocationUtil.lookAt(npc.getCurrentlocation(), toLookAt.getLocation()).getYaw(),
                LocationUtil.lookAt(npc.getCurrentlocation(), toLookAt.getLocation()).getPitch(),
                true
        ));
    }

    /**
     * Make a {@link FakePlayer} stare at a specific Player, which another specific {@link Player} can see
     *
     * @param p         the {@link Player} to see the following watch
     * @param toStareAt the {@link Player} to stare at
     * @param npc       the {@link FakePlayer} who should stare at the {@link Player}
     */
    public static void stareAtPlayer(final Player p, final Player toStareAt, final FakePlayer npc) {
        STARE_MAP.put(p.getName(), new BukkitRunnable() {
            @Override
            public void run() {
                lookAtPlayer(p, toStareAt, npc);
            }
        }.runTaskTimer(AlphaLibary.getInstance(), 0, 1));
    }

    /**
     * Reset the look for all {@link FakePlayer}s which a specific {@link Player} can see
     *
     * @param p the {@link Player}
     */
    public static void normalizeLook(Player p) {
        if (STARE_MAP.containsKey(p.getName())) {
            STARE_MAP.get(p.getName()).cancel();
            STARE_MAP.remove(p.getName());
        }
    }

    /**
     * Make a {@link FakePlayer} attack a {@link Player}
     *
     * @param p        the {@link Player} who can see the attack
     * @param toAttack the {@link Player} who should be attacked
     * @param npc      the {@link FakePlayer} who should attack
     * @param damage   the damage which should be done by the {@link FakePlayer}
     */
    public static void attackPlayer(Player p, Player toAttack, FakePlayer npc, double damage) {
        if (!FakeAPI.getFakePlayersInRadius(toAttack, 4).contains(npc)) return;

        lookAtPlayer(p, toAttack, npc);

        ReflectionUtil.sendPacket(p, new PPOAnimation(npc.getNmsEntity(), 0));

        toAttack.damage(damage);
    }

    /**
     * Teleport a {@link FakePlayer} to a specific {@link Location} in certain intervals, which is visible for all Players
     *
     * @param p             the {@link Player} to teleport the {@link FakePlayer} for
     * @param to            the {@link Location} where the {@link FakePlayer} should be teleported to
     * @param teleportCount the amount of teleportation that should be made
     * @param wait          the amount of time to wait 'till the next teleport starts
     * @param npc           the {@link FakePlayer} which should be teleported
     */
    public static void splitTeleportPlayer(final Player p, final Location to, final int teleportCount, final long wait, final FakePlayer npc) {
        try {
            final Location currentLocation = npc.getCurrentlocation();
            Vector between = to.toVector().subtract(currentLocation.toVector());

            final double toMoveInX = between.getX() / teleportCount;
            final double toMoveInY = between.getY() / teleportCount;
            final double toMoveInZ = between.getZ() / teleportCount;

            SPLIT_MAP.put(p.getName(), new BukkitRunnable() {
                public void run() {
                    if (!LocationUtil.isSameLocation(currentLocation, to)) {
                        teleportPlayer(p, currentLocation.add(new Vector(toMoveInX, toMoveInY, toMoveInZ)), npc);
                    } else
                        this.cancel();
                }
            }.runTaskTimer(AlphaLibary.getInstance(), 0, wait));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Cancels all teleport tasks for the {@link Player}
     *
     * @param p the {@link Player} to cancel all teleport tasks
     */
    public static void cancelAllSplittedTasks(Player p) {
        if (SPLIT_MAP.containsKey(p.getName())) {
            SPLIT_MAP.get(p.getName()).cancel();
            SPLIT_MAP.remove(p.getName());
        }
    }

    /**
     * Sets the head of the {@link FakePlayer} to a custom {@link org.bukkit.material.Skull} for a specific {@link Player}
     * You can use custom textures in the format of a 1.7 skin here
     *
     * @param p          the {@link Player} to show the custom Skull
     * @param mob        the {@link FakePlayer} which should get equipped
     * @param textureURL the URL where to find the plain 1.7 skin
     */
    public static void equipPlayerSkull(Player p, FakePlayer mob, String textureURL) {
        try {
            equipPlayer(p, mob, SkullItemBuilder.getCustomSkull(textureURL), REnumEquipSlot.HELMET);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the head of the {@link FakePlayer} to a custom {@link org.bukkit.material.Skull} for a specific {@link Player}
     * You can use custom textures in the format of a 1.7 skin here
     *
     * @param p       the {@link Player} to show the custom Skull
     * @param mob     the {@link FakePlayer} which should get equipped
     * @param profile the {@link GameProfile} of the owner of the skull
     */
    public static void equipPlayerSkull(Player p, FakePlayer mob, GameProfile profile) {
        try {
            equipPlayer(p, mob, SkullItemBuilder.getPlayerSkull(profile.getName()), REnumEquipSlot.HELMET);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopRide(Player p, FakePlayer player) {
        EntityWrapper e = new EntityWrapper(player.getNmsEntity());

        e.stopRiding();

        ReflectionUtil.sendPacket(p, new PPOMount(player.getNmsEntity()));
    }

    public static void ride(Player p, FakePlayer player, FakeEntity entity) {
        EntityWrapper e = new EntityWrapper(player.getNmsEntity());

        e.startRiding(entity.getNmsEntity());

        ReflectionUtil.sendPacket(p, new PPOMount(player.getNmsEntity()));
    }
}
