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

package de.alphahelix.alphalibary.airdrops;

import com.google.common.base.Objects;
import de.alphahelix.alphalibary.listener.SimpleListener;
import de.alphahelix.alphalibary.utils.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class AirDrop extends SimpleListener implements Serializable {

    private static final transient ArrayList<Entity> AIR_DROPS = new ArrayList<>();

    private DropOffLocation dropOff;
    private ArrayList<ItemStack> dropList = new ArrayList<>();

    /**
     * Creates a new AirDrop at a random location in the given radius around the center
     *
     * @param center the center of the circle where the random location can be located at
     * @param radius the radius around the center to create the circle
     * @param drops  the {@link ItemStack} which are inside the airdrop
     */
    public AirDrop(Location center, int radius, ItemStack... drops) {
        super();
        this.dropOff = new DropOffLocation(center, radius);
        Collections.addAll(this.dropList, drops);
    }

    /**
     * Creates a new AirDrop at a random location in the given radius around the center
     *
     * @param world  the world at which spawn location the center of the circle where the random location can be located at
     * @param radius the radius around the center to create the circle
     * @param drops  the {@link ItemStack} which are inside the airdrop
     */
    public AirDrop(World world, int radius, ItemStack... drops) {
        super();
        this.dropOff = new DropOffLocation(world.getSpawnLocation(), radius);
        Collections.addAll(this.dropList, drops);
    }

    /**
     * Sets the center of the drop off
     *
     * @param center the center of the drop off
     * @param radius the radius of the drop
     */
    public void setDropOff(Location center, int radius) {
        this.dropOff = new DropOffLocation(center, radius);
    }

    /**
     * Adds in some drops into the airdrop
     *
     * @param drops the ItemStacks to add in
     */
    public void addDrops(ItemStack... drops) {
        Collections.addAll(dropList, drops);
    }

    /**
     * Removes some drops from the airdrop
     *
     * @param drops the ItemStacks to remove
     */
    public void removeDrops(ItemStack... drops) {
        for (ItemStack drop : drops) {
            if (dropList.contains(drop))
                dropList.remove(drop);
        }
    }

    public void setDrops(ItemStack... drops) {
        dropList = new ArrayList<>();

        Collections.addAll(dropList, drops);
    }

    public void spawn() {
        FallingBlock chest = dropOff.getDropOff().getWorld().spawnFallingBlock(dropOff.getDropOff(), new MaterialData(Material.CHEST));

        AIR_DROPS.add(chest);
    }

    @EventHandler
    public void onHit(EntityChangeBlockEvent e) {
        if (e.getEntity() instanceof FallingBlock) {
            if (AIR_DROPS.contains(e.getEntity())) {
                Util.runLater(5, false, () -> {
                    Chest chest = (Chest) e.getBlock().getState();

                    for (ItemStack is : dropList)
                        chest.getBlockInventory().addItem(is);

                    AIR_DROPS.remove(e.getEntity());
                });
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AirDrop airDrop = (AirDrop) o;
        return Objects.equal(dropOff, airDrop.dropOff) &&
                Objects.equal(dropList, airDrop.dropList);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(dropOff, dropList);
    }

    @Override
    public String toString() {
        return "AirDrop{" +
                "dropOff=" + dropOff +
                ", dropList=" + dropList +
                '}';
    }
}
