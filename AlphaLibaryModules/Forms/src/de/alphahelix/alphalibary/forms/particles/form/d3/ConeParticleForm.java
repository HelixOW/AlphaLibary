package de.alphahelix.alphalibary.forms.particles.form.d3;

import de.alphahelix.alphalibary.forms.d3.ConeForm;
import de.alphahelix.alphalibary.forms.particles.data.EffectData;
import org.apache.commons.lang.Validate;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.util.Vector;


public class ConeParticleForm extends ConeForm {
	public ConeParticleForm(Effect effect, EffectData<?> effectData, Location location, Vector axis, double dense, double angle, double baseRadius, double size, boolean filled) {
		super(location, axis, dense, angle, baseRadius, size, filled, null);
		
		if(effectData != null)
			Validate.isTrue(effect.getData() != null && effect.getData().isAssignableFrom(effectData.getDataValue().getClass()), "Wrong kind of effectData for this effect!");
		else {
			Validate.isTrue(effect.getData() == null, "Wrong kind of effectData for this effect!");
			effectData = new EffectData<>(null);
		}
		
		EffectData<?> finalEffectData = effectData;
		
		setAction((p, loc) -> p.playEffect(loc, effect, finalEffectData.getDataValue()));
	}
}
