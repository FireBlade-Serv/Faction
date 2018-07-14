package eu.fireblade.faction.faction;

import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import eu.fireblade.faction.Main;

public class FactionManager {
	
	private FactionConfig config;
	@SuppressWarnings("unused")
	private Main main;

	public FactionManager(FactionConfig config, Main main) {
		this.main = main;
		this.config = config;
	}
	
	public boolean isFree(String name){
		return !this.config.getNewConfiguration().contains("factions."+name);
	}
	
	public void createFaction(String name, Player factionOwner) {
		if(this.isFree(name)) {
			YamlConfiguration config = this.config.getNewConfiguration();
			config.set("factions."+name+".owner", factionOwner.getName());
			
			try {
				config.save(this.config.getFile());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void removeFaction(String name, Player p) {
		if(!this.isFree(name)) {
			if(p.getName().equals(config.getNewConfiguration().get("factions."+name+".owner"))) {
				YamlConfiguration config = this.config.getNewConfiguration();
				
				config.set("factions."+name, null);
		
				try {
					config.save(this.config.getFile());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public String getFaction(Player p) {
		YamlConfiguration config = this.config.getNewConfiguration();
		
		if(!config.contains("factions")) {
			return "non";
		}
		
		for(String factionName : config.getConfigurationSection("factions").getKeys(false)) {
			if(factionName == null) continue;
			
			if(config.get("factions."+factionName+".owner").equals(p.getName())) {
				return factionName;
			}
			for(String factionAdmins : config.getStringList("factions."+factionName+".admins")) {
				if(factionAdmins.equals(p.getName())) {
					return factionName;
				}
			}
			for(String factionMembres : config.getStringList("factions."+factionName+".membres")) {
				if(factionMembres.equals(p.getName())) {
					return factionName;
				}
			}
		}
		return "non";
	}
	
	public boolean hasFaction(Player p) {
		if(getFaction(p).equals("non")) return false;
		else return true;
	}
	
	public FactionRank getRank(Player p) {
		if(hasFaction(p)) {
			YamlConfiguration config = this.config.getNewConfiguration();
			
			if(!config.contains("factions")) {
				return FactionRank.NOTHING;
			}
			
			for(String factionName : config.getConfigurationSection("factions").getKeys(false)) {
				if(factionName == null) continue;
				
				if(config.get("factions."+factionName+".owner").equals(p.getName())) {
					return FactionRank.OWNER;
				}
				for(String factionAdmins : config.getStringList("factions."+factionName+".admins")) {
					if(factionAdmins.equals(p.getName())) {
						return FactionRank.ADMIN;
					}
				}
				for(String factionMembres : config.getStringList("factions."+factionName+".membres")) {
					if(factionMembres.equals(p.getName())) {
						return FactionRank.MEMBER;
					}
				}
			}
			return FactionRank.NOTHING;					
		}else {
			return FactionRank.NOTHING;
		}
	}	
}
