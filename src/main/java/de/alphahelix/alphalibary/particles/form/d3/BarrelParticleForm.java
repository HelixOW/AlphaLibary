package de.alphahelix.alphalibary.particles.form.d3;

import de.alphahelix.alphalibary.forms.d2.BarrelForm;
import de.alphahelix.alphalibary.particles.data.EffectData;
import org.apache.commons.lang.Validate;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

public class BarrelParticleForm extends BarrelForm {
    public BarrelParticleForm(Effect effect, EffectData<?> effectData, Location location, String axis, double dense, double depth, double radius, BlockFace direction) {
        super(location, axis, dense, depth, radius, direction, null);

        if (effectData != null)
            Validate.isTrue(effect.getData() != null && effect.getData().isAssignableFrom(effectData.getDataValue().getClass()), "Wrong kind of effectData for this effect!");
        else {
            Validate.isTrue(effect.getData() == null, "Wrong kind of effectData for this effect!");
            effectData = new EffectData<>(null);
        }

        EffectData<?> finalEffectData = effectData;

        setAction((p, loc) -> p.playEffect(loc, effect, finalEffectData.getDataValue()));
    }
}
