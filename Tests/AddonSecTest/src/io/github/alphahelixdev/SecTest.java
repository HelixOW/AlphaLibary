package io.github.alphahelixdev;

import io.github.alphahelixdev.alpary.addons.CrossAddonStorage;
import io.github.alphahelixdev.alpary.addons.core.Addon;

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
		CrossAddonStorage.getVariables().put("a", "Du kannst nicht wissen, was hier steht");
	}
}
