package de.alphahelix.alphalibary.core.utils;

import org.bukkit.util.Vector;

public class RotationUtil {
	
	public static Vector rotatePitch(Vector toRotate, double pitch) {
		return rotate(toRotate, 0, pitch);
	}
	
	public static Vector rotate(Vector toRotate, double yaw, double pitch) {
		return rotate(toRotate, yaw, pitch, 0);
	}
	
	public static Vector rotate(Vector toRotate, double yaw, double pitch, double roll) {
		Vector temp1 = rotate(toRotate, new Vector(0, 1, 0), yaw);
		Vector temp2 = rotate(temp1, new Vector(1, 0, 0), pitch);
		return rotate(temp2, new Vector(0, 0, 1), roll);
	}
	
	public static Vector rotate(Vector toRotate, Vector around, double angle) {
		if(angle == 0)
			return toRotate;

        /*
        v = around;

        x1 = x * ((vx * vx) * (1 - cos a) + cos a) + y * ((vx * vy) * (1 - cos a) - vz * sin a) + ((vx * vz) * (1 - cos a) + vy * sin a)
        x2 = x * (())

         */
		
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
	
	public static Vector rotateYaw(Vector toRotate, double yaw) {
		return rotate(toRotate, yaw, 0);
	}
	
}
