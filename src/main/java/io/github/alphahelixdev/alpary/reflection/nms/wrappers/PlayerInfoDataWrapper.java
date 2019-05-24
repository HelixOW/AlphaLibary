package io.github.alphahelixdev.alpary.reflection.nms.wrappers;

import com.mojang.authlib.GameProfile;
import io.github.alphahelixdev.alpary.utils.NMSUtil;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.alphahelixdev.helius.reflection.SaveConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class PlayerInfoDataWrapper {

    private static final Class<?> P_ID_CLAZZ = Utils.nms().getNMSClass("PacketPlayOutPlayerInfo$PlayerInfoData");

    private static final SaveConstructor P_ID_CONSTUCTOR = NMSUtil.getReflections().getDeclaredConstructor(P_ID_CLAZZ,
            Utils.nms().getNMSClass("PacketPlayOutPlayerInfo"), GameProfile.class, int.class,
            Utils.nms().getNMSClass("EnumGamemode"), Utils.nms().getNMSClass("IChatBaseComponent"));

    private final int ping;
    private final Object gameMode;
    private final GameProfile profile;
    private final String name;
    private final Object playerinfoaction;

    public static PlayerInfoDataWrapper getPlayerInfo(Object nmsPlayerInfoData) {
        int ping = (int) NMSUtil.getReflections().getDeclaredField("b", PlayerInfoDataWrapper.getpIdClazz())
                .get(nmsPlayerInfoData);
        Object gamemode = NMSUtil.getReflections().getDeclaredField("c", PlayerInfoDataWrapper.getpIdClazz())
                .get(nmsPlayerInfoData);
        GameProfile profile = (GameProfile) NMSUtil.getReflections().getDeclaredField("d",
                PlayerInfoDataWrapper.getpIdClazz()).get(nmsPlayerInfoData);
        Object name = NMSUtil.getReflections().getDeclaredField("e", PlayerInfoDataWrapper.getpIdClazz())
                .get(nmsPlayerInfoData);
        Object infoAction = NMSUtil.getReflections().getDeclaredField("a", PlayerInfoDataWrapper.getpIdClazz())
                .get(nmsPlayerInfoData);
	
	    return new PlayerInfoDataWrapper(
			    ping, gamemode, profile, Utils.nms().fromIChatBaseComponent(name)[0], infoAction);
    }

    public static boolean isUnknown(Object playerInfoData) {
        GameProfile profile = (GameProfile) NMSUtil.getReflections().getDeclaredField("d",
                PlayerInfoDataWrapper.getpIdClazz()).get(playerInfoData);

        return playerInfoData == null || profile == null;
    }

    public static Class<?> getpIdClazz() {
        return PlayerInfoDataWrapper.P_ID_CLAZZ;
    }

    public static SaveConstructor getpIdConstuctor() {
        return PlayerInfoDataWrapper.P_ID_CONSTUCTOR;
    }

    public Object getPlayerInfoData() {
        return PlayerInfoDataWrapper.getpIdConstuctor().newInstance(true, this.getPlayerinfoaction(),
                this.getProfile(), this.getPing(), this.getGameMode(),
                Utils.nms().toIChatBaseComponentArray(this.getName()));
    }
}
