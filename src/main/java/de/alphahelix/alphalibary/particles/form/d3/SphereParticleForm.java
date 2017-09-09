package de.alphahelix.alphalibary.particles.form.d3;

import de.alphahelix.alphalibary.forms.d3.SphereForm;
import de.alphahelix.alphalibary.particles.data.EffectData;
import org.apache.commons.lang.Validate;
import org.bukkit.Effect;
import org.bukkit.Location;

public class SphereParticleForm extends SphereForm {

    public SphereParticleForm(Effect effect, EffectData<?> effectData, Location location, double dense, double radius) {
        super(location, dense, radius, null);

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
