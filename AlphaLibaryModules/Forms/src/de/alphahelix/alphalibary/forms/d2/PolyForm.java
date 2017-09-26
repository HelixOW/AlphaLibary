package de.alphahelix.alphalibary.forms.d2;

import de.alphahelix.alphalibary.forms.Form;
import de.alphahelix.alphalibary.forms.FormAction;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

@SuppressWarnings("ALL")
public class PolyForm extends Form {

    private Vector[] points;

    public PolyForm(Location location, String axis, double dense, FormAction action, Vector... points) {
        super(location, axis, dense, action);
        Validate.isTrue(points.length % 2 == 0, "It always has to be multiple of 2 points");
        this.points = points;
    }

    public Vector[] getPoints() {
        return points;
    }

    @Override
    public void send(Player p) {
        for (int i = 0; i < points.length; i += 2) {
            new PointForm(getLocation(), getAxis(), getDense(), getAction(), getPoints()[i], getPoints()[i + 1]).send(p);
        }
    }
}
