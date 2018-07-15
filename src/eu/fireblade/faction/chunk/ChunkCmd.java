package eu.fireblade.faction.chunk;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.fireblade.faction.faction.FactionManager;
import eu.fireblade.faction.faction.FactionRank;

public class ChunkCmd implements CommandExecutor{
	
	private ChunkManager cm;
	private FactionManager fm;
	
	public ChunkCmd(ChunkManager cm, FactionManager fm) {
		this.cm = cm;
		this.fm = fm;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(label.equals("claim")) {
				if(p.getWorld().getName().equals("world")) {
					if(fm.hasFaction(p)) {
						if(fm.getRank(p).equals(FactionRank.OWNER) || fm.getRank(p).equals(FactionRank.ADMIN)) {
							FactionChunk fc = new FactionChunk(p.getLocation());
							if(!cm.isClaimed(fc.getChunk())) {
								cm.claimChunk(fc.getChunk(), p);
								p.sendMessage("Chunk claim");
							}else p.sendMessage("Ce chunk est déjà claim !");
						}else p.sendMessage("Seul l'owner et les administrateurs d'un faction peuvent claim !");
					}else p.sendMessage("Vous ne pouvez pas claim sans avoir de faction !");
				}else p.sendMessage("Vous ne pouvez pas claim ailleur que dans l'overworld !");
			}else if(label.equals("unclaim")) {
				if(p.getWorld().getName().equals("world")) {
					if(fm.hasFaction(p)) {
						if(fm.getRank(p).equals(FactionRank.OWNER) || fm.getRank(p).equals(FactionRank.ADMIN)) {
							FactionChunk fc = new FactionChunk(p.getLocation());
							if(cm.isClaimed(fc.getChunk())) {
								if(cm.hasClaim(fc.getChunk(), fm.getFaction(p))) {
									cm.unclaimChunk(fc.getChunk(), p);
									p.sendMessage("Chunk unclaim");
								}else p.sendMessage("Ce claim ne vous appartient pas !");
							}else p.sendMessage("Ce chunk n'est pas claim !");
						}
					}
				}else p.sendMessage("Vous ne pouvez pas unclaim ailleur que dans l'overworld !");
			}
		} return false;
	}	
}