package io.github.alphahelixdev.alpary.fake.entities;

import com.google.gson.annotations.Expose;
import com.mojang.authlib.GameProfile;
import io.github.alphahelixdev.alpary.Alpary;
import io.github.alphahelixdev.alpary.fake.Fake;
import io.github.alphahelixdev.alpary.fake.FakeEntity;
import io.github.alphahelixdev.alpary.reflection.nms.PacketCreator;
import io.github.alphahelixdev.alpary.reflection.nms.enums.AnimationType;
import io.github.alphahelixdev.alpary.reflection.nms.enums.REnumEquipSlot;
import io.github.alphahelixdev.alpary.reflection.nms.enums.REnumGamemode;
import io.github.alphahelixdev.alpary.reflection.nms.enums.REnumPlayerInfoAction;
import io.github.alphahelixdev.alpary.reflection.nms.packets.*;
import io.github.alphahelixdev.alpary.reflection.nms.wrappers.EntityWrapper;
import io.github.alphahelixdev.alpary.utilities.UUIDFetcher;
import io.github.alphahelixdev.alpary.utils.NMSUtil;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.alphahelixdev.helius.sql.annotations.datatypes.sqlite.Text;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Consumer;

import java.util.UUID;

public class FakePlayer extends FakeEntity {

	@Text
	private final UUID skinUUID;
	@Expose
	private final OfflinePlayer skinPlayer;
	
	public FakePlayer(String name, Location start, Object nmsEntity, UUID skinUUID) {
		super(name, start, nmsEntity);
		this.skinUUID = skinUUID;
		this.skinPlayer = Bukkit.getOfflinePlayer(skinUUID);
	}
	
	public static void spawn(Player p, Location loc, OfflinePlayer skin, String customName,
	                         Consumer<FakePlayer> callback) {
		spawnTemporary(p, loc, skin, customName, entity -> {
			Fake.storage(FakePlayer.class).addEntity(entity);
			callback.accept(entity);
		});
	}
	
	public static void spawnTemporary(Player p, Location loc, OfflinePlayer skin, String customName,
	                                  Consumer<FakePlayer> callback) {
		Alpary.getInstance().uuidFetcher().getUUID(skin, id ->
				Alpary.getInstance().gameProfileFetcher().fetch(id, gameProfile ->
						callback.accept(spawnTemporary(p, loc, gameProfile, customName)))
		);
	}
	
	public static FakePlayer spawnTemporary(Player p, Location loc, GameProfile skin, String customName) {
		Object npc = CustomSpawnable.PLAYER.newInstance(false,
				Utils.nms().getMinecraftServer(),
				Utils.nms().getWorldServer(loc.getWorld()),
				skin,
				NMSUtil.getReflections().getDeclaredConstructor(Utils.nms().getNMSClass("PlayerInteractManager"),
						Utils.nms().getNMSClass("World"))
						.newInstance(false, Utils.nms().getWorldServer(loc.getWorld())));
		EntityWrapper e = new EntityWrapper(npc);
		
		e.setLocation(loc);
		
		Fake.npcs().add(customName);
		
		new BukkitRunnable() {
			@Override
			public void run() {
				Utils.nms().sendPacket(p, PacketCreator.createPlayerInfoPacket(
						REnumPlayerInfoAction.ADD_PLAYER,
						skin,
						0,
						REnumGamemode.SURVIVAL,
						customName));
				
				Utils.nms().sendPackets(p, new NamedEntitySpawnPacket(npc),
						new EntityLookPacket(e.getEntityID(), loc.getYaw(), loc.getPitch(), true));
				
				Utils.nms().sendPacket(p, PacketCreator.createPlayerInfoPacket(
						REnumPlayerInfoAction.REMOVE_PLAYER,
						skin,
						0,
						REnumGamemode.SURVIVAL,
						customName));
			}
		}.runTaskLater(Alpary.getInstance(), 4);
		
		FakePlayer fakePlayer = new FakePlayer(customName, loc, npc, skin.getId());
		
		Fake.getEntityHandler().addFakeEntity(p, fakePlayer);
		
		return fakePlayer;
	}
	
