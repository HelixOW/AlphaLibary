package de.alphahelix.alphalibary.airdrops;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.utils.LocationUtil;
import org.bukkit.Location;

import java.io.Serializable;

public class DropOffLocation implements Serializable {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DropOffLocation that = (DropOffLocation) o;
        return radius == that.radius &&
                Objects.equal(center, that.center);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(center, radius);
    }

    @Override
    public String toString() {
        return "DropOffLocation{" +
                "center=" + center +
                ", radius=" + radius +
                '}';
    }
}
