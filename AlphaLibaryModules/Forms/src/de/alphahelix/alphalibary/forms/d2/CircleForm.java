package de.alphahelix.alphalibary.forms.d2;

import de.alphahelix.alphalibary.core.utils.RotationUtil;
import de.alphahelix.alphalibary.forms.Form;
import de.alphahelix.alphalibary.forms.FormAction;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.List;

public class CircleForm extends Form {
	
	private final double radius;
	
	public CircleForm(Location location, Vector axis, double dense, double angle, double radius, FormAction action) {
		super(location, axis, dense, angle, action);
		this.radius = radius;
		apply();
	}
	
	@Override
	public void calculate(List<Location> locations) {
		for(float alpha = 0; alpha < 180; alpha += getDense()) {
			Vector v = new Vector(getRadius() * Math.cos(alpha), getRadius() * Math.sin(alpha), 0);
			
			locations.add(getLocation().add(RotationUtil.rotate(v, getAxis(), getAngle())));
		}
	}
	
	public double getRadius() {
		return radius;
	}
}
