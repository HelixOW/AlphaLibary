package io.github.alphahelixdev.alpary.input.gui;

import io.github.alphahelixdev.alpary.reflection.nms.BlockPos;
import io.github.alphahelixdev.alpary.reflection.nms.packets.OpenSignEditorPacket;
import io.github.alphahelixdev.alpary.utils.Utils;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SignGUI implements InputGUI {

    private static final List<String> OPEN_GUIS = new ArrayList<>();

    public static List<String> getOpenGUIs() {
        return OPEN_GUIS;
    }

    @Override
    public void openGUI(Player p) {
        BlockPos s = new BlockPos(0, 0, 0);

        Utils.nms().sendPacket(p, new OpenSignEditorPacket(s).getPacket(false));

        SignGUI.getOpenGUIs().add(p.getName());
    }
}
