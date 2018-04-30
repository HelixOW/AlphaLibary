package de.alphahelix.alphalibary.fakeapi2.instances;

import com.mojang.authlib.GameProfile;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;

public interface CustomSpawnable {
	
	ReflectionUtil.SaveConstructor ARMORSTAND = ReflectionUtil.getDeclaredConstructor("EntityArmorStand", ReflectionUtil.getNmsClass("World"));
	ReflectionUtil.SaveConstructor ENDERCRYSTAL = ReflectionUtil.getDeclaredConstructor("EntityEnderCrystal", ReflectionUtil.getNmsClass("World"));
	ReflectionUtil.SaveConstructor ITEM = ReflectionUtil.getDeclaredConstructor("EntityItem", ReflectionUtil.getNmsClass("World"), double.class, double.class, double.class, ReflectionUtil.getNmsClass("ItemStack"));
	ReflectionUtil.SaveConstructor PLAYER = ReflectionUtil.getDeclaredConstructor("EntityPlayer", ReflectionUtil.getNmsClass("MinecraftServer"), ReflectionUtil.getNmsClass("WorldServer"), GameProfile.class, ReflectionUtil.getNmsClass("PlayerInteractManager"));
	ReflectionUtil.SaveConstructor XP_ORB =
			ReflectionUtil.getDeclaredConstructor("EntityExperienceOrb", ReflectionUtil.getNmsClass("World"));
	
	ReflectionUtil.SaveConstructor SPAWN_XP_ORB =
			ReflectionUtil.getDeclaredConstructor("PacketPlayOutSpawnEntityExperienceOrb", ReflectionUtil.getNmsClass("EntityExperienceOrb"));
	
	
}
