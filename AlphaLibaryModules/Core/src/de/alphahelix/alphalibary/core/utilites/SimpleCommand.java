/*
 *     Copyright (C) <2017>  <AlphaHelixDev>
 *
 *     This program is free software: you can redistribute it under the
 *     terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.alphahelix.alphalibary.core.utilites;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class SimpleCommand extends Command {
	
	public SimpleCommand(String command) {
		this(command, "");
	}
	
	public SimpleCommand(String command, String description) {
		this(command, description, new String[]{});
	}
	
	/**
	 * Creates a new {@link SimpleCommand} to not manually implement the Command inside the plugin.yml
	 *
	 * @param command     the command name
	 * @param description the description which should be printed out at '/help commandName'
	 * @param aliases     the aliases which can be used to run the command as well
	 */
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
			map.register("AlphaLibary", this);
		} catch(Exception exc) {
			exc.printStackTrace();
		}
	}
	
	/**
	 * Everything what should get run when the command is executed
	 */
	@Override
	public abstract boolean execute(CommandSender cs, String label, String[] args);
	
	/**
	 * Suggestions in the chat when you press TAB
	 */
	@Override
	public List<String> tabComplete(CommandSender cs, String label, String[] args) {
		return new ArrayList<>();
	}
	
	/**
	 * Create a {@link String} out of a {@link String[]}
	 *
	 * @param args  The {@link String[]} which should be a {@link String}
	 * @param start At which index of the array the {@link String} should start
	 *
	 * @return the new created {@link String}
	 */
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
