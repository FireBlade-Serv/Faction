package eu.fireblade.faction.faction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
			return "THISPLAYERHAVENOFACTION116545745";
		}
		
		for(String factionName : config.getConfigurationSection("factions").getKeys(false)) {
			if(factionName == null) continue;
			
			if(config.get("factions."+factionName+".owner").equals(p.getName())) {
				return factionName;
			}
			if(config.contains("factions."+factionName+".admins")) {
				for(String membres : config.getStringList("factions."+factionName+".admins")) {
					if(membres.equals(p.getName())) return factionName;
				}
			}
			if(config.contains("factions."+factionName+".membres")) {
				for(String membres : config.getStringList("factions."+factionName+".membres")) {
					if(membres.equals(p.getName())) return factionName;
				}
			}	
		}
		return "THISPLAYERHAVENOFACTION116545745";
	}
	
	public boolean hasFaction(Player p) {
		return !(getFaction(p).equals("THISPLAYERHAVENOFACTION116545745"));
	}
	
	public FactionRank getRank(Player p) {
		if(this.hasFaction(p)) {
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
	
	public void addMember(String factionName, Player p) {
		if(!this.hasFaction(p)) {
			YamlConfiguration config = this.config.getNewConfiguration();
			List<String> membres = new ArrayList<>();
			for(String membre : config.getStringList("factions."+factionName+".membres")) {
				membres.add(membre);
			}
			membres.add(p.getName());
			config.set("factions."+factionName+".membres", membres);
							
			try {
				config.save(this.config.getFile());
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void delMember(String factionName, Player p) {
		if(this.hasFaction(p)) {
			if(this.getFaction(p).equals(factionName)) {
				if(this.getRank(p).equals(FactionRank.MEMBER)) {
					YamlConfiguration config = this.config.getNewConfiguration();
					List<String> membres = new ArrayList<>();
					for(String membre : config.getStringList("factions."+factionName+".membres")) {
						membres.add(membre);
					}
					membres.remove(p.getName());
					config.set("factions."+factionName+".membres", membres);
					
					try {
						config.save(this.config.getFile());
					} catch(IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
}
