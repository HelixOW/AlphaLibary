package de.alphahelix.alphalibary.core.utils.implementations;

import de.alphahelix.alphalibary.core.utilites.DefaultFontInfo;
import de.alphahelix.alphalibary.core.utilites.UUIDFetcher;
import de.alphahelix.alphalibary.core.utils.abstracts.AbstractMessageUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class IMessageUtil extends AbstractMessageUtil {
	
	public void sendCenteredMessage(Player player, String message) {
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
		int toCompensate = AbstractMessageUtil.CENTER_PX - halvedMessageSize;
		int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
		int compensated = 0;
		StringBuilder sb = new StringBuilder();
		while(compensated < toCompensate) {
			sb.append(" ");
			compensated += spaceLength;
		}
		player.sendMessage(sb.toString().replace("&", "§") + message.replace("&", "§"));
	}
	
	@Override
	public void sendClickText(Player p, String text, String id, Consumer<Player> action) {
		UUIDFetcher.getUUID(p, playerID -> {
			new AbstractMessageUtil.ActionTextCommand(playerID, id, action);
			
			TextComponent txt = new TextComponent(text);
			
			txt.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/atcPerform_" + playerID + "_" + id));
			
			p.spigot().sendMessage(txt);
		});
	}
	
	@Override
	public void sendHoverClickText(Player p, String clickText, String hovertext, String id, Consumer<Player> clickAction) {
		UUIDFetcher.getUUID(p, playerID -> {
			new ActionTextCommand(playerID, id, clickAction);
			
			TextComponent txt = new TextComponent(clickText);
			
			txt.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/atcPerform_" + playerID + "_" + id));
			txt.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hovertext).create()));
			
			p.spigot().sendMessage(txt);
		});
	}
}
