package de.alphahelix.alphalibary.forms.d3;

import de.alphahelix.alphalibary.core.utils.RotationUtil;
import de.alphahelix.alphalibary.forms.Animatable;
import de.alphahelix.alphalibary.forms.Form;
import de.alphahelix.alphalibary.forms.FormAction;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.List;


public class SphereForm extends Form implements Animatable {
	
	private double radius;
	private boolean grow = true;
	
	public SphereForm(Location location, double dense, FormAction action, double radius) {
		this(location, dense, false, action, radius);
	}
	
	public SphereForm(Location location, double dense, boolean filled, FormAction action, double radius) {
		super(location, new Vector(1, 0, 0), dense, 0, filled, action);
		this.radius = radius;
		getVisualizer().setDense(.2);
		apply();
	}
	
	@Override
	public void calculate(List<Location> locations) {
		for(float angle = 0; angle < 180; angle += getDense()) {
			Vector v = RotationUtil.rotate(new Vector(getRadius() * Math.cos(angle), getRadius() * Math.sin(angle), 0), getAxis(), angle);
			
			locations.add(getLocation().add(v));
			
			if(isFilled())
				locations.addAll(getVisualizer().getVectorLocations(getLocation(), v));
		}
	}
	
	public double getRadius() {
		return radius;
	}
	
	public SphereForm setRadius(double radius) {
		this.radius = radius;
		return this;
	}
	
	@Override
	public void animate(double minSize, double maxSize) {
		if(getRadius() < maxSize && grow) {
			setRadius(getRadius() + getDense());
		} else if(getRadius() >= maxSize) {
			grow = false;
			setRadius(getRadius() - getDense());
		} else if(getRadius() > minSize) {
			setRadius(getRadius() - getDense());
		} else {
			grow = true;
		}
		
		apply();
	}
}
