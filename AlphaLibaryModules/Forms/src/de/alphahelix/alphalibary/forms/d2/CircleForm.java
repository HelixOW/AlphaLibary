package de.alphahelix.alphalibary.forms.d2;

import de.alphahelix.alphalibary.forms.Form;
import de.alphahelix.alphalibary.forms.FormAction;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CircleForm extends Form {

    private final double radius;

    public CircleForm(Location location, String axis, double dense, double radius, FormAction action) {
        super(location, axis, dense, action);
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public void send(Player p) {
        if (getAxis().equalsIgnoreCase("x")) {
            for (float angle = 0; angle < 180; angle += getDense()) {
                getAction().action(p, getLocation().clone().add(
                        getRadius() * Math.cos(angle),
                        getRadius() * Math.sin(angle),
                        0));
            }
        } else if (getAxis().equalsIgnoreCase("z")) {
            for (float angle = 0; angle < 180; angle += getDense()) {
                getAction().action(p, getLocation().clone().add(
                        0,
                        getRadius() * Math.sin(angle),
                        getRadius() * Math.cos(angle)));
            }
        } else if (getAxis().equalsIgnoreCase("xz") || getAxis().equalsIgnoreCase("zx")) {
            for (float angle = 0; angle < 180; angle += getDense()) {
                getAction().action(p, getLocation().clone().add(
                        (getRadius() * Math.cos(angle)),
                        getRadius() * Math.sin(angle),
                        (getRadius() * Math.cos(angle))));
            }
        } else if (getAxis().equalsIgnoreCase("y")) {
            for (float angle = 0; angle < 180; angle += getDense()) {
                getAction().action(p, getLocation().clone().add(
                        getRadius() * Math.cos(angle),
                        0,
                        getRadius() * Math.sin(angle)));
            }
        }
    }
}
