package eu.fireblade.faction.chunk;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import eu.fireblade.faction.Config;
import eu.fireblade.faction.faction.FactionManager;
import eu.fireblade.faction.faction.FactionRank;

public class ChunkCmd implements CommandExecutor{
	
	private Config config;
	private ChunkManager cm;
	private FactionManager fm;
	
	public ChunkCmd(ChunkManager cm, FactionManager fm, Config config) {
		this.cm = cm;
		this.fm = fm;
		this.config = config;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		YamlConfiguration serverConfig = this.config.getNewConfiguration();
		String prefix = (String) serverConfig.get("faction.prefix");
		
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(label.equals(serverConfig.get("ChunkCmd.argument.Claim"))) {
				if(p.getWorld().getName().equals("world")) {
					if(fm.hasFaction(p)) {
						if(fm.getRank(p).equals(FactionRank.OWNER) || fm.getRank(p).equals(FactionRank.ADMIN)) {
							FactionChunk fc = new FactionChunk(p.getLocation());
							if(!cm.isClaimed(fc.getChunk())) {
								cm.claimChunk(fc.getChunk(), p);
								p.sendMessage(prefix+" "+serverConfig.get("ChunkCmd.message.Chunk claim"));
							}else p.sendMessage(prefix+" "+serverConfig.get("ChunkCmd.message.Chunk indisponible"));
						}else p.sendMessage(prefix+" "+serverConfig.get("ChunkCmd.message.Claim pour owner et admins uniquement"));
					}else p.sendMessage(prefix+" "+serverConfig.get("ChunkCmd.message.Absence de faction"));
				}else p.sendMessage(prefix+" "+serverConfig.get("ChunkCmd.message.Claim dans overworld"));
			}else if(label.equals(serverConfig.get("ChunkCmd.argument.UnClaim"))) {
				if(p.getWorld().getName().equals("world")) {
					if(fm.hasFaction(p)) {
						if(fm.getRank(p).equals(FactionRank.OWNER) || fm.getRank(p).equals(FactionRank.ADMIN)) {
							FactionChunk fc = new FactionChunk(p.getLocation());
							if(cm.isClaimed(fc.getChunk())) {
								if(cm.hasClaim(fc.getChunk(), fm.getFaction(p))) {
									cm.unclaimChunk(fc.getChunk(), p);
									p.sendMessage(prefix+" "+serverConfig.get("ChunkCmd.message.Chunk unclaim"));
								}else p.sendMessage(prefix+" "+serverConfig.get("ChunkCmd.message.Claim non posseder"));
							}else p.sendMessage(prefix+" "+serverConfig.get("ChunkCmd.message.Claim libre"));
						}
					}
				}else p.sendMessage(prefix+" "+serverConfig.get("ChunkCmd.message.Claim dans overworld"));
			}
		} return false;
	}	
}