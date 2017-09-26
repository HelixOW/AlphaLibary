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

package de.alphahelix.alphalibary.input;

import de.alphahelix.alphalibary.core.uuid.UUIDFetcher;
import de.alphahelix.alphalibary.input.events.ArmorChangeEvent;
import de.alphahelix.alphalibary.input.events.ItemRenameEvent;
import de.alphahelix.alphalibary.input.events.PlayerInputEvent;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import de.alphahelix.alphalibary.reflection.nms.BlockPos;
import de.alphahelix.alphalibary.reflection.nms.netty.PacketListenerAPI;
import de.alphahelix.alphalibary.reflection.nms.netty.handler.PacketHandler;
import de.alphahelix.alphalibary.reflection.nms.netty.handler.PacketOptions;
import de.alphahelix.alphalibary.reflection.nms.netty.handler.ReceivedPacket;
import de.alphahelix.alphalibary.reflection.nms.netty.handler.SentPacket;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;
import java.util.WeakHashMap;

public abstract class InputGUI {

    private static final WeakHashMap<UUID, Double> OLD_VALUES = new WeakHashMap<>();
    private static final ArrayList<String> OPEN_GUIS = new ArrayList<>();

    static {
        PacketListenerAPI.addPacketHandler(new PacketHandler() {
            @Override
            @PacketOptions(forcePlayer = true)
            public void onSend(SentPacket packet) {
                if (packet.getPacketName().equals("PacketPlayOutUpdateAttributes")) {
                    Player p = packet.getPlayer();

                    if ((int) packet.getPacketValue("a") == p.getEntityId()) {
                        UUIDFetcher.getUUID(p, id -> {
                            double nV = p.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                            double oV;

                            if (OLD_VALUES.containsKey(id)) {
                                if (OLD_VALUES.get(id) != nV) {
                                    oV = OLD_VALUES.get(id);

                                    ArmorChangeEvent ace = new ArmorChangeEvent(p, oV, nV);
                                    Bukkit.getPluginManager().callEvent(ace);

                                    if (!ace.isCancelled())
                                        OLD_VALUES.put(id, nV);
                                    else
                                        packet.setCancelled(true);

                                }
                            } else {
                                ArmorChangeEvent ace = new ArmorChangeEvent(p, 0.0, nV);
                                Bukkit.getPluginManager().callEvent(ace);

                                if (!ace.isCancelled())
                                    OLD_VALUES.put(id, nV);
                                else
                                    packet.setCancelled(true);
                            }
                        });
                    }
                }
            }

            @Override
            public void onReceive(ReceivedPacket packet) {
                if (packet.getPacketName().equals("PacketPlayInUpdateSign")) {
                    if (packet.getPlayer() == null) return;
                    BlockPos bp = ReflectionUtil.fromBlockPostition(packet.getPacketValue("a"));
                    if (bp.getX() == 0 && bp.getY() == 0 && bp.getZ() == 0) {
                        if (!SignGUI.getOpenGuis().contains(packet.getPlayer().getName())) return;
                        int i = 0;
                        for (String line : (String[]) packet.getPacketValue(1)) {
                            if (i == 1)
                                Bukkit.getPluginManager().callEvent(new PlayerInputEvent(packet.getPlayer(), line));
                            i++;
                        }
                        SignGUI.getOpenGuis().remove(packet.getPlayer().getName());
                    }
                } else if (packet.getPacketName().equals("PacketPlayInWindowClick")) {
                    if (packet.getPlayer() == null) return;
                    if (!AnvilGUI.getOpenGuis().contains(packet.getPlayer().getName())) return;
                    InventoryView view = packet.getPlayer().getOpenInventory();

                    if (view != null && view.getTopInventory().getType() == InventoryType.ANVIL) {
                        int slot = (int) packet.getPacketValue("slot");

                        if (slot == 2) {
                            ItemStack is = ReflectionUtil.getBukkitItemStack(packet.getPacketValue("item"));
                            if (is == null) return;
                            if (is.hasItemMeta()) {
                                if (is.getItemMeta().hasDisplayName()) {
                                    ItemRenameEvent event = new ItemRenameEvent(packet.getPlayer(), view, is.getItemMeta().getDisplayName());
                                    Bukkit.getPluginManager().callEvent(event);
                                    Bukkit.getPluginManager().callEvent(new PlayerInputEvent(packet.getPlayer(), is.getItemMeta().getDisplayName()));

                                    AnvilGUI.getOpenGuis().remove(packet.getPlayer().getName());

                                    if (event.isCancelled()) packet.getPlayer().closeInventory();
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    public static ArrayList<String> getOpenGuis() {
        return OPEN_GUIS;
    }

    public abstract void openGUI(Player p);
}
