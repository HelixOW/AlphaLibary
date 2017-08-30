package de.alphahelix.alphalibary.nbt;

import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import org.bukkit.Bukkit;

import java.util.logging.Level;

public class NBTList {

    private String name;
    private NBTCompound parent;
    private NBTType type;
    private Object listObj;

    public NBTList(String name, NBTCompound parent, NBTType type, Object listObj) {
        this.name = name;
        this.parent = parent;
        this.type = type;
        this.listObj = listObj;

        if (!(type == NBTType.STRING || type == NBTType.COMPOUND))
            Bukkit.getLogger().log(Level.SEVERE, type.name().toLowerCase() + " lists are not implemented yet!");
    }

    public void save() {
        parent.set(name, listObj);
    }

    public NBTListCompound addCompound() {
        if (type != NBTType.COMPOUND) {
            new Throwable("Cannot use Compound method on a non Compound list!").printStackTrace();
            return null;
        }

        Object comp = ReflectionUtil.getNewNBTTag();

        ReflectionUtil.getDeclaredMethod("add", listObj.getClass(), ReflectionUtil.getNmsClass("NBTBase")).invoke(listObj, true, comp);
        return new NBTListCompound(this, comp);
    }

    public NBTListCompound getCompound(int id) {
        if (type != NBTType.COMPOUND) {
            new Throwable("Cannot use Compound method on a non Compound list!").printStackTrace();
            return null;
        }

        Object comp = ReflectionUtil.getDeclaredMethod("get", listObj.getClass(), int.class).invoke(listObj, true, id);
        return new NBTListCompound(this, comp);
    }

    public String getString(int id) {
        if (type != NBTType.COMPOUND) {
            new Throwable("Cannot use String method on a non String list!").printStackTrace();
            return null;
        }

        return (String) ReflectionUtil.getDeclaredMethod("getString", listObj.getClass(), int.class).invoke(listObj, true, id);
    }

    public void addString(String s) {
        if (type != NBTType.STRING) {
            new Throwable("Cannot use String method on a non String list!").printStackTrace();
            return;
        }

        ReflectionUtil.getDeclaredMethod("add", listObj.getClass(), ReflectionUtil.getNmsClass("NBTBase"))
                .invoke(listObj, true,
                        ReflectionUtil.getDeclaredConstructor(ReflectionUtil.getNmsClass("NBTTagString"), String.class).newInstance(true, s));
        save();
    }

    public void setString(int i, String s) {
        if (type != NBTType.STRING) {
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

    public NBTType getType() {
        return type;
    }
}
