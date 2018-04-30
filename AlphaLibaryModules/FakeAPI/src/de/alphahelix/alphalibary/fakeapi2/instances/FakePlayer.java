package de.alphahelix.alphalibary.fakeapi2.instances;

import com.google.gson.annotations.Expose;
import com.mojang.authlib.GameProfile;
import de.alphahelix.alphalibary.core.AlphaLibary;
import de.alphahelix.alphalibary.core.utilites.UUIDFetcher;
import de.alphahelix.alphalibary.fakeapi.FakeAPI;
import de.alphahelix.alphalibary.fakeapi2.FakeModule;
import de.alphahelix.alphalibary.inventories.item.SkullItemBuilder;
import de.alphahelix.alphalibary.reflection.PacketUtil;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import de.alphahelix.alphalibary.reflection.nms.GameProfileBuilder;
import de.alphahelix.alphalibary.reflection.nms.enums.REnumEquipSlot;
import de.alphahelix.alphalibary.reflection.nms.enums.REnumGamemode;
import de.alphahelix.alphalibary.reflection.nms.enums.REnumPlayerInfoAction;
import de.alphahelix.alphalibary.reflection.nms.packets.*;
import de.alphahelix.alphalibary.reflection.nms.wrappers.EntityWrapper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Consumer;

import java.util.UUID;

public class FakePlayer extends FakeEntity {
	
	private final UUID skinUUID;
	@Expose
	private final OfflinePlayer skinPlayer;
	
	public FakePlayer(String name, Location start, Object nmsEntity, UUID skinUUID) {
		super(name, start, nmsEntity);
		this.skinUUID = skinUUID;
		this.skinPlayer = Bukkit.getOfflinePlayer(skinUUID);
	}
	
	public static void spawn(Player p, Location loc, OfflinePlayer skin, String customName, Consumer<FakePlayer> callback) {
		spawnTemporary(p, loc, skin, customName, entity -> {
			FakeModule.getStorage(FakePlayer.class).addEntity(entity);
			callback.accept(entity);
		});
	}
	
	public static void spawnTemporary(Player p, Location loc, OfflinePlayer skin, String customName, Consumer<FakePlayer> callback) {
		UUIDFetcher.getUUID(skin, id ->
				GameProfileBuilder.fetch(id, gameProfile -> callback.accept(spawnTemporary(p, loc, gameProfile, customName)))
		);
	}
	
	public static FakePlayer spawnTemporary(Player p, Location loc, GameProfile skin, String customName) {
		Object npc = CustomSpawnable.PLAYER.newInstance(false,
				ReflectionUtil.getMinecraftServer(),
				ReflectionUtil.getWorldServer(loc.getWorld()),
				skin,
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
						skin,
						0,
						REnumGamemode.SURVIVAL,
						customName));
				
				ReflectionUtil.sendPacket(p, new PPONamedEntitySpawn(npc));
				ReflectionUtil.sendPacket(p, new PPOEntityLook(e.getEntityID(), loc.getYaw(), loc.getPitch(), true));
				
