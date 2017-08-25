package de.alphahelix.alphalibary;

import de.alphahelix.alphalibary.airdrops.AirDrop;
import de.alphahelix.alphalibary.annotations.Annotations;
import de.alphahelix.alphalibary.listener.SimpleLoader;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class SimplePack implements SimpleLoader {

    public SimplePack() {
        super();
        Annotations.ITEM.registerItems(this);
        Annotations.COMMAND.registerCommands(this);

        new AirDrop(new Location(Bukkit.getWorld("world"), 100, 100, 100), 5);
    }
}
