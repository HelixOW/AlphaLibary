package io.github.alphahelixdev.alpary.game;

import io.github.alphahelixdev.alpary.game.events.teams.TeamJoinEvent;
import io.github.alphahelixdev.alpary.game.events.teams.TeamLeaveEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class GameTeam {

    private static final List<GameTeam> TEAMS = new ArrayList<>();
    private final String name;
    private final ChatColor color;
    private final List<UUID> members = new ArrayList<>();
    private Location spawn;
    private int maximumPlayers;
    private boolean friendlyFire;


    public GameTeam(String name, ChatColor color, Location spawn, int maximumPlayers) {
        this(name, color, spawn, maximumPlayers, false);
    }

    public GameTeam(String name, ChatColor color, Location spawn, int maximumPlayers, boolean friendlyFire) {
        this.name = name;
        this.color = color;
        this.spawn = spawn;
        this.maximumPlayers = maximumPlayers;
        this.friendlyFire = friendlyFire;

        TEAMS.add(this);
    }

    public static GameTeam of(Player p) {
        return TEAMS.stream().filter(gameTeam -> gameTeam.hasMember(p)).findFirst().orElse(null);
    }

    public static GameTeam of(String rawName) {
        return TEAMS.stream().filter(gameTeam -> gameTeam.getRawTeamName().equals(rawName)).findFirst()
                .orElse(null);
    }

    public static GameTeam of(ChatColor color) {
        return TEAMS.stream().filter(gameTeam -> gameTeam.getColor() == color).findFirst().orElse(null);
    }

    public static GameTeam fewestMembers() {
        return TEAMS.stream().sorted((o1, o2) -> o2.getMembers().size() - o1.getMembers().size())
                .filter(gameTeam -> gameTeam.getMembers().size() < gameTeam.getMaximumPlayers()).findFirst()
                .orElse(null);
    }

    public GameTeam addMember(Player p) {
        return addMember(p, true);
    }

    public GameTeam addMember(Player p, boolean updateTab) {
        TeamJoinEvent tje = new TeamJoinEvent(p, this);
        Bukkit.getPluginManager().callEvent(tje);

        if (tje.isCancelled())
            return this;

        getMembers().add(p.getUniqueId());

        if (updateTab) {
            Scoreboard s = p.getScoreboard();

            if (s == null)
                s = Bukkit.getScoreboardManager().getMainScoreboard();

            setupScoreboard(p, s);
        }

        return this;
    }

    public GameTeam removeMember(Player p) {
        return removeMember(p, true);
    }

    public GameTeam removeMember(Player p, boolean updateTab) {
        TeamLeaveEvent tle = new TeamLeaveEvent(p, this);
        Bukkit.getPluginManager().callEvent(tle);

        if (tle.isCancelled())
            return this;

        if (getMembers().contains(p.getUniqueId())) {
            if (updateTab && p.getScoreboard() != null) {
                Scoreboard s = p.getScoreboard();

                if (s.getTeam(this.getRawTeamName()) != null)
                    s.getTeam(this.getRawTeamName()).removeEntry(p.getName());
            }

            members.remove(p.getUniqueId());
        }

        return this;
    }

    private void setupScoreboard(Player p, Scoreboard s) {
        if (s.getTeam(this.getRawTeamName()) == null) {
            Team team = s.registerNewTeam(this.getRawTeamName());

            team.addEntry(p.getName());
            team.setPrefix(this.getColor() + "");
            team.setAllowFriendlyFire(this.isFriendlyFire());
        } else
            s.getTeam(this.getRawTeamName()).addEntry(p.getName());
    }

    public String getRawTeamName() {
        return ChatColor.stripColor(this.name).toLowerCase();
    }

    public boolean hasMember(Player p) {
        return members.contains(p.getUniqueId());
    }

    public String getName() {
        return name;
    }

    public ChatColor getColor() {
        return color;
    }

    public List<UUID> getMembers() {
        return members;
    }

    public Location getSpawn() {
        return spawn;
    }

    public GameTeam setSpawn(Location spawn) {
        this.spawn = spawn;
        return this;
    }

    public int getMaximumPlayers() {
        return maximumPlayers;
    }

    public GameTeam setMaximumPlayers(int maximumPlayers) {
        this.maximumPlayers = maximumPlayers;
        return this;
    }

    public boolean isFriendlyFire() {
        return friendlyFire;
    }

    public GameTeam setFriendlyFire(boolean friendlyFire) {
        this.friendlyFire = friendlyFire;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameTeam gameTeam = (GameTeam) o;
        return maximumPlayers == gameTeam.maximumPlayers &&
                friendlyFire == gameTeam.friendlyFire &&
                Objects.equals(name, gameTeam.name) &&
                color == gameTeam.color &&
                Objects.equals(members, gameTeam.members) &&
                Objects.equals(spawn, gameTeam.spawn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color, members, spawn, maximumPlayers, friendlyFire);
    }

    @Override
    public String toString() {
        return "GameTeam{" +
                "name='" + name + '\'' +
                ", color=" + color +
                ", members=" + members +
                ", spawn=" + spawn +
                ", maximumPlayers=" + maximumPlayers +
                ", friendlyFire=" + friendlyFire +
                '}';
    }
}
