package de.alphahelix.alphalibary.forms.d3;

import de.alphahelix.alphalibary.forms.Form;
import de.alphahelix.alphalibary.forms.d2.RectangleForm;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;


public class CuboidForm extends Form {
	
	private RectangleForm rectangleForm;
	private double width;
	
	public CuboidForm(RectangleForm rectangleForm, double width) {
		super(rectangleForm.getLocation(), rectangleForm.getAxis(), rectangleForm.getDense(), rectangleForm.getAngle(), rectangleForm.getAction());
		this.width = width;
		this.rectangleForm = rectangleForm;
		apply();
	}
	
	@Override
	public void send(Player p) {
		new RectangleForm(getLocation(), getAxis(), getDense(), getAngle(), getRectangleForm().getLenght(), getRectangleForm().getHeight(), getRectangleForm().isFilled(), getAction()).send(p);
		new RectangleForm(getLocation().add(0, 0, getWidth()), getAxis(), getDense(), getAngle(), getRectangleForm().getLenght(), getRectangleForm().getHeight(), getRectangleForm().isFilled(), getAction()).send(p);
		
		for(double w = getDense(); w < (getWidth() - getDense()); w += getDense())
			new RectangleForm(getLocation().add(0, 0, w), getAxis(), getDense(), getAngle(), getRectangleForm().getLenght(), getRectangleForm().getHeight(), false, getAction()).send(p);
	}
	
	@Override
	public void calculate(List<Location> locations) {
	
	}
	
	public RectangleForm getRectangleForm() {
		return rectangleForm;
	}
	
	public CuboidForm setRectangleForm(RectangleForm rectangleForm) {
		this.rectangleForm = rectangleForm;
		return this;
	}
	
	public double getWidth() {
		return width;
	}
	
	public CuboidForm setWidth(double width) {
		this.width = width;
		return this;
	}
}
