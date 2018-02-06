package de.alphahelix.alphalibary.forms.particles.form.d2;

import de.alphahelix.alphalibary.forms.d2.PointForm;
import de.alphahelix.alphalibary.forms.particles.data.EffectData;
import org.apache.commons.lang.Validate;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.util.Vector;


public class PointParticleForm extends PointForm {
    public PointParticleForm(Effect effect, EffectData<?> effectData, Location location, double dense, Vector p1, Vector p2) {
        super(location, dense, null, p1, p2);

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
