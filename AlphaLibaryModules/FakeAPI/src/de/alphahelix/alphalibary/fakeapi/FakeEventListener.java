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

package de.alphahelix.alphalibary.fakeapi;

import de.alphahelix.alphalibary.core.SimpleLoader;
import de.alphahelix.alphalibary.core.utilites.SimpleListener;
import de.alphahelix.alphalibary.core.utilites.UUIDFetcher;
import de.alphahelix.alphalibary.fakeapi.instances.*;
import de.alphahelix.alphalibary.fakeapi.utils.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@SimpleLoader
public class FakeEventListener extends SimpleListener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		
		UUIDFetcher.getUUID(p, id -> FakeAPI.getEntityIds().put(p.getEntityId(), id));
		
		for(FakeArmorstand stands : FakeRegister.getArmorstandLocationsFile().getFakeArmorstandFromFile()) {
			ArmorstandFakeUtil.spawnTemporaryArmorstand(
					p,
					stands.getStartLocation(),
					stands.getName());
		}
		
		for(FakeBigItem bigItem : FakeRegister.getBigItemLocationsFile().getFakeBigItemFromFile()) {
			BigItemFakeUtil.spawnTemporaryBigItem(
					p,
					bigItem.getStartLocation(),
					bigItem.getName(),
					bigItem.getItemStack());
		}
		
		for(FakeEndercrystal endercrystal : FakeRegister.getEndercrystalLocationsFile().getFakeEndercrystalsFromFile()) {
			EndercrystalFakeUtil.spawnTemporaryEndercrystal(
					p,
					endercrystal.getStartLocation(),
					endercrystal.getName());
		}
		
		for(FakeItem item : FakeRegister.getItemLocationsFile().getFakeItemsFromFile()) {
			ItemFakeUtil.spawnTemporaryItem(
					p,
					item.getStartLocation(),
					item.getName(),
					item.getType());
		}
		
		for(FakeMob mob : FakeRegister.getMobLocationsFile().getFakeMobsFromFile()) {
			MobFakeUtil.spawnTemporaryMob(
					p,
					mob.getStartLocation(),
					mob.getName(),
					mob.getFakeMobType(),
					mob.isBaby());
		}
		
		for(FakePlayer player : FakeRegister.getPlayerLocationsFile().getFakePlayersFromFile()) {
			PlayerFakeUtil.spawnTemporaryPlayer(
					p,
					player.getStartLocation(),
					player.getSkinUUID(),
					player.getName(),
					entity -> {
					}
			);
		}
		
		for(FakeXPOrb xpOrb : FakeRegister.getXpOrbLocationsFile().getFakeXPOrbFromFile()) {
			XPOrbFakeUtil.spawnTemporaryXPOrb(
					p,
					xpOrb.getStartLocation(),
					xpOrb.getName());
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if(FakeAPI.getFakeEntities().containsKey(p.getName())) {
			PlayerFakeUtil.unFollowPlayer(p);
			PlayerFakeUtil.normalizeLook(p);
			PlayerFakeUtil.cancelAllSplittedTasks(p);
			
			ArmorstandFakeUtil.unFollowArmorstand(p);
			ArmorstandFakeUtil.cancelAllSplittedTasks(p);
			
			EndercrystalFakeUtil.cancelAllSplittedTasks(p);
			
			MobFakeUtil.unFollowPlayer(p);
			MobFakeUtil.normalizeLook(p);
			MobFakeUtil.cancelAllSplittedTasks(p);
			
			BigItemFakeUtil.cancelAllSplittedTasks(p);
			
			FakeAPI.getFakeEntities().remove(p.getName());
		}
	}
}
