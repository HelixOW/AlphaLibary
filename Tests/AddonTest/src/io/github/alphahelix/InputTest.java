package io.github.alphahelix;

import io.github.alphahelixdev.alpary.input.InputHandler;
import io.github.alphahelixdev.alpary.input.InputListener;
import org.bukkit.entity.Player;

@InputListener
public class InputTest implements InputHandler {
	
	public InputTest() {
		System.out.println("Works!");
	}
	
	@Override
	public void handle(Player player, String input) {
	
	}
	
	@Override
	public void handle(org.bukkit.entity.Player player, org.bukkit.inventory.InventoryView inventoryView, String input) {
	
	}
}
