package de.alphahelix.alphalibary.core.utils;

import de.alphahelix.alphalibary.core.utils.abstracts.AbstractRotationUtil;
import org.bukkit.util.Vector;

public interface RotationUtil {
	
	static Vector findPerpendicularVector(Vector from) {
		return AbstractRotationUtil.instance.findPerpendicularVector(from);
	}
	
	static Vector findPerpendicularVector2D(Vector from) {
		return AbstractRotationUtil.instance.findPerpendicularVector2D(from);
	}
	
	static Vector rotate(Vector toRotate, double yaw, double pitch) {
		return AbstractRotationUtil.instance.rotate(toRotate, yaw, pitch);
	}
	
	static Vector rotate(Vector toRotate, double yaw, double pitch, double roll) {
		return AbstractRotationUtil.instance.rotate(toRotate, yaw, pitch, roll);
	}
	
	static Vector rotate(Vector toRotate, Vector around, double angle) {
		return AbstractRotationUtil.instance.rotate(toRotate, around, angle);
	}
	
	static Vector rotateYaw(Vector toRotate, double yaw) {
		return AbstractRotationUtil.instance.rotateYaw(toRotate, yaw);
	}
	
	static Vector rotatePitch(Vector toRotate, double pitch) {
		return AbstractRotationUtil.instance.rotatePitch(toRotate, pitch);
	}
}
