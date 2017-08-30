package de.alphahelix.alphalibary.nbt;

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
}
