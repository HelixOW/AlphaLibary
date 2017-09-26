package de.alphahelix.alphalibary.forms.particles.form.d2;

import de.alphahelix.alphalibary.forms.d2.ArrowForm;
import de.alphahelix.alphalibary.forms.particles.data.EffectData;
import org.apache.commons.lang.Validate;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

@SuppressWarnings("ALL")
public class ArrowParticleForm extends ArrowForm {

    public ArrowParticleForm(Effect effect, EffectData<?> effectData, BlockFace direction, Location location, String axis, double dense, double lenght, double width) {
        super(direction, location, axis, dense, lenght, width, null);

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
