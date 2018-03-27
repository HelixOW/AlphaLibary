package de.alphahelix.alphalibary.core.utils.abstracts;

import de.alphahelix.alphalibary.core.utilites.Utility;
import de.alphahelix.alphalibary.core.utils.implementations.IRotationUtil;
import org.bukkit.util.Vector;

@Utility(implementation = IRotationUtil.class)
public abstract class AbstractRotationUtil {
	
	public static AbstractRotationUtil instance;
	
	public abstract Vector findPerpendicularVector(Vector from);
	
	public abstract Vector rotate(Vector toRotate, double yaw, double pitch);
	
	public abstract Vector rotate(Vector toRotate, double yaw, double pitch, double roll);
	
	public abstract Vector rotate(Vector toRotate, Vector around, double angle);
	
	public abstract Vector rotateYaw(Vector toRotate, double yaw);
	
	public abstract Vector rotatePitch(Vector toRotate, double pitch);
}
