package de.alphahelix.alphalibary.particles.form.d3;

import de.alphahelix.alphalibary.forms.d3.ConeForm;
import de.alphahelix.alphalibary.particles.data.EffectData;
import org.apache.commons.lang.Validate;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

public class ConeParticleForm extends ConeForm {
    public ConeParticleForm(Effect effect, EffectData<?> effectData, Location location, double dense, double baseRadius, double size, boolean filled, BlockFace direction) {
        super(location, dense, baseRadius, size, filled, direction, null);

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
