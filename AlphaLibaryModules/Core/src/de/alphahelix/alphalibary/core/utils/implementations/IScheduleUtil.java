package de.alphahelix.alphalibary.core.utils.implementations;

import de.alphahelix.alphalibary.core.AlphaLibary;
import de.alphahelix.alphalibary.core.utils.abstracts.AbstractScheduleUtil;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class IScheduleUtil extends AbstractScheduleUtil {
	
	public <T> void cooldown(int length, final T key, final List<T> cooldownList) {
		cooldownList.add(key);
		new BukkitRunnable() {
			public void run() {
				cooldownList.remove(key);
			}
		}.runTaskLaterAsynchronously(AlphaLibary.getInstance(), length);
	}
	
	public void runLater(long ticks, boolean async, Runnable timer) {
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
	
	public void runTimer(long wait, long ticks, boolean async, Runnable timer) {
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
