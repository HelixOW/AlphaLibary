package de.alphahelix.alphalibary.fakeapi2;

import com.mojang.authlib.GameProfile;
import de.alphahelix.alphalibary.core.AlphaModule;
import de.alphahelix.alphalibary.core.Dependency;
import de.alphahelix.alphalibary.fakeapi2.events.FakeEntityClickEvent;
import de.alphahelix.alphalibary.fakeapi2.exceptions.NoSuchFakeEntityException;
import de.alphahelix.alphalibary.fakeapi2.handlers.EntityHandler;
import de.alphahelix.alphalibary.fakeapi2.instances.FakeArmorstand;
import de.alphahelix.alphalibary.fakeapi2.instances.FakeBigItem;
import de.alphahelix.alphalibary.fakeapi2.instances.FakeEntity;
import de.alphahelix.alphalibary.fakeapi2.storage.EntityStorage;
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
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Dependency(dependencies = {
		"PacketListenerAPI", "StorageModule", "InventoryModule"
})
public class FakeModule implements AlphaModule {
	
	private static final EntityHandler ENTITY_HANDLER = new EntityHandler();
	private static final List<String> NPCS = new ArrayList<>();
	private static final Map<Class<? extends FakeEntity>, EntityStorage<? extends FakeEntity>> STORAGE = new HashMap<>();
	
	public static EntityHandler getEntityHandler() {
		return ENTITY_HANDLER;
	}
	
	public static void init(JavaPlugin plugin) {
		STORAGE.put(FakeArmorstand.class,
				new EntityStorage<>(plugin, "armorstands", FakeArmorstand.class));
		STORAGE.put(FakeBigItem.class,
				new EntityStorage<>(plugin, "bigItems", FakeBigItem.class));
	}
	
	public static <T extends FakeEntity> EntityStorage<T> getStorage(Class<T> entity) {
		return (EntityStorage<T>) STORAGE.get(entity);
	}
	
	@Override
	public void enable() {
		PacketListenerAPI.addPacketHandler(new PacketHandler() {
			@PacketOptions(forcePlayer = true)
			public void onSend(SentPacket packet) {
				if(packet.getPacketName().equals("PacketPlayOutPlayerInfo")) {
					Object enumAction = packet.getPacketValue("a");
					
					if(!enumAction.toString().equals("ADD_PLAYER")) return;
					
					ArrayList<Object> newPlayerInfo = new ArrayList<>();
					
					for(Object playerInfo : (ArrayList) packet.getPacketValue("b")) {
						
						if(PlayerInfoDataWrapper.isUnknown(playerInfo)) {
							newPlayerInfo.add(playerInfo);
							continue;
						}
						
						PlayerInfoDataWrapper wrapper = PlayerInfoDataWrapper.getPlayerInfo(playerInfo);
						OfflinePlayer p = Bukkit.getOfflinePlayer(wrapper.getProfile().getId());
						
						if(getNPCS().contains(wrapper.getName())) {
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
				if(packet.getPacketName().equals("PacketPlayInUseEntity")) {
					Player p = packet.getPlayer();
					int id = (int) packet.getPacketValue("a");
					try {
						FakeEntity clicked = ENTITY_HANDLER.getFakeEntityByID(p, id);
						
						REnumAction action = REnumAction.values()[ReflectionUtil.getEnumConstantID(packet.getPacketValue("action"))];
						REnumHand hand = REnumHand.MAIN_HAND;
						
						if(packet.getPacketValue("d") != null)
							hand = REnumHand.values()[ReflectionUtil.getEnumConstantID(packet.getPacketValue("d"))];
						
						if(action != REnumAction.INTERACT_AT)
							Bukkit.getPluginManager().callEvent(new FakeEntityClickEvent(p, clicked, action, hand));
					} catch(NoSuchFakeEntityException ignore) {
					}
				}
			}
		});
	}
	
	public static List<String> getNPCS() {
		return NPCS;
	}
}
