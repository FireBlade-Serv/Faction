package eu.fireblade.faction;

import org.bukkit.plugin.java.JavaPlugin;

import eu.fireblade.faction.chunk.ChunkCmd;
import eu.fireblade.faction.chunk.ChunkConfig;
import eu.fireblade.faction.chunk.ChunkManager;

public class Main extends JavaPlugin{
	
	private ChunkConfig config;
	private ChunkManager man;
	
	@Override
	public void onEnable() {
		//init
		this.config = new ChunkConfig(this);
		this.config.initFolder();
		this.config.initFile();
		
		this.man = new ChunkManager(this.config, this);

		getCommand("claim").setExecutor(new ChunkCmd(this.man));
	}
	
	@Override
	public void onDisable() {

	}
	
}
