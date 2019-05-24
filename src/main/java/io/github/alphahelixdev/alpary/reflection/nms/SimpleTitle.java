package io.github.alphahelixdev.alpary.reflection.nms;

import io.github.alphahelixdev.alpary.utils.NMSUtil;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.alphahelixdev.helius.reflection.SaveConstructor;
import lombok.*;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class SimpleTitle {

    private static final SaveConstructor TITLE_PACKET = NMSUtil.getReflections().getDeclaredConstructor(Utils.nms().getNMSClass("PacketPlayOutTitle"), Utils.nms().getNMSClass("PacketPlayOutTitle$EnumTitleAction"), Utils.nms().getNMSClass("IChatBaseComponent"));
    private static final SaveConstructor TIMING_PACKET = NMSUtil.getReflections().getDeclaredConstructor(Utils.nms().getNMSClass("PacketPlayOutTitle"), int.class, int.class, int.class);
	
	@NonNull
	private String title, sub;
    private int fadeIn, fadeOut, stay;

    public SimpleTitle send(Player p) {
        Object pTitle = TITLE_PACKET.newInstance(true, TitleAction.TITLE.getNmsEnumObject(), Utils.nms().toIChatBaseComponentArray(this.title));
        Object pSubTitle = TITLE_PACKET.newInstance(true, TitleAction.SUBTITLE.getNmsEnumObject(), Utils.nms().toIChatBaseComponentArray(this.sub));
        Object pTimings = TIMING_PACKET.newInstance(true, this.fadeIn * 20, this.stay * 20, this.fadeOut * 20);

        Utils.nms().sendPackets(p, pTimings, pTitle, pSubTitle);
        return this;
    }

    public SimpleTitle send(World w) {
        w.getPlayers().forEach(this::send);
        return this;
    }

    public SimpleTitle send() {
        Bukkit.getOnlinePlayers().forEach(this::send);
        return this;
    }

    public enum TitleAction {

        TITLE,
        SUBTITLE,
        TIMES,
        CLEAR,
        RESET;

        Object getNmsEnumObject() {
            if (ordinal() > 4)
                return null;
            return Utils.nms().getNMSClass("PacketPlayOutTitle$EnumTitleAction").getEnumConstants()[ordinal()];
        }
    }
}
