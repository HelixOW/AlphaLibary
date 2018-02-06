package de.alphahelix.alphalibary.forms.d3;

import de.alphahelix.alphalibary.forms.Form;
import de.alphahelix.alphalibary.forms.FormAction;
import de.alphahelix.alphalibary.forms.d2.CircleForm;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;


public class ConeForm extends Form {

    private final double baseRadius;
    private double size;
    private boolean filled;

    public ConeForm(Location location, Vector axis, double dense, double angle, double baseRadius, double size, boolean filled, FormAction action) {
        super(location, axis, dense, angle, action);
        this.baseRadius = baseRadius;
        this.size = size;
        this.filled = filled;
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

    @Override
    public void send(Player p) {
        for (double r = baseRadius; r > 0 && size != 0; r -= getDense(), size -= getDense())
            new CircleForm(getLocation().subtract(0, r, 0), getAxis(), getDense(), getAngle(), r, getAction()).send(p);
    }
}
