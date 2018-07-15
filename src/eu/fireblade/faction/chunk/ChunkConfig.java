package eu.fireblade.faction.chunk;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import eu.fireblade.faction.Main;

public class ChunkConfig {

	private Main main;
	private File file;
	
	public ChunkConfig(Main main) {
		this.main = main;
	}
	
	public void initFolder() {
		if(!this.main.getDataFolder().exists()) this.main.getDataFolder().mkdirs();
	}
	
	public void initFile() {
		this.file = new File(this.main.getDataFolder(), "chunk.yml");
		
		if(!this.file.exists()) {
			try {
				this.file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public File getFile() {
		return this.file;
	}
	
	public YamlConfiguration getNewConfiguration() {
		YamlConfiguration config = new YamlConfiguration();
		
		try {
			config.load(this.file);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		} 
		return config;
	}
	
}
