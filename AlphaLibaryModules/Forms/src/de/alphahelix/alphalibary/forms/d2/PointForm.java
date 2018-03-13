package de.alphahelix.alphalibary.forms.d2;

import de.alphahelix.alphalibary.forms.FormAction;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class PointForm extends RayForm {
	public PointForm(Location location, double dense, FormAction action, Vector p2, Vector p1) {
		super(location,
				new Vector(p2.getX() - p1.getX(), p2.getY() - p1.getY(), p2.getZ() - p1.getZ()),
				dense, p1.distance(p2), action);
	}
}
