package de.alphahelix.alphalibary.forms.actions;

import de.alphahelix.alphalibary.forms.FormAction;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class BlockAction implements FormAction {
	
	private Material toPlace;
	
	public BlockAction(Material toPlace) {
		this.toPlace = toPlace;
	}
	
	@Override
	public void action(Player p, Location loc) {
		loc.getBlock().setType(toPlace);
	}
}
