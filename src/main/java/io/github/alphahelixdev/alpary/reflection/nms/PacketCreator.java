package io.github.alphahelixdev.alpary.reflection.nms;

import com.mojang.authlib.GameProfile;
import io.github.alphahelixdev.alpary.reflection.nms.enums.REnumGamemode;
import io.github.alphahelixdev.alpary.reflection.nms.enums.REnumPlayerInfoAction;
import io.github.alphahelixdev.alpary.utils.Utils;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class PacketCreator {
	
	/**
	 * Creates a new PacketPlayOutPlayerInfoAction packet
	 *
	 * @param enumPlayerInfoAction the {@link } to use
	 * @param gameProfile          the {@link GameProfile} to perform the action on
	 * @param ping                 the ping which should be displayed inside the tablist
	 * @param enumGamemode         the {@link REnumGamemode} to use
	 * @param name                 the name which should be displayed inside the tablist
	 *
	 * @return the PacketPlayOutPlayerInfoAction
	 */
	public static Object createPlayerInfoPacket(REnumPlayerInfoAction enumPlayerInfoAction, GameProfile gameProfile,
	                                            int ping, REnumGamemode enumGamemode, String name) {
		
		Class<?> cIChatBaseComponent = Utils.nms().getNMSClass("IChatBaseComponent");
		Class<?> cPacketPlayOutPlayerInfo = Utils.nms().getNMSClass("PacketPlayOutPlayerInfo");
		Class<?> cPlayerInfoData = Utils.nms().getNMSClass("PacketPlayOutPlayerInfo$PlayerInfoData");
		
		Class<?> cEnumGamemode;
		cEnumGamemode = Utils.nms().getNMSClass("EnumGamemode");
		
		try {
			Object pPacketPlayOutInfo = cPacketPlayOutPlayerInfo.getConstructor().newInstance();
			
			Field fa = pPacketPlayOutInfo.getClass().getDeclaredField("a");
			fa.setAccessible(true);
			fa.set(pPacketPlayOutInfo, enumPlayerInfoAction.getPlayerInfoAction());
			
			Object oPlayerInfoData = cPlayerInfoData.getConstructor(cPacketPlayOutPlayerInfo, GameProfile.class,
					int.class, cEnumGamemode, cIChatBaseComponent)
					.newInstance(pPacketPlayOutInfo, gameProfile, ping, enumGamemode.getEnumGamemode(),
							Utils.nms().toIChatBaseComponentArray(name));
			
			Field b = pPacketPlayOutInfo.getClass().getDeclaredField("b");
			b.setAccessible(true);
			ArrayList<Object> array = (ArrayList<Object>) b.get(pPacketPlayOutInfo);
			
			array.add(oPlayerInfoData);
			
			b.set(pPacketPlayOutInfo, array);
			
			return pPacketPlayOutInfo;
			
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
}
