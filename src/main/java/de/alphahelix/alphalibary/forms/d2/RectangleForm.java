package de.alphahelix.alphalibary.forms.d2;

import de.alphahelix.alphalibary.forms.Form;
import de.alphahelix.alphalibary.forms.FormAction;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class RectangleForm extends Form {

    private double lenght, height;
    private boolean filled;
    private FormAction action;

    public RectangleForm(Location location, String axis, double dense, double lenght, double height, boolean filled, FormAction action) {
        super(location, axis, dense);

        this.lenght = lenght;
        this.height = height;
        this.filled = filled;
        this.action = action;
    }

    public double getLenght() {
        return lenght;
    }

    public RectangleForm setLenght(double lenght) {
        this.lenght = lenght;
        return this;
    }

    public double getHeight() {
        return height;
    }

    public RectangleForm setHeight(double height) {
        this.height = height;
        return this;
    }

    public boolean isFilled() {
        return filled;
    }

    public RectangleForm setFilled(boolean filled) {
        this.filled = filled;
        return this;
    }

    public FormAction getAction() {
        return action;
    }

    public RectangleForm setAction(FormAction action) {
        this.action = action;
        return this;
    }

    @Override
    public void send(Player p) {
        if (getAxis().equalsIgnoreCase("x")) {
            if (!filled) {
                for (double x = 0; x < (lenght); x += getDense()) {

                    action.action(p, getLocation().clone().add(x, 0, 0));
                    action.action(p, getLocation().clone().add(x, height, 0));

                }

                for (double y = 0; y < (lenght); y += getDense()) {
                    action.action(p, getLocation().clone().add(0, y, 0));
                    action.action(p, getLocation().clone().add(lenght, y, 0));
                }
            } else {
                for (double x = 0; x < lenght; x += 0.1) {
                    for (double y = 0; y < height; y += 0.1) {
                        action.action(p, getLocation().clone().add(x, y, 0));
                    }
                }
            }
        } else if (getAxis().equalsIgnoreCase("z")) {
            if (!filled) {
                for (double z = 0; z < (lenght); z += getDense()) {
                    action.action(p, getLocation().clone().add(0, 0, z));
                    action.action(p, getLocation().clone().add(0, height, z));
                }

                for (double y = 0; y < (lenght); y += getDense()) {
                    action.action(p, getLocation().clone().add(0, y, 0));
                    action.action(p, getLocation().clone().add(0, y, lenght));
                }
            } else {
                for (double z = 0; z < lenght; z += 0.1) {
                    for (double y = 0; y < height; y += 0.1) {
                        action.action(p, getLocation().clone().add(0, y, z));
                    }
                }
            }
        } else if (getAxis().equalsIgnoreCase("xz") || getAxis().equalsIgnoreCase("zx")) {
            if (!filled) {
                for (double xz = 0; xz < (lenght); xz += getDense()) {
                    action.action(p, getLocation().clone().add(xz, 0, xz));
                    action.action(p, getLocation().clone().add(xz, height, xz));
                }

                for (double y = 0; y < (lenght); y += getDense()) {
                    action.action(p, getLocation().clone().add(0, y, 0));
                    action.action(p, getLocation().clone().add(lenght / 2, y, lenght / 2));
                }
            } else {
                for (double xz = 0; xz < lenght; xz += 0.1) {
                    for (double y = 0; y < height; y += 0.1) {
                        action.action(p, getLocation().clone().add(xz, y, xz));
                    }
                }
            }
        } else if (getAxis().equalsIgnoreCase("y")) {
            if (!filled) {
                for (double x = 0; x < (lenght); x += getDense()) {
                    action.action(p, getLocation().clone().add(x, 0, 0));
                    action.action(p, getLocation().clone().add(0, 0, height));
                }

                for (double z = 0; z < (lenght); z += getDense()) {
                    action.action(p, getLocation().clone().add(0, 0, z));
                    action.action(p, getLocation().clone().add(lenght, 0, z));
                }
            } else {
                for (double z = 0; z < lenght; z += 0.1) {
                    for (double x = 0; x < height; x += 0.1) {
                        action.action(p, getLocation().clone().add(z, 0, x));
                    }
                }
            }
        } else if (getAxis().equalsIgnoreCase("xzy") || getAxis().equalsIgnoreCase("yzx") || getAxis().equalsIgnoreCase("zyx") || getAxis().equalsIgnoreCase("zxy") ||
                getAxis().equalsIgnoreCase("xyz") || getAxis().equalsIgnoreCase("yxz")) {

            if (!filled) {
                for (double y = 0; y < (lenght); y += getDense()) {
                    action.action(p, getLocation().clone().add(0, y, 0));
                    action.action(p, getLocation().clone().add(height / 2, 0, height / 2));
                }

                for (double xz = 0; xz < (lenght); xz += getDense()) {
                    action.action(p, getLocation().clone().add(xz, 0, xz));
                    action.action(p, getLocation().clone().add(xz, lenght, xz));
                }
            } else {
                for (double xz = 0; xz < lenght; xz += 0.1) {
                    for (double y = 0; y < height; y += 0.1) {
                        action.action(p, getLocation().clone().add(y, xz, y));
                    }
                }
            }
        }
    }
}
