package de.alphahelix.alphalibary.forms;

import de.alphahelix.alphalibary.core.AlphaLibary;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public abstract class Form implements Serializable, Listener {
	
	private double dense, angle;
	private boolean filled;
	private Location location;
	private Vector axis;
	private Visualizer visualizer;
	private FormAction action;
	private List<Location> toSpawn;
	
	public Form(Location location, double dense, FormAction action) {
		this(location, null, dense, 0, false, action);
	}
	
	public Form(Location location, Vector axis, double dense, double angle, boolean filled, FormAction action) {
		Bukkit.getPluginManager().registerEvents(this, AlphaLibary.getInstance());
		this.action = action;
		this.location = location;
		this.axis = axis == null ? new Vector(0, 0, 0) : axis;
		this.dense = dense;
		this.angle = angle;
		this.filled = filled;
		visualizer = new Visualizer(action, dense);
	}
	
	public Form(Location location, double dense, boolean filled, FormAction action) {
		this(location, null, dense, 0, filled, action);
	}
	
	public Form(Location location, Vector axis, double dense, double angle, FormAction action) {
		this(location, axis, dense, angle, false, action);
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
	
	public void send(Player p) {
		for(Location loc : getToSpawn()) {
			getAction().action(p, loc);
		}
	}
	
	public List<Location> getToSpawn() {
		return toSpawn;
	}
	
	public FormAction getAction() {
		return action;
	}
	
	public Form setAction(FormAction formAction) {
		this.action = formAction;
		return this;
	}
	
	public void apply() {
		List<Location> locations = new ArrayList<>();
		
		calculate(locations);
		
		this.toSpawn = locations;
	}
	
	public abstract void calculate(List<Location> locations);
	
	public Visualizer getVisualizer() {
		return visualizer;
	}
	
	public Form setVisualizer(Visualizer visualizer) {
		this.visualizer = visualizer;
		return this;
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
	
	public Form setAxis(Vector axis) {
		this.axis = axis;
		return this;
	}
	
	public double getDense() {
		return dense;
	}
	
	public Form setDense(double dense) {
		this.dense = dense;
		return this;
	}
	
	public boolean isFilled() {
		return filled;
	}
	
	public Form setFilled(boolean filled) {
		this.filled = filled;
		return this;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(action, location, axis, dense, angle, toSpawn, visualizer);
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		Form form = (Form) o;
		return Double.compare(form.dense, dense) == 0 &&
				Double.compare(form.angle, angle) == 0 &&
				Objects.equals(action, form.action) &&
				Objects.equals(location, form.location) &&
				Objects.equals(axis, form.axis) &&
				Objects.equals(toSpawn, form.toSpawn) &&
				Objects.equals(visualizer, form.visualizer);
	}
	
	@Override
	public String toString() {
		return "Form{" +
				"action=" + action +
				", location=" + location +
				", axis=" + axis +
				", dense=" + dense +
				", angle=" + angle +
				", toSpawn=" + toSpawn +
				", visualizer=" + visualizer +
				'}';
	}
}
