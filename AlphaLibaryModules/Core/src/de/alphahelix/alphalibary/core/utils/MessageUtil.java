package de.alphahelix.alphalibary.core.utils;

import de.alphahelix.alphalibary.core.utils.abstracts.AbstractMessageUtil;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public interface MessageUtil {
	
	static void sendCenteredMessage(Player player, String message) {
		AbstractMessageUtil.instance.sendCenteredMessage(player, message);
	}
	
	static void sendClickText(Player p, String text, String id, Consumer<Player> action) {
		AbstractMessageUtil.instance.sendClickText(p, text, id, action);
	}
	
	static void sendHoverClickText(Player p, String clickText, String hovertext, String id, Consumer<Player> clickAction) {
		AbstractMessageUtil.instance.sendHoverClickText(p, clickText, hovertext, id, clickAction);
	}
}
