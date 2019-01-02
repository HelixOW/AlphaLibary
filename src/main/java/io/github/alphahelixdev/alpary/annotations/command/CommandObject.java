package io.github.alphahelixdev.alpary.annotations.command;

public interface CommandObject<T> {
	
	T fromCommandString(String commandString) throws Exception;
	
}
