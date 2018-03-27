package de.alphahelix.alphalibary.reflection.nms.wrappers;

import com.google.common.base.Objects;
import com.mojang.authlib.GameProfile;
import de.alphahelix.alphalibary.core.utils.abstracts.AbstractReflectionUtil;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;


public class PlayerInfoDataWrapper {
	
	private static final Class<?> P_ID_CLAZZ = ReflectionUtil.getNmsClass("PacketPlayOutPlayerInfo$PlayerInfoData");
	
	private static final AbstractReflectionUtil.SaveConstructor P_ID_CONSTUCTOR = ReflectionUtil.getDeclaredConstructor(
			P_ID_CLAZZ, ReflectionUtil.getNmsClass("PacketPlayOutPlayerInfo"), GameProfile.class, int.class, ReflectionUtil.getNmsClass("EnumGamemode"),
			ReflectionUtil.getNmsClass("IChatBaseComponent")
	);
	
	private final int ping;
	private final Object gameMode;
	private final GameProfile profile;
	private final String name;
	private final Object playerinfoaction;
	
	public PlayerInfoDataWrapper(GameProfile gp, int ping, Object gm, String name, Object playerinfoaction) {
		this.profile = gp;
		this.ping = ping;
		this.gameMode = gm;
		this.name = name;
		this.playerinfoaction = playerinfoaction;
	}
	
	public static PlayerInfoDataWrapper getPlayerInfo(Object nmsPlayerInfoData) {
		int ping = (int) ReflectionUtil.getDeclaredField("b", P_ID_CLAZZ).get(nmsPlayerInfoData);
		Object gamemode = ReflectionUtil.getDeclaredField("c", P_ID_CLAZZ).get(nmsPlayerInfoData);
		GameProfile profile = (GameProfile) ReflectionUtil.getDeclaredField("d", P_ID_CLAZZ).get(nmsPlayerInfoData);
		Object name = ReflectionUtil.getDeclaredField("e", P_ID_CLAZZ).get(nmsPlayerInfoData);
		Object infoAction = ReflectionUtil.getDeclaredField("a", P_ID_CLAZZ).get(nmsPlayerInfoData);
		
		return new PlayerInfoDataWrapper(profile, ping, gamemode, ReflectionUtil.fromIChatBaseComponent(name)[0], infoAction);
	}
	
	public static boolean isUnknown(Object playerInfoData) {
		GameProfile profile = (GameProfile) ReflectionUtil.getDeclaredField("d", P_ID_CLAZZ).get(playerInfoData);
		
		return playerInfoData == null || profile == null;
	}
	
	public Object getPlayerInfoData() {
		return P_ID_CONSTUCTOR.newInstance(true, getPlayerinfoaction(), getProfile(), getPing(), getGameMode(), ReflectionUtil.toIChatBaseComponentArray(getName()));
	}
	
	public Object getPlayerinfoaction() {
		return playerinfoaction;
	}
	
	public GameProfile getProfile() {
		return profile;
	}
	
	public int getPing() {
		return ping;
	}
	
	public Object getGameMode() {
		return gameMode;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(getPing(), getGameMode(), getProfile(), getName(), getPlayerinfoaction());
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		PlayerInfoDataWrapper that = (PlayerInfoDataWrapper) o;
		return getPing() == that.getPing() &&
				Objects.equal(getGameMode(), that.getGameMode()) &&
				Objects.equal(getProfile(), that.getProfile()) &&
				Objects.equal(getName(), that.getName()) &&
				Objects.equal(getPlayerinfoaction(), that.getPlayerinfoaction());
	}
	
	@Override
	public String toString() {
		return "PlayerInfoDataWrapper{" +
				"ping=" + ping +
				", gameMode=" + gameMode +
				", profile=" + profile +
				", name='" + name + '\'' +
				", playerinfoaction=" + playerinfoaction +
				'}';
	}
}
