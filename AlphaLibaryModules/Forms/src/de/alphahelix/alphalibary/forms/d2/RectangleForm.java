package de.alphahelix.alphalibary.forms.d2;

import de.alphahelix.alphalibary.core.utils.RotationUtil;
import de.alphahelix.alphalibary.forms.Form;
import de.alphahelix.alphalibary.forms.FormAction;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.List;

public class RectangleForm extends Form {
	
	private double lenght, height;
	private boolean filled;
	
	public RectangleForm(Location location, Vector axis, double dense, double angle, double lenght, double height, boolean filled, FormAction action) {
		super(location, axis, dense, angle, action);
		
		this.lenght = lenght;
		this.height = height;
		this.filled = filled;
		apply();
	}
	
	public double getLenght() {
		return lenght;
	}
	
	public RectangleForm setLenght(double lenght) {
		this.lenght = lenght;
		return this;
	}
	
	public double getHeight() {
		return height;
	}
	
	public RectangleForm setHeight(double height) {
		this.height = height;
		return this;
	}
	
	public boolean isFilled() {
		return filled;
	}
	
	public RectangleForm setFilled(boolean filled) {
		this.filled = filled;
		return this;
	}
	
	@Override
	public void calculate(List<Location> locations) {
		if(!filled) {
			for(double x = 0; x < (height); x += getDense()) {
				Vector a = new Vector(x, 0, 0);
				Vector b = new Vector(x, height, 0);
				
				locations.add(getLocation().add(RotationUtil.rotate(a, getAxis(), getAngle())));
				locations.add(getLocation().add(RotationUtil.rotate(b, getAxis(), getAngle())));
			}
			
			for(double y = 0; y < (lenght); y += getDense()) {
				Vector a = new Vector(0, y, 0);
				Vector b = new Vector(lenght, y, 0);
				
				locations.add(getLocation().add(RotationUtil.rotate(a, getAxis(), getAngle())));
				locations.add(getLocation().add(RotationUtil.rotate(b, getAxis(), getAngle())));
			}
		} else {
			for(double x = 0; x < lenght; x += getDense()) {
				for(double y = 0; y < height; y += getDense()) {
					Vector v = new Vector(x, y, 0);
					
					locations.add(getLocation().add(RotationUtil.rotate(v, getAxis(), getAngle())));
				}
			}
		}
	}
}
