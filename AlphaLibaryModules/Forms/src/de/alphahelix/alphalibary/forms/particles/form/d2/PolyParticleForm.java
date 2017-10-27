package de.alphahelix.alphalibary.forms.particles.form.d2;

import de.alphahelix.alphalibary.core.utils.Pair;
import de.alphahelix.alphalibary.forms.d2.PolyForm;
import de.alphahelix.alphalibary.forms.particles.data.EffectData;
import org.apache.commons.lang.Validate;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.util.Vector;

@SuppressWarnings("ALL")
public class PolyParticleForm extends PolyForm {
    public PolyParticleForm(Effect effect, EffectData<?> effectData, Location location, double dense, Pair<Vector, Vector>... points) {
        super(location, dense, null, points);

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
