package de.alphahelix.alphalibary.countdown;

import de.alphahelix.alphalibary.AlphaLibary;
import de.alphahelix.alphalibary.events.countdown.CountDownFinishEvent;
import de.alphahelix.alphalibary.events.countdown.CountDownStartEvent;
import de.alphahelix.alphalibary.events.countdown.CountDownTimeEvent;
import de.alphahelix.alphalibary.nms.SimpleActionBar;
import de.alphahelix.alphalibary.nms.SimpleTitle;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class GameCountdown {

    private final String name;
    private GameCountdown instance;
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

    /**
     * Starts the countdown
     *
     * @param titleMessage the message to display
     */
    public void start(String titleMessage) {
        Bukkit.getPluginManager().callEvent(new CountDownStartEvent(instance, time));
        schedulerID = new BukkitRunnable() {
            long currentTime = time;

            public void run() {
                if (Arrays.asList(messageTimes).contains(currentTime)) {
                    Bukkit.getPluginManager().callEvent(new CountDownTimeEvent(instance, currentTime));
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
                    Bukkit.getPluginManager().callEvent(new CountDownFinishEvent(instance));
                    stop();
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
}

