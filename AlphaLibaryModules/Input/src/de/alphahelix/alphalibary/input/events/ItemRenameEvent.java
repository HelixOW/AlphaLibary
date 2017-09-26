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

package de.alphahelix.alphalibary.input.events;

import com.google.common.base.Objects;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.InventoryView;

@SuppressWarnings("ALL")
public class ItemRenameEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final InventoryView inventory;
    private final String name;
    private boolean cancelled;

    public ItemRenameEvent(Player who, InventoryView iw, String nN) {
        super(who);
        this.inventory = iw;
        this.name = nN;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public InventoryView getInventory() {
        return inventory;
    }

    public String getName() {
        return name;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemRenameEvent that = (ItemRenameEvent) o;
        return isCancelled() == that.isCancelled() &&
                Objects.equal(getInventory(), that.getInventory()) &&
                Objects.equal(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getInventory(), getName(), isCancelled());
    }

    @Override
    public String toString() {
        return "ItemRenameEvent{" +
                "inventory=" + inventory +
                ", name='" + name + '\'' +
                ", cancelled=" + cancelled +
                '}';
    }
}