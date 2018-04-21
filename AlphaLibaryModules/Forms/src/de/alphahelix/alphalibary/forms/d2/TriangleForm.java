package de.alphahelix.alphalibary.forms.d2;

import de.alphahelix.alphalibary.core.utils.RotationUtil;
import de.alphahelix.alphalibary.forms.Form;
import de.alphahelix.alphalibary.forms.FormAction;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.List;

public abstract class TriangleForm extends Form {
	
	private final Location[] points = new Location[3];
	private final Vector direction, perp;
	
	public TriangleForm(Location location, Vector axis, double dense, double angle, FormAction action, Vector direction) {
		super(location, axis, dense, angle, action);
		this.direction = direction;
		this.perp = RotationUtil.findPerpendicularVector(direction);
	}
	
	public Location[] getPoints() {
		return points;
	}
	
	public Vector getDirection() {
		return direction;
	}
	
	public Vector getPerp() {
		return perp;
	}
	
	public void drawLines(List<Location> locations) {
		Vector ab = getB().clone().subtract(getA()).toVector(), ac = getC().clone().subtract(getA()).toVector(), bc = getC().clone().subtract(getB()).toVector();
		
		double lAB = ab.length(), lAC = ac.length(), lBC = bc.length();
		
		ab.normalize();
		ac.normalize();
		bc.normalize();
		
		locations.add(getA());
		locations.add(getB());
		locations.add(getC());
		
		for(double r = getDense(); r < lAB; r += getDense()) {
			locations.add(getA().clone().add(ab.clone().multiply(r)));
		}
		
		for(double r = getDense(); r < lAC; r += getDense()) {
			locations.add(getA().clone().add(ac.clone().multiply(r)));
		}
		
		for(double r = getDense(); r < lBC; r += getDense()) {
			locations.add(getB().clone().add(bc.clone().multiply(r)));
		}
	}
	
	public Location getB() {
		return points[1];
	}
	
	public Location getA() {
		return points[0];
	}
	
	public void setA(Location a) {
		points[0] = a;
	}
	
	public Location getC() {
		return points[2];
	}
	
	public void setC(Location c) {
		points[2] = c;
	}
	
	public void setB(Location b) {
		points[1] = b;
	}
	
	public Vector rotate(Vector v) {
		return RotationUtil.rotate(v, getAxis(), getAngle());
	}
}
