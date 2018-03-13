package de.alphahelix.alphalibary.reflection.nms.nbt;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import org.bukkit.Bukkit;

import java.util.logging.Level;


public class NBTList {
	
	private final String name;
	private final NBTCompound parent;
	private final NBTType type;
	private final Object listObj;
	
	public NBTList(String name, NBTCompound parent, NBTType type, Object listObj) {
		this.name = name;
		this.parent = parent;
		this.type = type;
		this.listObj = listObj;
		
		if(!(type == NBTType.STRING || type == NBTType.COMPOUND))
			Bukkit.getLogger().log(Level.SEVERE, type.name().toLowerCase() + " lists are not implemented yet!");
	}
	
	public NBTListCompound addCompound() {
		if(type != NBTType.COMPOUND) {
			new Throwable("Cannot use Compound method on a non Compound list!").printStackTrace();
			return null;
		}
		
		Object comp = ReflectionUtil.getNewNBTTag();
		
		ReflectionUtil.getDeclaredMethod("add", listObj.getClass(), ReflectionUtil.getNmsClass("NBTBase")).invoke(listObj, true, comp);
		return new NBTListCompound(this, comp);
	}
	
	public NBTListCompound getCompound(int id) {
		if(type != NBTType.COMPOUND) {
			new Throwable("Cannot use Compound method on a non Compound list!").printStackTrace();
			return null;
		}
		
		Object comp = ReflectionUtil.getDeclaredMethod("get", listObj.getClass(), int.class).invoke(listObj, true, id);
		return new NBTListCompound(this, comp);
	}
	
	public String getString(int id) {
		if(type != NBTType.COMPOUND) {
			new Throwable("Cannot use String method on a non String list!").printStackTrace();
			return null;
		}
		
		return (String) ReflectionUtil.getDeclaredMethod("getString", listObj.getClass(), int.class).invoke(listObj, true, id);
	}
	
	public void addString(String s) {
		if(type != NBTType.STRING) {
			new Throwable("Cannot use String method on a non String list!").printStackTrace();
			return;
		}
		
		ReflectionUtil.getDeclaredMethod("add", listObj.getClass(), ReflectionUtil.getNmsClass("NBTBase"))
				.invoke(listObj, true,
						ReflectionUtil.getDeclaredConstructor(ReflectionUtil.getNmsClass("NBTTagString"), String.class).newInstance(true, s));
		save();
	}
	
	public void save() {
		parent.set(name, listObj);
	}
	
	public void setString(int i, String s) {
		if(type != NBTType.STRING) {
			new Throwable("Cannot use String method on a non String list!").printStackTrace();
			return;
		}
		
		ReflectionUtil.getDeclaredMethod("a", listObj.getClass(), int.class, ReflectionUtil.getNmsClass("NBTBase"))
				.invoke(listObj, true,
						i, ReflectionUtil.getDeclaredConstructor(ReflectionUtil.getNmsClass("NBTTagString"), String.class).newInstance(true, s));
		save();
	}
	
	public void remove(int i) {
		ReflectionUtil.getDeclaredMethod("remove", listObj.getClass(), int.class).invoke(listObj, true, i);
		save();
	}
	
	public int size() {
		return (int) ReflectionUtil.getDeclaredMethod("size", listObj.getClass()).invoke(listObj, true);
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(name, parent, getType(), listObj);
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		NBTList nbtList = (NBTList) o;
		return Objects.equal(name, nbtList.name) &&
				Objects.equal(parent, nbtList.parent) &&
				getType() == nbtList.getType() &&
				Objects.equal(listObj, nbtList.listObj);
	}
	
	@Override
	public String toString() {
		return "NBTList{" +
				"name='" + name + '\'' +
				", parent=" + parent +
				", type=" + type +
				", listObj=" + listObj +
				'}';
	}
	
	public NBTType getType() {
		return type;
	}
}
