package de.alphahelix.alphalibary.forms.d3;

import de.alphahelix.alphalibary.forms.Form;
import de.alphahelix.alphalibary.forms.FormAction;
import de.alphahelix.alphalibary.forms.d2.CircleForm;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;


public class BarrelForm extends Form {
	
	private double depth, radius;
	
	public BarrelForm(Location location, Vector axis, double dense, double angle, double depth, double radius, FormAction action) {
		super(location, axis, dense, angle, action);
		this.depth = depth;
		this.radius = radius;
	}
	
	@Override
	public void send(Player p) {
		new CircleForm(getLocation(), getAxis(), getDense(), getAngle(), getRadius(), getAction()).send(p);
		new CircleForm(getLocation().add(0, getDepth(), 0), getAxis(), getDense(), getAngle(), getRadius(), getAction()).send(p);
		
		for(double d = getDense(); d < getDepth() - getDense(); d += getDense())
			new CircleForm(getLocation().add(0, d, 0), getAxis(), getDense(), getAngle(), getRadius(), getAction()).send(p);
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
}
