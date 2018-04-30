package de.alphahelix.alphalibary.reflection.nms.packets;

import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import de.alphahelix.alphalibary.reflection.nms.enums.REnumDifficulty;
import de.alphahelix.alphalibary.reflection.nms.enums.REnumGamemode;
import org.bukkit.WorldType;

import java.util.Objects;

public class PPORespawn implements IPacket {
	
	private static final ReflectionUtil.SaveConstructor PACKET =
			ReflectionUtil.getDeclaredConstructor("PacketPlayOutRespawn",
					int.class,
					ReflectionUtil.getNmsClass("EnumDifficulty"),
					ReflectionUtil.getNmsClass("WorldType"),
					ReflectionUtil.getNmsClass("EnumGamemode"));
	
	private final REnumDifficulty difficulty;
	private final WorldType worldType;
	private final REnumGamemode gamemode;
	
	public PPORespawn(REnumDifficulty difficulty, WorldType worldType, REnumGamemode gamemode) {
		this.difficulty = difficulty;
		this.worldType = worldType;
		this.gamemode = gamemode;
	}
	
	@Override
	public Object getPacket(boolean stackTrace) {
		return PACKET.newInstance(true, 0, getDifficulty().getEnumDifficulty(), ReflectionUtil.getDeclaredField(getWorldType().getName(), "WorldType").get(null), getGamemode().getEnumGamemode());
	}
	
	public REnumDifficulty getDifficulty() {
		return difficulty;
	}
	
	public WorldType getWorldType() {
		return worldType;
	}
	
	public REnumGamemode getGamemode() {
		return gamemode;
	}
	
	@Override
	public int hashCode() {
		
		return Objects.hash(difficulty, worldType, gamemode);
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		PPORespawn that = (PPORespawn) o;
		return difficulty == that.difficulty &&
				worldType == that.worldType &&
				gamemode == that.gamemode;
	}
	
	@Override
	public String toString() {
		return "PPORespawn{" +
				"difficulty=" + difficulty +
				", worldType=" + worldType +
				", gamemode=" + gamemode +
				'}';
	}
}
