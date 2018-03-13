package de.alphahelix.alphalibary.reflection.nms.nbt;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import org.bukkit.entity.Entity;

public class NBTEntity extends NBTCompound {
	
	private final Entity entity;
	
	public NBTEntity(Entity entity) {
		super(null, null);
		this.entity = entity;
	}
	
	@Override
	public Object getCoumpound() {
		return ReflectionUtil.getEntityNBTTagCompound(ReflectionUtil.getCraftbukkitEntity(entity));
	}
	
	@Override
	public void setCompound(Object comp) {
		ReflectionUtil.setEntityNBTTag(comp, ReflectionUtil.getCraftbukkitEntity(entity));
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(super.hashCode(), entity);
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		if(!super.equals(o)) return false;
		NBTEntity nbtEntity = (NBTEntity) o;
		return Objects.equal(entity, nbtEntity.entity);
	}
	
	@Override
	public String toString() {
		return "NBTEntity{" +
				"entity=" + entity +
				'}';
	}
}
