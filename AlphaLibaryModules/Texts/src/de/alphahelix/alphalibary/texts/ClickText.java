package de.alphahelix.alphalibary.texts;

import de.alphahelix.alphalibary.command.SimpleCommand;
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

public class ClickText {

    public static void sendClickText(Player p, String text, String id, Consumer<Player> action) {
        UUIDFetcher.getUUID(p, playerID -> {
            new ActionTextCommand(playerID, id, action);

            TextComponent txt = new TextComponent(text);

            txt.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/atcPerform_" + playerID + "_" + id));

            p.spigot().sendMessage(txt);
        });
    }

    public static void sendHoverClickText(Player p, String clickText, String hovertext, String id, Consumer<Player> clickAction) {
        UUIDFetcher.getUUID(p, playerID -> {
            new ActionTextCommand(playerID, id, clickAction);

            TextComponent txt = new TextComponent(clickText);

            txt.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/atcPerform_" + playerID + "_" + id));
            txt.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hovertext).create()));

            p.spigot().sendMessage(txt);
        });
    }

    private static class ActionTextCommand extends SimpleCommand {

        private final Consumer<Player> action;

        ActionTextCommand(UUID player, String id, Consumer<Player> action) {
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
