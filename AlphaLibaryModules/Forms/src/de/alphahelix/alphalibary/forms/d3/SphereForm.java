package de.alphahelix.alphalibary.forms.d3;

import de.alphahelix.alphalibary.forms.Form;
import de.alphahelix.alphalibary.forms.FormAction;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@SuppressWarnings("ALL")
public class SphereForm extends Form {

    private double radius;

    public SphereForm(Location location, double dense, double radius, FormAction action) {
        super(location, "", dense, action, x -> Math.sqrt(radius * radius - x[0] * x[0] - x[1] * x[1]));
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public SphereForm setRadius(double radius) {
        this.radius = radius;
        return this;
    }

    @Override
    public void send(Player p) {
        for (double x = -radius; x < radius; x += getDense()) {
            for (double z = -radius; z < radius; z += getDense()) {
                double y = getFormFunctions()[0].f(x, z);

                getAction().action(p, getLocation().clone().subtract(0, y, 0).add(x, 0, z));
                getAction().action(p, getLocation().clone().add(0, y, 0).subtract(x, 0, z));
            }
        }
    }
}
