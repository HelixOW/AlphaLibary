package de.alphahelix.alphalibary.forms.d3;

import de.alphahelix.alphalibary.forms.Form;
import de.alphahelix.alphalibary.forms.FormAction;
import de.alphahelix.alphalibary.forms.d2.CircleForm;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

@SuppressWarnings("ALL")
public class BarrelForm extends Form {

    private double depth, radius;
    private BlockFace direction;

    public BarrelForm(Location location, String axis, double dense, double depth, double radius, BlockFace direction, FormAction action) {
        super(location, axis, dense, action);
        this.depth = depth;
        this.radius = radius;
        this.direction = direction;
    }

    public double getDepth() {
        return depth;
    }

    public BarrelForm setDepth(double depth) {
        this.depth = depth;
        return this;
    }

    public double getRadius() {
        return radius;
    }

    public BarrelForm setRadius(double radius) {
        this.radius = radius;
        return this;
    }

    public BlockFace getDirection() {
        return direction;
    }

    public BarrelForm setDirection(BlockFace direction) {
        this.direction = direction;
        return this;
    }

    @Override
    public void send(Player p) {
        if (direction == BlockFace.UP || direction == BlockFace.DOWN) {
            new CircleForm(getLocation(), "y", getDense(), getRadius(), getAction()).send(p);
            new CircleForm(getLocation().clone().add(0, getDepth(), 0), "y", getDense(), getRadius(), getAction()).send(p);

            for (double d = getDense(); d < getDepth() - getDense(); d += getDense())
                new CircleForm(getLocation().clone().add(0, d, 0), "y", getDense(), getRadius(), getAction()).send(p);
        } else if (direction == BlockFace.EAST || direction == BlockFace.WEST) {
            new CircleForm(getLocation(), "z", getDense(), getRadius(), getAction()).send(p);
            new CircleForm(getLocation().clone().add(getDepth(), 0, 0), "z", getDense(), getRadius(), getAction()).send(p);

            for (double d = getDense(); d < getDepth() - getDense(); d += getDense())
                new CircleForm(getLocation().clone().add(d, 0, 0), "z", getDense(), getRadius(), getAction()).send(p);
        } else {
            new CircleForm(getLocation(), "x", getDense(), getRadius(), getAction()).send(p);
            new CircleForm(getLocation().clone().add(0, 0, getDepth()), "x", getDense(), getRadius(), getAction()).send(p);

            for (double d = getDense(); d < getDepth() - getDense(); d += getDense())
                new CircleForm(getLocation().clone().add(0, 0, d), "x", getDense(), getRadius(), getAction()).send(p);
        }
    }
}
