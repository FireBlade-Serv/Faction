package eu.fireblade.faction;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import eu.fireblade.faction.chunk.ChunkCmd;
import eu.fireblade.faction.chunk.ChunkConfig;
import eu.fireblade.faction.chunk.ChunkManager;
import eu.fireblade.faction.faction.FactionCmd;
import eu.fireblade.faction.faction.FactionConfig;
import eu.fireblade.faction.faction.FactionManager;

public class Main extends JavaPlugin{
	
	private ChunkConfig cConfig;
	private ChunkManager cMan;
	private FactionConfig fConfig;
	private FactionManager fMan;
	
	@Override
	public void onEnable() {
		//init
		this.cConfig = new ChunkConfig(this);
		this.cConfig.initFolder();
		this.cConfig.initFile();
		
		this.fConfig = new FactionConfig(this);
		this.fConfig.initFolder();
		this.fConfig.initFile();
		
		this.fMan = new FactionManager(this.fConfig, this);
		this.cMan = new ChunkManager(this.cConfig, this, this.fMan);;	

		getCommand("claim").setExecutor(new ChunkCmd(this.cMan, this.fMan));
		getCommand("unclaim").setExecutor(new ChunkCmd(this.cMan, this.fMan));
		
		getCommand("f").setExecutor(new FactionCmd(this.fMan, this.fConfig));
		
		Bukkit.getPluginManager().registerEvents(new eu.fireblade.faction.faction.FactionEvent(fMan), this);
	}
	
	@Override
	public void onDisable() {
	}	
}
