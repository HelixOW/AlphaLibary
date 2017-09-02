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

public class CrossSystemManager {

    private static ArrayList<CrossSystemVariable> crossSystemVariables = new ArrayList<>();
	
	/**
	 * Adds in a new variable which can be accessed across addons
	 *
	 * @param crossSystemVariable the {@link CrossSystemVariable} to add
	 */
	public static void addVar(CrossSystemVariable crossSystemVariable) {
		crossSystemVariables.add(crossSystemVariable);
	}
	
	/**
	 * Checks if there is a certain variable available
	 *
	 * @param name the name of the {@link CrossSystemVariable}
	 *
	 * @return if the variable is available inside the system
	 */
	public static boolean hasVar (String name) {
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
		for (CrossSystemVariable vars : crossSystemVariables) {
            if (vars.name().equals(name)) return vars.value();
        }
        return null;
    }
}
