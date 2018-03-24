package de.alphahelix.alphalibary.forms.d2.triangles;

import de.alphahelix.alphalibary.core.AlphaLibary;
import de.alphahelix.alphalibary.core.utils.LocationUtil;
import de.alphahelix.alphalibary.core.utils.RotationUtil;
import de.alphahelix.alphalibary.forms.Form;
import de.alphahelix.alphalibary.forms.FormAction;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;

public class EquilateralTriangleForm extends Form {
	
	private final Vector direction;
	private double size;
	
	public EquilateralTriangleForm(Location location, Vector axis, double dense, double angle, Vector direction, double size, FormAction action) {
		super(location, axis, dense, angle, action);
		this.direction = direction;
		this.size = size;
		apply();
	}
	
	public double getSize() {
		return size;
	}
	
	public EquilateralTriangleForm setSize(double size) {
		this.size = size;
		return this;
	}
	
	@Override
	public void calculate(List<Location> locations) {
		double height = (this.size / 2.0) * 1.732;
		
		Vector dirPerp = RotationUtil.findPerpendicularVector(getDirection());
		
		Location posA = getLocation().clone().add(dirPerp.clone().multiply(this.size / 2.0)); // Going right side for the A point
		Location posB = getLocation().clone().add(dirPerp.clone().multiply(this.size / 2.0).multiply(-1.0)); //Going left side for the B point
		Location posC = getLocation().clone().add(this.direction.clone().multiply(height));
		
		new BukkitRunnable() {
			@Override
			public void run() {
				posA.getWorld().spawnParticle(Particle.HEART, posA.clone(), 1);
				posA.getWorld().spawnParticle(Particle.HEART, posB.clone(), 1);
				posA.getWorld().spawnParticle(Particle.HEART, posC.clone(), 1);
			}
		}.runTaskTimer(AlphaLibary.getInstance(), 20, 20);
		
		
		double maxX = Math.max(posA.getX(), Math.max(posB.getX(), posC.getX()));
		double minX = Math.min(posA.getX(), Math.min(posB.getX(), posC.getX()));
		
		double maxY = Math.max(posA.getY(), Math.max(posB.getY(), posC.getY()));
		double minY = Math.min(posA.getY(), Math.min(posB.getY(), posC.getY()));
		
		double maxZ = Math.max(posA.getZ(), Math.max(posB.getZ(), posC.getZ()));
		double minZ = Math.min(posA.getZ(), Math.min(posB.getZ(), posC.getZ()));
		
		for(double x = minX; x < maxX; x += .05) {
			for(double y = minY; y < maxY; y += .05) {
				for(double z = minZ; z < maxZ; z += .05) {
					Location r = getLocation().clone();
					
					r.setX(x);
					r.setY(y);
					r.setZ(z);
					
					if(LocationUtil.isInside(r, posA, posB, posC))
						locations.add(r);
				}
			}
		}
	}
	
	public Vector getDirection() {
		return direction;
	}
}
