package de.alphahelix.alphalibary.core.utils.abstracts;

import de.alphahelix.alphalibary.core.utilites.SimpleCommand;
import de.alphahelix.alphalibary.core.utilites.Utility;
import de.alphahelix.alphalibary.core.utils.implementations.IMessageUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@Utility(implementation = IMessageUtil.class)
public abstract class AbstractMessageUtil {
	
	public static final int CENTER_PX = 154;
	public static AbstractMessageUtil instance;
	
	public abstract void sendCenteredMessage(Player player, String message);
	
	public abstract void sendClickText(Player p, String text, String id, Consumer<Player> action);
	
	public abstract void sendHoverClickText(Player p, String clickText, String hovertext, String id, Consumer<Player> clickAction);
	
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
