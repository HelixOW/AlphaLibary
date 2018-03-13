package de.alphahelix.alphalibary.annotations.command;

import de.alphahelix.alphalibary.annotations.Accessor;
import de.alphahelix.alphalibary.annotations.command.errors.ErrorHandler;
import de.alphahelix.alphalibary.annotations.command.exceptions.*;
import de.alphahelix.alphalibary.core.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class AnnotatedCommand {
	
	private final Object cmdClass;
	private final Method cmdMethod;
	private final Command cmdAnnotation;
	private final Permission permissionAnnotation;
	private final Method completeMethod;
	private final Complete completeAnnotation;
	private final ErrorHandler errorHandler;
	
	public String name;
	public String[] aliases;
	public String usage;
	public String description;
	public String permission = "";
	public String permissionMsg = "";
	public String resultPrefix;
	
	private CommandMap commandMap;
	
	public AnnotatedCommand(Object cmdClass, Method cmdMethod, Command cmdAnnotation, Permission permissionAnnotation, Method completeMethod, Complete completeAnnotation) {
		this.cmdClass = cmdClass;
		this.cmdMethod = cmdMethod;
		this.cmdAnnotation = cmdAnnotation;
		this.permissionAnnotation = permissionAnnotation;
		this.completeMethod = completeMethod;
		this.completeAnnotation = completeAnnotation;
		
		if(!cmdAnnotation.name().isEmpty())
			this.name = cmdAnnotation.name();
		else
			this.name = cmdMethod.getName();
		
		this.aliases = cmdAnnotation.alias();
		this.usage = cmdAnnotation.usage();
		this.description = cmdAnnotation.description();
		this.resultPrefix = cmdAnnotation.resultPrefix();
		
		try {
			this.errorHandler = cmdAnnotation.errorHandler().getDeclaredConstructor().newInstance();
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
		
		if(permissionAnnotation != null) {
			if(!permissionAnnotation.value().isEmpty())
				this.permission = permissionAnnotation.value();
			if(permissionAnnotation.permissionMsg().isEmpty())
				this.permissionMsg = permissionAnnotation.permissionMsg();
		}
	}
	
	boolean onCommand(CommandSender sender, BukkitCMD cmd, String label, String[] args) {
		try {
			if(cmdAnnotation.onlyPlayers())
				if(!(sender instanceof Player)) throw new IllegalSenderException();
			
			if(!hasPerm(sender))
				throw new PermissionException(permission);
			
			if(cmdAnnotation.max() != -1 && args.length > cmdAnnotation.max())
				throw new InvalidLenghtException(cmdAnnotation.max(), args.length);
			
			try {
				Class<?>[] paramTypes = cmdMethod.getParameterTypes();
				
				if(paramTypes.length == 0)
					throw new CommandException("Command method '" + cmdMethod.getName() + "' in '" + cmdClass + "' has no CommandSender parameter.");
				
				if(!CommandSender.class.isAssignableFrom(paramTypes[0]))
					throw new CommandException("First parameter of method '" + cmdMethod.getName() + "' in '" + cmdClass + "' is no CommandSender.");
				
				if(Player.class.isAssignableFrom(paramTypes[0]))
					if(cmdAnnotation.onlyPlayers() && !(sender instanceof Player)) throw new IllegalSenderException();
				
				if((paramTypes.length - 1 < cmdAnnotation.min()) || (cmdAnnotation.max() != -1 && paramTypes.length - 1 > cmdAnnotation.max()))
					throw new CommandException("Parameter lenght of method '" + cmdMethod.getName() + "' in '" + cmdClass + "' is not the given lenght of arguments.");
				
				
				Object[] parsedArgs = new Object[paramTypes.length];
				
				for(int i = 1; i < args.length + 1; i++) {
					if(i == paramTypes.length - 1) {
						Joined joinedAnnotation = getMethodParameterAnnotation(cmdMethod, paramTypes.length - 1, Joined.class);
						
						if(joinedAnnotation != null) {
							parsedArgs[parsedArgs.length - 1] = joinArguments(args, i - 1, joinedAnnotation.joinIn());
							break;
						} else if(String[].class.isAssignableFrom(paramTypes[paramTypes.length - 1])) {
							parsedArgs[parsedArgs.length - 1] = getLeftoverArguments(args, i - 1);
							break;
						}
					}
					
					if(i >= parsedArgs.length) break;
					
					Alias alias = getMethodParameterAnnotation(cmdMethod, Alias.class);
					
					String arg = args[i - 1];
					
					if(alias != null && StringUtil.upperEverything(Arrays.asList(alias.alias())).contains(arg.toUpperCase()))
						parsedArgs[i] = parseArgument(paramTypes[i], alias.alias()[0]);
					else
						parsedArgs[i] = parseArgument(paramTypes[i], arg);
				}
				parsedArgs[0] = sender;
				
				if(paramTypes.length - 1 > args.length) {
					for(int i = args.length; i < paramTypes.length; i++) {
						Optional optional = getMethodParameterAnnotation(cmdMethod, i, Optional.class);
						
						if(optional != null) {
							if(!optional.define().isEmpty()) {
								parsedArgs[i] = parseArgument(paramTypes[i], optional.define());
							}
						}
					}
				}
				
				cmdMethod.invoke(cmdClass, parsedArgs);
			} catch(InvocationTargetException e) {
				Throwable cause = e.getCause();
				if(cause instanceof CommandException) {
					throw (CommandException) cause;
				}
				throw new CommandException("Unhandled exception while invoking command method in " + cmdClass + "#" + cmdMethod.getName(), cause);
			} catch(CommandException commandException) {
				throw commandException;
			} catch(Throwable throwable) {
				throw new CommandException("Unhandled exception in " + cmdClass + "#" + cmdMethod.getName(), throwable);
			}
			
		} catch(PermissionException permissionException) {
			if(errorHandler != null) {
				errorHandler.handlePermissionException(permissionException, sender, cmd, args);
				return false;
			} else
				throw permissionException;
		} catch(IllegalSenderException illegalSenderException) {
			if(errorHandler != null) {
				errorHandler.handleIllegalSenderException(illegalSenderException, sender, cmd, args);
				return false;
			}
		} catch(ArgumentException parseException) {
			if(errorHandler != null) {
				errorHandler.handleArgumentException(parseException, sender, cmd, args);
				return false;
			} else
				throw parseException;
		} catch(InvalidLenghtException lengthException) {
			if(errorHandler != null) {
				errorHandler.handleInvalidLenghtException(lengthException, sender, cmd, args);
				return false;
			} else
				throw lengthException;
		} catch(CommandException commandException) {
			if(errorHandler != null) {
				errorHandler.handleCommandException(commandException, sender, cmd, args);
				return false;
			} else
				throw commandException;
		}
		
		return true;
	}
	
	boolean hasPerm(CommandSender sender) {
		return permissionAnnotation == null || sender.hasPermission(permission);
	}
	
	<A extends Annotation> A getMethodParameterAnnotation(Method method, int index, Class<A> clazz) {
		Annotation[] annotations = method.getParameterAnnotations()[index];
		if(annotations != null) {
			for(Annotation annotation : annotations) {
				if(clazz.isAssignableFrom(annotation.getClass())) {
					return (A) annotation;
				}
			}
		}
		return null;
	}
	
	String joinArguments(String[] args, int start, String joiner) {
		if(start > args.length) {
			throw new IllegalArgumentException("start > length");
		}
		
		StringBuilder joined = new StringBuilder();
		for(int i = start; i < args.length; i++) {
			if(i != start) {
				joined.append(joiner);
			}
			joined.append(args[i]);
		}
		return joined.toString();
	}
	
	String[] getLeftoverArguments(String[] args, int start) {
		String[] newArray = new String[args.length - start];
		System.arraycopy(args, start, newArray, 0, args.length - start);
		return newArray;
	}
	
	<A extends Annotation> A getMethodParameterAnnotation(Method method, Class<A> clazz) {
		Annotation[][] annotations = method.getParameterAnnotations();
		for(Annotation[] annotationA : annotations) {
			for(Annotation annotation : annotationA) {
				if(clazz.isAssignableFrom(annotation.getClass())) {
					return (A) annotation;
				}
			}
		}
		return null;
	}
	
	Object parseArgument(Class<?> parameterType, String argument) {
		try {
			if(String.class.isAssignableFrom(parameterType))
				return argument;
			
			try {
				Constructor stringConstructor = parameterType.getConstructor(String.class);
				if(stringConstructor != null)
					return stringConstructor.newInstance(argument);
			} catch(NoSuchMethodException ignored) {
			}
			
			if(Number.class.isAssignableFrom(parameterType)) {
				String parseName = parameterType.getSimpleName();
				if(Integer.class.equals(parameterType))
					parseName = "Int";
				
				return parameterType.getDeclaredMethod("parse" + parseName, String.class).invoke(null, argument);
			} else if(Enum.class.isAssignableFrom(parameterType)) {
				try {
					return Enum.valueOf((Class<? extends Enum>) parameterType, argument.toUpperCase());
				} catch(Exception ignored) {
				
				}
			}
		} catch(ReflectiveOperationException e) {
			if(e instanceof InvocationTargetException) {
				Throwable cause = e.getCause();
				if(cause instanceof NumberFormatException)
					throw new ArgumentException("Could not parse number " + argument, argument, parameterType);
			}
			throw new ArgumentException("Exception while parsing argument '" + argument + "' to " + parameterType, e, argument, parameterType);
		}
		throw new ArgumentException("Failed to parse argument '" + argument + "' to " + parameterType, argument, parameterType);
	}
	
	List<String> onTabComplete(CommandSender sender, BukkitCMD cmd, String label, String[] args) {
		if(completeAnnotation == null || completeMethod == null) return null;
		if(!hasPerm(sender)) return null;
		
		try {
			Class<?>[] parameterTypes = completeMethod.getParameterTypes();
			
			if(parameterTypes.length <= 1)
				throw new CommandException("Completion method '" + completeMethod.getName() + "' in '" + cmdClass + "' is missing the List or CommandSender parameter");
			if(!List.class.isAssignableFrom(parameterTypes[0]))
				throw new CommandException("First parameter of method '" + completeMethod.getName() + "' in '" + completeMethod + "' is no List");
			if(!CommandSender.class.isAssignableFrom(parameterTypes[1]))
				throw new CommandException("Second parameter of method '" + completeMethod.getName() + "' in '" + completeMethod + "' is no CommandSender");
			
			if(Player.class.isAssignableFrom(parameterTypes[0]))
				if(!(sender instanceof Player))
					return null;
			
			
			Object[] parsedArguments = new Object[parameterTypes.length];
			for(int i = 2; i < args.length + 2; i++) {
				if(i == parameterTypes.length - 1) {
					Joined joinedAnnotation = getMethodParameterAnnotation(completeMethod, parameterTypes.length - 1, Joined.class);
					if(joinedAnnotation != null) {
						parsedArguments[parsedArguments.length - 1] = joinArguments(args, i - 1, joinedAnnotation.joinIn());
						break;
					} else if(String[].class.isAssignableFrom(parameterTypes[parameterTypes.length - 1])) {
						parsedArguments[parsedArguments.length - 1] = getLeftoverArguments(args, i - 1);
						break;
					}
				}
				
				if(i >= parsedArguments.length)
					break;
				
				if(args[i - 2] == null || args[i - 2].isEmpty())
					parsedArguments[i] = null;
				else {
					try {
						parsedArguments[i] = parseArgument(parameterTypes[i], args[i - 2]);
					} catch(Exception ignored) {
					}
				}
			}
			List<String> list = (List) (parsedArguments[0] = new ArrayList<>());
			parsedArguments[1] = sender;
			
			completeMethod.invoke(cmdClass, parsedArguments);
			return getPossibleCompletions(args, list.toArray(new String[list.size()]));
		} catch(CommandException commandException) {
			throw commandException;
		} catch(Throwable throwable) {
			throw new CommandException("Unhandled exception in " + cmdClass + "#" + completeMethod.getName(), throwable);
		}
	}
	
	public static List<String> getPossibleCompletions(String[] args, String[] possibilities) {
		final String argumentToFindCompletionFor = args[args.length - 1];
		
		final List<String> listOfPossibleCompletions = new ArrayList<>();
		for(String possibility : possibilities) {
			try {
				if(possibility != null && possibility.regionMatches(true, 0, argumentToFindCompletionFor, 0, argumentToFindCompletionFor.length())) {
					listOfPossibleCompletions.add(possibility);
				}
			} catch(Exception e) {
				throw new RuntimeException(e);
			}
		}
		Collections.sort(listOfPossibleCompletions);
		
		return listOfPossibleCompletions;
	}
	
	public final AnnotatedCommand register() {
		BukkitCMD command = new BukkitCMD(name);
		
		if(description != null)
			command.setDescription(description);
		if(usage != null)
			command.setUsage(usage);
		if(permission != null)
			command.setPermission(permission);
		if(permissionMsg != null)
			command.setPermissionMessage(this.permissionMsg);
		if(aliases.length != 0) {
			List<String> aliasList = new ArrayList<>();
			for(String s : aliases)
				aliasList.add(s.toLowerCase());
			
			command.setAliases(aliasList);
		}
		getCommandMap().register(resultPrefix != null ? resultPrefix : "", command);
		return command.exec = this;
	}
	
	private CommandMap getCommandMap() {
		if(commandMap == null) {
			try {
				commandMap = (CommandMap) Accessor.access(Bukkit.getServer().getClass().getDeclaredField("commandMap")).get(Bukkit.getServer());
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return commandMap;
	}
	
	private class BukkitCMD extends org.bukkit.command.Command {
		private AnnotatedCommand exec;
		
		BukkitCMD(String name) {
			super(name);
		}
		
		@Override
		public boolean execute(CommandSender commandSender, String s, String[] strings) {
			return exec != null && exec.onCommand(commandSender, this, s, strings);
		}
		
		@Override
		public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
			if(exec != null) return exec.onTabComplete(sender, this, alias, args);
			return null;
		}
	}
	
}
