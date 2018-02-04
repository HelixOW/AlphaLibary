package de.alphahelix.alphalibary.forms.d2;

import de.alphahelix.alphalibary.core.utils.Util;
import de.alphahelix.alphalibary.forms.Form;
import de.alphahelix.alphalibary.forms.FormAction;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class CircleForm extends Form {

    private final double radius;

    public CircleForm(Location location, Vector axis, double dense, double angle, double radius, FormAction action) {
        super(location, axis, dense, angle, action);
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public void send(Player p) {
        for (float angle = 0; angle < 180; angle += getDense()) {
            Vector v = new Vector(getRadius() * Math.cos(angle), getRadius() * Math.sin(angle), 0);

            getAction().action(p, getLocation().add(Util.rotate(v, getAxis(), getAngle())));
        }
    }
}
