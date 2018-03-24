package de.alphahelix.alphalibary.forms;

import de.alphahelix.alphalibary.core.AlphaLibary;
import de.alphahelix.alphalibary.core.AlphaModule;
import de.alphahelix.alphalibary.core.utils.ScheduleUtil;
import de.alphahelix.alphalibary.forms.particles.form.d2.triangles.EquilateralTriangleParticleForm;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.util.Vector;

public class FormTester implements AlphaModule, Listener {
	
	public FormTester() {
		Bukkit.getPluginManager().registerEvents(this, AlphaLibary.getInstance());
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		final EquilateralTriangleParticleForm[] triangleParticleForm = new EquilateralTriangleParticleForm[1];
		
		ScheduleUtil.runLater(1, false, () -> triangleParticleForm[0] = new EquilateralTriangleParticleForm(
				Effect.COLOURED_DUST, null, e.getPlayer().getLocation(), new Vector(1, 0, 0),
				.5, 0, e.getPlayer().getLocation().getDirection(), 3
		));
		
		ScheduleUtil.runTimer(5, 2, false, () -> triangleParticleForm[0].send(e.getPlayer()));
	}
}
