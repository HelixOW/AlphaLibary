package de.alphahelix.alphalibary.forms.d2;

import de.alphahelix.alphalibary.core.utils.RotationUtil;
import de.alphahelix.alphalibary.forms.Form;
import de.alphahelix.alphalibary.forms.FormAction;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.List;

public class ArrowForm extends Form {
	
	private double length, angle;
	private Vector direction;
	
	public ArrowForm(Location location, Vector axis, double dense, double angle, FormAction action, double length, double angle1, Vector direction) {
		super(location, axis, dense, angle, action);
		this.angle = angle1;
		this.direction = RotationUtil.rotate(direction.multiply(-1), axis, angle);
		this.length = length;
		apply();
	}
	
	@Override
	public void calculate(List<Location> locations) {
		Vector a = RotationUtil.rotate(direction, RotationUtil.findPerpendicularVector(direction), 360 - (angle / 2));
		Vector b = RotationUtil.rotate(direction, RotationUtil.findPerpendicularVector(direction), angle / 2);
		
		b.normalize();
		
		for(double x = 0; x < length; x += getDense()) {
			locations.add(getLocation().add(b.clone().multiply(x)));
			locations.add(getLocation().add(a.clone().multiply(x)));
		}
	}
}
