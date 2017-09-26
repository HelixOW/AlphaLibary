package de.alphahelix.alphalibary.forms.d2;

import de.alphahelix.alphalibary.forms.Form;
import de.alphahelix.alphalibary.forms.FormAction;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class PointForm extends Form {

    private final double distance;
    private final double slope;

    public PointForm(Location location, String axis, double dense, FormAction action, Vector p1, Vector p2) {
        super(location, axis, dense, action);

        distance = p1.distance(p2);
        slope = p1.angle(p2);
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public void send(Player p) {
        new RayForm(getLocation(), getAxis(), getDense(), distance, getAction(),
                x -> slope * x[0]).send(p);
    }
}
