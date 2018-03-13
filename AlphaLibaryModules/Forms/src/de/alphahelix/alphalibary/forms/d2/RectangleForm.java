package de.alphahelix.alphalibary.forms.d2;

import de.alphahelix.alphalibary.core.utils.RotationUtil;
import de.alphahelix.alphalibary.forms.Form;
import de.alphahelix.alphalibary.forms.FormAction;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class RectangleForm extends Form {
	
	private double lenght, height;
	private boolean filled;
	
	public RectangleForm(Location location, Vector axis, double dense, double angle, double lenght, double height, boolean filled, FormAction action) {
		super(location, axis, dense, angle, action);
		
		this.lenght = lenght;
		this.height = height;
		this.filled = filled;
	}
	
	public double getLenght() {
		return lenght;
	}
	
	public RectangleForm setLenght(double lenght) {
		this.lenght = lenght;
		return this;
	}
	
	public double getHeight() {
		return height;
	}
	
	public RectangleForm setHeight(double height) {
		this.height = height;
		return this;
	}
	
	public boolean isFilled() {
		return filled;
	}
	
	public RectangleForm setFilled(boolean filled) {
		this.filled = filled;
		return this;
	}
	
	@Override
	public void send(Player p) {
		if(!filled) {
			for(double x = 0; x < (height); x += getDense()) {
				Vector a = new Vector(x, 0, 0);
				Vector b = new Vector(x, height, 0);
				
				getAction().action(p, getLocation().add(RotationUtil.rotate(a, getAxis(), getAngle())));
				getAction().action(p, getLocation().add(RotationUtil.rotate(b, getAxis(), getAngle())));
			}
			
			for(double y = 0; y < (lenght); y += getDense()) {
				Vector a = new Vector(0, y, 0);
				Vector b = new Vector(lenght, y, 0);
				
				getAction().action(p, getLocation().add(RotationUtil.rotate(a, getAxis(), getAngle())));
				getAction().action(p, getLocation().add(RotationUtil.rotate(b, getAxis(), getAngle())));
			}
		} else {
			for(double x = 0; x < lenght; x += getDense()) {
				for(double y = 0; y < height; y += getDense()) {
					Vector v = new Vector(x, y, 0);
					
					getAction().action(p, getLocation().add(RotationUtil.rotate(v, getAxis(), getAngle())));
				}
			}
		}
		
		
		//        if (getAxis().equalsIgnoreCase("x")) {
		//            if (!filled) {
		//                for (double x = 0; x < (lenght); x += getDense()) {
		//                    getAction().action(p, getLocation().clone().add(x, 0, 0));
		//                    getAction().action(p, getLocation().clone().add(x, height, 0));
		//                }
		//
		//                for (double y = 0; y < (lenght); y += getDense()) {
		//                    getAction().action(p, getLocation().clone().add(0, y, 0));
		//                    getAction().action(p, getLocation().clone().add(lenght, y, 0));
		//                }
		//            } else {
		//                for (double x = 0; x < lenght; x += 0.1) {
		//                    for (double y = 0; y < height; y += 0.1) {
		//                        getAction().action(p, getLocation().clone().add(x, y, 0));
		//                    }
		//                }
		//            }
		//        } else if (getAxis().equalsIgnoreCase("z")) {
		//            if (!filled) {
		//                for (double z = 0; z < (lenght); z += getDense()) {
		//                    getAction().action(p, getLocation().clone().add(0, 0, z));
		//                    getAction().action(p, getLocation().clone().add(0, height, z));
		//                }
		//
		//                for (double y = 0; y < (lenght); y += getDense()) {
		//                    getAction().action(p, getLocation().clone().add(0, y, 0));
		//                    getAction().action(p, getLocation().clone().add(0, y, lenght));
		//                }
		//            } else {
		//                for (double z = 0; z < lenght; z += 0.1) {
		//                    for (double y = 0; y < height; y += 0.1) {
		//                        getAction().action(p, getLocation().clone().add(0, y, z));
		//                    }
		//                }
		//            }
		//        } else if (getAxis().equalsIgnoreCase("xz") || getAxis().equalsIgnoreCase("zx")) {
		//            if (!filled) {
		//                for (double xz = 0; xz < (lenght); xz += getDense()) {
		//                    getAction().action(p, getLocation().clone().add(xz, 0, xz));
		//                    getAction().action(p, getLocation().clone().add(xz, height, xz));
		//                }
		//
		//                for (double y = 0; y < (lenght); y += getDense()) {
		//                    getAction().action(p, getLocation().clone().add(0, y, 0));
		//                    getAction().action(p, getLocation().clone().add(lenght / 2, y, lenght / 2));
		//                }
		//            } else {
		//                for (double xz = 0; xz < lenght; xz += 0.1) {
		//                    for (double y = 0; y < height; y += 0.1) {
		//                        getAction().action(p, getLocation().clone().add(xz, y, xz));
		//                    }
		//                }
		//            }
		//        } else if (getAxis().equalsIgnoreCase("y")) {
		//            if (!filled) {
		//                for (double x = 0; x < (lenght); x += getDense()) {
		//                    getAction().action(p, getLocation().clone().add(x, 0, 0));
		//                    getAction().action(p, getLocation().clone().add(0, 0, height));
		//                }
		//
		//                for (double z = 0; z < (lenght); z += getDense()) {
		//                    getAction().action(p, getLocation().clone().add(0, 0, z));
		//                    getAction().action(p, getLocation().clone().add(lenght, 0, z));
		//                }
		//            } else {
		//                for (double z = 0; z < lenght; z += 0.1) {
		//                    for (double x = 0; x < height; x += 0.1) {
		//                        getAction().action(p, getLocation().clone().add(z, 0, x));
		//                    }
		//                }
		//            }
		//        } else if (getAxis().equalsIgnoreCase("xzy") || getAxis().equalsIgnoreCase("yzx") || getAxis().equalsIgnoreCase("zyx") || getAxis().equalsIgnoreCase("zxy") ||
		//                getAxis().equalsIgnoreCase("xyz") || getAxis().equalsIgnoreCase("yxz")) {
		//
		//            if (!filled) {
		//                for (double y = 0; y < (lenght); y += getDense()) {
		//                    getAction().action(p, getLocation().clone().add(0, y, 0));
		//                    getAction().action(p, getLocation().clone().add(height / 2, 0, height / 2));
		//                }
		//
		//                for (double xz = 0; xz < (lenght); xz += getDense()) {
		//                    getAction().action(p, getLocation().clone().add(xz, 0, xz));
		//                    getAction().action(p, getLocation().clone().add(xz, lenght, xz));
		//                }
		//            } else {
		//                for (double xz = 0; xz < lenght; xz += 0.1) {
		//                    for (double y = 0; y < height; y += 0.1) {
		//                        getAction().action(p, getLocation().clone().add(y, xz, y));
		//                    }
		//                }
		//            }
		//        }
	}
}
