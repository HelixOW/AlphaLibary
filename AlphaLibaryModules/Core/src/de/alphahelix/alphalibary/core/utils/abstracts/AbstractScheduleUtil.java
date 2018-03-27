package de.alphahelix.alphalibary.core.utils.abstracts;

import de.alphahelix.alphalibary.core.utilites.Utility;
import de.alphahelix.alphalibary.core.utils.implementations.IScheduleUtil;

import java.util.List;

@Utility(implementation = IScheduleUtil.class)
public abstract class AbstractScheduleUtil {
	
	public static AbstractScheduleUtil instance;
	
	/**
	 * Creates a cooldown
	 *
	 * @param length       the lenght of the cooldown in ticks
	 * @param key          the key to add a cooldown for
	 * @param cooldownList the {@link List} where the key is in
	 */
	public abstract <T> void cooldown(int length, final T key, final List<T> cooldownList);
	
	public abstract void runLater(long ticks, boolean async, Runnable timer);
	
	public abstract void runTimer(long wait, long ticks, boolean async, Runnable timer);
}
