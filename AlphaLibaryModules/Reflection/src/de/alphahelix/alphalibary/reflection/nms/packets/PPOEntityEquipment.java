package de.alphahelix.alphalibary.reflection.nms.packets;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import de.alphahelix.alphalibary.reflection.nms.enums.REnumEquipSlot;
import org.bukkit.inventory.ItemStack;


public class PPOEntityEquipment implements IPacket {
	
	private static final ReflectionUtil.SaveConstructor PACKET =
			ReflectionUtil.getDeclaredConstructor("PacketPlayOutEntityEquipment",
					int.class, ReflectionUtil.getNmsClass("EnumItemSlot"), ReflectionUtil.getNmsClass("ItemStack"));
	
	private int entityID;
	private ItemStack itemStack;
	private REnumEquipSlot equipSlot;
	
	public PPOEntityEquipment(int entityID, ItemStack itemStack, REnumEquipSlot equipSlot) {
		this.entityID = entityID;
		this.itemStack = itemStack;
		this.equipSlot = equipSlot;
	}
	
	@Override
	public Object getPacket(boolean stackTrace) {
		return PACKET.newInstance(stackTrace, entityID, ReflectionUtil.getNMSItemStack(itemStack), equipSlot.getNmsSlot());
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(getEntityID(), getItemStack(), getEquipSlot());
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		PPOEntityEquipment that = (PPOEntityEquipment) o;
		return getEntityID() == that.getEntityID() &&
				Objects.equal(getItemStack(), that.getItemStack()) &&
				getEquipSlot() == that.getEquipSlot();
	}
	
	@Override
	public String toString() {
		return "PPOEntityEquipment{" +
				"entityID=" + entityID +
				", itemStack=" + itemStack +
				", equipSlot=" + equipSlot +
				'}';
	}
	
	public int getEntityID() {
		return entityID;
	}
	
	public PPOEntityEquipment setEntityID(int entityID) {
		this.entityID = entityID;
		return this;
	}
	
	public ItemStack getItemStack() {
		return itemStack;
	}
	
	public PPOEntityEquipment setItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;
		return this;
	}
	
	public REnumEquipSlot getEquipSlot() {
		return equipSlot;
	}
	
	public PPOEntityEquipment setEquipSlot(REnumEquipSlot equipSlot) {
		this.equipSlot = equipSlot;
		return this;
	}
}
