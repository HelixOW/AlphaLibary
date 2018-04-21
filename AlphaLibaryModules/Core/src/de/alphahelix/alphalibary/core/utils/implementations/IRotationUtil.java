package de.alphahelix.alphalibary.core.utils.implementations;

import de.alphahelix.alphalibary.core.utils.abstracts.AbstractRotationUtil;
import org.bukkit.util.Vector;

public class IRotationUtil extends AbstractRotationUtil {
	
	public Vector findPerpendicularVector(Vector from) {
		return from.getCrossProduct(new Vector(1, 0, 0));
	}
	
	@Override
	public Vector findPerpendicularVector2D(Vector from) {
		return findPerpendicularVector(from.clone().setY(0));
	}
	
	public Vector rotate(Vector toRotate, double yaw, double pitch) {
		return rotate(toRotate, yaw, pitch, 0);
	}
	
	public Vector rotate(Vector toRotate, double yaw, double pitch, double roll) {
		Vector temp1 = rotate(toRotate, new Vector(0, 1, 0), yaw);
		Vector temp2 = rotate(temp1, new Vector(1, 0, 0), pitch);
		return rotate(temp2, new Vector(0, 0, 1), roll);
	}
	
	public Vector rotate(Vector toRotate, Vector around, double angle) {
		if(around == null || angle == 0)
			return toRotate;
		
		double vx = around.getX(), vy = around.getY(), vz = around.getZ();
		double x = toRotate.getX(), y = toRotate.getY(), z = toRotate.getZ();
		double sinA = Math.sin(Math.toRadians(angle)), cosA = Math.cos(Math.toRadians(angle));
		
		double x1, y1, z1;
		
		if(angle < 0) {
			x1 = x * ((vx * vx) * (1 - cosA) + cosA) - y * ((vx * vy) * (1 - cosA) - vz * sinA) - z * ((vx * vz) * (1 - cosA) + vy * sinA);
			y1 = x * ((vy * vx) * (1 - cosA) + vz * sinA) - y * ((vy * vy) * (1 - cosA) + cosA) - z * ((vy * vz) * (1 - cosA) - vx * sinA);
			z1 = x * ((vz * vx) * (1 - cosA) - vy * sinA) - y * ((vz * vy) * (1 - cosA) + vx * sinA) - z * ((vz * vz) * (1 - cosA) + cosA);
		} else {
			x1 = x * ((vx * vx) * (1 - cosA) + cosA) + y * ((vx * vy) * (1 - cosA) - vz * sinA) + z * ((vx * vz) * (1 - cosA) + vy * sinA);
			y1 = x * ((vy * vx) * (1 - cosA) + vz * sinA) + y * ((vy * vy) * (1 - cosA) + cosA) + z * ((vy * vz) * (1 - cosA) - vx * sinA);
			z1 = x * ((vz * vx) * (1 - cosA) - vy * sinA) + y * ((vz * vy) * (1 - cosA) + vx * sinA) + z * ((vz * vz) * (1 - cosA) + cosA);
		}
		
		return new Vector(x1, y1, z1);
	}
	
	public Vector rotateYaw(Vector toRotate, double yaw) {
		return rotate(toRotate, yaw, 0);
	}
	
	
	public Vector rotatePitch(Vector toRotate, double pitch) {
		return rotate(toRotate, 0, pitch);
	}
}
