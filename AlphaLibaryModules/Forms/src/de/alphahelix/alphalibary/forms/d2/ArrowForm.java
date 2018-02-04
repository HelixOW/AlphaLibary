package de.alphahelix.alphalibary.forms.d2;

import de.alphahelix.alphalibary.core.utils.Util;
import de.alphahelix.alphalibary.forms.Form;
import de.alphahelix.alphalibary.forms.FormAction;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

@SuppressWarnings("ALL")
public class ArrowForm extends Form {

    private double lenght, width, angle;

    public ArrowForm(Location location, Vector axis, double dense, double angle, double lenght, double width, FormAction action) {
        super(location, axis, dense, angle, action,
                x -> x[0] * (-1),
                x -> x[0],
                x -> x[0]);
        this.lenght = lenght;
        this.width = width;
    }

    public double getLenght() {
        return lenght;
    }

    public ArrowForm setLenght(double lenght) {
        this.lenght = lenght;
        return this;
    }

    public double getWidth() {
        return width;
    }

    public ArrowForm setWidth(double width) {
        this.width = width;
        return this;
    }

    @Override
    public void send(Player p) {
        Vector v;
        for (double x = 0; x < (width / 2); x += getDense()) {
            v = new Vector(x, getFormFunctions()[0].f(x), 0);

            getAction().action(p, getLocation().add(Util.rotate(v, getAxis(), getAngle())));
        }

        for (double x = ((width / 2) * (-1)); x < 0; x += getDense()) {
            v = new Vector(x, getFormFunctions()[1].f(x), 0);

            getAction().action(p, getLocation().add(Util.rotate(v, getAxis(), getAngle())));
        }

        for (double x = 0; x < lenght; x += getDense()) {
            v = new Vector(x, getFormFunctions()[1].f(x), 0);

            getAction().action(p, getLocation().add(Util.rotate(v, getAxis(), getAngle())));
        }
    }
}
