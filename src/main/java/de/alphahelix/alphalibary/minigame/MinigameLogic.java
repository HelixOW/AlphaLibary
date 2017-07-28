/*
 *
 *  * Copyright (C) <2017>  <AlphaHelixDev>
 *  *
 *  *       This program is free software: you can redistribute it under the
 *  *       terms of the GNU General Public License as published by
 *  *       the Free Software Foundation, either version 3 of the License.
 *  *
 *  *       This program is distributed in the hope that it will be useful,
 *  *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  *       GNU General Public License for more details.
 *  *
 *  *       You should have received a copy of the GNU General Public License
 *  *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package de.alphahelix.alphalibary.minigame;

import de.alphahelix.alphalibary.listener.SimpleListener;
import de.alphahelix.alphalibary.status.GameStatus;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerPortalEvent;

public class MinigameLogic extends SimpleListener {

    private boolean canConsumeItems, canUsePortals, canOpenBlockInv, canCollect,
            canStarve, canSpawn, canDamageEntity, canDamage, canBreak, canPlace, canDrop;
    private GameStatus gameStatus;

    public MinigameLogic(boolean canConsumeItems, boolean canUsePortals, boolean canOpenBlockInv, boolean canCollect, boolean canStarve, boolean canSpawn, boolean canDamageEntity, boolean canDamage, boolean canBreak, boolean canPlace, boolean canDrop, GameStatus gameStatus) {
        super();
        this.canConsumeItems = canConsumeItems;
        this.canUsePortals = canUsePortals;
        this.canOpenBlockInv = canOpenBlockInv;
        this.canCollect = canCollect;
        this.canStarve = canStarve;
        this.canSpawn = canSpawn;
        this.canDamageEntity = canDamageEntity;
        this.canDamage = canDamage;
        this.canBreak = canBreak;
        this.canPlace = canPlace;
        this.canDrop = canDrop;
        this.gameStatus = gameStatus;
    }

    @EventHandler
    public void onItemComsune(PlayerItemConsumeEvent e) {
        if (GameStatus.isState(gameStatus))
            e.setCancelled(!canConsumeItems);
    }

    @EventHandler
    public void onTeleport(PlayerPortalEvent e) {
        if (GameStatus.isState(gameStatus))
            e.setCancelled(!canUsePortals);
    }

    @EventHandler
    public void onBlockInvOpen(InventoryOpenEvent e) {
        if (GameStatus.isState(gameStatus))
            if (e.getInventory().getType() != InventoryType.PLAYER
                    || e.getInventory().getType() != InventoryType.CHEST)
                e.setCancelled(!canOpenBlockInv);
    }

    @EventHandler
    public void onCollect(PlayerPickupItemEvent e) {
        if (GameStatus.isState(gameStatus))
            e.setCancelled(!canCollect);
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent e) {
        if (GameStatus.isState(gameStatus))
            if (!canStarve)
                e.setFoodLevel(20);
    }

    @EventHandler
    public void onAlternateSpawn(EntitySpawnEvent e) {
        if (GameStatus.isState(gameStatus))
            e.setCancelled(!canSpawn);
    }

    @EventHandler
    public void onSpawn(CreatureSpawnEvent e) {
        if (GameStatus.isState(gameStatus))
            e.setCancelled(!canSpawn);
    }

    @EventHandler
    public void onHurt(EntityDamageEvent e) {
        if (GameStatus.isState(gameStatus))
            if (e.getEntity() instanceof Player)
                e.setCancelled(!canDamage);
    }

    @EventHandler
    public void onEntityHurt(EntityDamageByEntityEvent e) {
        if (GameStatus.isState(gameStatus))
            if (e.getDamager() instanceof Player)
                e.setCancelled(!canDamageEntity);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (GameStatus.isState(gameStatus))
            e.setCancelled(!canBreak);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (GameStatus.isState(gameStatus))
            e.setCancelled(!canPlace);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (GameStatus.isState(gameStatus))
            e.setCancelled(!canDrop);
    }
}
