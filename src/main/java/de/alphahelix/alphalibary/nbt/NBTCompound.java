package de.alphahelix.alphalibary.nbt;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class NBTCompound {

    private String name;
    private NBTCompound parent;

    public NBTCompound(String name, NBTCompound parent) {
        this.name = name;
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public NBTCompound getParent() {
        return parent;
    }

    public Object getCoumpound() {
        return getParent().getCoumpound();
    }

    public void setCompound(Object comp) {
        parent.setCompound(comp);
    }

    public void setItem(ItemStack item) {
        parent.setItem(item);
    }

    public void set(String key, Object val) {
        ReflectionUtil.set(this, key, val);
    }

    public void setString(String key, String value) {
        ReflectionUtil.setString(this, key, value);
    }

    public String getString(String key) {
        return ReflectionUtil.getString(this, key);
    }

    public Object getContent(String key) {
        return ReflectionUtil.getContent(this, key);
    }

    public void setInteger(String key, Integer value) {
        ReflectionUtil.setInt(this, key, value);
    }

    public Integer getInteger(String key) {
        return ReflectionUtil.getInt(this, key);
    }

    public void setDouble(String key, Double value) {
        ReflectionUtil.setDouble(this, key, value);
    }

    public Double getDouble(String key) {
        return ReflectionUtil.getDouble(this, key);
    }

    public void setByte(String key, Byte value) {
        ReflectionUtil.setByte(this, key, value);
    }

    public Byte getByte(String key) {
        return ReflectionUtil.getByte(this, key);
    }

    public void setShort(String key, Short value) {
        ReflectionUtil.setShort(this, key, value);
    }

    public Short getShort(String key) {
        return ReflectionUtil.getShort(this, key);
    }

    public void setLong(String key, Long value) {
        ReflectionUtil.setLong(this, key, value);
    }

    public Long getLong(String key) {
        return ReflectionUtil.getLong(this, key);
    }

    public void setFloat(String key, Float value) {
        ReflectionUtil.setFloat(this, key, value);
    }

    public Float getFloat(String key) {
        return ReflectionUtil.getFloat(this, key);
    }

    public void setByteArray(String key, byte[] value) {
        ReflectionUtil.setByteArray(this, key, value);
    }

    public byte[] getByteArray(String key) {
        return ReflectionUtil.getByteArray(this, key);
    }

    public void setIntArray(String key, int[] value) {
        ReflectionUtil.setIntArray(this, key, value);
    }

    public int[] getIntArray(String key) {
        return ReflectionUtil.getIntArray(this, key);
    }

    public void setBoolean(String key, Boolean value) {
        ReflectionUtil.setBoolean(this, key, value);
    }

    public Boolean getBoolean(String key) {
        return ReflectionUtil.getBoolean(this, key);
    }

    public void setObject(String key, Object value) {
        ReflectionUtil.setObject(this, key, value);
    }

    public <T> T getObject(String key, Class<T> type) {
        return ReflectionUtil.getObject(this, key, type);
    }

    public Boolean hasKey(String key) {
        return ReflectionUtil.hasKey(this, key);
    }

    public void removeKey(String key) {
        ReflectionUtil.remove(this, key);
    }

    public Set<String> getKeys() {
        return ReflectionUtil.getKeys(this);
    }

    public NBTCompound addCompound(String name) {
        ReflectionUtil.addNBTTagCompound(this, name);
        return getCompound(name);
    }

    public NBTCompound getCompound(String name) {
        NBTCompound next = new NBTCompound(name, this);
        if (ReflectionUtil.validCompound(next)) return next;
        return null;
    }

    public NBTList getList(String name, NBTType type) {
        return ReflectionUtil.getList(this, name, type);
    }

    public NBTType getType(String name) {
        return NBTType.valueOf(ReflectionUtil.getType(this, name));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NBTCompound that = (NBTCompound) o;
        return Objects.equal(getName(), that.getName()) &&
                Objects.equal(getParent(), that.getParent());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName(), getParent());
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("NBTCompound{");
        for (String k : getKeys()) {
            str.append(toString(k));
        }
        return str.append("}").toString();
    }

    public String toString(String key) {
        StringBuilder s = new StringBuilder();
        NBTCompound c = this;
        while (c.getParent() != null) {
            s.append("   ");
            c = c.getParent();
        }
        if (this.getType(key) == NBTType.COMPOUND)
            return this.getCompound(key).toString();
        else
            return s + "-" + key + ": " + getContent(key) + System.lineSeparator();
    }
}
