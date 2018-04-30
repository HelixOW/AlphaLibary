package de.alphahelix.alphalibary.core.utils;

import de.alphahelix.alphalibary.core.utilites.DefaultFontInfo;
import de.alphahelix.alphalibary.core.utilites.SimpleCommand;
import de.alphahelix.alphalibary.core.utilites.UUIDFetcher;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class MessageUtil {
	
	public static final int CENTER_PX = 154;
	
	public static void sendCenteredMessage(Player player, String message) {
		if(message == null || message.equals("")) return;
		
		int messagePxSize = 0;
		boolean previousCode = false;
		boolean isBold = false;
		
		for(char c : message.toCharArray()) {
			if(c == '&') {
				previousCode = true;
			} else if(previousCode) {
				previousCode = false;
				isBold = c == 'l' || c == 'L';
			} else {
				DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
				messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
				messagePxSize++;
			}
		}
		
		int halvedMessageSize = messagePxSize / 2;
		int toCompensate = CENTER_PX - halvedMessageSize;
		int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
		int compensated = 0;
		StringBuilder sb = new StringBuilder();
		while(compensated < toCompensate) {
			sb.append(" ");
			compensated += spaceLength;
		}
		player.sendMessage(sb.toString().replace("&", "§") + message.replace("&", "§"));
	}
	
	public static void sendClickText(Player p, String text, String id, Consumer<Player> action) {
		UUIDFetcher.getUUID(p, playerID -> {
			new ActionTextCommand(playerID, id, action);
			
			TextComponent txt = new TextComponent(text);
			
			txt.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/atcPerform_" + playerID + "_" + id));
			
			p.spigot().sendMessage(txt);
		});
	}
	
	public void sendHoverClickText(Player p, String clickText, String hovertext, String id, Consumer<Player> clickAction) {
		UUIDFetcher.getUUID(p, playerID -> {
			new ActionTextCommand(playerID, id, clickAction);
			
			TextComponent txt = new TextComponent(clickText);
			
			txt.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/atcPerform_" + playerID + "_" + id));
			txt.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hovertext).create()));
			
			p.spigot().sendMessage(txt);
		});
	}
	
	public static class ActionTextCommand extends SimpleCommand {
		
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
		
		@Override
		public List<String> tabComplete(CommandSender cs, String label, String[] args) {
			return null;
		}
	}
}
