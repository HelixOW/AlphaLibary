package de.alphahelix.alphalibary.schematic;

import de.alphahelix.alphalibary.core.utils.Cuboid;
import de.alphahelix.alphalibary.core.utils.JSONUtil;
import de.alphahelix.alphalibary.storage.file.SimpleJSONFile;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.material.MaterialData;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

interface UndoSave {
    Material getType();

    MaterialData getData();

    Location getOld();
}

@SuppressWarnings("ALL")
public class SchematicManager {

    private static final HashMap<String, ArrayList<UndoSave>> SAVE_MAP = new HashMap<>();

    public static void save(Location location1, Location location2, String name) {
        new SchematicFile(new Schematic(name, SchematicManager.getBlocks(location1, location2)));
    }

    public static void paste(String name, Location loc) {
        Schematic schematic = SchematicFile.getSchematic(name);
        ArrayList<UndoSave> save = new ArrayList<>();

        for (Schematic.LocationDiff diff : schematic.getBlocks()) {
            Block toEdit = loc.clone().add(diff.getX(), diff.getY(), diff.getZ()).getBlock();

            save.add(new UndoSave() {
                @Override
                public Material getType() {
                    return toEdit.getType();
                }

                @Override
                public MaterialData getData() {
                    return toEdit.getState().getData();
                }

                @Override
                public Location getOld() {
                    return toEdit.getLocation();
                }
            });

            toEdit.setType(diff.getBlockType());
            toEdit.getState().setData(diff.getBlockData());
        }

        SAVE_MAP.put(name, save);
    }

    public static void undo(String name) {
        if (SAVE_MAP.containsKey(name))
            for (UndoSave us : SAVE_MAP.get(name)) {
                us.getOld().getBlock().setType(us.getType());
                us.getOld().getBlock().getState().setData(us.getData());
            }
    }

    private static ArrayList<Schematic.LocationDiff> getBlocks(Location l1, Location l2) {
        ArrayList<Schematic.LocationDiff> b = new ArrayList<>();
        List<Block> blocks = new Cuboid(l1, l2).getBlocks();

        for (Block block : blocks) {
            if (block.getType() == Material.AIR) continue;

            b.add(new Schematic.LocationDiff() {
                @Override
                public Material getBlockType() {
                    return block.getType();
                }

                @Override
                public MaterialData getBlockData() {
                    return block.getState().getData();
                }

                @Override
                public int getBlockPower() {
                    return block.getBlockPower();
                }

                @Override
                public int getX() {
                    return block.getX() - l1.getBlockX();
                }

                @Override
                public int getY() {
                    return block.getY() - l1.getBlockY();
                }

                @Override
                public int getZ() {
                    return block.getZ() - l1.getBlockZ();
                }
            });
        }

        return b;
    }
}

class SchematicFile extends SimpleJSONFile {

    public SchematicFile(Schematic schematic) {
        super("plugins/AlphaLibary/schematics", schematic.getName() + ".json");
        setValue(schematic.getName(), Base64Coder.encodeString(JSONUtil.getGson().toJson(schematic)));
    }

    public static Schematic getSchematic(String name) {
        Validate.isTrue(new File("plugins/AlphaLibary/schematics", name + ".json").exists(), "There is no schematic called " + name);

        SimpleJSONFile schemFile = new SimpleJSONFile("plugins/AlphaLibary/schematics", name + ".json");

        String jsonInBase64 = schemFile.getValue(name, String.class);
        String json = Base64Coder.decodeString(jsonInBase64);

        return JSONUtil.getGson().fromJson(json, Schematic.class);
    }
}