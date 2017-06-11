package de.alphahelix.alphalibary.command.arguments;

import org.bukkit.Material;

public class MaterialArgument implements IArgument<Material> {
    @Override
    public boolean isCorrect(String arg) {
        try {
            Material.valueOf(arg);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public Material get(String arg) {
        if (isCorrect(arg))
            return Material.getMaterial(arg);
        return Material.AIR;
    }
}
