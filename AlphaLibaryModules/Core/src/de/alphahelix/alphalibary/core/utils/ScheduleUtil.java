package de.alphahelix.alphalibary.core.utils;

import de.alphahelix.alphalibary.core.AlphaLibary;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class ScheduleUtil {
	
	/**
	 * Creates a cooldown
	 *
	 * @param length       the lenght of the cooldown in ticks
	 * @param key          the key to add a cooldown for
	 * @param cooldownList the {@link List} where the key is in
	 */
	public static <T> void cooldown(int length, final T key, final List<T> cooldownList) {
		cooldownList.add(key);
		new BukkitRunnable() {
			public void run() {
				cooldownList.remove(key);
			}
		}.runTaskLaterAsynchronously(AlphaLibary.getInstance(), length);
	}
	
	public static void runLater(long ticks, boolean async, Runnable timer) {
		if(async)
			new BukkitRunnable() {
				public void run() {
					timer.run();
				}
			}.runTaskLaterAsynchronously(AlphaLibary.getInstance(), ticks);
		else
			new BukkitRunnable() {
				public void run() {
					timer.run();
				}
			}.runTaskLater(AlphaLibary.getInstance(), ticks);
	}
	
	public static void runTimer(long wait, long ticks, boolean async, Runnable timer) {
		if(async)
			new BukkitRunnable() {
				public void run() {
					timer.run();
				}
			}.runTaskTimerAsynchronously(AlphaLibary.getInstance(), wait, ticks);
		else
			new BukkitRunnable() {
				public void run() {
					timer.run();
				}
			}.runTaskTimer(AlphaLibary.getInstance(), wait, ticks);
	}
}
