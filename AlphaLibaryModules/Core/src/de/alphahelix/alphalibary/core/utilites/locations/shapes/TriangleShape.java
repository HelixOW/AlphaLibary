package de.alphahelix.alphalibary.core.utilites.locations.shapes;

import de.alphahelix.alphalibary.core.utilites.locations.Shape;
import org.bukkit.util.Vector;


public class TriangleShape implements Shape {
	
	@Override
	public double area(Vector... points) {
		Vector aC = points[2].subtract(points[0]);
		Vector aB = points[1].subtract(points[0]);
		
		double theta = aC.angle(aB);
		
		return (1 / 2) * aC.length() * aB.length() * Math.sin(theta);
	}
}
