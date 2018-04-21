package de.alphahelix.alphalibary.forms.d2.triangles;

import de.alphahelix.alphalibary.core.utils.RotationUtil;
import de.alphahelix.alphalibary.forms.FormAction;
import de.alphahelix.alphalibary.forms.d2.TriangleForm;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.List;

public class RightTriangleForm extends TriangleForm {
	
	private double length, height;
	
	public RightTriangleForm(Location location, Vector axis, double dense, double angle, FormAction action, Vector direction, double length, double height) {
		super(location, axis, dense, angle, action, direction);
		this.length = length;
		this.height = height;
		apply();
	}
	
	@Override
	public void calculate(List<Location> locations) {
		Vector pA = RotationUtil.rotate(getPerp().clone().multiply(length / 2), getAxis(), getAngle());
		Vector pB = RotationUtil.rotate(getPerp().clone().multiply(length / 2).multiply(-1), getAxis(), getAngle());
		Vector pC = RotationUtil.rotate(pA.clone().add(getDirection().clone().multiply(height)), getAxis(), getAngle());
		
		setA(getLocation().clone().add(pA));
		setB(getLocation().clone().add(pB));
		setC(getLocation().clone().add(pC));
		
		drawLines(locations);
	}
}
