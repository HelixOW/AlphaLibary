package io.github.alphahelixdev.alpary.game;

import io.github.alphahelixdev.alpary.game.events.teams.TeamJoinEvent;
import io.github.alphahelixdev.alpary.game.events.teams.TeamLeaveEvent;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@ToString
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
}
