package io.github.alphahelixdev.alpary.reflection.nms.packets;

import io.github.alphahelixdev.alpary.reflection.nms.BlockPos;
import io.github.alphahelixdev.alpary.utils.NMSUtil;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.alphahelixdev.helius.reflection.SaveConstructor;

import java.util.Objects;

public class SpawnEntityPacket implements IPacket {
	
	private static final SaveConstructor PACKET_OPT_1 = NMSUtil.getReflections().getDeclaredConstructor(
			Utils.nms().getNMSClass("PacketPlayOutSpawnEntity"), Utils.nms().getNMSClass("Entity"),
			int.class, int.class);
	private static final SaveConstructor PACKET_OPT_2 = NMSUtil.getReflections().getDeclaredConstructor(
			Utils.nms().getNMSClass("PacketPlayOutSpawnEntity"), Utils.nms().getNMSClass("Entity"),
			int.class, int.class, Utils.nms().getNMSClass("BlockPosition"));
	
	private Object entity;
	private int type;
	private int metaData;
	private BlockPos pos;
	
	public SpawnEntityPacket(Object entity, int type, int metaData) {
		this.entity = entity;
		this.type = type;
		this.metaData = metaData;
	}
	
	public SpawnEntityPacket(Object entity, int type, int metaData, BlockPos pos) {
		this.entity = entity;
		this.type = type;
		this.metaData = metaData;
		this.pos = pos;
	}
	
	@Override
	public Object getPacket(boolean stackTrace) {
		if(this.getPos() == null)
			return SpawnEntityPacket.getPacketOpt1().newInstance(stackTrace, this.getEntity(), this.getType(),
					this.getMetaData());
		return SpawnEntityPacket.getPacketOpt2().newInstance(stackTrace, this.getEntity(), this.getType(),
				this.getMetaData(), Utils.nms().toBlockPosition(this.getPos()));
	}
	
	public BlockPos getPos() {
		return this.pos;
	}
	
	private static SaveConstructor getPacketOpt1() {
		return SpawnEntityPacket.PACKET_OPT_1;
	}
	
	public Object getEntity() {
		return this.entity;
	}
	
	public SpawnEntityPacket setEntity(Object entity) {
		this.entity = entity;
		return this;
	}
	
	public int getType() {
		return this.type;
	}
	
	public SpawnEntityPacket setType(int type) {
		this.type = type;
		return this;
	}
	
	public int getMetaData() {
		return this.metaData;
	}
	
	private static SaveConstructor getPacketOpt2() {
		return SpawnEntityPacket.PACKET_OPT_2;
	}
	
	public SpawnEntityPacket setMetaData(int metaData) {
		this.metaData = metaData;
		return this;
	}
	
	public SpawnEntityPacket setPos(BlockPos pos) {
		this.pos = pos;
		return this;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.getEntity(), this.getType(), this.getMetaData(), this.getPos());
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		SpawnEntityPacket that = (SpawnEntityPacket) o;
		return this.getType() == that.getType() &&
				this.getMetaData() == that.getMetaData() &&
				Objects.equals(this.getEntity(), that.getEntity()) &&
				Objects.equals(this.getPos(), that.getPos());
	}
	
	@Override
	public String toString() {
		return "SpawnEntityPacket{" +
				"                            entity=" + this.entity +
				",                             type=" + this.type +
				",                             metaData=" + this.metaData +
				",                             pos=" + this.pos +
				'}';
	}
}
