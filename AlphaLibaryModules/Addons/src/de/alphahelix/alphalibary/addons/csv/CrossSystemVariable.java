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

public interface CrossSystemVariable {
	/**
	 * To send a variable across addons, you need to specify a value in serialized JSON to access it inside the other Addons
	 *
	 * @return a serialized String of the {@link CrossSystemVariable}
	 */
	String value();
	
	/**
	 * To access a value of another Addon, you need to specify a key for the value
	 *
	 * @return the name of the String
	 */
	String name();
}
