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

import com.google.gson.annotations.Expose;
import org.bukkit.Location;

import java.io.Serializable;
import java.util.UUID;

public class FakeEntity implements Serializable {

    private Location startLocation;
    private String name;
    private UUID uuid;


    @Expose
    private transient Object nmsEntity;
    @Expose
    private transient Location currentlocation;

    public FakeEntity() {
    }

    public FakeEntity(Location startLocation, String name, Object nmsEntity) {
        this.startLocation = startLocation;
        this.currentlocation = startLocation;
        this.name = name;
        this.nmsEntity = nmsEntity;
        this.uuid = UUID.randomUUID();
    }


    /**
     * Gets the {@link Location} where the {@link FakeEntity} currently is
     *
     * @return the current {@link Location} of the {@link FakeEntity}
     */
    public Location getCurrentlocation() {
        return currentlocation;
    }

    /**
     * Sets the {@link Location} of the {@link FakeEntity}
     *
     * @param currentlocation the new {@link Location} of the {@link FakeEntity}
     */
    public void setCurrentlocation(Location currentlocation) {
        this.currentlocation = currentlocation;
    }

    /**
     * Gets the name of the {@link FakeEntity} which is saved inside the File
     *
     * @return the name of the {@link FakeEntity} inside the file
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the NMS Class of the {@link FakeEntity} which is saved inside the File
     *
     * @return the instance of the NMS Class of the {@link FakeEntity}
     */
    public Object getNmsEntity() {
        return nmsEntity;
    }

    /**
     * Gets the {@link Location} of the {@link FakeEntity} where it was spawned
     *
     * @return the {@link Location} where the {@link FakeEntity} was spawned
     */
    public Location getStartLocation() {
        return startLocation;
    }

    public UUID getUUID() {
        return uuid;
    }

    @Override
    public String toString() {
        return "FakeEntity{" +
                "startLocation=" + startLocation +
                ", name='" + name + '\'' +
                ", uuid=" + uuid +
                '}';
    }
}