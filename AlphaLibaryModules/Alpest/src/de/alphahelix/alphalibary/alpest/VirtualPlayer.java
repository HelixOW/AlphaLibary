package de.alphahelix.alphalibary.alpest;

import de.alphahelix.alphalibary.core.utils.ScheduleUtil;
import de.alphahelix.alphalibary.core.utils.StringUtil;
import de.alphahelix.alphalibary.storage.file.SimpleTXTFile;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.entity.*;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.*;
import org.bukkit.map.MapView;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Consumer;
import org.bukkit.util.Vector;

import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Used to spawn a {@link Player} which is no real User, but can be treated as one by plugins
 *
 * @author AlphaHelix
 * @version 1.0
 * @see Player
 * @see VirtualPlayerInventory
 * @since 1.9.2.1
 */
public class VirtualPlayer implements Player {
	
	private final String name;
	private final UUID id;
	private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEE, d MMM yyyy HH:mm:ss");
	private final List<String> hidden = new ArrayList<>();
	private final List<Entity> nearbyEntities = new ArrayList<>();
	private final Map<Material, Integer> cooldown = new HashMap<>();
	private SimpleTXTFile txtfile;
	private Location fakeLocation, fakeBed;
	private boolean sneak = false, sprint = false, playedBefore = false;
	private int level, totalExp, foodLevel;
	private float exp, exhaustion, saturation;
	private boolean allowFlight;
	
	private boolean fly;
	private float flySpeed;
	private float walkSpeed;
	private Scoreboard board;
	private boolean healthScaled;
	private double healthScale;
	private Entity specTarget;
	private Vector velocity;
	
	private int fireTicks;
	private List<Entity> passenger;
	private float fallDistance;
	private EntityDamageEvent ldmgc;
	private int ticksLived;
	private Vehicle vehicle;
	
	private boolean glowing;
	private boolean invincible;
	private boolean silent;
	private boolean gravity;
	
	private int portalCooldown;
	private Set<String> scoreboardtags;
	private VirtualPlayerInventory vpi = new VirtualPlayerInventory(
			this, new ItemStack[]{}, new ItemStack[]{},
			null, null, null, null,
			null, null, 0
	);
	private Inventory enderchest;
	private InventoryView inventoryView;
	private ItemStack inHand;
	private ItemStack onCursor;
	
	private int sleepTicks;
	private GameMode mode;
	
	private boolean blocking;
	private boolean handRaised;
	
	private int exptoLevel;
	private Entity shoulderLeft;
	
	private Entity shoulderRight;
	private double eyehight;
	
	private Location eyeLocation;
	private List<Block> lineOfSight = new ArrayList<>();
	
	private Block targetBlock;
	private List<Block> lastTwoTargetBlocks = new ArrayList<>();
	
	private int remainAir;
	private int maxAir;
	
	private int maxNoDmg;
	private double lastDamage;
	
	private int noDmg;
	private Player killer;
	
	private Collection<PotionEffect> potionEffects = new ArrayList<>();
	private boolean gliding;
	
	private double health;
	private double maxHealth;
	
	
	public VirtualPlayer(JavaPlugin plugin, String name, Location fakeLocation) {
		this(plugin, name, UUID.randomUUID(), fakeLocation);
	}
	
	public VirtualPlayer(JavaPlugin plugin, String name, UUID id, Location fakeLocation) {
		this.name = name;
		this.id = id;
		this.fakeLocation = fakeLocation;
		this.txtfile = new SimpleTXTFile(plugin.getDataFolder().getAbsolutePath() + "/virtualPlayerLogs", name + "_" + id.toString() + ".log");
		callEvent(new PlayerJoinEvent(this, ""), playerJoinEvent -> sendRawMessage("Player joined!"));
		Bukkit.getPluginManager().callEvent(new PlayerJoinEvent(this, ""));
	}
	
	public VirtualPlayer(JavaPlugin plugin, UUID id, Location fakeLocation) {
		this(plugin, StringUtil.generateRandomString(15), id, fakeLocation);
	}
	
	public VirtualPlayer(JavaPlugin plugin, Location fakeLocation) {
		this(plugin, StringUtil.generateRandomString(15), UUID.randomUUID(), fakeLocation);
	}
	
	public SimpleTXTFile getConsole() {
		return txtfile;
	}
	
	public UUID getId() {
		return id;
	}
	
	public Location getFakeLocation() {
		return fakeLocation;
	}
	
	public VirtualPlayer setFakeLocation(Location fakeLocation) {
		this.fakeLocation = fakeLocation;
		return this;
	}
	
	public VirtualPlayer setPlayedBefore(boolean playedBefore) {
		this.playedBefore = playedBefore;
		return this;
	}
	
	public Location getFakeBed() {
		return fakeBed;
	}
	
	public VirtualPlayer setFakeBed(Location fakeBed) {
		this.fakeBed = fakeBed;
		return this;
	}
	
	@Override
	public InventoryView openInventory(Inventory inventory) {
		this.inventoryView = new InventoryView() {
			@Override
			public Inventory getTopInventory() {
				return inventory;
			}
			
			@Override
			public Inventory getBottomInventory() {
				return vpi;
			}
			
			@Override
			public HumanEntity getPlayer() {
				return (HumanEntity) vpi;
			}
			
			@Override
			public InventoryType getType() {
				return inventory.getType();
			}
		};
		
		callEvent(new InventoryOpenEvent(inventoryView), inventoryOpenEvent -> sendRawMessage("Opened a inventory " + inventory));
		return inventoryView;
	}
	
	@Override
	public void setCompassTarget(Location loc) {
	
	}
	
	public InventoryView openPlayerInventory() {
		this.inventoryView = new InventoryView() {
			@Override
			public Inventory getTopInventory() {
				return null;
			}
			
			@Override
			public Inventory getBottomInventory() {
				return vpi;
			}
			
			@Override
			public HumanEntity getPlayer() {
				return (HumanEntity) vpi;
			}
			
			@Override
			public InventoryType getType() {
				return InventoryType.PLAYER;
			}
		};
		
		callEvent(new InventoryOpenEvent(inventoryView), inventoryOpenEvent -> sendRawMessage("Opened the players inventory"));
		return inventoryView;
	}
	
	@Override
	public Location getCompassTarget() {
		return fakeLocation;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(name, id, txtfile, dtf, fakeLocation, fakeBed, sneak, sprint, playedBefore, level, totalExp, foodLevel, exp, exhaustion, saturation, allowFlight, hidden, fly, flySpeed, walkSpeed, board, healthScaled, healthScale, specTarget, velocity, nearbyEntities, fireTicks, passenger, fallDistance, ldmgc, ticksLived, vehicle, glowing, invincible, silent, gravity, portalCooldown, scoreboardtags, vpi, enderchest, inventoryView, inHand, onCursor, cooldown, sleepTicks, mode, blocking, handRaised, exptoLevel, shoulderLeft, shoulderRight, eyehight, eyeLocation, lineOfSight, targetBlock, lastTwoTargetBlocks, remainAir, maxAir, maxNoDmg, lastDamage, noDmg, killer, potionEffects, gliding, health, maxHealth);
	}
	
