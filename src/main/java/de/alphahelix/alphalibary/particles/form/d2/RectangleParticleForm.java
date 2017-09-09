package de.alphahelix.alphalibary.particles.form.d2;

import de.alphahelix.alphalibary.forms.d2.RectangleForm;
import de.alphahelix.alphalibary.particles.data.EffectData;
import org.apache.commons.lang.Validate;
import org.bukkit.Effect;
import org.bukkit.Location;

public class RectangleParticleForm extends RectangleForm {

    public RectangleParticleForm(Effect effect, EffectData<?> effectData, Location location, String axis, double dense, double width, double lenght, boolean filled) {
        super(location, axis, dense, width, lenght, filled, null);

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
