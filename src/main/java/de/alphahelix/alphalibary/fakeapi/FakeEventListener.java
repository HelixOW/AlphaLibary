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

import de.alphahelix.alphalibary.fakeapi.utils.*;
import de.alphahelix.alphalibary.listener.SimpleListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class FakeEventListener extends SimpleListener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        for (String names : FakeRegister.getArmorstandLocationsFile().getPacketArmorstand().keySet()) {
            ArmorstandFakeUtil.spawnArmorstand(p,
                    FakeRegister.getArmorstandLocationsFile().getPacketArmorstand().get(names),
                    names);
        }
        for (String names : FakeRegister.getEndercrystalLocationsFile().getPacketEndercrystal().keySet()) {
            EndercrystalFakeUtil.spawnEndercrystal(p,
                    FakeRegister.getEndercrystalLocationsFile().getPacketEndercrystal().get(names),
                    names);
        }
        for (String names : FakeRegister.getPlayerLocationsFile().getPacketPlayerLocations().keySet()) {
            PlayerFakeUtil.spawnPlayer(p,
                    FakeRegister.getPlayerLocationsFile().getPacketPlayerLocations().get(names),
                    FakeRegister.getPlayerLocationsFile().getPacketPlayerSkins().get(names),
                    names);
        }
        for (String names : FakeRegister.getItemLocationsFile().getPacketItemsLocations().keySet()) {
            ItemFakeUtil.spawnItem(p,
                    FakeRegister.getItemLocationsFile().getPacketItemsLocations().get(names),
                    names,
                    FakeRegister.getItemLocationsFile().getPacketItemsTypes().get(names));

        }
        for (String names : FakeRegister.getMobLocationsFile().getPacketMobLocations().keySet()) {
            MobFakeUtil.spawnTemporaryMob(p,
                    FakeRegister.getMobLocationsFile().getPacketMobLocations().get(names),
                    names,
                    FakeRegister.getMobLocationsFile().getPacketMobTypes().get(names),
                    false);
        }
        for (String names : FakeRegister.getBigItemLocationsFile().getPacketBigItemsLocations().keySet()) {
            BigItemFakeUtil.spawnBigItem(p,
                    FakeRegister.getBigItemLocationsFile().getPacketBigItemsLocations().get(names),
                    names,
                    FakeRegister.getBigItemLocationsFile().getPacketBigItemsTypes().get(names));
        }
        for (String names : FakeRegister.getXpOrbLocationsFile().getPacketXPOrb().keySet()) {
            XPOrbFakeUtil.spawnTemporaryXPOrb(p,
                    FakeRegister.getXpOrbLocationsFile().getPacketXPOrb().get(names),
                    names);
        }
        for (String names : FakeRegister.getPaintingLocationsFile().getPacketPainting().keySet()) {
            PaintingFakeUtil.spawnTemporaryPainting(p,
                    FakeRegister.getPaintingLocationsFile().getPacketPainting().get(names),
                    names);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (FakeAPI.getFakePlayers().containsKey(p.getName())) {
            PlayerFakeUtil.unFollowPlayer(p);
            PlayerFakeUtil.normalizeLook(p);
            PlayerFakeUtil.cancelAllSplittedTasks(p);
            FakeAPI.getFakePlayers().remove(p.getName());
        }
        if (FakeAPI.getFakeArmorstands().containsKey(p.getName())) {
            ArmorstandFakeUtil.unFollowArmorstand(p);
            ArmorstandFakeUtil.cancelAllSplittedTasks(p);
            FakeAPI.getFakeArmorstands().remove(p.getName());
        }
        if (FakeAPI.getFakeEndercrystals().containsKey(p.getName())) {
            EndercrystalFakeUtil.cancelAllSplittedTasks(p);
            FakeAPI.getFakeEndercrystals().remove(p.getName());
        }
        if (FakeAPI.getFakeMobs().containsKey(p.getName())) {
            MobFakeUtil.unFollowPlayer(p);
            MobFakeUtil.normalizeLook(p);
            MobFakeUtil.cancelAllSplittedTasks(p);
            FakeAPI.getFakeMobs().remove(p.getName());
        }
        if (FakeAPI.getFakeBigItems().containsKey(p.getName())) {
            BigItemFakeUtil.cancelAllSplittedTasks(p);
            FakeAPI.getFakeBigItems().remove(p.getName());
        }
    }
}