				ReflectionUtil.sendPacket(p, PacketUtil.createPlayerInfoPacket(
						REnumPlayerInfoAction.REMOVE_PLAYER,
						skin,
						0,
						REnumGamemode.SURVIVAL,
						customName));
			}
		}.runTaskLater(AlphaLibary.getInstance(), 4);
		
		FakePlayer fakePlayer = new FakePlayer(customName, loc, npc, skin.getId());
		
		FakeModule.getEntityHandler().addFakeEntity(p, fakePlayer);
		
		return fakePlayer;
	}
	
	public static FakePlayer spawn(Player p, Location loc, OfflinePlayer skin, String customName) {
		FakePlayer player = spawnTemporary(p, loc, skin, customName);
		
		if(player == null)
			return null;
		
		FakeModule.getStorage(FakePlayer.class).addEntity(player);
		
		return player;
	}
	
	public static FakePlayer spawnTemporary(Player p, Location loc, OfflinePlayer skin, String customName) {
		return spawnTemporary(p, loc, GameProfileBuilder.fetch(UUIDFetcher.getUUID(skin)), customName);
	}
	
	public static FakePlayer spawn(Player p, Location loc, UUID skin, String name) {
		FakePlayer player = spawnTemporary(p, loc, skin, name);
		
		if(player == null)
			return null;
		
		FakeModule.getStorage(FakePlayer.class).addEntity(player);
		
		return player;
	}
	
	public static FakePlayer spawnTemporary(Player p, Location loc, UUID skin, String customName) {
		return spawnTemporary(p, loc, GameProfileBuilder.fetch(skin), customName);
	}
	
	public static void spawn(Player p, Location loc, UUID skin, String name, Consumer<FakePlayer> callback) {
		spawnTemporary(p, loc, skin, name, entity -> {
			FakeModule.getStorage(FakePlayer.class).addEntity(entity);
			callback.accept(entity);
		});
	}
	
	public static void spawnTemporary(Player p, Location loc, UUID skin, String customName, Consumer<FakePlayer> callback) {
		GameProfileBuilder.fetch(skin, gameProfile -> callback.accept(spawnTemporary(p, loc, gameProfile, customName)));
	}
	
	public static FakePlayer spawn(Player p, Location loc, GameProfile skin, String name) {
		FakePlayer tR = spawnTemporary(p, loc, skin, name);
		
		if(tR == null)
			return null;
		
		FakeModule.getStorage(FakePlayer.class).addEntity(tR);
		
		return tR;
	}
	
	public void spawn(Player p, Consumer<FakePlayer> callback) {
		FakePlayer.spawnTemporary(p, getStart(), getSkinUUID(), getName(), callback);
	}
	
	public UUID getSkinUUID() {
		return skinUUID;
	}
	
	public FakePlayer attack(Player p, Player toAttack, double damage) {
		if(!FakeModule.getEntityHandler().getFakeEntitiesInRadius(toAttack, 4).contains(this)) return this;
		
		lookAtPlayer(p, toAttack);
		
		ReflectionUtil.sendPacket(p, new PPOAnimation(getNmsEntity(), 0));
		
		toAttack.damage(damage);
		return this;
	}
	
	public FakePlayer equipSkull(Player p, String textureURL) {
		return equip(p, SkullItemBuilder.getCustomSkull(textureURL), REnumEquipSlot.HELMET);
	}
	
	public FakePlayer equip(Player p, ItemStack item, REnumEquipSlot slot) {
		ReflectionUtil.sendPacket(p, new PPOEntityEquipment(getEntityID(), item, slot));
		return this;
	}
	
	public FakePlayer equipSkull(Player p, GameProfile profile) {
		return equip(p, SkullItemBuilder.getPlayerSkull(profile.getName()), REnumEquipSlot.HELMET);
	}
	
	public FakePlayer stopRide(Player p) {
		EntityWrapper e = new EntityWrapper(getNmsEntity());
		
		e.stopRiding();
		
		ReflectionUtil.sendPacket(p, new PPOMount(getNmsEntity()));
		return this;
	}
	
	public FakePlayer ride(Player p, FakeEntity entity) {
		EntityWrapper e = new EntityWrapper(getNmsEntity());
		
		e.startRiding(entity.getNmsEntity());
		
		ReflectionUtil.sendPacket(p, new PPOMount(entity.getNmsEntity()));
		return this;
	}
	
	public FakePlayer ride(Player p, Player toRide) {
		EntityWrapper e = new EntityWrapper(getNmsEntity());
		
		e.startRiding(ReflectionUtil.getEntityPlayer(toRide));
		
		ReflectionUtil.sendPacket(p, new PPOMount(ReflectionUtil.getEntityPlayer(p)));
		return this;
	}
	
	public OfflinePlayer getSkinPlayer() {
		return skinPlayer;
	}
	
	@Override
	public FakePlayer spawn(Player p) {
		return FakePlayer.spawnTemporary(p, getStart(), getSkinUUID(), getName());
	}
}
