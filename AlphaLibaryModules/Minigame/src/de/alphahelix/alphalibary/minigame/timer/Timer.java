package de.alphahelix.alphalibary.minigame.timer;


import de.alphahelix.alphalibary.core.AlphaLibary;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class Timer {
	
	private final String id;
	private long time;
	private int schedulerID = -1;
	
	public Timer(String id, long time) {
		this.id = id;
		this.time = time;
	}
	
	public void start() {
		started();
		
		schedulerID = new BukkitRunnable() {
			long currentTime = time;
			
			public void run() {
				timeReached(currentTime);
				
				if(currentTime == 0) {
					endReached();
					stop();
				}
				
				currentTime--;
			}
		}.runTaskTimer(AlphaLibary.getInstance(), 0L, 20L).getTaskId();
	}
	
	public abstract void started();
	
	public abstract void timeReached(long time);
	
	public abstract void endReached();
	
	public void stop() {
		if(schedulerID != -1) {
			Bukkit.getScheduler().cancelTask(schedulerID);
			schedulerID = -1;
		}
	}
	
	public boolean hasStarted() {
		return schedulerID != -1;
	}
}
