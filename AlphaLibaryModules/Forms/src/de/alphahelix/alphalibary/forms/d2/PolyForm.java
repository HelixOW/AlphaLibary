package de.alphahelix.alphalibary.forms.d2;

import de.alphahelix.alphalibary.core.utilites.Pair;
import de.alphahelix.alphalibary.forms.Form;
import de.alphahelix.alphalibary.forms.FormAction;
import org.bukkit.Location;

import java.util.List;


public class PolyForm extends Form {
	
	private Pair<Location, Location>[] points;
	
	public PolyForm(Location location, double dense, FormAction action, Pair<Location, Location>... points) {
		super(location, dense, action);
		this.points = points;
		apply();
	}
	
	@Override
	public void calculate(List<Location> locations) {
		for(int i = 0; i < getPoints().length; i++) {
			locations.addAll(new RayForm(getLocation(), getDense(), getAction(), 0, getPoints()[i].getKey(), getPoints()[i].getValue()).getToSpawn());
		}
	}
	
	public Pair<Location, Location>[] getPoints() {
		return points;
	}
	
	public PolyForm setPoints(Pair<Location, Location>[] points) {
		this.points = points;
		return this;
	}
}
