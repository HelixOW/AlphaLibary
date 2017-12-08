package de.alphahelix.alphalibary.forms.d3;

import de.alphahelix.alphalibary.forms.Form;
import de.alphahelix.alphalibary.forms.FormAction;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@SuppressWarnings("ALL")
public class SphereForm extends Form {

    private double radius;

    /*
    x = r * cos b * cos a
    y = r * cos b * sin a
    z = r * sin b
     */

    public SphereForm(Location location, double dense, double radius, FormAction action) {
        super(location, "", dense, action);
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
        for (float angleB = 0; angleB < 360; angleB++) {
            for (float angleA = 0; angleA < 360; angleA++) {
                getAction().action(p, getLocation().clone().add(
                        getRadius() * Math.cos(angleB) * Math.cos(angleA),
                        getRadius() * Math.cos(angleB) * Math.sin(angleA),
                        getRadius() * Math.sin(angleB)
                ));
            }
        }
    }
}
