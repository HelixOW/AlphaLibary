package de.alphahelix.alphalibary.forms;

import com.google.common.base.Objects;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public abstract class Form implements Serializable {
	
	private FormFunction[] formFunctions;
	private FormAction formAction;
	private Location location;
	private Vector axis;
	private double dense, angle;
	private List<Location> toSpawn;
	
	public Form(Location location, Vector axis, double dense, double angle, FormAction action, FormFunction... formFunctions) {
		this.formFunctions = formFunctions;
		this.formAction = action;
		this.location = location;
		this.axis = axis;
		this.dense = dense;
		this.angle = angle;
	}
	
	public Form setParticleFunctions(FormFunction... functions) {
		this.formFunctions = functions;
		return this;
	}
	
	public Form setAxis(double x, double y, double z) {
		this.axis = new Vector(x, y, z);
		return this;
	}
	
	public double getAngle() {
		return angle;
	}
	
	public Form setAngle(double angle) {
		this.angle = angle;
		return this;
	}
	
	public FormAction getAction() {
		return formAction;
	}
	
	public Form setAction(FormAction formAction) {
		this.formAction = formAction;
		return this;
	}
	
	public void send(Player p) {
		for(Location loc : getToSpawn()) {
			getAction().action(p, loc);
		}
	}
	
	public List<Location> getToSpawn() {
		return toSpawn;
	}
	
	public void apply() {
		List<Location> locations = new ArrayList<>();
		
		calculate(locations);
		
		this.toSpawn = locations;
	}
	
	public abstract void calculate(List<Location> locations);
	
	@Override
	public int hashCode() {
		return Objects.hashCode(getFormFunctions(), getLocation(), getAxis(), getDense());
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		Form form = (Form) o;
		return Double.compare(form.getDense(), getDense()) == 0 &&
				Objects.equal(getFormFunctions(), form.getFormFunctions()) &&
				Objects.equal(getLocation(), form.getLocation()) &&
				Objects.equal(getAxis(), form.getAxis());
	}
	
	@Override
	public String toString() {
		return "Form{" +
				"formFunctions=" + Arrays.toString(formFunctions) +
				", location=" + location +
				", axis='" + axis + '\'' +
				", dense=" + dense +
				'}';
	}
	
	public FormFunction[] getFormFunctions() {
		return formFunctions;
	}
	
	public Location getLocation() {
		return location.clone();
	}
	
	public Form setLocation(Location location) {
		this.location = location;
		return this;
	}
	
	public Vector getAxis() {
		return axis;
	}
	
	public double getDense() {
		return dense;
	}
	
	public Form setDense(double dense) {
		this.dense = dense;
		return this;
	}
	
	public Form setAxis(Vector axis) {
		this.axis = axis;
		return this;
	}
}
