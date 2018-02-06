package de.alphahelix.alphalibary.forms.particles.form.d3;

import de.alphahelix.alphalibary.forms.d2.RectangleForm;
import de.alphahelix.alphalibary.forms.d3.CuboidForm;
import de.alphahelix.alphalibary.forms.particles.data.EffectData;
import org.apache.commons.lang.Validate;
import org.bukkit.Effect;


public class CuboidParticleForm extends CuboidForm {
    public CuboidParticleForm(Effect effect, EffectData<?> effectData, RectangleForm rectangleForm, double width) {
        super(rectangleForm, width);

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
