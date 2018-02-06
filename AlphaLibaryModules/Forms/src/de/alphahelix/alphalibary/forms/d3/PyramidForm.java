package de.alphahelix.alphalibary.forms.d3;

import de.alphahelix.alphalibary.forms.Form;
import de.alphahelix.alphalibary.forms.FormAction;
import de.alphahelix.alphalibary.forms.d2.RectangleForm;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;


public class PyramidForm extends Form {

    private final double basis;
    private double size;
    private boolean filled;

    public PyramidForm(Location location, Vector axis, double dense, double angle, double basis, double size, boolean filled, FormAction action) {
        super(location, axis, dense, angle, action);
        this.basis = basis;
        this.size = size;
        this.filled = filled;
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

    @Override
    public void send(Player p) {
        for (double r = basis; r > 0 && size != 0; r -= getDense(), size -= getDense())
            new RectangleForm(getLocation().subtract(0, r, 0), getAxis(), getDense(), getAngle(), r, r, isFilled(), getAction()).send(p);
    }
}
