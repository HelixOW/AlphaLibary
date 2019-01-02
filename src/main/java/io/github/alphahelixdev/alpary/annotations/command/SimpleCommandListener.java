package io.github.alphahelixdev.alpary.annotations.command;

import io.github.alphahelixdev.alpary.Alpary;

public class SimpleCommandListener {
	
	public SimpleCommandListener() {
		Alpary.getInstance().annotationHandler().registerCommands(this);
	}
}
