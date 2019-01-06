package io.github.alphahelixdev.alpary.utils;

import io.github.alphahelixdev.alpary.Alpary;
import io.github.alphahelixdev.alpary.commands.SimpleCommand;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.function.Consumer;

public class MessageUtil {

	public MessageUtil sendClickText(Player p, String text, String id, Consumer<Player> action) {
        Alpary.getInstance().uuidFetcher().getUUID(p, uuid -> p.spigot().sendMessage(createTextComponent(text, id, action, uuid)));
		return this;
	}

	public void sendHoverClickText(Player p, String clickText, String hovertext, String id, Consumer<Player> clickAction) {
		Alpary.getInstance().uuidFetcher().getUUID(p, playerID -> {
            TextComponent txt = createTextComponent(clickText, id, clickAction, playerID);

            txt.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(hovertext)));

            p.spigot().sendMessage(txt);
        });
    }

    private TextComponent createTextComponent(String clickText, String id, Consumer<Player> clickAction, UUID playerID) {
        new ActionTextCommand(playerID, id, clickAction);

        TextComponent txt = new TextComponent(clickText);

        txt.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/atcPerform_" + playerID + "_" + id));

        return txt;
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
	}
}