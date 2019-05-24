package io.github.alphahelixdev.alpary.game;

import io.github.alphahelixdev.alpary.Alpary;
import io.github.alphahelixdev.alpary.game.events.countdown.CountDownFinishEvent;
import io.github.alphahelixdev.alpary.game.events.countdown.CountDownStartEvent;
import io.github.alphahelixdev.alpary.game.events.countdown.CountDownTimeEvent;
import lombok.*;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class GameCountdown {
	
	private final String name;
	@NonNull
	private long[] messageTimes;
	@NonNull
	private long time;
	private int schedulerID = -1;
	
	public void start(Runnable preStart, Consumer<Long> timeReached, Runnable end) {
		CountDownStartEvent cdse = new CountDownStartEvent(this);
		Bukkit.getPluginManager().callEvent(cdse);
		
		if (cdse.isCancelled())
			return;
		
		preStart.run();
		
		schedulerID = new BukkitRunnable() {
			long currentTime = getTime();
			
			@Override
			public void run() {
				if (getMessageTimes().contains(currentTime)) {
					CountDownTimeEvent cdte = new CountDownTimeEvent(GameCountdown.this, currentTime);
					
					timeReached.accept(currentTime);
					
					Bukkit.getPluginManager().callEvent(cdte);
				}
				
				if (currentTime == 0) {
					CountDownFinishEvent cdfe = new CountDownFinishEvent(GameCountdown.this);
					
					end.run();
					
					Bukkit.getPluginManager().callEvent(cdfe);
					
					if (!cdfe.isCancelled())
						stop();
					else
						currentTime++;
				}
				currentTime--;
			}
		}.runTaskTimer(Alpary.getInstance(), 0L, 20L).getTaskId();
	}
	
	public void stop() {
		if(getSchedulerID() != -1) {
			Bukkit.getScheduler().cancelTask(getSchedulerID());
			schedulerID = -1;
		}
	}
	
	public boolean hasStarted() {
		return getSchedulerID() != -1;
	}
	
	public List<Long> getMessageTimes() {
		return Arrays.stream(messageTimes).boxed().collect(Collectors.toList());
	}
}
