package de.alphahelix.alphalibary.core.utils;

import de.alphahelix.alphalibary.core.utils.abstracts.AbstractScheduleUtil;

import java.util.List;

public interface ScheduleUtil {
	
	static <T> void cooldown(int length, final T key, final List<T> cooldownList) {
		AbstractScheduleUtil.instance.cooldown(length, key, cooldownList);
	}
	
	static void runLater(long ticks, boolean async, Runnable timer) {
		AbstractScheduleUtil.instance.runLater(ticks, async, timer);
	}
	
	static void runTimer(long wait, long ticks, boolean async, Runnable timer) {
		AbstractScheduleUtil.instance.runTimer(wait, ticks, async, timer);
	}
}
