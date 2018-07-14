package eu.fireblade.faction;

import org.bukkit.plugin.java.JavaPlugin;

import eu.fireblade.faction.chunk.ChunkCmd;
import eu.fireblade.faction.chunk.ChunkConfig;
import eu.fireblade.faction.chunk.ChunkManager;
import eu.fireblade.faction.faction.FactionCmd;
import eu.fireblade.faction.faction.FactionConfig;
import eu.fireblade.faction.faction.FactionManager;

public class Main extends JavaPlugin{
	
	private ChunkConfig config;
	private ChunkManager man;
	private FactionConfig fConfig;
	private FactionManager fMan;
	
	@Override
	public void onEnable() {
		//init
		this.config = new ChunkConfig(this);
		this.config.initFolder();
		this.config.initFile();
		
		this.fConfig = new FactionConfig(this);
		this.fConfig.initFolder();
		this.fConfig.initFile();
		
		this.fMan = new FactionManager(this.fConfig, this);
		this.man = new ChunkManager(config, this, this.fMan);

		getCommand("claim").setExecutor(new ChunkCmd(this.man, this.fMan));
		getCommand("unclaim").setExecutor(new ChunkCmd(this.man, this.fMan));
		
		getCommand("f").setExecutor(new FactionCmd(this.fMan));
	}
	
	@Override
	public void onDisable() {

	}
	
}
