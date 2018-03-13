package de.alphahelix.alphalibary.forms.d2;

import de.alphahelix.alphalibary.core.utils.RotationUtil;
import de.alphahelix.alphalibary.forms.Form;
import de.alphahelix.alphalibary.forms.FormAction;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class CircleForm extends Form {
	
	private final double radius;
	
	public CircleForm(Location location, Vector axis, double dense, double angle, double radius, FormAction action) {
		super(location, axis, dense, angle, action);
		this.radius = radius;
	}
	
	@Override
	public void send(Player p) {
		for(float alpha = 0; alpha < 180; alpha += getDense()) {
			Vector v = new Vector(getRadius() * Math.cos(alpha), getRadius() * Math.sin(alpha), 0);
			
			getAction().action(p, getLocation().add(RotationUtil.rotate(v, getAxis(), getAngle())));
		}
	}
	
	public double getRadius() {
		return radius;
	}
}
