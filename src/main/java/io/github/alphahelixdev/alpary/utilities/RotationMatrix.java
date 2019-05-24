package io.github.alphahelixdev.alpary.utilities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.bukkit.Location;

@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class RotationMatrix {
	
	private double[][] matrix;
	
	public Location toPoint(Location start) {
		double x = start.getX(), y = start.getZ(), z = start.getY();
		
		double dX = (getX()[0] * x) + (getX()[1] * y) + (getX()[2] * z);
		double dY = (getY()[0] * x) + (getY()[1] * y) + (getY()[2] * z);
		double dZ = (getZ()[0] * x) + (getZ()[1] * y) + (getZ()[2] * z);
		
		return new Location(start.getWorld(), dX, dZ, dY);
	}
	
	public double[] getX() {
		return matrix[0];
	}
	
	public double[] getY() {
		return matrix[1];
	}
	
	public double[] getZ() {
		return matrix[2];
	}
	
}