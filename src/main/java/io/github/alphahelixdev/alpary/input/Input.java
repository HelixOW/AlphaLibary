package io.github.alphahelixdev.alpary.input;

import io.github.alphahelixdev.alpary.Alpary;
import io.github.alphahelixdev.alpary.input.events.ArmorChangeEvent;
import io.github.alphahelixdev.alpary.input.events.ItemRenameEvent;
import io.github.alphahelixdev.alpary.input.events.PlayerInputEvent;
import io.github.alphahelixdev.alpary.input.gui.AnvilGUI;
import io.github.alphahelixdev.alpary.input.gui.SignGUI;
import io.github.alphahelixdev.alpary.reflection.nms.BlockPos;
import io.github.alphahelixdev.alpary.reflection.nms.netty.PacketListener;
import io.github.alphahelixdev.alpary.reflection.nms.netty.handler.PacketHandler;
import io.github.alphahelixdev.alpary.reflection.nms.netty.handler.PacketOptions;
import io.github.alphahelixdev.alpary.reflection.nms.netty.handler.ReceivedPacket;
import io.github.alphahelixdev.alpary.reflection.nms.netty.handler.SentPacket;
import io.github.alphahelixdev.alpary.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Input {

    private static final Map<UUID, Double> OLD_VALUES = new HashMap<>();
    private static final List<InputHandler> HANDLERS = new ArrayList<>();

    public Input() {
        Alpary.getInstance().reflections().getTypesAnnotatedWith(InputListener.class).forEach(System.out::println);
        Alpary.getInstance().reflections().getTypesAnnotatedWith(InputListener.class).stream().filter(InputHandler.class::isAssignableFrom).filter(aClass -> {
            try {
                return aClass.getDeclaredConstructor() != null;
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                return false;
            }
        }).forEach(inputHandlingClass -> {
            try {
                HANDLERS.add((InputHandler) inputHandlingClass.getDeclaredConstructor().newInstance());
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        });
    }

    public static Map<UUID, Double> getOldValues() {
        return OLD_VALUES;
    }

    public static List<InputHandler> handlers() {
        return HANDLERS;
    }

    public void startListening() {
        PacketListener.addPacketHandler(new PacketHandler() {
            @Override
            @PacketOptions(forcePlayer = true)
            public void onSend(SentPacket packet) {
                if (packet.getPacketName().equals("PacketPlayOutUpdateAttributes")) {
                    Player p = packet.getPlayer();

                    if ((int) packet.getPacketValue("a") == p.getEntityId()) {
                        Alpary.getInstance().uuidFetcher().getUUID(p, id -> {
                            double nV = p.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                            double oV;

                            if (Input.getOldValues().containsKey(id)) {
                                if (Input.getOldValues().get(id) != nV) {
                                    oV = Input.getOldValues().get(id);

                                    ArmorChangeEvent ace = new ArmorChangeEvent(p, oV, nV);
                                    Bukkit.getPluginManager().callEvent(ace);

                                    if (!ace.isCancelled())
                                        Input.getOldValues().put(id, nV);
                                    else
                                        packet.setCancelled(true);

                                }
                            } else {
                                ArmorChangeEvent ace = new ArmorChangeEvent(p, 0.0, nV);
                                Bukkit.getPluginManager().callEvent(ace);

                                if (!ace.isCancelled())
                                    Input.getOldValues().put(id, nV);
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
                    BlockPos bp = Utils.nms().fromBlockPostition(packet.getPacketValue("a"));
                    if (bp.getX() == 0 && bp.getY() == 0 && bp.getZ() == 0) {
                        if (!SignGUI.getOpenGUIs().contains(packet.getPlayer().getName())) return;
                        int i = 0;
                        for (String line : (String[]) packet.getPacketValue(1)) {
                            if (i == 1) {
                                Bukkit.getPluginManager().callEvent(new PlayerInputEvent(packet.getPlayer(), line));
                                for (InputHandler handler : Input.handlers())
                                    handler.handle(packet.getPlayer(), line);
                            }
                            i++;
                        }
                        SignGUI.getOpenGUIs().remove(packet.getPlayer().getName());
                    }
                } else if (packet.getPacketName().equals("PacketPlayInWindowClick")) {
                    if (packet.getPlayer() == null) return;
                    if (!AnvilGUI.getOpenGUIs().contains(packet.getPlayer().getName())) return;
                    InventoryView view = packet.getPlayer().getOpenInventory();

                    if (view != null && view.getTopInventory().getType() == InventoryType.ANVIL) {
                        int slot = (int) packet.getPacketValue("slot");

                        if (slot == 2) {
                            ItemStack is = Utils.nms().getBukkitItemStack(packet.getPacketValue("item"));
                            if (is == null) return;
                            if (is.hasItemMeta()) {
                                if (is.getItemMeta().hasDisplayName()) {
                                    ItemRenameEvent event = new ItemRenameEvent(packet.getPlayer(), view, is.getItemMeta().getDisplayName());
                                    Bukkit.getPluginManager().callEvent(event);
                                    Bukkit.getPluginManager().callEvent(new PlayerInputEvent(packet.getPlayer(), is.getItemMeta().getDisplayName()));

                                    for (InputHandler handler : Input.handlers()) {
                                        handler.handle(packet.getPlayer(), view, is.getItemMeta().getDisplayName());
                                        handler.handle(packet.getPlayer(), is.getItemMeta().getDisplayName());
                                    }

                                    AnvilGUI.getOpenGUIs().remove(packet.getPlayer().getName());

                                    if (event.isCancelled()) packet.getPlayer().closeInventory();
                                }
                            }
                        }
                    }
                }
            }
        });
    }
}
