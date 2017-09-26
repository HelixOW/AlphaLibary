package de.alphahelix.alphalibary.forms.d2;

import de.alphahelix.alphalibary.forms.Form;
import de.alphahelix.alphalibary.forms.FormAction;
import de.alphahelix.alphalibary.forms.FormFunction;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class RayForm extends Form {

    private double lenght;

    public RayForm(Location location, String axis, double dense, double lenght, FormAction action, FormFunction formFunction) {
        super(location, axis, dense, action, formFunction);
        this.lenght = lenght;
    }

    public double getLenght() {
        return lenght;
    }

    public RayForm setLenght(double lenght) {
        this.lenght = lenght;
        return this;
    }

    @Override
    public void send(Player p) {
        if (getAxis().equalsIgnoreCase("x")) {
            for (double x = 0; x < lenght; x += getDense())
                getAction().action(p, getLocation().clone().add(x, getFormFunctions()[0].f(x), 0));
        } else if (getAxis().equalsIgnoreCase("z")) {
            for (double z = 0; z < lenght; z += getDense())
                getAction().action(p, getLocation().clone().add(0, getFormFunctions()[0].f(z), z));
        } else if (getAxis().equalsIgnoreCase("xz") || getAxis().equalsIgnoreCase("zx")) {
            for (double xz = 0; xz < lenght; xz += getDense())
                getAction().action(p, getLocation().clone().add(xz, getFormFunctions()[0].f(xz), xz));
        } else if (getAxis().equalsIgnoreCase("xy") || getAxis().equalsIgnoreCase("yx")) {
            for (double xy = 0; xy < lenght; xy += getDense())
                getAction().action(p, getLocation().clone().add(getFormFunctions()[0].x(xy), 0, xy));
        } else if (getAxis().equalsIgnoreCase("zy") || getAxis().equalsIgnoreCase("yz")) {
            for (double zy = 0; zy < lenght; zy += getDense())
                getAction().action(p, getLocation().clone().add(zy, 0, getFormFunctions()[0].x(zy)));
        } else if (getAxis().equalsIgnoreCase("xzy") || getAxis().equalsIgnoreCase("yzx") || getAxis().equalsIgnoreCase("zyx") || getAxis().equalsIgnoreCase("zxy") ||
                getAxis().equalsIgnoreCase("xyz") || getAxis().equalsIgnoreCase("yxz")) {
            for (double xyz = 0; xyz < lenght; xyz += getDense())
                getAction().action(p, getLocation().clone().add(xyz, 0, getFormFunctions()[0].x(xyz)));
        }
    }
}
