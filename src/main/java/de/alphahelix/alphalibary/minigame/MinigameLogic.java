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
	
	public MinigameLogic (boolean canConsumeItems, boolean canUsePortals, boolean canOpenBlockInv, boolean canCollect, boolean canStarve, boolean canSpawn, boolean canDamageEntity, boolean canDamage, boolean canBreak, boolean canPlace, boolean canDrop) {
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
	}
	
	@EventHandler
	public void onItemComsune (PlayerItemConsumeEvent e) {
		e.setCancelled(!canConsumeItems);
	}
	
	@EventHandler
	public void onTeleport (PlayerPortalEvent e) {
		e.setCancelled(!canUsePortals);
	}
	
	@EventHandler
	public void onBlockInvOpen (InventoryOpenEvent e) {
		if(e.getInventory().getType() != InventoryType.PLAYER
				|| e.getInventory().getType() != InventoryType.CHEST) {
			e.setCancelled(!canOpenBlockInv);
		}
	}
	
	@EventHandler
	public void onCollect (PlayerPickupItemEvent e) {
		e.setCancelled(!canCollect);
	}
	
	@EventHandler
	public void onHunger (FoodLevelChangeEvent e) {
		if(!canStarve) {
			e.setFoodLevel(20);
		}
	}
	
	@EventHandler
	public void onAlternateSpawn (EntitySpawnEvent e) {
		e.setCancelled(!canSpawn);
	}
	
	@EventHandler
	public void onSpawn (CreatureSpawnEvent e) {
		e.setCancelled(!canSpawn);
	}
	
	@EventHandler
	public void onHurt (EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
			e.setCancelled(!canDamage);
		}
	}
	
	@EventHandler
	public void onEntityHurt (EntityDamageByEntityEvent e) {
		if(!(e.getDamager() instanceof Player))
			return;
		e.setCancelled(!canDamageEntity);
	}
	
	@EventHandler
	public void onBreak (BlockBreakEvent e) {
		e.setCancelled(!canBreak);
	}
	
	@EventHandler
	public void onPlace (BlockPlaceEvent e) {
		e.setCancelled(!canPlace);
	}
	
	@EventHandler
	public void onDrop (PlayerDropItemEvent e) {
		e.setCancelled(!canDrop);
	}
}
