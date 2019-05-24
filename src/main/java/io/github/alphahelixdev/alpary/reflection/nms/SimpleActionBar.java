package io.github.alphahelixdev.alpary.reflection.nms;

import lombok.*;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Collection;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class SimpleActionBar {
	
	private String message;
	
	public void send(Player p) {
		p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(this.getMessage()));
	}
	
	public void send(World w) {
		send(w.getPlayers());
	}
	
	public void send() {
		send(Bukkit.getOnlinePlayers());
	}
	
	public void send(Collection<? extends Player> players) {
		players.forEach(this::send);
	}
	
	public void clear(Player p) {
		setMessage("");
		send(p);
	}
	
	public void clear(World w) {
		clear(w.getPlayers());
	}
	
	public void clear() {
		clear(Bukkit.getOnlinePlayers());
	}
	
	public void clear(Collection<? extends Player> players) {
		players.forEach(this::clear);
	}
}