	@Override
	public InetSocketAddress getAddress() {
		return InetSocketAddress.createUnresolved("127.0.0.1", 25565);
	}
	
	@Override
	public void sendRawMessage(String message) {
		if(getConsole() == null) return;
		getConsole().setValue("[" + dtf.format(LocalDateTime.now()) + "] " + message);
	}
	
	@Override
	public void kickPlayer(String message) {
		callEvent(new PlayerKickEvent(this, "Kicked by Plugin", message), playerKickEvent ->
				sendRawMessage("KICKED -> " + message));
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		VirtualPlayer that = (VirtualPlayer) o;
		return sneak == that.sneak &&
				sprint == that.sprint &&
				playedBefore == that.playedBefore &&
				level == that.level &&
				totalExp == that.totalExp &&
				foodLevel == that.foodLevel &&
				Float.compare(that.exp, exp) == 0 &&
				Float.compare(that.exhaustion, exhaustion) == 0 &&
				Float.compare(that.saturation, saturation) == 0 &&
				allowFlight == that.allowFlight &&
				fly == that.fly &&
				Float.compare(that.flySpeed, flySpeed) == 0 &&
				Float.compare(that.walkSpeed, walkSpeed) == 0 &&
				healthScaled == that.healthScaled &&
				Double.compare(that.healthScale, healthScale) == 0 &&
				fireTicks == that.fireTicks &&
				Float.compare(that.fallDistance, fallDistance) == 0 &&
				ticksLived == that.ticksLived &&
				glowing == that.glowing &&
				invincible == that.invincible &&
				silent == that.silent &&
				gravity == that.gravity &&
				portalCooldown == that.portalCooldown &&
				sleepTicks == that.sleepTicks &&
				blocking == that.blocking &&
				handRaised == that.handRaised &&
				exptoLevel == that.exptoLevel &&
				Double.compare(that.eyehight, eyehight) == 0 &&
				remainAir == that.remainAir &&
				maxAir == that.maxAir &&
				maxNoDmg == that.maxNoDmg &&
				Double.compare(that.lastDamage, lastDamage) == 0 &&
				noDmg == that.noDmg &&
				gliding == that.gliding &&
				Double.compare(that.health, health) == 0 &&
				Double.compare(that.maxHealth, maxHealth) == 0 &&
				Objects.equals(name, that.name) &&
				Objects.equals(id, that.id) &&
				Objects.equals(txtfile, that.txtfile) &&
				Objects.equals(dtf, that.dtf) &&
				Objects.equals(fakeLocation, that.fakeLocation) &&
				Objects.equals(fakeBed, that.fakeBed) &&
				Objects.equals(hidden, that.hidden) &&
				Objects.equals(board, that.board) &&
				Objects.equals(specTarget, that.specTarget) &&
				Objects.equals(velocity, that.velocity) &&
				Objects.equals(nearbyEntities, that.nearbyEntities) &&
				Objects.equals(passenger, that.passenger) &&
				Objects.equals(ldmgc, that.ldmgc) &&
				Objects.equals(vehicle, that.vehicle) &&
				Objects.equals(scoreboardtags, that.scoreboardtags) &&
				Objects.equals(vpi, that.vpi) &&
				Objects.equals(enderchest, that.enderchest) &&
				Objects.equals(inventoryView, that.inventoryView) &&
				Objects.equals(inHand, that.inHand) &&
				Objects.equals(onCursor, that.onCursor) &&
				Objects.equals(cooldown, that.cooldown) &&
				mode == that.mode &&
				Objects.equals(shoulderLeft, that.shoulderLeft) &&
				Objects.equals(shoulderRight, that.shoulderRight) &&
				Objects.equals(eyeLocation, that.eyeLocation) &&
				Objects.equals(lineOfSight, that.lineOfSight) &&
				Objects.equals(targetBlock, that.targetBlock) &&
				Objects.equals(lastTwoTargetBlocks, that.lastTwoTargetBlocks) &&
				Objects.equals(killer, that.killer) &&
				Objects.equals(potionEffects, that.potionEffects);
	}
	
	@Override
	public void chat(String msg) {
		ScheduleUtil.runLater(0, true, () -> callEvent(new AsyncPlayerChatEvent(true, this, msg, new HashSet<>(Bukkit.getOnlinePlayers())),
				asyncPlayerChatEvent -> sendRawMessage("CHAT -> " + msg)));
		
	}
	
	@Override
	public boolean performCommand(String command) {
		sendRawMessage("COMMAND -> " + command);
		return Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
	}
	
	@Override
	public boolean isSneaking() {
		return sneak;
	}
	
	@Override
	public void setSneaking(boolean sneak) {
		callEvent(new PlayerToggleSneakEvent(this, sneak), playerToggleSneakEvent -> {
			this.sneak = sneak;
			sendRawMessage("Is now sneaking? -> " + sneak);
		});
	}
	
	@Override
	public boolean isSprinting() {
		return sprint;
	}
	
	@Override
	public void setSprinting(boolean sprinting) {
		callEvent(new PlayerToggleSprintEvent(this, sprinting), playerToggleSprintEvent -> {
			this.sprint = sprinting;
			sendRawMessage("Is now sprinting? -> " + sprinting);
		});
	}
	
	@Override
	public void saveData() {
	
	}
	
	@Override
	public void loadData() {
	
	}
	
	@Override
	public void setSleepingIgnored(boolean isSleeping) {
	
	}
	
	@Override
	public boolean isSleepingIgnored() {
		return false;
	}
	
	@Override
	public void playNote(Location loc, byte instrument, byte note) {
		sendRawMessage("Played instrument " + instrument + ", note " + note + " at " + loc);
	}
	
	@Override
	public void playNote(Location loc, Instrument instrument, Note note) {
		sendRawMessage("Played instrument " + instrument + ", note " + note + " at " + loc);
	}
	
	@Override
	public void playSound(Location location, Sound sound, float volume, float pitch) {
		sendRawMessage("Played Sound " + sound + " with volume " + volume + " and pitch of " + pitch + " at " + location);
	}
	
	@Override
	public void playSound(Location location, String sound, float volume, float pitch) {
		sendRawMessage("Played Sound " + sound + " with volume " + volume + " and pitch of " + pitch + " at " + location);
	}
	
	@Override
	public void playSound(Location location, Sound sound, SoundCategory category, float volume, float pitch) {
		sendRawMessage("Played Sound " + sound + " of SoundCategory " + category + " with volume " + volume + " and pitch of " + pitch + " at " + location);
	}
	
