package eu.fireblade.faction.chunk;

import java.io.IOException;

import org.bukkit.Chunk;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import eu.fireblade.faction.Main;
import eu.fireblade.faction.faction.FactionManager;

public class ChunkManager {
	
	private ChunkConfig config;
	private Main main;
	private FactionManager fm;

	public ChunkManager(ChunkConfig config, Main main, FactionManager fm) {
		this.main = main;
		this.config = config;
		this.fm = fm;
	}
	
	public boolean isClaimed(Chunk c){
		return this.config.getNewConfiguration().contains(this.format(c));
	}
	
	public String format(Chunk c) {
		return "x="+c.getX()+" "+"z="+c.getZ();
	}
	
	public Chunk readFormat(String text) {
		String partx = null, partz = null;
		
		for(String part : text.split(" ")) {
			if(part.startsWith("x=")) partx = part.substring(2);
			else if(part.startsWith("z=")) partz = part.substring(2);
		} 
		return this.main.getServer().getWorld("world").getChunkAt(Integer.valueOf(partx), Integer.valueOf(partz));
	}
	
	public void claimChunk(Chunk c, Player p) {
		if(!this.isClaimed(c)) {
			YamlConfiguration config = this.config.getNewConfiguration();
			config.set(this.format(c), fm.getFaction(p));
			
			try {
				config.save(this.config.getFile());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void unclaimChunk(Chunk c, Player p) {
		if(this.isClaimed(c)) {
			if(hasClaim(c, fm.getFaction(p))) {
				YamlConfiguration config = this.config.getNewConfiguration();
				config.set(this.format(c), null);
				
				try {
					config.save(this.config.getFile());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}			
		}
	}
	
	public boolean hasClaim(Chunk c, String factionName) {
		return this.config.getNewConfiguration().get(this.format(c)).equals(factionName);
	}
}
