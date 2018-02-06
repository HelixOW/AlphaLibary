/*
 *     Copyright (C) <2016>  <AlphaHelixDev>
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
package de.alphahelix.alphalibary.core;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;


public class SimpleListener implements Listener {

    /**
     * Automatic registering {@link Listener}
     */
    public SimpleListener() {
        Bukkit.getPluginManager().registerEvents(this, AlphaLibary.getInstance());
    }
}
