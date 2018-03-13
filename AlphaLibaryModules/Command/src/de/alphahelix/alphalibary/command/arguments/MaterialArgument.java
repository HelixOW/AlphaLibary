package de.alphahelix.alphalibary.command.arguments;

import org.bukkit.Material;

public class MaterialArgument extends Argument<Material> {
	@Override
	public boolean matches() {
		try {
			Material.valueOf(getEnteredArgument());
			return true;
		} catch(IllegalArgumentException e) {
			return false;
		}
	}
	
	@Override
	public Material fromArgument() {
		if(matches())
			return Material.getMaterial(getEnteredArgument());
		return Material.AIR;
	}
}