	public static FakePlayer spawn(Player p, Location loc, OfflinePlayer skin, String customName) {
		FakePlayer player = spawnTemporary(p, loc, skin, customName);
		
		if(player == null)
			return null;
		
		Fake.storage(FakePlayer.class).addEntity(player);
		
		return player;
	}
	
	public static FakePlayer spawnTemporary(Player p, Location loc, OfflinePlayer skin, String customName) {
		try {
			return spawnTemporary(p, loc, Alpary.getInstance().gameProfileFetcher().fetch(Alpary.getInstance().uuidFetcher().getUUID(skin), false), customName);
		} catch(UUIDFetcher.UUIDNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static FakePlayer spawn(Player p, Location loc, UUID skin, String name) {
		FakePlayer player = spawnTemporary(p, loc, skin, name);
		
		if(player == null)
			return null;
		
		Fake.storage(FakePlayer.class).addEntity(player);
		
		return player;
	}
	
	public static FakePlayer spawnTemporary(Player p, Location loc, UUID skin, String customName) {
		return spawnTemporary(p, loc, Alpary.getInstance().gameProfileFetcher().fetch(skin, false), customName);
	}
	
	public static void spawn(Player p, Location loc, UUID skin, String name, Consumer<FakePlayer> callback) {
		spawnTemporary(p, loc, skin, name, entity -> {
			Fake.storage(FakePlayer.class).addEntity(entity);
			callback.accept(entity);
		});
	}
	
	public static void spawnTemporary(Player p, Location loc, UUID skin, String customName, Consumer<FakePlayer> callback) {
		Alpary.getInstance().gameProfileFetcher().fetch(skin, gameProfile -> callback.accept(spawnTemporary(p, loc, gameProfile, customName)));
	}
	
	public static FakePlayer spawn(Player p, Location loc, GameProfile skin, String name) {
		FakePlayer tR = spawnTemporary(p, loc, skin, name);
		
		if(tR == null)
			return null;
		
		Fake.storage(FakePlayer.class).addEntity(tR);
		
		return tR;
	}
	
	public void spawn(Player p, Consumer<FakePlayer> callback) {
		FakePlayer.spawnTemporary(p, getStart(), getSkinUUID(), getName(), callback);
	}
	
	public UUID getSkinUUID() {
		return skinUUID;
	}
	
	public FakePlayer attack(Player p, Player toAttack, double damage) {
		if(!Fake.getEntityHandler().getFakeEntitiesInRadius(toAttack, 4).contains(this)) return this;
		
		lookAtPlayer(p, toAttack);
		
		Utils.nms().sendPacket(p, new AnimationPacket(getNmsEntity(), AnimationType.SWING_ARM));
		
		toAttack.damage(damage);
		return this;
	}
	
	public FakePlayer equipSkull(Player p, String textureURL) {
		return equip(p, Utils.skulls().getCustomSkull(textureURL), REnumEquipSlot.HELMET);
	}
	
	public FakePlayer equip(Player p, ItemStack item, REnumEquipSlot slot) {
		Utils.nms().sendPacket(p, new EntityEquipmentPacket(getEntityID(), item, slot));
		return this;
	}
	
	public FakePlayer equipSkull(Player p, GameProfile profile) {
		return equip(p, Utils.skulls().getPlayerSkull(profile.getName()), REnumEquipSlot.HELMET);
	}
	
	public FakePlayer stopRide(Player p) {
		EntityWrapper e = new EntityWrapper(getNmsEntity());
		
		e.stopRiding();
		
		Utils.nms().sendPacket(p, new MountPacket(getNmsEntity()));
		return this;
	}
	
	public FakePlayer ride(Player p, FakeEntity entity) {
		EntityWrapper e = new EntityWrapper(getNmsEntity());
		
		e.startRiding(entity.getNmsEntity());
		
		Utils.nms().sendPacket(p, new MountPacket(getNmsEntity()));
		return this;
	}
	
	public FakePlayer ride(Player p, Player toRide) {
		EntityWrapper e = new EntityWrapper(getNmsEntity());
		
		e.startRiding(Utils.nms().getCraftPlayer(toRide));
		
		Utils.nms().sendPacket(p, new MountPacket(Utils.nms().getCraftPlayer(p)));
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
