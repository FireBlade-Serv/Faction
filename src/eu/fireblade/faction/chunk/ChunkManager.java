package eu.fireblade.faction.chunk;

import java.io.IOException;

import org.bukkit.Chunk;
import org.bukkit.configuration.file.YamlConfiguration;

import eu.fireblade.faction.Main;

public class ChunkManager {
	
	private ChunkConfig config;
	private Main main;

	public ChunkManager(ChunkConfig config, Main main) {
		this.main = main;
		this.config = config;
	}
	
	public boolean isClaimed(Chunk c){
		return this.config.getNewConfiguration().contains(this.format(c));
	}
	
	public String format(Chunk c) {
		//   x=1@y=2
		return "x="+c.getX()+"@"+"z="+c.getZ();
	}
	
	public Chunk readFormat(String text) {
		String partx = null, partz = null;
		
		for(String part : text.split("@")) {
			if(part.startsWith("x=")) {
				partx = part.substring(2);
			}else if(part.startsWith("z=")) {
				partz = part.substring(2);
			}
		}
		
		return this.main.getServer().getWorld("world").getChunkAt(Integer.valueOf(partx), Integer.valueOf(partz));
	}
	
	public void addToConfig(Chunk c) {
		if(!this.isClaimed(c)) {
			YamlConfiguration config = this.config.getNewConfiguration();
			config.set(this.format(c), true);
			
			try {
				config.save(this.config.getFile());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
