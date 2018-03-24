package de.alphahelix.alphalibary.forms.d2;

import de.alphahelix.alphalibary.core.utilites.Pair;
import de.alphahelix.alphalibary.forms.Form;
import de.alphahelix.alphalibary.forms.FormAction;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;


public class PolyForm extends Form {
	
	private Pair<Vector, Vector>[] points;
	
	@SafeVarargs
	public PolyForm(Location location, double dense, FormAction action, Pair<Vector, Vector>... points) {
		super(location, new Vector(0, 0, 0), dense, 0, action);
		this.points = points;
		apply();
	}
	
	@Override
	public void send(Player p) {
		for(int i = 0; i < points.length; i++)
			new PointForm(getLocation(), getDense(), getAction(), getPoints()[i].getKey(), getPoints()[i].getValue()).send(p);
	}
	
	@Override
	public void calculate(List<Location> locations) {
	}
	
	public Pair<Vector, Vector>[] getPoints() {
		return points;
	}
	
	@SafeVarargs
	public final PolyForm setPoints(Pair<Vector, Vector>... points) {
		this.points = points;
		return this;
	}
}
