package de.alphahelix.alphalibary.forms;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Visualizer {
	
	private FormAction action;
	private double dense;
	
	public Visualizer(FormAction action) {
		this(action, .2);
	}
	
	public Visualizer(FormAction action, double dense) {
		this.action = action;
		this.dense = dense;
	}
	
	public FormAction getAction() {
		return action;
	}
	
	public Visualizer setAction(FormAction action) {
		this.action = action;
		return this;
	}
	
	public double getDense() {
		return dense;
	}
	
	public Visualizer setDense(double dense) {
		this.dense = dense;
		return this;
	}
	
	public List<Location> getVectorLocations(Player p, Vector v) {
		return getVectorLocations(p.getLocation(), v);
	}
	
	public List<Location> getVectorLocations(Location start, Vector v) {
		List<Location> locations = new ArrayList<>();
		double l = v.length();
		
		Vector draw = v.clone().normalize();
		
		for(double x = 0; x < l; x += dense) {
			locations.add(start.clone().add(draw.clone().multiply(x)));
		}
		
		return locations;
	}
	
	public void drawVector(Player p, Vector v) {
		drawVector(p, p.getLocation(), v);
	}
	
	public void drawVector(Player p, Location start, Vector v) {
		double l = v.length();
		
		Vector draw = v.clone().normalize();
		
		for(double x = 0; x < l; x += dense) {
			action.action(p, start.clone().add(draw.clone().multiply(x)));
		}
	}
	
	public void drawVector(Location start, Vector v) {
		drawVector(null, start, v);
	}
	
	public void drawAxis(Player p) {
		drawAxis(p, 1);
	}
	
	public void drawAxis(Player p, double length) {
		drawAxis(p, p.getLocation(), length);
	}
	
	public void drawAxis(Player p, Location start, double length) {
		drawVector(p, start, new Vector(length, 0, 0));
		drawVector(p, start, new Vector(0, length, 0));
		drawVector(p, start, new Vector(0, 0, length));
	}
	
	public void drawAxis(Location start, double length) {
		drawAxis(null, start, length);
	}
	
	public void drawAxis(Location start) {
		drawAxis(null, start, 1);
	}
}
