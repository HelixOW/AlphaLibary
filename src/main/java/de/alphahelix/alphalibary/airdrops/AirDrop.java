package de.alphahelix.alphalibary.airdrops;

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

import java.util.ArrayList;
import java.util.Collections;

public class AirDrop extends SimpleListener {

    private static ArrayList<Entity> airDrops = new ArrayList<>();

    private World world;
    private DropOffLocation dropOff;
    private ArrayList<ItemStack> dropList = new ArrayList<>();

    public AirDrop(World world, Location center, int radius, ItemStack... drops) {
        super();
        this.world = world;
        this.dropOff = new DropOffLocation(center, radius);
        Collections.addAll(this.dropList, drops);
    }

    public AirDrop(World world, int radius, ItemStack... drops) {
        super();
        this.world = world;
        this.dropOff = new DropOffLocation(world.getSpawnLocation(), radius);
        Collections.addAll(this.dropList, drops);
    }

    public void setDropOff(Location center, int radius) {
        this.dropOff = new DropOffLocation(center, radius);
    }

    public void addDrops(ItemStack... drops) {
        Collections.addAll(dropList, drops);
    }

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
        FallingBlock chest = world.spawnFallingBlock(dropOff.getDropOff(), Material.CHEST, (byte) 0);

        airDrops.add(chest);
    }

    @EventHandler
    public void onHit(EntityChangeBlockEvent e) {
        if (e.getEntity() instanceof FallingBlock) {
            if (airDrops.contains(e.getEntity())) {
                Util.runLater(5, false, () -> {
                    Chest chest = (Chest) e.getBlock().getState();

                    for (ItemStack is : dropList)
                        chest.getBlockInventory().addItem(is);

                    airDrops.remove(e.getEntity());
                });
            }
        }
    }
}
