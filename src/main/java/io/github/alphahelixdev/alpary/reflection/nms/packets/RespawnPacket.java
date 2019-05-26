package io.github.alphahelixdev.alpary.reflection.nms.packets;

import io.github.alphahelixdev.alpary.reflection.nms.enums.RDifficulty;
import io.github.alphahelixdev.alpary.reflection.nms.enums.RGamemode;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.whoisalphahelix.helix.reflection.SaveConstructor;
import lombok.*;
import org.bukkit.WorldType;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class RespawnPacket implements IPacket {
	
	private static final SaveConstructor PACKET =
            Utils.nms().getDeclaredConstructor(Utils.nms().getNMSClass("PacketPlayOutRespawn"),
					int.class, Utils.nms().getNMSClass("EnumDifficulty"),
					Utils.nms().getNMSClass("WorldType"), Utils.nms().getNMSClass("EnumGamemode"));

    private final RDifficulty difficulty;
	private final WorldType worldType;
    private final RGamemode gamemode;
	
	@Override
	public Object getPacket(boolean stackTrace) {
		return RespawnPacket.getPacket().newInstance(true, 0, this.getDifficulty().getEnumDifficulty(),
                Utils.nms().getDeclaredField(this.getWorldType().getName(),
						Utils.nms().getNMSClass("WorldType")).get(null), this.getGamemode().getEnumGamemode());
	}
	
	private static SaveConstructor getPacket() {
		return RespawnPacket.PACKET;
	}
}