	@Override
	public String getDisplayName() {
		return name;
	}
	
	@Override
	public void setDisplayName(String name) {
	
	}
	
	@Override
	public void playSound(Location location, String sound, SoundCategory category, float volume, float pitch) {
		sendRawMessage("Played Sound " + sound + " of SoundCategory " + category + " with volume " + volume + " and pitch of " + pitch + " at " + location);
	}
	
	@Override
	public String getPlayerListName() {
		return name;
	}
	
	@Override
	public void setPlayerListName(String name) {
	
	}
	
	@Override
	public void stopSound(Sound sound) {
		sendRawMessage("Stopped playing Sound " + sound);
	}
	
	@Override
	public boolean isConversing() {
		return false;
	}
	
	@Override
	public void acceptConversationInput(String input) {
	
	}
	
	@Override
	public void stopSound(String sound) {
		sendRawMessage("Stopped playing Sound " + sound);
	}
	
	@Override
	public boolean beginConversation(Conversation conversation) {
		return false;
	}
	
	@Override
	public void abandonConversation(Conversation conversation) {
	
	}
	
	@Override
	public void stopSound(Sound sound, SoundCategory category) {
		sendRawMessage("Stopped playing Sound " + sound + " of the category " + category);
	}
	
	@Override
	public void abandonConversation(Conversation conversation, ConversationAbandonedEvent details) {
	
	}
	
	@Override
	public boolean isOnline() {
		return true;
	}
	
	@Override
	public void stopSound(String sound, SoundCategory category) {
		sendRawMessage("Stopped playing Sound " + sound + " of the category " + category);
	}
	
	@Override
	public boolean isBanned() {
		return false;
	}
	
	@Override
	public boolean isWhitelisted() {
		return true;
	}
	
	@Override
	public void playEffect(Location loc, Effect effect, int data) {
		sendRawMessage("Played Effect " + effect + " with data " + data + " at " + loc);
	}
	
	@Override
	public void setWhitelisted(boolean value) {
		sendRawMessage("Changed whitelist status to " + value);
	}
	
	@Override
	public Player getPlayer() {
		return this;
	}
	
	@Override
	public <T> void playEffect(Location loc, Effect effect, T data) {
		sendRawMessage("Played Effect " + effect + " with data " + data + " at " + loc);
	}
	
	@Override
	public long getFirstPlayed() {
		return 0;
	}
	
	@Override
	public long getLastPlayed() {
		return 0;
	}
	
	@Override
	public void sendBlockChange(Location loc, Material material, byte data) {
		sendRawMessage("Sent block change of Material " + material + " and data " + data + " at " + loc);
	}
	
	@Override
	public boolean hasPlayedBefore() {
		return playedBefore;
	}
	
	public void die() {
		callEvent(new PlayerDeathEvent(this, Arrays.asList(vpi.getContents()), (int) this.getExp(), ""), playerDeathEvent -> {
			this.health = 0;
			this.exp = 0;
			this.level = 0;
			sendRawMessage("DEATH");
		});
		callEvent(new PlayerRespawnEvent(this, fakeLocation, false), playerRespawnEvent ->
				this.health = this.maxHealth);
	}
	
	@Override
	public boolean sendChunkChange(Location loc, int sx, int sy, int sz, byte[] data) {
		sendRawMessage("Sent chunk change at " + sx + ", " + sy + ", " + sz + " and " + Arrays.toString(data) + " at " + loc);
		return false;
	}
	
	@Override
	public Location getLocation() {
		return fakeLocation;
	}
	
	@Override
	public Location getLocation(Location loc) {
		return fakeLocation;
	}
	
	@Override
	public void sendBlockChange(Location loc, int material, byte data) {
		sendRawMessage("Sent block change of " + material + " and data " + data + " at " + loc);
	}
	
	public VirtualPlayer addNearbyEntity(Entity entity) {
		nearbyEntities.add(entity);
		return this;
	}
	
	@Override
	public void sendMessage(String message) {
		sendRawMessage("MESSAGE -> " + message);
	}
	
	@Override
	public void sendSignChange(Location loc, String[] lines) throws IllegalArgumentException {
		sendRawMessage("Sent sign change with " + Arrays.toString(lines) + " at " + loc);
	}
	
	@Override
	public void sendMessage(String[] messages) {
		sendRawMessage("MESSAGES -> " + Arrays.toString(messages));
	}
	
	@Override
	public Map<String, Object> serialize() {
		return new HashMap<>();
	}
	
