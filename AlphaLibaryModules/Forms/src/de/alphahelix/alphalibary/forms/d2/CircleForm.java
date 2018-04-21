package de.alphahelix.alphalibary.forms.d2;

import de.alphahelix.alphalibary.core.utils.RotationUtil;
import de.alphahelix.alphalibary.forms.Form;
import de.alphahelix.alphalibary.forms.FormAction;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.List;

public class CircleForm extends Form {
	
	private final double radius;
	
	public CircleForm(Location location, double dense, FormAction formAction, double radius) {
		this(location, dense, false, formAction, radius);
	}
	
	public CircleForm(Location location, double dense, boolean filled, FormAction formAction, double radius) {
		this(location, null, dense, 0, filled, formAction, radius);
	}
	
	public CircleForm(Location location, Vector axis, double dense, double angle, boolean filled, FormAction formAction, double radius) {
		super(location, axis, dense, angle, filled, formAction);
		this.radius = radius;
		getVisualizer().setDense(.2);
		apply();
	}
	
	public CircleForm(Location location, Vector axis, double dense, double angle, FormAction formAction, double radius) {
		this(location, axis, dense, angle, false, formAction, radius);
	}
	
	@Override
	public void calculate(List<Location> locations) {
		for(float alpha = 0; alpha < 180; alpha += getDense()) {
			Vector v = new Vector(getRadius() * Math.cos(alpha), getRadius() * Math.sin(alpha), 0);
			
			locations.add(getLocation().add(RotationUtil.rotate(v, getAxis(), getAngle())));
			
			if(isFilled())
				locations.addAll(getVisualizer().getVectorLocations(getLocation(), v));
		}
	}
	
	public double getRadius() {
		return radius;
	}
}
