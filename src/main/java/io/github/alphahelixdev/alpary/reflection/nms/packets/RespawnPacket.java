package io.github.alphahelixdev.alpary.reflection.nms.packets;

import io.github.alphahelixdev.alpary.reflection.nms.enums.REnumDifficulty;
import io.github.alphahelixdev.alpary.reflection.nms.enums.REnumGamemode;
import io.github.alphahelixdev.alpary.utils.NMSUtil;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.alphahelixdev.helius.reflection.SaveConstructor;
import lombok.*;
import org.bukkit.WorldType;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class RespawnPacket implements IPacket {
	
	private static final SaveConstructor PACKET =
			NMSUtil.getReflections().getDeclaredConstructor(Utils.nms().getNMSClass("PacketPlayOutRespawn"),
					int.class, Utils.nms().getNMSClass("EnumDifficulty"),
					Utils.nms().getNMSClass("WorldType"), Utils.nms().getNMSClass("EnumGamemode"));
	
	private final REnumDifficulty difficulty;
	private final WorldType worldType;
	private final REnumGamemode gamemode;
	
	@Override
	public Object getPacket(boolean stackTrace) {
		return RespawnPacket.getPacket().newInstance(true, 0, this.getDifficulty().getEnumDifficulty(),
				NMSUtil.getReflections().getDeclaredField(this.getWorldType().getName(),
						Utils.nms().getNMSClass("WorldType")).get(null), this.getGamemode().getEnumGamemode());
	}
	
	private static SaveConstructor getPacket() {
		return RespawnPacket.PACKET;
	}
}
