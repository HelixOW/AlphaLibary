package io.github.alphahelixdev.alpary.commands;

import io.github.alphahelixdev.alpary.annotations.command.CommandObject;
import org.bukkit.command.CommandSender;

import java.util.*;

public class ArgumentParser {
	
	private final List<CommandObject<?>> commandObjects = new ArrayList<>(Arrays.asList(
			(CommandObject<String>) commandString -> commandString,
			(CommandObject<Boolean>) commandString -> {
				List<String> trueBools = Arrays.asList("true", "on", "t");
				List<String> falseBools = Arrays.asList("false", "off", "f");
				
				if(trueBools.contains(commandString))
					return true;
				else if(falseBools.contains(commandString))
					return false;
				else
					throw new IllegalArgumentException("Can't cast " + commandString + " to a boolean!");
			},
			(CommandObject<Integer>) Integer::parseInt,
			(CommandObject<Short>) Short::parseShort,
			(CommandObject<Double>) Double::parseDouble,
			(CommandObject<Float>) Float::parseFloat,
			(CommandObject<Long>) Long::parseLong,
			(CommandObject<Byte>) Byte::parseByte,
			(CommandObject<Character>) commandString -> {
				if(commandString.length() == 1)
					return commandString.charAt(0);
				throw new IllegalArgumentException("Can't cast String with length of " + commandString.length() + " to char");
			},
			(CommandObject<UUID>) UUID::fromString
	));
	
	public final Object[] parseArguments(CommandSender cs, String[] args) {
		List<Object> objects = new ArrayList<>(Collections.singletonList(cs));
		
		Arrays.stream(args).forEach(arg -> {
			for(CommandObject<?> o : getCommandObjects())
				try {
					objects.add(o.fromCommandString(arg));
				} catch(Exception e) {
					e.printStackTrace();
				}
		});
		
		return objects.toArray();
	}
	
	public List<CommandObject<?>> getCommandObjects() {
		return this.commandObjects;
	}
}
