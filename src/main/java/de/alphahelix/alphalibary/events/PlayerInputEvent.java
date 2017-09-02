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

package de.alphahelix.alphalibary.events;

import com.google.common.base.Objects;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerInputEvent extends PlayerEvent {

	private static final HandlerList handlers = new HandlerList();

	private String input;

	public PlayerInputEvent(Player who, String input) {
		super(who); this.input = input;
	}

	public final static HandlerList getHandlerList() {
		return handlers;
	}

	public String getInput() {
		return input;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerInputEvent that = (PlayerInputEvent) o;
        return Objects.equal(getInput(), that.getInput());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getInput());
    }

    @Override
    public String toString() {
        return "PlayerInputEvent{" +
                "input='" + input + '\'' +
                '}';
    }
}
