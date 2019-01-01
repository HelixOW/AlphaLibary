package annotations.item;

import io.github.alphahelixdev.alpary.addons.csv.CrossSystemManager;

public class ItemCreator {
	
	public static void main(String[] args) {
		CrossSystemManager.getVariables().put("abc", "xyz");
		
		System.out.println(CrossSystemManager.getVariable("abc"));
	}
	
}
