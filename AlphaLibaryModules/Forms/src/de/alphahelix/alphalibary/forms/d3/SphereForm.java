package de.alphahelix.alphalibary.forms.d3;

import de.alphahelix.alphalibary.core.utils.RotationUtil;
import de.alphahelix.alphalibary.forms.Form;
import de.alphahelix.alphalibary.forms.FormAction;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;


public class SphereForm extends Form {
	
	private double radius;
	
	public SphereForm(Location location, double dense, double radius, FormAction action) {
		super(location, new Vector(1, 0, 0), dense, 0, action);
		this.radius = radius;
	}
	
	@Override
	public void send(Player p) {
		for(float angle = 0; angle < 180; angle += getDense()) {
			Vector v = new Vector(getRadius() * Math.cos(angle), getRadius() * Math.sin(angle), 0);
			
			getAction().action(p, getLocation().add(RotationUtil.rotate(v, getAxis(), angle)));
		}
	}
	
	public double getRadius() {
		return radius;
	}
	
	public SphereForm setRadius(double radius) {
		this.radius = radius;
		return this;
	}
}
