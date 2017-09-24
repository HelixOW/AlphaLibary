package de.alphahelix.alphalibary.forms.d3;

import de.alphahelix.alphalibary.forms.Form;
import de.alphahelix.alphalibary.forms.FormAction;
import de.alphahelix.alphalibary.utils.Util;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SphereForm extends Form {

    private FormAction action;
    private double radius;

    public SphereForm(Location location, double dense, double radius, FormAction action) {
        super(location, "", dense, x -> Util.sqrt(radius * radius - x[0] * x[0] - x[1] * x[1]));
        this.action = action;
        this.radius = radius;
    }

    public FormAction getAction() {
        return action;
    }

    public SphereForm setAction(FormAction action) {
        this.action = action;
        return this;
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

                action.action(p, getLocation().clone().subtract(0, y, 0).add(x, 0, z));
                action.action(p, getLocation().clone().add(0, y, 0).subtract(x, 0, z));
            }
        }
    }
}
