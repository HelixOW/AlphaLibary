package io.github.alphahelixdev.alpary.fake;

import io.github.alphahelixdev.alpary.utils.NMSUtil;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.alphahelixdev.helius.reflection.SaveConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum FakeMobType {
	
	GUARDIAN("EntityGuardian", Utils.nms().getNMSClass("World")),
	SKELETON("EntitySkeleton", Utils.nms().getNMSClass("World")),
	ZOMBIE("EntityZombie", Utils.nms().getNMSClass("World")),
	HORSE("EntityHorse", Utils.nms().getNMSClass("World")),
	CREEPER("EntityCreeper", Utils.nms().getNMSClass("World")),
	SPIDER("EntitySpider", Utils.nms().getNMSClass("World")),
	GIANT("EntityGiantZombie", Utils.nms().getNMSClass("World")),
	SLIME("EntitySlime", Utils.nms().getNMSClass("World")),
	GHAST("EntityGhast", Utils.nms().getNMSClass("World")),
	PIGMAN("EntityPigZombie", Utils.nms().getNMSClass("World")),
	ENDERMAN("EntityEnderman", Utils.nms().getNMSClass("World")),
	CAVE_SPIDER("EntityCaveSpider", Utils.nms().getNMSClass("World")),
	SILVERFISH("EntitySilverfish", Utils.nms().getNMSClass("World")),
	BLAZE("EntityBlaze", Utils.nms().getNMSClass("World")),
	ENDER_DRAGON("EntityEnderDragon", Utils.nms().getNMSClass("World")),
	WITHER("EntityWither", Utils.nms().getNMSClass("World")),
	BAT("EntityBat", Utils.nms().getNMSClass("World")),
	WITCH("EntityWitch", Utils.nms().getNMSClass("World")),
	ENDERMITE("EntityEndermite", Utils.nms().getNMSClass("World")),
	PIG("EntityPig", Utils.nms().getNMSClass("World")),
	SHEEP("EntitySheep", Utils.nms().getNMSClass("World")),
	COW("EntityCow", Utils.nms().getNMSClass("World")),
	CHICKEN("EntityChicken", Utils.nms().getNMSClass("World")),
	SQUID("EntitySquid", Utils.nms().getNMSClass("World")),
	WOLF("EntityWolf", Utils.nms().getNMSClass("World")),
	MUSHROOM_COW("EntityMushroomCow", Utils.nms().getNMSClass("World")),
	SNOWMAN("EntitySnowman", Utils.nms().getNMSClass("World")),
	OCELOT("EntityOcelot", Utils.nms().getNMSClass("World")),
	IRON_GOLEM("EntityIronGolem", Utils.nms().getNMSClass("World")),
	RABBIT("EntityRabbit", Utils.nms().getNMSClass("World")),
	VILLAGER("EntityVillager", Utils.nms().getNMSClass("World")),
	LLAMA("EntityLlama", Utils.nms().getNMSClass("World")),
	ILLAGER("EntityIllagerWizard", Utils.nms().getNMSClass("World")),
	VINDICATOR("EntityVindicator", Utils.nms().getNMSClass("World")),
	EVOKER("EntityEvoker", Utils.nms().getNMSClass("World")),
	ILLUSIONER("EntityIllagerIllusioner", Utils.nms().getNMSClass("World")),
	PARROT("EntityParrot", Utils.nms().getNMSClass("World"));
	
	private final String nmsClass;
	private final Class<?>[] classes;
	
	FakeMobType(String nmsClass, Class<?>... classes) {
		this.nmsClass = nmsClass;
		this.classes = classes;
	}
	
	public SaveConstructor getConstructor() {
		return NMSUtil.getReflections().getDeclaredConstructor(Utils.nms().getNMSClass(this.getNmsClass()), this.getClasses());
	}
}
