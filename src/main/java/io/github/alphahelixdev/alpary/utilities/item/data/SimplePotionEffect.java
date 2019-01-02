package io.github.alphahelixdev.alpary.utilities.item.data;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.Serializable;
import java.util.Objects;

public class SimplePotionEffect implements Serializable {

    private int durationInSec, amplifier;
    private PotionEffectType potionType;

    /**
     * Creates a new {@link SimplePotionEffect} to modify Potion
     *
     * @param durationInSec the amount of seconds the Potion should stay active
     * @param type          the {@link PotionEffectType} of the Potion
     * @param amplifier     the level of the Potion
     */
    public SimplePotionEffect(int durationInSec, PotionEffectType type, int amplifier) {
        this.setDurationInSec(durationInSec);
        this.setPotionType(type);
        this.setAmplifier(amplifier);
    }

    public PotionEffect createEffect() {
        return new PotionEffect(this.getPotionType(), this.getDurationInSec() * 20, this.getAmplifier());
    }

    public int getDurationInSec() {
        return this.durationInSec;
    }

    public SimplePotionEffect setDurationInSec(int durationInSec) {
        this.durationInSec = durationInSec;
        return this;
    }

    public int getAmplifier() {
        return this.amplifier;
    }

    public SimplePotionEffect setAmplifier(int amplifier) {
        this.amplifier = amplifier;
        return this;
    }

    public PotionEffectType getPotionType() {
        return this.potionType;
    }

    public SimplePotionEffect setPotionType(PotionEffectType potionType) {
        this.potionType = potionType;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimplePotionEffect effect = (SimplePotionEffect) o;
        return this.getDurationInSec() == effect.getDurationInSec() &&
                this.getAmplifier() == effect.getAmplifier() &&
                Objects.equals(this.getPotionType(), effect.getPotionType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getDurationInSec(), this.getAmplifier(), this.getPotionType());
    }


    @Override
    public String toString() {
        return "SimplePotionEffect{" +
                "                            durationInSec=" + this.durationInSec +
                ",                             amplifier=" + this.amplifier +
                ",                             potionType=" + this.potionType +
                '}';
    }
}
