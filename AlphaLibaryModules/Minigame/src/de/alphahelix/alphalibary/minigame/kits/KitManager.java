package de.alphahelix.alphalibary.minigame.kits;

import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;

public class KitManager {
	
	private static final Map<String, Kit> KITS = new WeakHashMap<>();
	
	public static Kit addKit(Kit kit) {
		KITS.put(kit.getRawName(), kit);
		return kit;
	}
	
	public static Kit removeKit(Kit kit) {
		KITS.remove(kit.getRawName());
		return kit;
	}
	
	public static Optional<Kit> getKitByName(String rawName) {
		return Optional.ofNullable(KITS.get(rawName));
	}
}
