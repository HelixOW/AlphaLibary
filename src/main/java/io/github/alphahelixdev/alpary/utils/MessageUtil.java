package io.github.alphahelixdev.alpary.utils;

import io.github.alphahelixdev.alpary.commands.BukkitCommand;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.function.Consumer;

@EqualsAndHashCode(callSuper = false)
@ToString
public class MessageUtil {
	
	public MessageUtil sendClickText(Player p, String text, String id, Consumer<Player> action) {
		p.spigot().sendMessage(createTextComponent(text, id, action, p.getUniqueId()));
		return this;
	}
	
	private TextComponent createTextComponent(String clickText, String id, Consumer<Player> clickAction, UUID playerID) {
		new ActionTextCommand(playerID, id, clickAction);
		
		TextComponent txt = new TextComponent(clickText);
		
		txt.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/atcPerform_" + playerID + "_" + id));
		
		return txt;
	}
	
	public void sendHoverClickText(Player p, String clickText, String hovertext, String id, Consumer<Player> clickAction) {
		TextComponent txt = createTextComponent(clickText, id, clickAction, p.getUniqueId());
		
		txt.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(hovertext)));
		
		p.spigot().sendMessage(txt);
	}
	
	public static class ActionTextCommand extends BukkitCommand {
		private final Consumer<Player> action;
		
		public ActionTextCommand(UUID player, String id, Consumer<Player> action) {
			super("atcPerform_" + player.toString() + "_" + id, "", "");
			this.action = action;
		}
		
		@Override
		public boolean execute(CommandSender cs, String label, String[] args) {
			action.accept((Player) cs);
			return true;
		}
	}
}