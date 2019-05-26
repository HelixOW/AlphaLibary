package io.github.alphahelixdev.alpary.commands;

import io.github.whoisalphahelix.helix.ParsedObject;
import io.github.whoisalphahelix.helix.StringParser;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class ArgumentParser {
	
	private final StringParser parser = new StringParser();
	
	public static void registerCommandObject(ParsedObject<?> parsedObject) {
        StringParser.getParsableObjects().add(0, parsedObject);
	}
	
	public final Object[] parseArguments(CommandSender e, String[] args) {
		List<Object> objects = new ArrayList<>(Collections.singletonList(e));
		
		Arrays.stream(args).map(String::trim).filter(s -> !s.isEmpty()).forEach(parser::parseString);
		
		return objects.toArray();
	}
}
