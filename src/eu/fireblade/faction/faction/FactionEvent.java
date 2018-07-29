package eu.fireblade.faction.faction;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class FactionEvent implements Listener {
	
	private FactionManager fm;

	public FactionEvent(FactionManager fm) {
		this.fm = fm;
	}
	
	@EventHandler
	private void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();		
		if(fm.containsPlayer(p)) {
			p.sendMessage("Mouvement detecté téléportation annulée !");
			fm.removePlayer(p);
		}
	}
	
	
	
}
