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

package de.alphahelix.alphalibary.minigame;

import com.google.common.base.Objects;
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
import org.bukkit.event.player.PlayerPortalEvent;

import java.io.Serializable;

public class MinigameLogic extends SimpleListener implements Serializable {

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
    public void onCollect(EntityPickupItemEvent e) {
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

    public boolean isCanConsumeItems() {
        return canConsumeItems;
    }

    public MinigameLogic setCanConsumeItems(boolean canConsumeItems) {
        this.canConsumeItems = canConsumeItems;
        return this;
    }

    public boolean isCanUsePortals() {
        return canUsePortals;
    }

    public MinigameLogic setCanUsePortals(boolean canUsePortals) {
        this.canUsePortals = canUsePortals;
        return this;
    }

    public boolean isCanOpenBlockInv() {
        return canOpenBlockInv;
    }

    public MinigameLogic setCanOpenBlockInv(boolean canOpenBlockInv) {
        this.canOpenBlockInv = canOpenBlockInv;
        return this;
    }

    public boolean isCanCollect() {
        return canCollect;
    }

    public MinigameLogic setCanCollect(boolean canCollect) {
        this.canCollect = canCollect;
        return this;
    }

    public boolean isCanStarve() {
        return canStarve;
    }

    public MinigameLogic setCanStarve(boolean canStarve) {
        this.canStarve = canStarve;
        return this;
    }

    public boolean isCanSpawn() {
        return canSpawn;
    }

    public MinigameLogic setCanSpawn(boolean canSpawn) {
        this.canSpawn = canSpawn;
        return this;
    }

    public boolean isCanDamageEntity() {
        return canDamageEntity;
    }

    public MinigameLogic setCanDamageEntity(boolean canDamageEntity) {
        this.canDamageEntity = canDamageEntity;
        return this;
    }

    public boolean isCanDamage() {
        return canDamage;
    }

    public MinigameLogic setCanDamage(boolean canDamage) {
        this.canDamage = canDamage;
        return this;
    }

    public boolean isCanBreak() {
        return canBreak;
    }

    public MinigameLogic setCanBreak(boolean canBreak) {
        this.canBreak = canBreak;
        return this;
    }

    public boolean isCanPlace() {
        return canPlace;
    }

    public MinigameLogic setCanPlace(boolean canPlace) {
        this.canPlace = canPlace;
        return this;
    }

    public boolean isCanDrop() {
        return canDrop;
    }

    public MinigameLogic setCanDrop(boolean canDrop) {
        this.canDrop = canDrop;
        return this;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public MinigameLogic setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MinigameLogic that = (MinigameLogic) o;
        return canConsumeItems == that.canConsumeItems &&
                canUsePortals == that.canUsePortals &&
                canOpenBlockInv == that.canOpenBlockInv &&
                canCollect == that.canCollect &&
                canStarve == that.canStarve &&
                canSpawn == that.canSpawn &&
                canDamageEntity == that.canDamageEntity &&
                canDamage == that.canDamage &&
                canBreak == that.canBreak &&
                canPlace == that.canPlace &&
                canDrop == that.canDrop &&
                Objects.equal(gameStatus, that.gameStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(canConsumeItems, canUsePortals, canOpenBlockInv, canCollect, canStarve, canSpawn, canDamageEntity, canDamage, canBreak, canPlace, canDrop, gameStatus);
    }

    @Override
    public String toString() {
        return "MinigameLogic{" +
                "canConsumeItems=" + canConsumeItems +
                ", canUsePortals=" + canUsePortals +
                ", canOpenBlockInv=" + canOpenBlockInv +
                ", canCollect=" + canCollect +
                ", canStarve=" + canStarve +
                ", canSpawn=" + canSpawn +
                ", canDamageEntity=" + canDamageEntity +
                ", canDamage=" + canDamage +
                ", canBreak=" + canBreak +
                ", canPlace=" + canPlace +
                ", canDrop=" + canDrop +
                ", gameStatus=" + gameStatus +
                '}';
    }
}
