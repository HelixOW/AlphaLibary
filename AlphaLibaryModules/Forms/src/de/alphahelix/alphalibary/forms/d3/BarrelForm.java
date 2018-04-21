package de.alphahelix.alphalibary.forms.d3;

import de.alphahelix.alphalibary.forms.Animatable;
import de.alphahelix.alphalibary.forms.Form;
import de.alphahelix.alphalibary.forms.FormAction;
import de.alphahelix.alphalibary.forms.d2.CircleForm;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.List;


public class BarrelForm extends Form implements Animatable {
	
	private double depth, radius;
	private boolean grow = true;
	
	public BarrelForm(Location location, double dense, FormAction action, double depth, double radius) {
		this(location, dense, false, action, depth, radius);
	}
	
	public BarrelForm(Location location, double dense, boolean filled, FormAction action, double depth, double radius) {
		this(location, null, dense, 0, action, depth, radius);
	}
	
	public BarrelForm(Location location, Vector axis, double dense, double angle, FormAction action, double depth, double radius) {
		this(location, axis, dense, angle, false, action, depth, radius);
	}
	
	public BarrelForm(Location location, Vector axis, double dense, double angle, boolean filled, FormAction action, double depth, double radius) {
		super(location, axis, dense, angle, filled, action);
		this.depth = depth;
		this.radius = radius;
		apply();
	}
	
	public BarrelForm(Location location, Vector axis, double dense, double angle, double depth, double radius, FormAction action) {
		this(location, axis, dense, angle, false, action, depth, radius);
	}
	
	@Override
	public void calculate(List<Location> locations) {
		locations.addAll(new CircleForm(getLocation(), getAxis(), getDense(), getAngle(), isFilled(), getAction(), getRadius()).getToSpawn());
		locations.addAll(new CircleForm(getLocation().add(0, getDepth(), 0), getAxis(), getDense(), getAngle(), isFilled(), getAction(), getRadius()).getToSpawn());
	
		for(double d = getDense(); d < getDepth() - getDense(); d += getDense())
			locations.addAll(new CircleForm(getLocation().add(0, d, 0), getAxis(), getDense(), getAngle(), isFilled(), getAction(), getRadius()).getToSpawn());
	}
	
	public double getRadius() {
		return radius;
	}
	
	public double getDepth() {
		return depth;
	}
	
	public BarrelForm setDepth(double depth) {
		this.depth = depth;
		return this;
	}
	
	public BarrelForm setRadius(double radius) {
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
