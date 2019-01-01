package io.github.alphahelixdev;

import io.github.alphahelixdev.alpary.addons.core.Addon;
import io.github.alphahelixdev.alpary.addons.csv.CrossSystemManager;

@Addon.AddonInfo(
		name = "U suck",
		description = "ma dick",
		author = "WhoIsAlphaHelix"
)
public class SecTest extends Addon {
	public SecTest(AddonInfo info) {
		super(info);
	}
	
	@Override
	public void onEnable() {
		CrossSystemManager.getVariables().put("a", "Du kannst nicht wissen, was hier steht");
	}
}
