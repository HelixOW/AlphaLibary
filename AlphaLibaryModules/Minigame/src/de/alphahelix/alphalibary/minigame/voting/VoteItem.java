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

package de.alphahelix.alphalibary.minigame.voting;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.inventories.item.InventoryItem;

import java.io.Serializable;


public class VoteItem<T> implements Serializable {
	
	private String name;
	private InventoryItem icon;
	private T toVoteFor;
	
	public VoteItem(String name, InventoryItem icon, T toVoteFor) {
		this.name = name;
		this.icon = icon;
		this.toVoteFor = toVoteFor;
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(getName(), getIcon(), getToVoteFor());
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		VoteItem<?> voteItem = (VoteItem<?>) o;
		return Objects.equal(getName(), voteItem.getName()) &&
				Objects.equal(getIcon(), voteItem.getIcon()) &&
				Objects.equal(getToVoteFor(), voteItem.getToVoteFor());
	}
	
	@Override
	public String toString() {
		return "VoteItem{" +
				"name='" + name + '\'' +
				", icon=" + icon +
				", toVoteFor=" + toVoteFor +
				'}';
	}
	
	public String getName() {
		return name;
	}
	
	public VoteItem setName(String name) {
		this.name = name;
		return this;
	}
	
	public InventoryItem getIcon() {
		return icon;
	}
	
	public VoteItem setIcon(InventoryItem icon) {
		this.icon = icon;
		return this;
	}
	
	public T getToVoteFor() {
		return toVoteFor;
	}
	
	public VoteItem setToVoteFor(T toVoteFor) {
		this.toVoteFor = toVoteFor;
		return this;
	}
}
