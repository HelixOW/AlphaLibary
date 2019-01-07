package io.github.alphahelixdev.alpary.fake.entities;

import com.mojang.authlib.GameProfile;
import io.github.alphahelixdev.alpary.utils.NMSUtil;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.alphahelixdev.helius.reflection.SaveConstructor;

public interface CustomSpawnable {
	
	SaveConstructor ARMORSTAND = NMSUtil.getReflections().getDeclaredConstructor(
			Utils.nms().getNMSClass("EntityArmorStand"), Utils.nms().getNMSClass("World"));
	SaveConstructor ENDERCRYSTAL = NMSUtil.getReflections().getDeclaredConstructor(
			Utils.nms().getNMSClass("EntityEnderCrystal"), Utils.nms().getNMSClass("World"));
	SaveConstructor ITEM = NMSUtil.getReflections().getDeclaredConstructor(
			Utils.nms().getNMSClass("EntityItem"), Utils.nms().getNMSClass("World"), double.class,
			double.class, double.class, Utils.nms().getNMSClass("ItemStack"));
	SaveConstructor PLAYER = NMSUtil.getReflections().getDeclaredConstructor(
			Utils.nms().getNMSClass("EntityPlayer"), Utils.nms().getNMSClass("MinecraftServer"),
			Utils.nms().getNMSClass("WorldServer"), GameProfile.class,
			Utils.nms().getNMSClass("PlayerInteractManager"));
	SaveConstructor XP_ORB = NMSUtil.getReflections().getDeclaredConstructor(
			Utils.nms().getNMSClass("EntityExperienceOrb"), Utils.nms().getNMSClass("World"));
	SaveConstructor SPAWN_XP_ORB = NMSUtil.getReflections().getDeclaredConstructor(
			Utils.nms().getNMSClass("PacketPlayOutSpawnEntityExperienceOrb"),
			Utils.nms().getNMSClass("EntityExperienceOrb"));
	
}
