package de.alphahelix.alphalibary.reflection.player;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.alphahelix.alphalibary.core.utilites.UUIDFetcher;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import de.alphahelix.alphalibary.reflection.nms.enums.REnumDifficulty;
import de.alphahelix.alphalibary.reflection.nms.enums.REnumGamemode;
import de.alphahelix.alphalibary.reflection.nms.enums.REnumPlayerInfoAction;
import de.alphahelix.alphalibary.reflection.nms.packets.PPORespawn;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.UUID;

public class AdvancedPlayer {
	
	private static final ReflectionUtil.SaveField gameProfileNameField;
	private static final ReflectionUtil.SaveConstructor playerInfoPacket;
	
	static {
		gameProfileNameField = ReflectionUtil.getDeclaredField("name", GameProfile.class);
		playerInfoPacket = ReflectionUtil.getDeclaredConstructor("PacketPlayOutPlayerInfo", ReflectionUtil.getNmsClass("PacketPlayOutPlayerInfo$EnumPlayerInfoAction"), ReflectionUtil.getNmsClassAsArray("EntityPlayer"));
	}
	
	private final JavaPlugin plugin;
	private Player player;
	private UUID id;
	private String customName = "";
	private Skin skin;
	private GameProfile profile;
	
	public AdvancedPlayer(JavaPlugin plugin, Player player) {
		this(plugin, UUIDFetcher.getUUID(player));
		
	}
	
	public AdvancedPlayer(JavaPlugin plugin, UUID id) {
		this.plugin = plugin;
		this.id = id;
		this.player = Bukkit.getPlayer(id);
		this.profile = ReflectionUtil.getGameProfile(player);
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public int getPing() {
		return ReflectionUtil.getPing(player);
	}
	
	public boolean isSwimming() {
		return (boolean) ReflectionUtil.getDeclaredField("inWater", "Entity").get(getEntityPlayer(), true);
	}
	
	public Object getEntityPlayer() {
		return ReflectionUtil.getEntityPlayer(player);
	}
	
	public AdvancedPlayer addPermission(String permission) {
		if(!player.hasPermission(permission))
			player.addAttachment(this.plugin, permission, true);
		return this;
	}
	
	public AdvancedPlayer removePermission(String permission) {
		if(!player.hasPermission(permission))
			player.addAttachment(this.plugin, permission, false);
		return this;
	}
	
	public String getCustomName() {
		return this.customName;
	}
	
	public AdvancedPlayer setCustomName(String name) {
		this.customName = name;
		gameProfileNameField.set(profile, name, true);
		ReflectionUtil.sendPackets(
				playerInfoPacket.newInstance(true, REnumPlayerInfoAction.REMOVE_PLAYER.getPlayerInfoAction(), new Object[]{getEntityPlayer()}),
				playerInfoPacket.newInstance(true, REnumPlayerInfoAction.ADD_PLAYER.getPlayerInfoAction(), new Object[]{getEntityPlayer()})
		);
		return this;
	}
	
	public Skin getSkin() {
		return skin;
	}
	
	public AdvancedPlayer setSkin(String name) throws IOException {
		this.skin = new Skin(getId());
		
		if(skin.getSkinName() != null) {
			getProfile().getProperties().removeAll("textures");
			getProfile().getProperties().put("textures", new Property("textures", skin.getSkinValue(), skin.getSkinSignature()));
			
			fakeRespawn();
		}
		return this;
	}
	
	public UUID getId() {
		return id;
	}
	
	public GameProfile getProfile() {
		return profile;
	}
	
	public void fakeRespawn() {
		Location loc = player.getLocation();
		REnumDifficulty difficulty = REnumDifficulty.EASY;
		Object enumGameMode = ReflectionUtil.getEnumGamemode(player);
		int level = player.getLevel();
		double health = player.getHealth();
		float sat = player.getSaturation();
		float exp = player.getExp();
		float exhaustion = player.getExhaustion();
		int foodLevel = player.getFoodLevel();
		double healthScale = player.getHealthScale();
		boolean healthScaled = player.isHealthScaled();
		
		ReflectionUtil.sendPacket(player, new PPORespawn(difficulty, player.getWorld().getWorldType(), REnumGamemode.getFromPlayer(player)));
		
		player.teleport(loc);
		player.setLevel(level);
		player.setHealth(health);
		player.setSaturation(sat);
		player.setExp(exp);
		player.setExhaustion(exhaustion);
		player.setFoodLevel(foodLevel);
		player.setHealthScaled(healthScaled);
		player.setHealthScale(healthScale);
	}
	
}
