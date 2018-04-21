package de.alphahelix.alphalibary.forms.actions;

import de.alphahelix.alphalibary.forms.FormAction;
import de.alphahelix.alphalibary.forms.particledata.EffectData;
import org.apache.commons.lang.Validate;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ParticleAction implements FormAction {
	
	private Effect effect;
	private EffectData<?> effectData;
	
	public ParticleAction(Effect effect, EffectData<?> data) {
		
		this.effect = effect;
		
		if(data != null)
			Validate.isTrue(effect.getData() != null && effect.getData().isAssignableFrom(data.getDataValue().getClass()), "Wrong kind of effectData for this effect!");
		else {
			Validate.isTrue(effect.getData() == null, "Wrong kind of effectData for this effect!");
			data = new EffectData<>(null);
		}
		
		this.effectData = data;
	}
	
	@Override
	public void action(Player p, Location loc) {
		if(p == null)
			loc.getWorld().playEffect(loc, effect, effectData.getDataValue());
		else
			p.playEffect(loc, effect, effectData.getDataValue());
	}
}
