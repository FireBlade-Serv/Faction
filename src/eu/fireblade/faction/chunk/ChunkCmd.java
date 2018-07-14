package eu.fireblade.faction.chunk;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChunkCmd implements CommandExecutor{
	
	private ChunkManager cm;

	public ChunkCmd(ChunkManager cm) {
		this.cm = cm;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(label.equals("claim")) {
				FactionChunk fc = new FactionChunk(p.getLocation());
				if(!cm.isClaimed(fc.getChunk())) {
					cm.addToConfig(fc.getChunk());
					p.sendMessage("Chunk claim");
				}else {
					p.sendMessage("Ce chunk est déjà claim !");
				}
			}
		}
		return false;
	}

	
	
}
