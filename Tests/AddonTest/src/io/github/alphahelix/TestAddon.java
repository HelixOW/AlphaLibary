package io.github.alphahelix;

import io.github.alphahelixdev.alpary.addons.core.Addon;
import io.github.alphahelixdev.alpary.addons.csv.CrossSystemManager;

@Addon.AddonInfo(
		name = "Test",
		author = "Alpha"
)
public class TestAddon extends Addon {
	public TestAddon(AddonInfo info) {
		super(info);
	}
	
	@Override
	public void onEnable() {
		System.out.println(CrossSystemManager.getVariable("a"));
	}
}
