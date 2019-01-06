package io.github.alphahelixdev.alpary.reflection.nms.packets;

import io.github.alphahelixdev.alpary.reflection.nms.enums.REnumDifficulty;
import io.github.alphahelixdev.alpary.reflection.nms.enums.REnumGamemode;
import io.github.alphahelixdev.alpary.utils.NMSUtil;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.alphahelixdev.helius.reflection.SaveConstructor;
import org.bukkit.WorldType;

import java.util.Objects;

public class RespawnPacket implements IPacket {

    private static final SaveConstructor PACKET =
            NMSUtil.getReflections().getDeclaredConstructor(Utils.nms().getNMSClass("PacketPlayOutRespawn"),
                    int.class, Utils.nms().getNMSClass("EnumDifficulty"),
                    Utils.nms().getNMSClass("WorldType"), Utils.nms().getNMSClass("EnumGamemode"));

    private final REnumDifficulty difficulty;
    private final WorldType worldType;
    private final REnumGamemode gamemode;

    public RespawnPacket(REnumDifficulty difficulty, WorldType worldType, REnumGamemode gamemode) {
        this.difficulty = difficulty;
        this.worldType = worldType;
        this.gamemode = gamemode;
    }

    private static SaveConstructor getPacket() {
        return RespawnPacket.PACKET;
    }

    @Override
    public Object getPacket(boolean stackTrace) {
        return RespawnPacket.getPacket().newInstance(true, 0, this.getDifficulty().getEnumDifficulty(),
                NMSUtil.getReflections().getDeclaredField(this.getWorldType().getName(),
                        Utils.nms().getNMSClass("WorldType")).get(null), this.getGamemode().getEnumGamemode());
    }

    public REnumDifficulty getDifficulty() {
        return this.difficulty;
    }

    public WorldType getWorldType() {
        return this.worldType;
    }

    public REnumGamemode getGamemode() {
        return this.gamemode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RespawnPacket that = (RespawnPacket) o;
        return this.getDifficulty() == that.getDifficulty() &&
                this.getWorldType() == that.getWorldType() &&
                this.getGamemode() == that.getGamemode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getDifficulty(), this.getWorldType(), this.getGamemode());
    }

    @Override
    public String toString() {
        return "RespawnPacket{" +
                "                            difficulty=" + this.difficulty +
                ",                             worldType=" + this.worldType +
                ",                             gamemode=" + this.gamemode +
                '}';
    }
}
