package de.alphahelix.alphalibary.annotations;

import de.alphahelix.alphalibary.annotations.command.CommandAnnotations;
import de.alphahelix.alphalibary.annotations.item.ItemAnnotations;
import org.bukkit.plugin.java.JavaPlugin;

public class Annotations {

    public static final CommandAnnotations COMMAND = new CommandAnnotations();
    public static final ItemAnnotations ITEM = new ItemAnnotations();

    public static final IAnnotation[] ANNOTATIONS = {COMMAND, ITEM};

    public static void loadAll(JavaPlugin jp, Object clazz) {
        for (IAnnotation annotation : ANNOTATIONS)
            annotation.load(clazz);
    }
}
