package de.alphahelix.alphalibary.airdrops;

import de.alphahelix.alphalibary.utils.LocationUtil;
import org.bukkit.Location;

public class DropOffLocation {

    private Location center;
    private int radius;

    public DropOffLocation(Location center, int radius) {
        this.center = center;
        this.radius = radius;
    }

    public Location getDropOff() {
        int minX = (int) (center.getX() - radius);
        int minZ = (int) (center.getZ() - radius);
        int maxX = (int) (center.getX() + radius);
        int maxZ = (int) (center.getZ() + radius);

        return LocationUtil.getRandomLocation(center, minX, maxX, minZ, maxZ);
    }
}
