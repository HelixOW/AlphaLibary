package de.alphahelix.alphalibary.core.scoreboard.type;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import de.alphahelix.alphalibary.core.AlphaLibary;
import de.alphahelix.alphalibary.core.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleScoreboard implements Scoreboard {

    private static final String TEAM_PREFIX = "Scoreboard_";
    private static int teamCounter = 0;

    private final org.bukkit.scoreboard.Scoreboard scoreboard;
    private Objective objective;

    private Player holder;
    private long updateInterval = 10L;

    private boolean activated;
    private ScoreboardHandler handler;
    private Map<FakeEntry, Integer> entryCache = new ConcurrentHashMap<>();
    private Table<String, Integer, FakeEntry> playerCache = HashBasedTable.create();
    private Table<Team, String, String> teamCache = HashBasedTable.create();
    private BukkitRunnable updateTask;

    public SimpleScoreboard(String id, Player holder) {
        this.holder = holder;
        // Initiate the Bukkit scoreboard
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        scoreboard.registerNewObjective(id, "dummy").setDisplaySlot(DisplaySlot.SIDEBAR);
        objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
    }

    @Override
    public org.bukkit.scoreboard.Scoreboard activate() {
        if (activated) return scoreboard;
        if (handler == null) throw new IllegalArgumentException("Scoreboard handler not set");
        activated = true;
        // Set to the custom scoreboard
        holder.setScoreboard(scoreboard);
        objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
        // And start updating on a desired interval
        updateTask = new BukkitRunnable() {
            @Override
            public void run() {
                update();
            }
        };
        updateTask.runTaskTimer(AlphaLibary.getInstance(), 0, updateInterval);
        return scoreboard;
    }

    @Override
    public void deactivate() {
        if (!activated) return;
        activated = false;
        // Set to the main scoreboard
        if (holder.isOnline()) {
            synchronized (this) {
                holder.setScoreboard((Bukkit.getScoreboardManager().getMainScoreboard()));
            }
        }
        // Unregister teams that are created for this scoreboard
        for (Team team : scoreboard.getTeams()) {
            team.unregister();
        }
        // Stop updating
        updateTask.cancel();
    }

    @Override
    public boolean isActivated() {
        return activated;
    }

    @Override
    public ScoreboardHandler getHandler() {
        return handler;
    }

    @Override
    public Scoreboard setHandler(ScoreboardHandler handler) {
        this.handler = handler;
        return this;
    }

    @Override
    public long getUpdateInterval() {
        return updateInterval;
    }

    @Override
    public SimpleScoreboard setUpdateInterval(long updateInterval) {
        if (activated) throw new IllegalStateException("Scoreboard is already activated");
        this.updateInterval = updateInterval;
        return this;
    }

    @Override
    public Player getHolder() {
        return holder;
    }

    @SuppressWarnings("deprecation")
    private void update() {
        if (!holder.isOnline()) {
            deactivate();
            return;
        }
        // Title
        String handlerTitle = handler.getTitle(holder);
        String finalTitle = handlerTitle != null ? handlerTitle : ChatColor.BOLD.toString();
        if (!objective.getDisplayName().equals(finalTitle))
            objective.setDisplayName(ChatColor.translateAlternateColorCodes('&', finalTitle));
        // Entries
        List<Entry> passed = handler.getEntries(holder);
        Map<String, Integer> appeared = new HashMap<>();
        Map<FakeEntry, Integer> current = new HashMap<>();
        if (passed == null) return;
        for (Entry entry : passed) {
            // Handle the entry
            String key = entry.getName();
            Integer score = entry.getPosition();
            if (key.length() > 48) key = key.substring(0, 47);
            String appearance;
            if (key.length() > 16) {
                appearance = key.substring(16);
            } else {
                appearance = key;
            }
            if (!appeared.containsKey(appearance)) appeared.put(appearance, -1);
            appeared.put(appearance, appeared.get(appearance) + 1);
            // Get fake player
            FakeEntry faker = getFakePlayer(key, appeared.get(appearance));
            // Set score
            objective.getScore(faker.name).setScore(score);
            // Update references
            entryCache.put(faker, score);
            current.put(faker, score);
        }
        appeared.clear();
        // Remove duplicated or non-existent entries
        for (FakeEntry fakeEntry : entryCache.keySet()) {
            if (!current.containsKey(fakeEntry)) {
                entryCache.remove(fakeEntry);
                scoreboard.resetScores(fakeEntry.getName());
            }
        }
    }

    @SuppressWarnings("deprecation")
    private FakeEntry getFakePlayer(String text, int offset) {
        Team team = null;
        String name;
        // If the text has a length less than 16, teams need not to be be created
        if (text.length() <= 16) {
            name = text + Util.repeat(" ", offset);
        } else {
            String prefix;
            String suffix = "";
            offset++;
            // Otherwise, iterate through the string and cut off prefix and suffix
            prefix = text.substring(0, 16 - offset);
            name = text.substring(16 - offset);
            if (name.length() > 16) name = name.substring(0, 16);
            if (text.length() > 32) suffix = text.substring(32 - offset);

            // If teams already exist, use them
            for (Team other : scoreboard.getTeams()) {
                if (other.getPrefix().equals(prefix) && other.getSuffix().equals(suffix)) {
                    team = other;
                }
            }

            // Otherwise create them
            if (team == null) {
                team = scoreboard.registerNewTeam(TEAM_PREFIX + teamCounter++);
                team.setPrefix(prefix);
                team.setSuffix(suffix);
                teamCache.put(team, prefix, suffix);
            }
        }
        FakeEntry faker;

        if (!playerCache.contains(name, offset)) {
            faker = new FakeEntry(name, team, offset);
            playerCache.put(name, offset, faker);
            if (faker.getTeam() != null)
                faker.getTeam().addEntry(faker.name);
        } else {
            faker = playerCache.get(name, offset);
            if (team != null && faker.getTeam() != null)
                if (scoreboard.getTeams().contains(faker.getTeam()))
                    faker.getTeam().removeEntry(faker.name);
            faker.setTeam(team);
            if (faker.getTeam() != null) {
                faker.getTeam().addEntry(faker.name);
            }
        }
        return faker;
    }

    public Objective getObjective() {
        return objective;
    }

    public org.bukkit.scoreboard.Scoreboard getScoreboard() {
        return scoreboard;
    }

    private static class FakeEntry {

        private final String name;

        private Team team;
        private int offset;

        FakeEntry(String name, Team team, int offset) {
            this.name = name;
            this.team = team;
            this.offset = offset;
        }

        public Team getTeam() {
            return team;
        }

        public void setTeam(Team team) {
            this.team = team;
        }

        public int getOffset() {
            return offset;
        }

        public String getFullName() {
            if (team == null) return name;
            if (team.getSuffix() == null) return team.getPrefix() + name;
            return team.getPrefix() + name + team.getSuffix();
        }

        public String getName() {
            return name;
        }
    }
}