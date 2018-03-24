/*
 *
 * Copyright (C) <2017>  <AlphaHelixDev>
 *
 *       This program is free software: you can redistribute it under the
 *       terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package de.alphahelix.alphalibary.addons.csv;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to share variables across {@link de.alphahelix.alphalibary.addons.core.Addon}s
 *
 * @author AlphaHelix
 * @version 1.0
 * @see de.alphahelix.alphalibary.addons.core.Addon
 * @see CrossSystemVariable
 * @since 1.9.2.1
 */
public class CrossSystemManager {
	
	private static final List<CrossSystemVariable> CROSS_SYSTEM_VARIABLES = new ArrayList<>();
	
	/**
	 * Adds in a new variable which can be accessed across addons
	 *
	 * @param crossSystemVariable the {@link CrossSystemVariable} to add
	 */
	public static void addVar(CrossSystemVariable crossSystemVariable) {
		CROSS_SYSTEM_VARIABLES.add(crossSystemVariable);
	}
	
	/**
	 * Checks if there is a certain variable available
	 *
	 * @param name the name of the {@link CrossSystemVariable}
	 *
	 * @return if the variable is available inside the system
	 */
	public static boolean hasVar(String name) {
		return getVar(name) != null;
	}
	
	/**
	 * Gets the serialized String of the {@link CrossSystemVariable}
	 *
	 * @param name the name of the {@link CrossSystemVariable}
	 *
	 * @return the serialized String of the {@link CrossSystemVariable}
	 */
	public static String getVar(String name) {
		for(CrossSystemVariable vars : CROSS_SYSTEM_VARIABLES) {
			if(vars.name().equals(name)) return vars.value();
		}
		return null;
	}
}
