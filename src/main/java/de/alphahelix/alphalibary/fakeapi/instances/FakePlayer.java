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

import de.alphahelix.alphalibary.uuid.UUIDFetcher;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class FakePlayer extends FakeEntity {

    private String skin;
    private UUID skinUUID;
    private OfflinePlayer skinPlayer;

    public FakePlayer(Location location, String name, OfflinePlayer skin, Object fake) {
        super(location, name, fake);
        this.skin = skin.getName();
        this.skinPlayer = skin;
        this.skinUUID = UUIDFetcher.getUUID(skin.getName());
    }

    /**
     * Gets the name of the owner of the skin for this {@link FakePlayer}
     *
     * @return the name of the owner of this {@link FakePlayer}
     */
    public String getSkin() {
        return skin;
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
}