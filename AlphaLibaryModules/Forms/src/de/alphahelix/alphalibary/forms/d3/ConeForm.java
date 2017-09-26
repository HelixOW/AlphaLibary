package de.alphahelix.alphalibary.forms.d3;

import de.alphahelix.alphalibary.forms.Form;
import de.alphahelix.alphalibary.forms.FormAction;
import de.alphahelix.alphalibary.forms.d2.CircleForm;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

@SuppressWarnings("ALL")
public class ConeForm extends Form {

    private final double baseRadius;
    private double size;
    private boolean filled;
    private BlockFace direction;

    public ConeForm(Location location, double dense, double baseRadius, double size, boolean filled, BlockFace direction, FormAction action) {
        super(location, "", dense, action);
        this.baseRadius = baseRadius;
        this.size = size;
        this.filled = filled;
        this.direction = direction;
    }

    public double getBaseRadius() {
        return baseRadius;
    }

    public double getSize() {
        return size;
    }

    public boolean isFilled() {
        return filled;
    }

    public ConeForm setFilled(boolean filled) {
        this.filled = filled;
        return this;
    }

    public BlockFace getDirection() {
        return direction;
    }

    public ConeForm setDirection(BlockFace direction) {
        this.direction = direction;
        return this;
    }

    @Override
    public void send(Player p) {
        if (direction == BlockFace.UP) {
            for (double r = baseRadius; r > 0 && size != 0; r -= getDense(), size -= getDense())
                new CircleForm(getLocation().clone().subtract(0, r, 0), "y", getDense(), r, getAction()).send(p);
        } else if (direction == BlockFace.EAST) {
            for (double r = baseRadius; r > 0 && size != 0; r -= getDense(), size -= getDense())
                new CircleForm(getLocation().clone().subtract(r, 0, 0), "z", getDense(), r, getAction()).send(p);
        } else if (direction == BlockFace.WEST) {
            for (double r = baseRadius; r > 0 && size != 0; r -= getDense(), size -= getDense())
                new CircleForm(getLocation().clone().add(r, 0, 0), "z", getDense(), r, getAction()).send(p);
        } else if (direction == BlockFace.SOUTH) {
            for (double r = baseRadius; r > 0 && size != 0; r -= getDense(), size -= getDense())
                new CircleForm(getLocation().clone().subtract(0, 0, r), "x", getDense(), r, getAction()).send(p);
        } else if (direction == BlockFace.NORTH) {
            for (double r = baseRadius; r > 0 && size != 0; r -= getDense(), size -= getDense())
                new CircleForm(getLocation().clone().add(0, 0, r), "x", getDense(), r, getAction()).send(p);
        } else {
            for (double r = baseRadius; r > 0 && size != 0; r -= getDense(), size -= getDense())
                new CircleForm(getLocation().clone().add(0, r, 0), "y", getDense(), r, getAction()).send(p);
        }
    }
}
