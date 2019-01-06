package io.github.alphahelixdev.alpary.reflection.nms;

import io.github.alphahelixdev.alpary.utils.NMSUtil;
import io.github.alphahelixdev.alpary.utils.Utils;
import io.github.alphahelixdev.helius.reflection.SaveConstructor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Objects;

public class SimpleTitle {

    private static final SaveConstructor TITLE_PACKET = NMSUtil.getReflections().getDeclaredConstructor(Utils.nms().getNMSClass("PacketPlayOutTitle"), Utils.nms().getNMSClass("PacketPlayOutTitle$EnumTitleAction"), Utils.nms().getNMSClass("IChatBaseComponent"));
    private static final SaveConstructor TIMING_PACKET = NMSUtil.getReflections().getDeclaredConstructor(Utils.nms().getNMSClass("PacketPlayOutTitle"), int.class, int.class, int.class);

    private String title, sub;
    private int fadeIn, fadeOut, stay;

    public SimpleTitle(String title, String sub) {
        this(title, sub, 1, 1, 3);
    }

    public SimpleTitle(String title, String sub, int fadeIn, int fadeOut, int stay) {
        this.title = title;
        this.sub = sub;
        this.fadeIn = fadeIn;
        this.fadeOut = fadeOut;
        this.stay = stay;
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public int getFadeIn() {
        return fadeIn;
    }

    public void setFadeIn(int fadeIn) {
        this.fadeIn = fadeIn;
    }

    public int getFadeOut() {
        return fadeOut;
    }

    public void setFadeOut(int fadeOut) {
        this.fadeOut = fadeOut;
    }

    public int getStay() {
        return stay;
    }

    public void setStay(int stay) {
        this.stay = stay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleTitle that = (SimpleTitle) o;
        return fadeIn == that.fadeIn &&
                fadeOut == that.fadeOut &&
                stay == that.stay &&
                Objects.equals(title, that.title) &&
                Objects.equals(sub, that.sub);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, sub, fadeIn, fadeOut, stay);
    }

    @Override
    public String toString() {
        return "SimpleTitle{" +
                "title='" + title + '\'' +
                ", sub='" + sub + '\'' +
                ", fadeIn=" + fadeIn +
                ", fadeOut=" + fadeOut +
                ", stay=" + stay +
                '}';
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
