package io.github.alphahelixdev.alpary.game;

import io.github.alphahelixdev.alpary.Alpary;
import io.github.alphahelixdev.alpary.game.events.countdown.CountDownFinishEvent;
import io.github.alphahelixdev.alpary.game.events.countdown.CountDownStartEvent;
import io.github.alphahelixdev.alpary.game.events.countdown.CountDownTimeEvent;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class GameCountdown {

    private final String name;
    private long[] messageTimes;
    private long time;
    private int schedulerID = -1;

    public GameCountdown(String name, long time, long... messageTimes) {
        this.name = name;
        this.messageTimes = messageTimes;
        this.time = time;
    }

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
        if (schedulerID != -1) {
            Bukkit.getScheduler().cancelTask(schedulerID);
            schedulerID = -1;
        }
    }

    public boolean hasStarted() {
        return schedulerID != -1;
    }

    public String getName() {
        return name;
    }

    public List<Long> getMessageTimes() {
        return Arrays.stream(messageTimes).boxed().collect(Collectors.toList());
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getSchedulerID() {
        return schedulerID;
    }

    public void setSchedulerID(int schedulerID) {
        this.schedulerID = schedulerID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameCountdown that = (GameCountdown) o;
        return time == that.time &&
                schedulerID == that.schedulerID &&
                Objects.equals(name, that.name) &&
                Arrays.equals(messageTimes, that.messageTimes);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, time, schedulerID);
        result = 31 * result + Arrays.hashCode(messageTimes);
        return result;
    }

    @Override
    public String toString() {
        return "GameCountdown{" +
                "name='" + name + '\'' +
                ", messageTimes=" + Arrays.toString(messageTimes) +
                ", time=" + time +
                ", schedulerID=" + schedulerID +
                '}';
    }
}
