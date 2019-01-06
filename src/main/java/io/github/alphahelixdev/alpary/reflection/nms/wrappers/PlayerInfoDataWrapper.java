package io.github.alphahelixdev.alpary.reflection.nms.wrappers;

import com.mojang.authlib.GameProfile;
import io.github.alphahelixdev.alpary.utils.NMSUtil;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.alphahelixdev.helius.reflection.SaveConstructor;

import java.util.Objects;

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

    public PlayerInfoDataWrapper(GameProfile gp, int ping, Object gm, String name, Object playerinfoaction) {
        this.profile = gp;
        this.ping = ping;
        this.gameMode = gm;
        this.name = name;
        this.playerinfoaction = playerinfoaction;
    }

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

        return new PlayerInfoDataWrapper(profile, ping, gamemode, Utils.nms().fromIChatBaseComponent(name)[0],
                infoAction);
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

    public int getPing() {
        return this.ping;
    }

    public Object getGameMode() {
        return this.gameMode;
    }

    public GameProfile getProfile() {
        return this.profile;
    }

    public String getName() {
        return this.name;
    }

    public Object getPlayerinfoaction() {
        return this.playerinfoaction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerInfoDataWrapper that = (PlayerInfoDataWrapper) o;
        return this.getPing() == that.getPing() &&
                Objects.equals(this.getGameMode(), that.getGameMode()) &&
                Objects.equals(this.getProfile(), that.getProfile()) &&
                Objects.equals(this.getName(), that.getName()) &&
                Objects.equals(this.getPlayerinfoaction(), that.getPlayerinfoaction());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getPing(), this.getGameMode(), this.getProfile(), this.getName(), this.getPlayerinfoaction());
    }

    @Override
    public String toString() {
        return "PlayerInfoDataWrapper{" +
                "                            ping=" + this.ping +
                ",                             gameMode=" + this.gameMode +
                ",                             profile=" + this.profile +
                ",                             name='" + this.name + '\'' +
                ",                             playerinfoaction=" + this.playerinfoaction +
                '}';
    }
}
