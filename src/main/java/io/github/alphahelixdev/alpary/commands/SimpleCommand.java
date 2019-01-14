package io.github.alphahelixdev.alpary.commands;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public abstract class SimpleCommand extends Command {
	
	private final ArgumentParser argumentParser = new ArgumentParser();
	
	public SimpleCommand(String command) {
		this(command, "");
	}
	
	public SimpleCommand(String command, String description) {
		this(command, description, new String[]{});
	}
	
	public SimpleCommand(String command, String description, String... aliases) {
		super(command);
		
		super.setDescription(description);
		List<String> aliasList = new ArrayList<>();
		Collections.addAll(aliasList, aliases);
		super.setAliases(aliasList);
		
		this.register();
	}
	
	private void register() {
		try {
			Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
			f.setAccessible(true);
			
			CommandMap map = (CommandMap) f.get(Bukkit.getServer());
			map.register("Alpary", this);
		} catch(Exception exc) {
			exc.printStackTrace();
		}
	}
	
	@Override
	public abstract boolean execute(CommandSender cs, String label, String[] args);
	
	@Override
	public List<String> tabComplete(CommandSender cs, String label, String[] args) {
		return new ArrayList<>();
	}
	
	public String buildString(String[] args, int start) {
		return buildString(args, start, args.length);
	}
	
	public String buildString(String[] args, int start, int end) {
		StringBuilder str = new StringBuilder();
		if(args.length > start) {
			str.append(args[start]);
			for(int i = start + 1; i < end; i++) {
				str.append(" ").append(args[i]);
			}
		}
		return str.toString();
	}
}
