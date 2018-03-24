package de.alphahelix.alphalibary.forms.particles.form.d2.triangles;

import de.alphahelix.alphalibary.forms.d2.triangles.EquilateralTriangleForm;
import de.alphahelix.alphalibary.forms.particles.data.EffectData;
import org.apache.commons.lang.Validate;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class EquilateralTriangleParticleForm extends EquilateralTriangleForm {
	public EquilateralTriangleParticleForm(Effect effect, EffectData<?> effectData, Location location, Vector axis, double dense, double angle, Vector direction, double size) {
		super(location, axis, dense, angle, direction, size, null);
		
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
