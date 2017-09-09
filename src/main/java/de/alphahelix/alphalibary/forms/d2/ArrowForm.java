package de.alphahelix.alphalibary.forms.d2;

import de.alphahelix.alphalibary.forms.Form;
import de.alphahelix.alphalibary.forms.FormAction;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class ArrowForm extends Form {

    private FormAction action;
    private double lenght, width;
    private BlockFace direction;

    public ArrowForm(BlockFace direction, Location location, String axis, double dense, double lenght, double width, FormAction action) {
        super(location, axis, dense,
                x -> x[0] * (-1),
                x -> x[0],
                x -> x[0]);
        this.lenght = lenght;
        this.width = width;
        this.direction = direction;
        this.action = action;
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

    public BlockFace getDirection() {
        return direction;
    }

    public ArrowForm setDirection(BlockFace direction) {
        this.direction = direction;
        return this;
    }

    public FormAction getAction() {
        return action;
    }

    public ArrowForm setAction(FormAction action) {
        this.action = action;
        return this;
    }

    @Override
    public void send(Player p) {
        if (getAxis().equalsIgnoreCase("x")) {
            if (direction == BlockFace.UP) {
                for (double x = 0; x < (width / 2); x += getDense())
                    action.action(p, getLocation().clone().add(x, getFormFunctions()[0].f(x), 0));

                for (double x = ((width / 2) * (-1)); x < 0; x += getDense())
                    action.action(p, getLocation().clone().add(x, getFormFunctions()[1].f(x), 0));

                for (double x = 0; x < lenght; x += getDense())
                    action.action(p, getLocation().clone().subtract(0, getFormFunctions()[1].f(x), 0));

            } else {
                for (double x = 0; x < (width / 2); x += getDense())
                    action.action(p, getLocation().clone().subtract(x, getFormFunctions()[0].f(x), 0));

                for (double x = ((width / 2) * (-1)); x < 0; x += getDense())
                    action.action(p, getLocation().clone().subtract(x, getFormFunctions()[1].f(x), 0));

                for (double x = 0; x < lenght; x += getDense())

                    action.action(p, getLocation().clone().add(0, getFormFunctions()[1].f(x), 0));
            }
        } else if (getAxis().equalsIgnoreCase("z")) {
            if (direction == BlockFace.UP) {
                for (double z = 0; z < (width / 2); z += getDense())
                    action.action(p, getLocation().clone().add(0, getFormFunctions()[0].f(z), z));

                for (double z = ((width / 2) * (-1)); z < 0; z += getDense())
                    action.action(p, getLocation().clone().add(0, getFormFunctions()[1].f(z), z));

                for (double z = 0; z < lenght; z += getDense())
                    action.action(p, getLocation().clone().subtract(0, getFormFunctions()[1].f(z), 0));
            } else {
                for (double z = 0; z < (width / 2); z += getDense())
                    action.action(p, getLocation().clone().subtract(0, getFormFunctions()[0].f(z), z));

                for (double z = ((width / 2) * (-1)); z < 0; z += getDense())
                    action.action(p, getLocation().clone().subtract(0, getFormFunctions()[1].f(z), z));

                for (double z = 0; z < lenght; z += getDense())
                    action.action(p, getLocation().clone().add(0, getFormFunctions()[1].f(z), 0));
            }
        } else if (getAxis().equalsIgnoreCase("xz") || getAxis().equalsIgnoreCase("zx")) {
            if (direction == BlockFace.UP) {
                for (double xz = 0; xz < (width / 2); xz += getDense())
                    action.action(p, getLocation().clone().add(xz, getFormFunctions()[0].f(xz), xz));

                for (double xz = ((width / 2) * (-1)); xz < 0; xz += getDense())
                    action.action(p, getLocation().clone().add(xz, getFormFunctions()[1].f(xz), xz));

                for (double xz = 0; xz < lenght; xz += getDense())
                    action.action(p, getLocation().clone().subtract(0, getFormFunctions()[1].f(xz), 0));
            } else {
                for (double xz = 0; xz < (width / 2); xz += getDense())
                    action.action(p, getLocation().clone().subtract(xz, getFormFunctions()[0].f(xz), xz));

                for (double xz = ((width / 2) * (-1)); xz < 0; xz += getDense())
                    action.action(p, getLocation().clone().subtract(xz, getFormFunctions()[1].f(xz), xz));

                for (double xz = 0; xz < lenght; xz += getDense())
                    action.action(p, getLocation().clone().add(0, getFormFunctions()[1].f(xz), 0));
            }
        } else if (getAxis().equalsIgnoreCase("xy") || getAxis().equalsIgnoreCase("yx")) {
            if (direction == BlockFace.EAST) {
                for (double xy = 0; xy < (width / 2); xy += getDense())
                    action.action(p, getLocation().clone().add(getFormFunctions()[0].x(xy), 0, xy));

                for (double xy = ((width / 2) * (-1)); xy < 0; xy += getDense())
                    action.action(p, getLocation().clone().add(getFormFunctions()[1].x(xy), 0, xy));

                for (double xy = 0; xy < lenght; xy += getDense())
                    action.action(p, getLocation().clone().subtract(getFormFunctions()[1].x(xy), 0, 0));

            } else {
                for (double xy = 0; xy < (width / 2); xy += getDense())
                    action.action(p, getLocation().clone().subtract(getFormFunctions()[0].x(xy), 0, xy));

                for (double xy = ((width / 2) * (-1)); xy < 0; xy += getDense())
                    action.action(p, getLocation().clone().subtract(getFormFunctions()[1].x(xy), 0, xy));

                for (double xy = 0; xy < lenght; xy += getDense())
                    action.action(p, getLocation().clone().add(getFormFunctions()[1].x(xy), 0, 0));

            }
        } else if (getAxis().equalsIgnoreCase("zy") || getAxis().equalsIgnoreCase("yz")) {
            if (direction == BlockFace.SOUTH) {
                for (double zy = 0; zy < (width / 2); zy += getDense())
                    action.action(p, getLocation().clone().add(zy, 0, getFormFunctions()[0].x(zy)));

                for (double zy = ((width / 2) * (-1)); zy < 0; zy += getDense())
                    action.action(p, getLocation().clone().add(zy, 0, getFormFunctions()[1].x(zy)));

                for (double zy = 0; zy < lenght; zy += getDense())
                    action.action(p, getLocation().clone().subtract(0, 0, getFormFunctions()[1].x(zy)));

            } else {
                for (double zy = 0; zy < (width / 2); zy += getDense())
                    action.action(p, getLocation().clone().subtract(zy, 0, getFormFunctions()[0].x(zy)));

                for (double zy = ((width / 2) * (-1)); zy < 0; zy += getDense())
                    action.action(p, getLocation().clone().subtract(zy, 0, getFormFunctions()[1].x(zy)));

                for (double zy = 0; zy < lenght; zy += getDense())
                    action.action(p, getLocation().clone().add(0, 0, getFormFunctions()[1].x(zy)));

            }
        } else if (getAxis().equalsIgnoreCase("xzy") || getAxis().equalsIgnoreCase("yzx") || getAxis().equalsIgnoreCase("zyx") || getAxis().equalsIgnoreCase("zxy") ||
                getAxis().equalsIgnoreCase("xyz") || getAxis().equalsIgnoreCase("yxz")) {
            if (direction == BlockFace.SOUTH_EAST) {
                for (double xyz = 0; xyz < (width / 2); xyz += getDense())
                    action.action(p, getLocation().clone().add(getFormFunctions()[0].x(xyz), 0, getFormFunctions()[0].x(xyz)));

                for (double xyz = ((width / 2) * (-1)); xyz < 0; xyz += getDense())
                    action.action(p, getLocation().clone().add(getFormFunctions()[1].x(xyz), 0, getFormFunctions()[1].x(xyz)));

                for (double xyz = 0; xyz < lenght; xyz += getDense())
                    action.action(p, getLocation().clone().subtract(getFormFunctions()[1].x(xyz), 0, getFormFunctions()[1].x(xyz)));

            } else {
                for (double xyz = 0; xyz < (width / 2); xyz += getDense())
                    action.action(p, getLocation().clone().subtract(getFormFunctions()[0].x(xyz), 0, getFormFunctions()[0].x(xyz)));

                for (double xyz = ((width / 2) * (-1)); xyz < 0; xyz += getDense())
                    action.action(p, getLocation().clone().subtract(getFormFunctions()[1].x(xyz), 0, getFormFunctions()[1].x(xyz)));

                for (double xyz = 0; xyz < lenght; xyz += getDense())
                    action.action(p, getLocation().clone().add(xyz, 0, getFormFunctions()[1].x(xyz)));
            }
        }
    }
}
