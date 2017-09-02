package de.alphahelix.alphalibary;

import de.alphahelix.alphalibary.airdrops.AirDrop;
import de.alphahelix.alphalibary.annotations.Annotations;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Listener;

public class SimplePack implements Listener {

    public SimplePack() {
        super();
        Annotations.ITEM.registerItems(this);
        Annotations.COMMAND.registerCommands(this);

        new AirDrop(new Location(Bukkit.getWorld("world"), 100, 100, 100), 5);
    }
}
