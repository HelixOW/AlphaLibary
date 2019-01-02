package io.github.alphahelixdev.alpary.utilities.item.data;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class SimpleFireworkEffect implements Serializable {

    private boolean flicker = false;
    private boolean trail = false;

    private Type type;
    private Color[] color = new Color[]{Color.WHITE}, fades = new Color[]{Color.WHITE};

    /**
     * Creates a new {@link SimpleFireworkEffect} to modify {@link org.bukkit.entity.Firework}s
     *
     * @param type the {@link Type} of the {@link org.bukkit.entity.Firework}
     */
    public SimpleFireworkEffect(Type type) {
        this.setType(type);
    }

    public FireworkEffect build() {
        return FireworkEffect.builder().flicker(this.isFlicker()).trail(this.isTrail()).withColor(this.getColor())
                .withFade(this.getFades()).with(this.getType()).build();
    }

    public boolean isFlicker() {
        return this.flicker;
    }

    public SimpleFireworkEffect setFlicker(boolean flicker) {
        this.flicker = flicker;
        return this;
    }

    public boolean isTrail() {
        return this.trail;
    }

    public SimpleFireworkEffect setTrail(boolean trail) {
        this.trail = trail;
        return this;
    }

    public Type getType() {
        return this.type;
    }

    public SimpleFireworkEffect setType(Type type) {
        this.type = type;
        return this;
    }

    public Color[] getColor() {
        return this.color;
    }

    public SimpleFireworkEffect setColor(Color[] color) {
        this.color = color;
        return this;
    }

    public Color[] getFades() {
        return this.fades;
    }

    public SimpleFireworkEffect setFades(Color[] fades) {
        this.fades = fades;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleFireworkEffect that = (SimpleFireworkEffect) o;
        return this.isFlicker() == that.isFlicker() &&
                this.isTrail() == that.isTrail() &&
                this.getType() == that.getType() &&
                Arrays.equals(this.getColor(), that.getColor()) &&
                Arrays.equals(this.getFades(), that.getFades());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(this.isFlicker(), this.isTrail(), this.getType());
        result = 31 * result + Arrays.hashCode(this.getColor());
        result = 31 * result + Arrays.hashCode(this.getFades());
        return result;
    }


    @Override
    public String toString() {
        return "SimpleFireworkEffect{" +
                "                            flicker=" + this.flicker +
                ",                             trail=" + this.trail +
                ",                             type=" + this.type +
                ",                                             color=" + Arrays.toString(this.color) +
                ",                                             fades=" + Arrays.toString(this.fades) +
                '}';
    }
}