package de.alphahelix.alphalibary.forms.d2;

import de.alphahelix.alphalibary.forms.Form;
import de.alphahelix.alphalibary.forms.FormAction;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CircleForm extends Form {

    private final double radius;
    private FormAction action;

    public CircleForm(Location location, String axis, double dense, double radius, FormAction action) {
        super(location, axis, dense, x -> Math.sqrt(radius * radius - x[0] * x[0]));
        this.action = action;
        this.radius = radius;
    }

    public FormAction getAction() {
        return action;
    }

    public CircleForm setAction(FormAction action) {
        this.action = action;
        return this;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public void send(Player p) {
        if (getAxis().equalsIgnoreCase("x")) {
            for (double x = -radius; x < radius; x += getDense()) {
                action.action(p, getLocation().clone().add(x, getFormFunctions()[0].f(x), 0));
                action.action(p, getLocation().clone().subtract(x, getFormFunctions()[0].f(x), 0));
            }
        } else if (getAxis().equalsIgnoreCase("z")) {
            for (double z = -radius; z < radius; z += getDense()) {
                action.action(p, getLocation().clone().add(0, getFormFunctions()[0].f(z), z));
                action.action(p, getLocation().clone().subtract(0, getFormFunctions()[0].f(z), z));
            }
        } else if (getAxis().equalsIgnoreCase("xz") || getAxis().equalsIgnoreCase("zx")) {
            for (double xz = -(radius); xz < (radius); xz += getDense()) {
                action.action(p, getLocation().clone().add(xz, getFormFunctions()[0].f(xz), xz));
                action.action(p, getLocation().clone().subtract(xz, getFormFunctions()[0].f(xz), xz));
            }
        } else if (getAxis().equalsIgnoreCase("y")) {
            for (double y = -(radius); y < (radius); y += getDense()) {
                action.action(p, getLocation().clone().add(getFormFunctions()[0].x(y), 0, y));
                action.action(p, getLocation().clone().subtract(getFormFunctions()[0].x(y), 0, y));
            }
        }
    }
}