	@Override
	public void sendMap(MapView map) {
		sendRawMessage("Sent a map!");
		sendRawMessage(map.toString());
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public PlayerInventory getInventory() {
		return vpi;
	}
	
	@Override
	public void updateInventory() {
		sendRawMessage("Updated the Inventory!");
	}
	
	@Override
	public Inventory getEnderChest() {
		return enderchest;
	}
	
	@Override
	public MainHand getMainHand() {
		return MainHand.LEFT;
	}
	
	@Override
	public void awardAchievement(Achievement achievement) {
		callEvent(new PlayerAchievementAwardedEvent(this, achievement), playerAchievementAwardedEvent -> sendRawMessage("Awarded a Achievement " + achievement));
	}
	
	@Override
	public boolean setWindowProperty(InventoryView.Property prop, int value) {
		return false;
	}
	
	@Override
	public InventoryView getOpenInventory() {
		return inventoryView;
	}
	
	@Override
	public void removeAchievement(Achievement achievement) {
		sendRawMessage("Removed a Achievement " + achievement);
	}
	
	@Override
	public String toString() {
		return "VirtualPlayer{" +
				"name='" + name + '\'' +
				", id=" + id +
				", txtfile=" + txtfile +
				", dtf=" + dtf +
				", fakeLocation=" + fakeLocation +
				", fakeBed=" + fakeBed +
				", sneak=" + sneak +
				", sprint=" + sprint +
				", playedBefore=" + playedBefore +
				", level=" + level +
				", totalExp=" + totalExp +
				", foodLevel=" + foodLevel +
				", exp=" + exp +
				", exhaustion=" + exhaustion +
				", saturation=" + saturation +
				", allowFlight=" + allowFlight +
				", hidden=" + hidden +
				", fly=" + fly +
				", flySpeed=" + flySpeed +
				", walkSpeed=" + walkSpeed +
				", board=" + board +
				", healthScaled=" + healthScaled +
				", healthScale=" + healthScale +
				", specTarget=" + specTarget +
				", velocity=" + velocity +
				", nearbyEntities=" + nearbyEntities +
				", fireTicks=" + fireTicks +
				", passenger=" + passenger +
				", fallDistance=" + fallDistance +
				", ldmgc=" + ldmgc +
				", ticksLived=" + ticksLived +
				", vehicle=" + vehicle +
				", glowing=" + glowing +
				", invincible=" + invincible +
				", silent=" + silent +
				", gravity=" + gravity +
				", portalCooldown=" + portalCooldown +
				", scoreboardtags=" + scoreboardtags +
				", vpi=" + vpi +
				", enderchest=" + enderchest +
				", inventoryView=" + inventoryView +
				", inHand=" + inHand +
				", onCursor=" + onCursor +
				", cooldown=" + cooldown +
				", sleepTicks=" + sleepTicks +
				", mode=" + mode +
				", blocking=" + blocking +
				", handRaised=" + handRaised +
				", exptoLevel=" + exptoLevel +
				", shoulderLeft=" + shoulderLeft +
				", shoulderRight=" + shoulderRight +
				", eyehight=" + eyehight +
				", eyeLocation=" + eyeLocation +
				", lineOfSight=" + lineOfSight +
				", targetBlock=" + targetBlock +
				", lastTwoTargetBlocks=" + lastTwoTargetBlocks +
				", remainAir=" + remainAir +
				", maxAir=" + maxAir +
				", maxNoDmg=" + maxNoDmg +
				", lastDamage=" + lastDamage +
				", noDmg=" + noDmg +
				", killer=" + killer +
				", potionEffects=" + potionEffects +
				", gliding=" + gliding +
				", health=" + health +
				", maxHealth=" + maxHealth +
				'}';
	}
	
	
	@Override
	public InventoryView openWorkbench(Location location, boolean force) {
		sendRawMessage("Opened a workbench at " + location);
		return openInventory(Bukkit.createInventory(this, InventoryType.CRAFTING));
	}
	
	@Override
	public boolean hasAchievement(Achievement achievement) {
		return false;
	}
	
	@Override
	public InventoryView openEnchanting(Location location, boolean force) {
		sendRawMessage("Opened a enchanting at " + location);
		return openInventory(Bukkit.createInventory(this, InventoryType.ENCHANTING));
	}
	
	@Override
	public void openInventory(InventoryView inventory) {
		callEvent(new InventoryOpenEvent(inventory), inventoryOpenEvent -> sendRawMessage("Opened a inventory"));
	}
	
	@Override
	public void incrementStatistic(Statistic statistic) throws IllegalArgumentException {
		sendRawMessage("Increased a Statistic " + statistic);
	}
	
	@Override
	public InventoryView openMerchant(Villager trader, boolean force) {
		sendRawMessage("Opened a merchant");
		return openInventory(Bukkit.createInventory(null, InventoryType.MERCHANT));
	}
	
	@Override
	public InventoryView openMerchant(Merchant merchant, boolean force) {
		sendRawMessage("Opened a merchant");
		return openInventory(Bukkit.createInventory(null, InventoryType.MERCHANT));
	}
	
	@Override
	public void decrementStatistic(Statistic statistic) throws IllegalArgumentException {
		sendRawMessage("Decreased a Statistic " + statistic);
	}
	
	@Override
	public void closeInventory() {
		callEvent(new InventoryCloseEvent(inventoryView), inventoryCloseEvent -> {
			sendRawMessage("Closed the Inventory");
			inventoryView = null;
		});
	}
	
	@Override
	public ItemStack getItemInHand() {
		return inHand;
	}
	
	@Override
	public void incrementStatistic(Statistic statistic, int amount) throws IllegalArgumentException {
		sendRawMessage("Increased a Statistic " + statistic + " by " + amount);
	}
	
	@Override
	public void setItemInHand(ItemStack item) {
		callEvent(new PlayerItemHeldEvent(this, vpi.getHeldItemSlot(), vpi.getHeldItemSlot()), playerItemHeldEvent -> {
			this.inHand = item;
			sendRawMessage("Set item in hand to " + item);
		});
	}
	
	@Override
	public ItemStack getItemOnCursor() {
		return onCursor;
	}
	
	@Override
	public void decrementStatistic(Statistic statistic, int amount) throws IllegalArgumentException {
		sendRawMessage("Decreased a Statistic " + statistic + " by " + amount);
	}
	
	@Override
	public void setItemOnCursor(ItemStack item) {
		this.onCursor = item;
		sendRawMessage("Set item on cursor to " + item);
	}
	
	@Override
	public boolean hasCooldown(Material material) {
		return cooldown.containsKey(material);
	}
	
	@Override
	public void setStatistic(Statistic statistic, int newValue) throws IllegalArgumentException {
		sendRawMessage("Set a Statistic " + statistic + " to " + newValue);
	}
	
	@Override
	public int getCooldown(Material material) {
		return cooldown.get(material);
	}
	
	@Override
	public void setCooldown(Material material, int ticks) {
		cooldown.put(material, ticks);
		sendRawMessage("Registered cooldown for " + material + " with " + ticks);
	}
	
	@Override
	public int getStatistic(Statistic statistic) throws IllegalArgumentException {
		return 0;
	}
	
	@Override
	public boolean isSleeping() {
		return sleepTicks > 0;
	}
	
	@Override
	public int getSleepTicks() {
		return sleepTicks;
	}
	
	@Override
	public void incrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
		sendRawMessage("Increased a Statistic " + statistic + " at " + material);
	}
	
	public VirtualPlayer setSleepTicks(int sleepTicks) {
		this.sleepTicks = sleepTicks;
		return this;
	}
	
	@Override
	public GameMode getGameMode() {
		return mode;
	}
	
