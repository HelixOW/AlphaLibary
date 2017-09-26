package de.alphahelix.alphalibary.forms.d3;

import de.alphahelix.alphalibary.forms.Form;
import de.alphahelix.alphalibary.forms.FormAction;
import de.alphahelix.alphalibary.forms.d2.RectangleForm;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

@SuppressWarnings("ALL")
public class PyramidForm extends Form {

    private final double basis;
    private double size;
    private boolean filled;
    private BlockFace direction;

    public PyramidForm(Location location, String axis, double dense, double basis, double size, boolean filled, BlockFace direction, FormAction action) {
        super(location, axis, dense, action);
        this.basis = basis;
        this.size = size;
        this.filled = filled;
        this.direction = direction;
    }

    public double getBasis() {
        return basis;
    }

    public double getSize() {
        return size;
    }

    public PyramidForm setSize(double size) {
        this.size = size;
        return this;
    }

    public boolean isFilled() {
        return filled;
    }

    public PyramidForm setFilled(boolean filled) {
        this.filled = filled;
        return this;
    }

    public BlockFace getDirection() {
        return direction;
    }

    public PyramidForm setDirection(BlockFace direction) {
        this.direction = direction;
        return this;
    }

    @Override
    public void send(Player p) {
        if (direction == BlockFace.UP) {
            for (double r = basis; r > 0 && size != 0; r -= getDense(), size -= getDense())
                new RectangleForm(getLocation().clone().subtract(0, r, 0), "y", getDense(), r, r, isFilled(), getAction()).send(p);
        } else if (direction == BlockFace.EAST) {
            for (double r = basis; r > 0 && size != 0; r -= getDense(), size -= getDense())
                new RectangleForm(getLocation().clone().subtract(r, 0, 0), "z", getDense(), r, r, isFilled(), getAction()).send(p);
        } else if (direction == BlockFace.WEST) {
            for (double r = basis; r > 0 && size != 0; r -= getDense(), size -= getDense())
                new RectangleForm(getLocation().clone().add(r, 0, 0), "z", getDense(), r, r, isFilled(), getAction()).send(p);
        } else if (direction == BlockFace.SOUTH) {
            for (double r = basis; r > 0 && size != 0; r -= getDense(), size -= getDense())
                new RectangleForm(getLocation().clone().subtract(0, 0, r), "x", getDense(), r, r, isFilled(), getAction()).send(p);
        } else if (direction == BlockFace.NORTH) {
            for (double r = basis; r > 0 && size != 0; r -= getDense(), size -= getDense())
                new RectangleForm(getLocation().clone().add(0, 0, r), "x", getDense(), r, r, isFilled(), getAction()).send(p);
        } else {
            for (double r = basis; r > 0 && size != 0; r -= getDense(), size -= getDense())
                new RectangleForm(getLocation().clone().add(0, r, 0), "y", getDense(), r, r, isFilled(), getAction()).send(p);
        }
    }
}
