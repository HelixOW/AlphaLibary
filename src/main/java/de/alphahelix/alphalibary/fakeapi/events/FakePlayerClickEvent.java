/*
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
 */

package de.alphahelix.alphalibary.fakeapi.events;

import de.alphahelix.alphalibary.fakeapi.instances.FakePlayer;
import de.alphahelix.alphalibary.nms.enums.REnumAction;
import de.alphahelix.alphalibary.nms.enums.REnumHand;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class FakePlayerClickEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private FakePlayer fakeArmorstand;
    private REnumAction clickAction;
    private REnumHand hand;

    public FakePlayerClickEvent(Player who, FakePlayer fakeArmorstand, REnumAction clickAction, REnumHand hand) {
        super(who);
        this.fakeArmorstand = fakeArmorstand;
        this.clickAction = clickAction;
        this.hand = hand;
    }

    /**
     * Gets a list of handlers handling this event.
     *
     * @return A list of handlers handling this event.
     */
    public final static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Gets a list of handlers handling this event.
     *
     * @return A list of handlers handling this event.
     */
    @Override
    public final HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Gets the clicked {@link FakePlayer}
     *
     * @return the clicked {@link FakePlayer}
     */
    public FakePlayer getFakePlayer() {
        return fakeArmorstand;
    }

    /**
     * gets the {@link REnumAction}
     *
     * @return the {@link REnumAction}
     */
    public REnumAction getClickAction() {
        return clickAction;
    }

    public REnumHand getHand() {
        return hand;
    }
}
