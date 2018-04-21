package de.alphahelix.alphalibary.forms.d2.triangles;

import de.alphahelix.alphalibary.forms.FormAction;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class EquilateralTriangleForm extends AcuteTriangleForm {
	
	public EquilateralTriangleForm(Location location, Vector axis, double dense, double angle, FormAction action, Vector direction, double length) {
		super(location, axis, dense, angle, action, direction, length, (length / 2.0) * Math.sqrt(3), new Vector(0, 0, 0));
	}
}
