package de.alphahelix.alphalibary.forms.d2;

import de.alphahelix.alphalibary.forms.Form;
import de.alphahelix.alphalibary.forms.FormAction;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.List;

public class RayForm extends Form {
	
	private double length;
	private Vector direction;
	
	public RayForm(Location location, double dense, FormAction action, double length, Location loc1, Location loc2) {
		this(location, dense, action, length, loc2.clone().subtract(loc1).toVector());
	}
	
	public RayForm(Location location, double dense, FormAction action, double length, Vector direction) {
		this(location, dense, false, action, length, direction);
	}
	
	public RayForm(Location location, double dense, boolean filled, FormAction action, double length, Vector direction) {
		this(location, null, dense, 0, action, length, direction);
	}
	
	public RayForm(Location location, Vector axis, double dense, double angle, FormAction action, double length, Vector direction) {
		this(location, axis, dense, angle, false, action, length, direction);
	}
	
	public RayForm(Location location, Vector axis, double dense, double angle, boolean filled, FormAction action, double length, Vector direction) {
		super(location, axis, dense, angle, filled, action);
		this.length = length;
		this.direction = direction;
		getVisualizer().setDense(.2);
		apply();
	}
	
	public RayForm(Location location, double dense, boolean filled, FormAction action, double length, Location loc1, Location loc2) {
		this(location, dense, filled, action, length, loc2.clone().subtract(loc1).toVector());
	}
	
	public RayForm(Location location, Vector axis, double dense, double angle, FormAction action, double length, Location loc1, Location loc2) {
		this(location, axis, dense, angle, action, length, loc2.clone().subtract(loc1).toVector());
	}
	
	public RayForm(Location location, Vector axis, double dense, double angle, boolean filled, FormAction action, double length, Location loc1, Location loc2) {
		this(location, axis, dense, angle, filled, action, length, loc2.clone().subtract(loc1).toVector());
	}
	
	@Override
	public void calculate(List<Location> locations) {
		for(double d = 0; d < getLength(); d += getDense()) {
			locations.add(getLocation().clone().add(getDirection().clone().multiply(d)));
		}
	}
	
	public double getLength() {
		return length;
	}
	
	public Vector getDirection() {
		return direction.clone().normalize();
	}
	
	public RayForm setLength(double length) {
		this.length = length;
		return this;
	}
	
	public RayForm setDirection(Vector direction) {
		this.direction = direction;
		return this;
	}
}
