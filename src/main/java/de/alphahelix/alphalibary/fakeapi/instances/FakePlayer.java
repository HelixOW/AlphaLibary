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

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class FakePlayer extends FakeEntity {

    private UUID skinUUID;
    private transient OfflinePlayer skinPlayer;

    public FakePlayer(Location location, String name, UUID skin, Object fake) {
        super(location, name, fake);
        this.skinPlayer = Bukkit.getOfflinePlayer(skin);
        this.skinUUID = skin;
    }


    /**
     * Gets the {@link UUID} of the owner of this {@link FakePlayer}'s skin
     *
     * @return the {@link UUID} of the skinowner of this {@link FakePlayer}
     */
    public UUID getSkinUUID() {
        return skinUUID;
    }


    public OfflinePlayer getSkinPlayer() {
        return skinPlayer;
    }

    @Override
    public String toString() {
        return "FakePlayer{" +
                ", skinUUID=" + skinUUID +
                "} " + super.toString();
    }
}