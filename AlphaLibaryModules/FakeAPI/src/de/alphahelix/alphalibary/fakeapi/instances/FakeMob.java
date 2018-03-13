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

package de.alphahelix.alphalibary.fakeapi.instances;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.fakeapi.FakeMobType;
import org.bukkit.Location;

public class FakeMob extends FakeEntity {
	
	private final FakeMobType fakeMobType;
	private final boolean baby;
	
	public FakeMob(Location location, String name, Object nmsEntity, FakeMobType type, boolean baby) {
		super(location, name, nmsEntity);
		this.fakeMobType = type;
		this.baby = baby;
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(super.hashCode(), getFakeMobType(), isBaby());
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		if(!super.equals(o)) return false;
		FakeMob fakeMob = (FakeMob) o;
		return isBaby() == fakeMob.isBaby() &&
				getFakeMobType() == fakeMob.getFakeMobType();
	}
	
	public boolean isBaby() {
		return baby;
	}
	
	public FakeMobType getFakeMobType() {
		return fakeMobType;
	}
	
	@Override
	public String toString() {
		return "FakeMob{" +
				"fakeMobType=" + fakeMobType +
				", baby=" + baby +
				"} " + super.toString();
	}
}