	@Override
	public void decrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
		sendRawMessage("Decreased a Statistic " + statistic + " at " + material);
	}
	
	@Override
	public void setGameMode(GameMode mode) {
		callEvent(new PlayerGameModeChangeEvent(this, mode), playerGameModeChangeEvent -> {
			this.mode = mode;
			sendRawMessage("Set GameMode to " + mode);
		});
	}
	
	@Override
	public boolean isBlocking() {
		return blocking;
	}
	
	@Override
	public int getStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
		return 0;
	}
	
	public VirtualPlayer setBlocking(boolean blocking) {
		this.blocking = blocking;
		return this;
	}
	
	@Override
	public boolean isHandRaised() {
		return handRaised;
	}
	
	@Override
	public void incrementStatistic(Statistic statistic, Material material, int amount) throws IllegalArgumentException {
		sendRawMessage("Increased a Statistic " + statistic + " at " + material + " by " + amount);
	}
	
	public VirtualPlayer setHandRaised(boolean handRaised) {
		this.handRaised = handRaised;
		return this;
	}
	
	@Override
	public int getExpToLevel() {
		return exptoLevel;
	}
	
	@Override
	public void decrementStatistic(Statistic statistic, Material material, int amount) throws IllegalArgumentException {
		sendRawMessage("Decreased a Statistic " + statistic + " at " + material + " by " + amount);
	}
	
	@Override
	public Entity getShoulderEntityLeft() {
		return shoulderLeft;
	}
	
	@Override
	public void setShoulderEntityLeft(Entity entity) {
		this.shoulderLeft = entity;
		sendRawMessage("Set parrot onto left shoulder");
	}
	
	@Override
	public void setStatistic(Statistic statistic, Material material, int newValue) throws IllegalArgumentException {
		sendRawMessage("Set a Statistic " + statistic + " at " + material + " to " + newValue);
	}
	
	@Override
	public Entity getShoulderEntityRight() {
		return shoulderRight;
	}
	
	@Override
	public void setShoulderEntityRight(Entity entity) {
		this.shoulderRight = entity;
		sendRawMessage("Set parrot onto right shoulder");
	}
	
	@Override
	public void incrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
		sendRawMessage("Increased a Statistic " + statistic + " at " + entityType);
	}
	
	public VirtualPlayer setPlayerInventory(VirtualPlayerInventory playerInventory) {
		this.vpi = playerInventory;
		return this;
	}
	
	public VirtualPlayer setInventoryView(InventoryView inventoryView) {
		this.inventoryView = inventoryView;
		return this;
	}
	
	@Override
	public void decrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
		sendRawMessage("Decreased a Statistic " + statistic + " at " + entityType);
	}
	
	public void dropItem() {
		vpi.setItem(vpi.getHeldItemSlot(), new ItemStack(Material.AIR));
	}
	
	public void dropItem(ItemStack item) {
		vpi.setItem(vpi.first(item), new ItemStack(Material.AIR));
	}
	
	@Override
	public int getStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
		return 0;
	}
	
	public VirtualPlayer setExptoLevel(int exptoLevel) {
		this.exptoLevel = exptoLevel;
		return this;
	}
	
	@Override
	public double getEyeHeight() {
		return eyehight;
	}
	
	@Override
	public void incrementStatistic(Statistic statistic, EntityType entityType, int amount) throws IllegalArgumentException {
		sendRawMessage("Increased a Statistic " + statistic + " at " + entityType + " by " + amount);
	}
	
	@Override
	public double getEyeHeight(boolean ignoreSneaking) {
		return eyehight;
	}
	
	@Override
	public Location getEyeLocation() {
		return eyeLocation;
	}
	
	@Override
	public void decrementStatistic(Statistic statistic, EntityType entityType, int amount) {
		sendRawMessage("Decreased a Statistic " + statistic + " at " + entityType + " by " + amount);
	}
	
	public VirtualPlayer setEyeLocation(Location eyeLocation) {
		this.eyeLocation = eyeLocation;
		return this;
	}
	
	@Override
	public List<Block> getLineOfSight(Set<Material> transparent, int maxDistance) {
		return lineOfSight;
	}
	
	@Override
	public void setStatistic(Statistic statistic, EntityType entityType, int newValue) {
		sendRawMessage("Set a Statistic " + statistic + " at " + entityType + " to " + newValue);
	}
	
	@Override
	public Block getTargetBlock(Set<Material> transparent, int maxDistance) {
		return targetBlock;
	}
	
	@Override
	public List<Block> getLastTwoTargetBlocks(Set<Material> transparent, int maxDistance) {
		return lastTwoTargetBlocks;
	}
	
	@Override
	public void setPlayerTime(long time, boolean relative) {
		sendRawMessage("Set time to " + time + " which is relative? " + relative);
	}
	
	@Override
	public int getRemainingAir() {
		return remainAir;
	}
	
	@Override
	public void setRemainingAir(int ticks) {
		this.remainAir = ticks;
		sendRawMessage("Set remaining air to " + ticks);
	}
	
	@Override
	public long getPlayerTime() {
		return 0;
	}
	
	@Override
	public int getMaximumAir() {
		return maxAir;
	}
	
	@Override
	public void setMaximumAir(int ticks) {
		this.maxAir = ticks;
		sendRawMessage("Set maximum air to " + ticks);
	}
	
	@Override
	public long getPlayerTimeOffset() {
		return 0;
	}
	
	@Override
	public int getMaximumNoDamageTicks() {
		return maxNoDmg;
	}
	
	@Override
	public void setMaximumNoDamageTicks(int ticks) {
		this.maxNoDmg = ticks;
		sendRawMessage("Set maximum no damage ticks to " + ticks);
	}
	
	@Override
	public boolean isPlayerTimeRelative() {
		return false;
	}
	
	@Override
	public double getLastDamage() {
		return lastDamage;
	}
	
	@Override
	public void setLastDamage(double damage) {
		this.lastDamage = damage;
		sendRawMessage("Set last damage to " + damage);
	}
	
	@Override
	public void resetPlayerTime() {
		sendRawMessage("Reset time!");
	}
	
	@Override
	public int getNoDamageTicks() {
		return noDmg;
	}
	
	@Override
	public void setNoDamageTicks(int ticks) {
		this.noDmg = ticks;
		sendRawMessage("Set no damage ticks to " + ticks);
	}
	
	@Override
	public void setPlayerWeather(WeatherType type) {
		sendRawMessage("Set weather to " + type);
	}
	
	@Override
	public Player getKiller() {
		return killer;
	}
	
	public VirtualPlayer setKiller(Player killer) {
		this.killer = killer;
		return this;
	}
	
	@Override
	public WeatherType getPlayerWeather() {
		return WeatherType.CLEAR;
	}
	
	@Override
	public boolean addPotionEffect(PotionEffect effect) {
		potionEffects.add(effect);
		sendRawMessage("Added potion effect " + effect);
		return true;
	}
	
	@Override
	public boolean addPotionEffect(PotionEffect effect, boolean force) {
		return addPotionEffect(effect);
	}
	
	@Override
	public void resetPlayerWeather() {
		sendRawMessage("Reset weather!");
	}
	
	@Override
	public boolean addPotionEffects(Collection<PotionEffect> effects) {
		potionEffects = effects;
		sendRawMessage("Changed potion effects to " + effects);
		return true;
	}
	
	@Override
	public boolean hasPotionEffect(PotionEffectType type) {
		return false;
	}
	
	@Override
	public PotionEffect getPotionEffect(PotionEffectType type) {
		return null;
	}
	
	@Override
	public void removePotionEffect(PotionEffectType type) {
		sendRawMessage("Potion effect removed!");
	}
	
	@Override
	public void giveExp(int amount) {
		callEvent(new PlayerExpChangeEvent(this, amount), playerExpChangeEvent -> {
			exp += amount;
			sendRawMessage("Gave " + amount + " EXP");
		});
	}
	
	@Override
	public Collection<PotionEffect> getActivePotionEffects() {
		return potionEffects;
	}
	
	@Override
	public boolean hasLineOfSight(Entity other) {
		return false;
	}
	
	@Override
	public void giveExpLevels(int amount) {
		callEvent(new PlayerLevelChangeEvent(this, level - amount, amount), playerLevelChangeEvent -> {
			level += amount;
			sendRawMessage("Gave " + amount + " EXP Levels");
		});
	}
	
	@Override
	public boolean getRemoveWhenFarAway() {
		return false;
	}
	
	@Override
	public void setRemoveWhenFarAway(boolean remove) {
	
	}
	
	@Override
	public float getExp() {
		return exp;
	}
	
	@Override
	public EntityEquipment getEquipment() {
		return null;
	}
	
	public VirtualPlayer setEyehight(double eyehight) {
		this.eyehight = eyehight;
		return this;
	}
	
	@Override
	public void setExp(float exp) {
		callEvent(new PlayerExpChangeEvent(this, (int) (this.exp - exp)), playerExpChangeEvent -> {
			this.exp = exp;
			sendRawMessage("Set EXP to " + exp);
		});
		
	}
	
	public VirtualPlayer setLineOfSight(List<Block> lineOfSight) {
		this.lineOfSight = lineOfSight;
		return this;
	}
	
	public VirtualPlayer setTargetBlock(Block targetBlock) {
		this.targetBlock = targetBlock;
		return this;
	}
	
	@Override
	public int getLevel() {
		return level;
	}
	
	public VirtualPlayer setLastTwoTargetBlocks(List<Block> lastTwoTargetBlocks) {
		this.lastTwoTargetBlocks = lastTwoTargetBlocks;
		return this;
	}
	
	@Override
	public AttributeInstance getAttribute(Attribute attribute) {
		return null;
	}
	
	@Override
	public void setLevel(int level) {
		callEvent(new PlayerLevelChangeEvent(this, this.level, level), playerLevelChangeEvent -> {
			this.level = level;
			sendRawMessage("Set Level to " + level);
		});
		
	}
	
	@Override
	public void damage(double amount) {
		callEvent(new EntityDamageEvent(this, EntityDamageEvent.DamageCause.CUSTOM, amount), entityDamageEvent -> {
			sendRawMessage("Damaged by " + amount);
			health -= amount;
		});
	}
	
	@Override
	public void damage(double amount, Entity source) {
		callEvent(new EntityDamageEvent(this, EntityDamageEvent.DamageCause.ENTITY_ATTACK, amount), entityDamageEvent -> {
			sendRawMessage("Damaged by " + amount + " by " + source);
			health -= amount;
		});
	}
	
	@Override
	public int getTotalExperience() {
		return totalExp;
	}
	
	@Override
	public double getHealth() {
		return health;
	}
	
	@Override
	public void setHealth(double health) {
		this.health = health;
		sendRawMessage("Health set to " + health);
	}
	
	@Override
	public void setTotalExperience(int exp) {
		this.totalExp = exp;
		sendRawMessage("Set total exp to " + exp);
	}
	
	@Override
	public double getMaxHealth() {
		return maxHealth;
	}
	
	@Override
	public void setMaxHealth(double health) {
		this.maxHealth = health;
		sendRawMessage("Max Health set to " + health);
	}
	
	@Override
	public float getExhaustion() {
		return exhaustion;
	}
	
	@Override
	public void resetMaxHealth() {
		this.maxHealth = 10;
		sendRawMessage("Max health set back");
	}
	
	@Override
	public String getCustomName() {
		return name;
	}
	
	@Override
	public void setExhaustion(float value) {
		this.exhaustion = value;
		sendRawMessage("Set exhaustion to " + value);
	}
	
	@Override
	public void setCustomName(String name) {
	
	}
	
	@Override
	public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
	
	}
	
	@Override
	public float getSaturation() {
		return saturation;
	}
	
	@Override
	public List<MetadataValue> getMetadata(String metadataKey) {
		return null;
	}
	
	@Override
	public boolean hasMetadata(String metadataKey) {
		return false;
	}
	
	@Override
	public void setSaturation(float value) {
		this.saturation = value;
		sendRawMessage("Set Saturation to " + value);
	}
	
	@Override
	public void removeMetadata(String metadataKey, Plugin owningPlugin) {
	
	}
	
	@Override
	public boolean isPermissionSet(String name) {
		return false;
	}
	
	@Override
	public int getFoodLevel() {
		return foodLevel;
	}
	
	@Override
	public boolean isPermissionSet(Permission perm) {
		return false;
	}
	
	@Override
	public boolean hasPermission(String name) {
		return false;
	}
	
	@Override
	public void setFoodLevel(int value) {
		callEvent(new FoodLevelChangeEvent(this, value), foodLevelChangeEvent -> {
			this.foodLevel = value;
			sendRawMessage("Set FoodLevel to " + value);
		});
	}
	
	@Override
	public boolean hasPermission(Permission perm) {
		return false;
	}
	
	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
		return null;
	}
	
	@Override
	public PermissionAttachment addAttachment(Plugin plugin) {
		return null;
	}
	
	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
		return null;
	}
	
	@Override
	public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
		return null;
	}
	
	@Override
	public void removeAttachment(PermissionAttachment attachment) {
	
	}
	
	@Override
	public void recalculatePermissions() {
	
	}
	
	@Override
	public Set<PermissionAttachmentInfo> getEffectivePermissions() {
		return null;
	}
	
	@Override
	public boolean isOp() {
		return true;
	}
	
	@Override
	public void setOp(boolean value) {
	
	}
	
	@Override
	public Location getBedSpawnLocation() {
		return fakeBed;
	}
	
	@Override
	public void sendPluginMessage(Plugin source, String channel, byte[] message) {
	
	}
	
	@Override
	public Set<String> getListeningPluginChannels() {
		return null;
	}
	
	@Override
	public void setBedSpawnLocation(Location location) {
		setFakeBed(location);
		sendRawMessage("Set bed to " + location);
	}
	
	@Override
	public <T extends Projectile> T launchProjectile(Class<? extends T> projectile) {
		return null;
	}
	
	@Override
	public <T extends Projectile> T launchProjectile(Class<? extends T> projectile, Vector velocity) {
		return null;
	}
	
	@Override
	public void setBedSpawnLocation(Location location, boolean force) {
		setFakeBed(location);
		sendRawMessage("Set bed to " + location);
	}
	
	@Override
	public boolean getAllowFlight() {
		return allowFlight;
	}
	
	@Override
	public void setAllowFlight(boolean flight) {
		this.allowFlight = flight;
		sendRawMessage("Allowed to flight? " + flight);
	}
	
	
	@Override
	public void hidePlayer(Player player) {
		sendRawMessage("Hidden " + player.getName());
		hidden.add(player.getName());
	}
	
	@Override
	public void showPlayer(Player player) {
		sendRawMessage("Shown " + player.getName());
		hidden.remove(player.getName());
	}
	
	@Override
	public boolean canSee(Player player) {
		return hidden.contains(player.getName());
	}
	
	
	@Override
	public boolean isFlying() {
		return fly;
	}
	
	@Override
	public void setFlying(boolean value) {
		callEvent(new PlayerToggleFlightEvent(this, value), playerToggleFlightEvent -> {
			this.fly = value;
			sendRawMessage("Can now fly? " + value);
		});
	}
	
	
	@Override
	public void setFlySpeed(float value) throws IllegalArgumentException {
		this.flySpeed = value;
		sendRawMessage("Set fly speed to " + value);
	}
	
	@Override
	public void setWalkSpeed(float value) throws IllegalArgumentException {
		this.walkSpeed = value;
		sendRawMessage("Set walk speed to" + value);
	}
	
	@Override
	public float getFlySpeed() {
		return flySpeed;
	}
	
	@Override
	public float getWalkSpeed() {
		return walkSpeed;
	}
	
	@Override
	public void setTexturePack(String url) {
		callEvent(new PlayerResourcePackStatusEvent(this, PlayerResourcePackStatusEvent.Status.ACCEPTED), playerResourcePackStatusEvent ->
				sendRawMessage("Set Texturepack to " + url));
	}
	
	@Override
	public void setResourcePack(String url) {
		callEvent(new PlayerResourcePackStatusEvent(this, PlayerResourcePackStatusEvent.Status.ACCEPTED), playerResourcePackStatusEvent -> sendRawMessage("Set Resourcepack to " + url));
	}
	
	@Override
	public void setResourcePack(String url, byte[] hash) {
		callEvent(new PlayerResourcePackStatusEvent(this, PlayerResourcePackStatusEvent.Status.ACCEPTED), playerResourcePackStatusEvent -> sendRawMessage("Set Resourcepack to " + url + " with hash " + Arrays.toString(hash)));
	}
	
	
	@Override
	public Scoreboard getScoreboard() {
		return board;
	}
	
	@Override
	public void setScoreboard(Scoreboard scoreboard) throws IllegalArgumentException, IllegalStateException {
		this.board = scoreboard;
		sendRawMessage("Set Scoreboard!");
	}
	
	
	@Override
	public boolean isHealthScaled() {
		return healthScaled;
	}
	
	@Override
	public void setHealthScaled(boolean scale) {
		this.healthScaled = scale;
		sendRawMessage("Set health scaled to " + scale);
	}
	
	
	@Override
	public void setHealthScale(double scale) throws IllegalArgumentException {
		this.healthScale = scale;
	}
	
	@Override
	public double getHealthScale() {
		return healthScale;
	}
	
	
	@Override
	public Entity getSpectatorTarget() {
		return specTarget;
	}
	
	@Override
	public void setSpectatorTarget(Entity entity) {
		this.specTarget = entity;
		sendRawMessage("Set spectator target to " + entity.getName());
	}
	
	@Override
	public void sendTitle(String title, String subtitle) {
		sendRawMessage("TITLE -> " + title + "    SUBTITLE -> " + subtitle);
	}
	
	@Override
	public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
		sendRawMessage("TITLE -> " + title + "    SUBTITLE -> " + subtitle + " with fadeIn of " + fadeIn + " a stay of " + stay + " and a fadeOut of " + fadeOut);
	}
	
	@Override
	public void resetTitle() {
		sendRawMessage("Title was reset");
	}
	
	@Override
	public void spawnParticle(Particle particle, Location location, int count) {
		sendRawMessage("Spawned " + count + " particle " + particle + " at " + location);
	}
	
	@Override
	public void spawnParticle(Particle particle, double x, double y, double z, int count) {
		sendRawMessage("Spawned " + count + " particle " + particle + " at " + x + ", " + y + ", " + z);
	}
	
	@Override
	public <T> void spawnParticle(Particle particle, Location location, int count, T data) {
		sendRawMessage("Spawned " + count + " particle " + particle + " with a data of " + data + " at " + location);
	}
	
	@Override
	public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, T data) {
		sendRawMessage("Spawned " + count + " particle " + particle + " with a data of " + data + " at " + x + ", " + y + ", " + z);
	}
	
	@Override
	public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ) {
		sendRawMessage("Spawned " + count + " particle " + particle + " at " + location);
	}
	
	@Override
	public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ) {
		sendRawMessage("Spawned " + count + " particle " + particle + " at " + x + ", " + y + ", " + z);
	}
	
	@Override
	public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, T data) {
		sendRawMessage("Spawned " + count + " particle " + particle + " at " + location);
	}
	
	@Override
	public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, T data) {
		sendRawMessage("Spawned " + count + " particle " + particle + " at " + x + ", " + y + ", " + z);
	}
	
	@Override
	public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra) {
		sendRawMessage("Spawned " + count + " particle " + particle + " at " + location);
	}
	
	@Override
	public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra) {
		sendRawMessage("Spawned " + count + " particle " + particle + " at " + x + ", " + y + ", " + z);
	}
	
	@Override
	public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
		sendRawMessage("Spawned " + count + " particle " + particle + " at " + location);
	}
	
	@Override
	public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
		sendRawMessage("Spawned " + count + " particle " + particle + " at " + x + ", " + y + ", " + z);
	}
	
	@Override
	public AdvancementProgress getAdvancementProgress(Advancement advancement) {
		callEvent(new PlayerAdvancementDoneEvent(this, advancement), playerAdvancementDoneEvent -> {});
		return null;
	}
	
	@Override
	public String getLocale() {
		return "en_US";
	}
	
	
	@Override
	public void setVelocity(Vector velocity) {
		callEvent(new PlayerVelocityEvent(this, velocity), playerVelocityEvent -> {
			this.velocity = velocity;
			sendRawMessage("Velocity set to " + velocity);
		});
	}
	
	@Override
	public Vector getVelocity() {
		return velocity;
	}
	
	@Override
	public double getHeight() {
		return 0;
	}
	
	@Override
	public double getWidth() {
		return 0;
	}
	
	@Override
	public boolean isOnGround() {
		return true;
	}
	
	@Override
	public World getWorld() {
		return fakeLocation.getWorld();
	}
	
	@Override
	public boolean teleport(Location location) {
		return teleport(location, PlayerTeleportEvent.TeleportCause.UNKNOWN);
	}
	
	@Override
	public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause cause) {
		callEvent(new PlayerTeleportEvent(this, fakeLocation, location, cause), playerTeleportEvent -> {
			if(!fakeLocation.getWorld().equals(location.getWorld()))
				callEvent(new PlayerChangedWorldEvent(this, fakeLocation.getWorld()), playerChangedWorldEvent -> {});
			fakeLocation = location;
			sendRawMessage("Teleported to " + location);
		});
		return true;
	}
	
	@Override
	public boolean teleport(Entity destination) {
		return teleport(destination.getLocation());
	}
	
	@Override
	public boolean teleport(Entity destination, PlayerTeleportEvent.TeleportCause cause) {
		return teleport(destination);
	}
	
	
	@Override
	public List<Entity> getNearbyEntities(double x, double y, double z) {
		return nearbyEntities;
	}
	
	
	@Override
	public int getEntityId() {
		return -1;
	}
	
	
	@Override
	public int getFireTicks() {
		return fireTicks;
	}
	
	@Override
	public int getMaxFireTicks() {
		return 0;
	}
	
	@Override
	public void setFireTicks(int ticks) {
		this.fireTicks = ticks;
		sendRawMessage("Fireticks set to " + ticks);
	}
	
	@Override
	public void remove() {
		sendRawMessage("Player removed");
		Bukkit.getPluginManager().callEvent(new PlayerQuitEvent(this, ""));
		txtfile = null;
	}
	
	@Override
	public boolean isDead() {
		return false;
	}
	
	@Override
	public boolean isValid() {
		return true;
	}
	
	
	@Override
	public Server getServer() {
		return Bukkit.getServer();
	}
	
	
	@Override
	public Entity getPassenger() {
		return passenger.get(0);
	}
	
	@Override
	public boolean setPassenger(Entity passenger) {
		this.passenger.add(passenger);
		sendRawMessage("Added passenger " + this.passenger);
		return true;
	}
	
	@Override
	public List<Entity> getPassengers() {
		return passenger;
	}
	
	@Override
	public boolean addPassenger(Entity passenger) {
		setPassenger(passenger);
		return true;
	}
	
	@Override
	public boolean removePassenger(Entity passenger) {
		this.passenger.remove(passenger);
		sendRawMessage("Removed passenger " + this.passenger);
		return true;
	}
	
	@Override
	public boolean isEmpty() {
		return false;
	}
	
	@Override
	public boolean eject() {
		return false;
	}
	
	
	@Override
	public float getFallDistance() {
		return fallDistance;
	}
	
	@Override
	public void setFallDistance(float distance) {
		this.fallDistance = distance;
		sendRawMessage("Set Falldistance to " + distance);
	}
	
	
	@Override
	public void setLastDamageCause(EntityDamageEvent event) {
		this.ldmgc = event;
		sendRawMessage("Set last damage cause to " + ldmgc);
	}
	
	@Override
	public EntityDamageEvent getLastDamageCause() {
		return ldmgc;
	}
	
	@Override
	public UUID getUniqueId() {
		return id;
	}
	
	
	@Override
	public int getTicksLived() {
		return ticksLived;
	}
	
	@Override
	public void setTicksLived(int value) {
		this.ticksLived = value;
		sendRawMessage("Set ticks lived to " + value);
	}
	
	@Override
	public void playEffect(EntityEffect type) {
		sendRawMessage("Played Effect " + type);
	}
	
	@Override
	public EntityType getType() {
		return EntityType.PLAYER;
	}
	
	
	public VirtualPlayer setVehicle(Entity vehicle) {
		this.vehicle = (Vehicle) vehicle;
		return this;
	}
	
	@Override
	public boolean isInsideVehicle() {
		return getVehicle() != null;
	}
	
	@Override
	public boolean leaveVehicle() {
		callEvent(new VehicleExitEvent(vehicle, this), vehicleExitEvent -> {
			sendRawMessage("Left vehicle");
			vehicle = null;
		});
		return true;
	}
	
	@Override
	public Entity getVehicle() {
		return vehicle;
	}
	
	@Override
	public void setCustomNameVisible(boolean flag) {
	
	}
	
	@Override
	public boolean isCustomNameVisible() {
		return false;
	}
	
	
	@Override
	public void setGlowing(boolean flag) {
		this.glowing = flag;
		sendRawMessage("Set glowing to " + flag);
	}
	
	@Override
	public boolean isGlowing() {
		return glowing;
	}
	
	
	@Override
	public void setInvulnerable(boolean flag) {
		this.invincible = flag;
		sendRawMessage("Set invincible to " + flag);
	}
	
	@Override
	public boolean isInvulnerable() {
		return invincible;
	}
	
	
	@Override
	public boolean isSilent() {
		return silent;
	}
	
	@Override
	public void setSilent(boolean flag) {
		this.silent = flag;
		sendRawMessage("Set silent to " + flag);
	}
	
	
	@Override
	public boolean hasGravity() {
		return gravity;
	}
	
	@Override
	public void setGravity(boolean gravity) {
		this.gravity = gravity;
		sendRawMessage("Set gravity to " + gravity);
	}
	
	
	@Override
	public int getPortalCooldown() {
		return portalCooldown;
	}
	
	@Override
	public void setPortalCooldown(int cooldown) {
		this.portalCooldown = cooldown;
		sendRawMessage("Set portal cooldown to " + cooldown);
	}
	
	
	@Override
	public Set<String> getScoreboardTags() {
		return scoreboardtags;
	}
	
	@Override
	public boolean addScoreboardTag(String tag) {
		scoreboardtags.add(tag);
		sendRawMessage("Added Scoreboard tag " + tag);
		return true;
	}
	
	@Override
	public boolean removeScoreboardTag(String tag) {
		scoreboardtags.remove(tag);
		sendRawMessage("Removed scoreboard tag " + tag);
		return true;
	}
	
	@Override
	public PistonMoveReaction getPistonMoveReaction() {
		return PistonMoveReaction.IGNORE;
	}
	
	@Override
	public Spigot spigot() {
		return null;
	}
	
	
	@Override
	public void setCanPickupItems(boolean pickup) {
	
	}
	
	@Override
	public boolean getCanPickupItems() {
		return true;
	}
	
	@Override
	public boolean isLeashed() {
		return false;
	}
	
	@Override
	public Entity getLeashHolder() throws IllegalStateException {
		return null;
	}
	
	@Override
	public boolean setLeashHolder(Entity holder) {
		return false;
	}
	
	
	@Override
	public boolean isGliding() {
		return gliding;
	}
	
	@Override
	public void setGliding(boolean gliding) {
		this.gliding = gliding;
		sendRawMessage("Set gliding to " + gliding);
	}
	
	@Override
	public void setAI(boolean ai) {
	
	}
	
	@Override
	public boolean hasAI() {
		return false;
	}
	
	@Override
	public void setCollidable(boolean collidable) {
	
	}
	
	@Override
	public boolean isCollidable() {
		return true;
	}
	
	private <E extends Event> void callEvent(E e, Consumer<E> action) {
		Bukkit.getPluginManager().callEvent(e);
		
		if(e instanceof Cancellable) {
			if(!((Cancellable) e).isCancelled())
				action.accept(e);
		} else
			action.accept(e);
	}
	
	
}
