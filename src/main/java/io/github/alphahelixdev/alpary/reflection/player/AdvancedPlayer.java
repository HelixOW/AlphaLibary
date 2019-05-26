package io.github.alphahelixdev.alpary.reflection.player;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import io.github.alphahelixdev.alpary.Alpary;
import io.github.alphahelixdev.alpary.reflection.nms.enums.RDifficulty;
import io.github.alphahelixdev.alpary.reflection.nms.enums.RGamemode;
import io.github.alphahelixdev.alpary.reflection.nms.enums.RPlayerInfoAction;
import io.github.alphahelixdev.alpary.reflection.nms.packets.RespawnPacket;
import io.github.alphahelixdev.alpary.utilities.UUIDFetcher;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.whoisalphahelix.helix.reflection.SaveConstructor;
import io.github.whoisalphahelix.helix.reflection.SaveField;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class AdvancedPlayer {
	
	private static final SaveField GAME_PROFILE_NAME_FIELD;
	private static final SaveConstructor PLAYER_INFO_PACKET;
	
	static {
        GAME_PROFILE_NAME_FIELD = Utils.nms().getDeclaredField("name", GameProfile.class);
        PLAYER_INFO_PACKET = Utils.nms().getDeclaredConstructor(
				Utils.nms().getNMSClass("PacketPlayOutPlayerInfo"),
				Utils.nms().getNMSClass("PacketPlayOutPlayerInfo$EnumPlayerInfoAction"),
				Utils.nms().getNmsClassAsArray("EntityPlayer"));
	}
	
	private final JavaPlugin plugin;
	private Player player;
	private UUID id;
	private String customName = "";
	private Skin skin;
	private GameProfile profile;
	
	public AdvancedPlayer(JavaPlugin plugin, Player player) throws UUIDFetcher.UUIDNotFoundException {
		this(plugin, Alpary.getInstance().uuidFetcher().getUUID(player));
	}
	
	public AdvancedPlayer(JavaPlugin plugin, UUID id) {
		this.plugin = plugin;
		this.id = id;
		this.player = Bukkit.getPlayer(id);
		this.profile = Utils.nms().getGameProfile(this.getPlayer());
	}
	
	public static SaveField getGameProfileNameField() {
		return AdvancedPlayer.GAME_PROFILE_NAME_FIELD;
	}
	
	public static SaveConstructor getPlayerInfoPacket() {
		return AdvancedPlayer.PLAYER_INFO_PACKET;
	}
	
	public int getPing() {
		return Utils.nms().getPing(this.getPlayer());
	}
	
	public boolean isSwimming() {
        return (boolean) Utils.nms().getDeclaredField("inWater",
				Utils.nms().getNMSClass("Entity")).get(this.getEntityPlayer(), true);
	}
	
	public Object getEntityPlayer() {
		return Utils.nms().getCraftPlayer(this.getPlayer());
	}
	
	public AdvancedPlayer addPermission(String permission) {
		if (!this.getPlayer().hasPermission(permission))
			this.getPlayer().addAttachment(this.getPlugin(), permission, true);
		return this;
	}
	
	public AdvancedPlayer removePermission(String permission) {
		if (!this.getPlayer().hasPermission(permission))
			this.getPlayer().addAttachment(this.getPlugin(), permission, false);
		return this;
	}
	
	public void fakeRespawn() {
		Location loc = this.getPlayer().getLocation();
        RDifficulty difficulty = RDifficulty.EASY;
		int level = this.getPlayer().getLevel();
		double health = this.getPlayer().getHealth();
		float sat = this.getPlayer().getSaturation();
		float exp = this.getPlayer().getExp();
		float exhaustion = this.getPlayer().getExhaustion();
		int foodLevel = this.getPlayer().getFoodLevel();
		double healthScale = this.getPlayer().getHealthScale();
		boolean healthScaled = this.getPlayer().isHealthScaled();
		
		Utils.nms().sendPacket(this.getPlayer(), new RespawnPacket(difficulty,
                this.getPlayer().getWorld().getWorldType(), RGamemode.getFromPlayer(this.getPlayer())));
		
		this.getPlayer().teleport(loc);
		this.getPlayer().setLevel(level);
		this.getPlayer().setHealth(health);
		this.getPlayer().setSaturation(sat);
		this.getPlayer().setExp(exp);
		this.getPlayer().setExhaustion(exhaustion);
		this.getPlayer().setFoodLevel(foodLevel);
		this.getPlayer().setHealthScaled(healthScaled);
		this.getPlayer().setHealthScale(healthScale);
	}
	
	public AdvancedPlayer setCustomName(String name) {
		this.customName = name;
		AdvancedPlayer.getGameProfileNameField().set(this.getProfile(), name, true);
		Utils.nms().sendPackets(
                AdvancedPlayer.getPlayerInfoPacket().newInstance(true, RPlayerInfoAction.REMOVE_PLAYER
						.getPlayerInfoAction(), new Object[]{this.getEntityPlayer()}),
                AdvancedPlayer.getPlayerInfoPacket().newInstance(true, RPlayerInfoAction.ADD_PLAYER
						.getPlayerInfoAction(), new Object[]{this.getEntityPlayer()})
		);
		return this;
	}
	
	public AdvancedPlayer setSkin(String name) throws IOException {
		this.skin = new Skin(getId());
		
		if (this.getSkin().getSkinName() != null) {
			this.getProfile().getProperties().removeAll("textures");
			this.getProfile().getProperties().put("textures", new Property("textures",
					this.getSkin().getSkinValue(), this.getSkin().getSkinSignature()));
			
			this.fakeRespawn();
		}
		return this;
	}
}
