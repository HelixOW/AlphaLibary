package de.alphahelix.alphalibary.minigame.countdown;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.core.AlphaLibary;
import de.alphahelix.alphalibary.minigame.events.countdown.CountDownFinishEvent;
import de.alphahelix.alphalibary.minigame.events.countdown.CountDownStartEvent;
import de.alphahelix.alphalibary.minigame.events.countdown.CountDownTimeEvent;
import de.alphahelix.alphalibary.reflection.nms.SimpleActionBar;
import de.alphahelix.alphalibary.reflection.nms.SimpleTitle;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.Serializable;
import java.util.Arrays;

@SuppressWarnings("ALL")
public class GameCountdown implements Serializable {

    private final String name;
    private final GameCountdown instance;
    private Long[] messageTimes = null;
    private long time = 0;
    private int schedulerID = -1;
    private boolean useXP = true, useTitle = false;

    /**
     * Creates a new GameCountdown
     *
     * @param messageTimes the times where the <code>CountDownTimeEvent</code> should get triggerd
     * @param time         the lenght of the countdown
     */
    public GameCountdown(String name, Long[] messageTimes, long time) {
        this.instance = this;
        this.name = name;
        this.messageTimes = messageTimes;
        this.time = time;
    }

    /**
     * Creates a new GameCountdown
     *
     * @param messageTimes the times where the <code>CountDownTimeEvent</code> should get triggerd
     * @param time         the lenght of the countdown
     * @param useXP        if the levelbar should be updated
     * @param useTitle     if the message should be displayed inside the tablist aswell
     */
    public GameCountdown(String name, Long[] messageTimes, long time, boolean useXP, boolean useTitle) {
        this.instance = this;
        this.name = name;
        this.messageTimes = messageTimes;
        this.time = time;
        this.useXP = useXP;
        this.useTitle = useTitle;
    }

    public void start(Runnable startAction, Runnable timeReachedAction, Runnable endAction) {
        CountDownStartEvent countDownStartEvent = new CountDownStartEvent(instance, time);
        Bukkit.getPluginManager().callEvent(countDownStartEvent);

        if (countDownStartEvent.isCancelled()) return;

        startAction.run();

        schedulerID = new BukkitRunnable() {
            long currentTime = time;

            public void run() {
                if (Arrays.asList(messageTimes).contains(currentTime)) {
                    CountDownTimeEvent countDownTimeEvent = new CountDownTimeEvent(instance, currentTime);

                    timeReachedAction.run();

                    Bukkit.getPluginManager().callEvent(countDownTimeEvent);
                }

                if (currentTime == 0) {
                    CountDownFinishEvent countDownFinishEvent = new CountDownFinishEvent(instance);

                    endAction.run();

                    Bukkit.getPluginManager().callEvent(countDownFinishEvent);

                    if (!countDownFinishEvent.isCancelled())
                        stop();
                    else
                        currentTime++;
                }
                currentTime--;
            }
        }.runTaskTimer(AlphaLibary.getInstance(), 0L, 20L).getTaskId();
    }

    /**
     * Starts the countdown
     *
     * @param titleMessage the message to display
     */
    public void start(String titleMessage) {
        CountDownStartEvent countDownStartEvent = new CountDownStartEvent(instance, time);
        Bukkit.getPluginManager().callEvent(countDownStartEvent);

        if (countDownStartEvent.isCancelled()) return;

        schedulerID = new BukkitRunnable() {
            long currentTime = time;

            public void run() {
                if (Arrays.asList(messageTimes).contains(currentTime)) {
                    CountDownTimeEvent countDownTimeEvent = new CountDownTimeEvent(instance, currentTime);

                    Bukkit.getPluginManager().callEvent(countDownTimeEvent);
                }

                if (useXP) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.setLevel(Math.toIntExact(currentTime));
                    }
                }

                if (useTitle) {
                    String titleMSG = titleMessage.replace("[time]", String.valueOf(currentTime));

                    if (currentTime > 9) {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            SimpleTitle.sendTitle(p, "", titleMSG, 2, 2, 2);
                        }
                    } else {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            SimpleActionBar.send(p, titleMSG);
                        }
                    }
                }

                if (currentTime == 0) {
                    CountDownFinishEvent countDownFinishEvent = new CountDownFinishEvent(instance);
                    Bukkit.getPluginManager().callEvent(countDownFinishEvent);

                    if (!countDownFinishEvent.isCancelled())
                        stop();
                    else
                        currentTime++;
                }
                currentTime--;
            }
        }.runTaskTimer(AlphaLibary.getInstance(), 0L, 20L).getTaskId();
    }

    /**
     * Stop the countdown
     */
    public void stop() {
        if (schedulerID != -1) {
            Bukkit.getScheduler().cancelTask(schedulerID);
            schedulerID = -1;
        }
    }

    public boolean hasStarted() {
        return schedulerID != -1;
    }

    public void setUseXP(boolean useXP) {
        this.useXP = useXP;
    }

    public void setUseTitle(boolean useTitle) {
        this.useTitle = useTitle;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameCountdown that = (GameCountdown) o;
        return time == that.time &&
                schedulerID == that.schedulerID &&
                useXP == that.useXP &&
                useTitle == that.useTitle &&
                Objects.equal(getName(), that.getName()) &&
                Objects.equal(instance, that.instance) &&
                Objects.equal(messageTimes, that.messageTimes);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName(), instance, messageTimes, time, schedulerID, useXP, useTitle);
    }

    @Override
    public String toString() {
        return "GameCountdown{" +
                "name='" + name + '\'' +
                ", instance=" + instance +
                ", messageTimes=" + Arrays.toString(messageTimes) +
                ", time=" + time +
                ", schedulerID=" + schedulerID +
                ", useXP=" + useXP +
                ", useTitle=" + useTitle +
                '}';
    }
}

