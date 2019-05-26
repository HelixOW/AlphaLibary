package io.github.alphahelixdev.alpary.fake.entities;

import com.google.gson.annotations.Expose;
import com.mojang.authlib.GameProfile;
import io.github.alphahelixdev.alpary.Alpary;
import io.github.alphahelixdev.alpary.fake.Fake;
import io.github.alphahelixdev.alpary.fake.FakeEntity;
import io.github.alphahelixdev.alpary.reflection.nms.PacketCreator;
import io.github.alphahelixdev.alpary.reflection.nms.enums.RAnimationType;
import io.github.alphahelixdev.alpary.reflection.nms.enums.REquipSlot;
import io.github.alphahelixdev.alpary.reflection.nms.enums.RGamemode;
import io.github.alphahelixdev.alpary.reflection.nms.enums.RPlayerInfoAction;
import io.github.alphahelixdev.alpary.reflection.nms.packets.*;
import io.github.alphahelixdev.alpary.reflection.nms.wrappers.EntityWrapper;
import io.github.alphahelixdev.alpary.utilities.UUIDFetcher;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.whoisalphahelix.sql.annotations.Column;
import io.github.whoisalphahelix.sql.annotations.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Consumer;

import java.util.UUID;

@Table("players")
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public class FakePlayer extends FakeEntity {
	
	@Column(name = "skin", type = "text")
	private final UUID skinUUID;
	@Expose
	private final OfflinePlayer skinPlayer;
	
	public FakePlayer(String name, Location start, Object nmsEntity, UUID skinUUID) {
		super(name, start, nmsEntity);
		this.skinUUID = skinUUID;
		this.skinPlayer = Bukkit.getOfflinePlayer(skinUUID);
	}
	
	public static FakePlayer spawnTemporary(Player p, Location loc, String customName, GameProfile skin) {
		Object npc = CustomSpawnable.PLAYER.newInstance(false,
				Utils.nms().getMinecraftServer(),
				Utils.nms().getWorldServer(loc.getWorld()),
				skin,
                Alpary.getInstance().reflection().getDeclaredConstructor(Utils.nms().getNMSClass("PlayerInteractManager"),
						Utils.nms().getNMSClass("World"))
						.newInstance(false, Utils.nms().getWorldServer(loc.getWorld())));
		EntityWrapper e = new EntityWrapper(npc);
		
		e.setLocation(loc);
		
		Fake.npcs().add(customName);
		
		new BukkitRunnable() {
			@Override
			public void run() {
				Utils.nms().sendPacket(p, PacketCreator.createPlayerInfoPacket(
                        RPlayerInfoAction.ADD_PLAYER,
						skin,
						0,
                        RGamemode.SURVIVAL,
						customName));
				
				Utils.nms().sendPackets(p, new NamedEntitySpawnPacket(npc),
						new EntityLookPacket(e.getEntityID(), loc.getYaw(), loc.getPitch(), true));
				
				Utils.nms().sendPacket(p, PacketCreator.createPlayerInfoPacket(
                        RPlayerInfoAction.REMOVE_PLAYER,
						skin,
						0,
                        RGamemode.SURVIVAL,
						customName));
			}
		}.runTaskLater(Alpary.getInstance(), 4);
		
		FakePlayer fakePlayer = new FakePlayer(customName, loc, npc, skin.getId());
		
		Fake.getEntityHandler().addFakeEntity(p, fakePlayer);
		
		return fakePlayer;
	}
	
	public static FakePlayer spawn(Player p, Location loc, String customName, OfflinePlayer skin) {
		FakePlayer player = spawnTemporary(p, loc, customName, skin);
		
		if(player == null)
			return null;
		
		Fake.storage(FakePlayer.class).addEntity(player);
		
		return player;
	}
	
	public static FakePlayer spawnTemporary(Player p, Location loc, String customName, OfflinePlayer skin) {
		try {
			return spawnTemporary(p, loc, customName, Alpary.getInstance().gameProfileFetcher().fetch(Alpary.getInstance().uuidFetcher().getUUID(skin), false));
		} catch(UUIDFetcher.UUIDNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static FakePlayer spawn(Player p, Location loc, String name, UUID skin) {
		FakePlayer player = spawnTemporary(p, loc, name, skin);
		
		if(player == null)
			return null;
		
		Fake.storage(FakePlayer.class).addEntity(player);
		
		return player;
	}
	
	public static FakePlayer spawnTemporary(Player p, Location loc, String customName, UUID skin) {
		return spawnTemporary(p, loc, customName, Alpary.getInstance().gameProfileFetcher().fetch(skin, false));
	}
	
	public static void spawn(Player p, Location loc, UUID skin, String name, Consumer<FakePlayer> callback) {
		spawnTemporary(p, loc, name, skin, entity -> {
			Fake.storage(FakePlayer.class).addEntity(entity);
			callback.accept(entity);
		});
	}
	
	public static void spawnTemporary(Player p, Location loc, String customName, UUID skin, Consumer<FakePlayer> callback) {
		Alpary.getInstance().gameProfileFetcher().fetch(skin, gameProfile -> callback.accept(spawnTemporary(p, loc, customName, gameProfile)));
	}
	
	public static FakePlayer spawn(Player p, Location loc, String name, GameProfile skin) {
		FakePlayer tR = spawnTemporary(p, loc, name, skin);
		
		if(tR == null)
			return null;
		
		Fake.storage(FakePlayer.class).addEntity(tR);
		
		return tR;
	}
	
	public void spawn(Player p, Consumer<FakePlayer> callback) {
		FakePlayer.spawnTemporary(p, getStart(), getName(), getSkinUUID(), callback);
	}
	
	public FakePlayer attack(Player p, Player toAttack, double damage) {
		if(!Fake.getEntityHandler().getFakeEntitiesInRadius(toAttack, 4).contains(this)) return this;
		
		lookAtPlayer(p, toAttack);

        Utils.nms().sendPacket(p, new AnimationPacket(getNmsEntity(), RAnimationType.SWING_ARM));
		
		toAttack.damage(damage);
		return this;
	}
	
	public FakePlayer equipSkull(Player p, String textureURL) {
        return equip(p, Utils.skulls().getCustomSkull(textureURL), REquipSlot.HELMET);
	}

    public FakePlayer equip(Player p, ItemStack item, REquipSlot slot) {
		Utils.nms().sendPacket(p, new EntityEquipmentPacket(getEntityID(), item, slot));
		return this;
	}
	
	public FakePlayer equipSkull(Player p, GameProfile profile) {
        return equip(p, Utils.skulls().getPlayerSkull(profile.getName()), REquipSlot.HELMET);
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
	
	@Override
	public FakePlayer spawn(Player p) {
		return FakePlayer.spawnTemporary(p, getStart(), getName(), getSkinUUID());
	}
}
