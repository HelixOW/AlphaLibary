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
import java.util.Arrays;
import java.util.List;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public abstract class BukkitCommand extends Command {
	
	private final ArgumentParser argumentParser = new ArgumentParser();
	
	public BukkitCommand(String command) {
		this(command, "");
	}
	
	public BukkitCommand(String command, String description) {
		this(command, "", description);
	}
	
	public BukkitCommand(String command, String usage, String description, String... aliases) {
		super(command);
		
		super.setUsage(usage);
		super.setDescription(description);
		super.setAliases(new ArrayList<>(Arrays.asList(aliases)));
		
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
