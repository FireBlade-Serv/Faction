package eu.fireblade.faction.faction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import eu.fireblade.faction.Main;
import eu.fireblade.faction.chunk.ChunkConfig;

public class FactionManager {
	
	private List<Player> tpJoueur = new ArrayList<>();
	private FactionConfig config;
	private ChunkConfig cConfig;
	private Main main;

	public FactionManager(FactionConfig config, ChunkConfig cConfig, Main main) {
		this.main = main;
		this.config = config;
		this.cConfig = cConfig;
	}
	
	public boolean containsPlayer(Player p) {
		return this.tpJoueur.contains(p);
	}
	
	public void removePlayer(Player p) {
		this.tpJoueur.remove(p);
	}
	
	public boolean isFree(String name){
		return !this.config.getNewConfiguration().contains("factions."+name);
	}
	
	public void createFaction(String name, Player factionOwner) {
		if(this.isFree(name.replace('&', '§'))) {
			YamlConfiguration config = this.config.getNewConfiguration();
			config.set("factions."+name.replace('&', '§')+"§r"+".owner", factionOwner.getName());
			
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
				YamlConfiguration cConfig = this.cConfig.getNewConfiguration();
				
				for(String chunk : cConfig.getKeys(false) ) {
					if(cConfig.get(chunk).equals(name)) {
						cConfig.set(chunk, null);
					}
				}
				
				try {
					config.save(this.config.getFile());
					cConfig.save(this.cConfig.getFile());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public String getFaction(OfflinePlayer p) {
		YamlConfiguration config = this.config.getNewConfiguration();
		
		if(!config.contains("factions")) return "THISPLAYERHAVENOFACTION116545745";
		
		for(String factionName : config.getConfigurationSection("factions").getKeys(false)) {
			if(factionName == null) continue;
			if(config.get("factions."+factionName+".owner").equals(p.getName())) return factionName;
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
	
	public boolean hasFaction(OfflinePlayer p) {
		return !(getFaction(p).equals("THISPLAYERHAVENOFACTION116545745"));
	}
	
	public FactionRank getRank(OfflinePlayer p) {
		if(this.hasFaction(p)) {
			YamlConfiguration config = this.config.getNewConfiguration();
			
			if(!config.contains("factions")) return FactionRank.NOTHING;
			
			for(String factionName : config.getConfigurationSection("factions").getKeys(false)) {
				if(factionName == null) continue;			
				if(config.get("factions."+factionName+".owner").equals(p.getName())) return FactionRank.OWNER;
				if(config.contains("factions."+factionName+".admins")) {
					for(String membres : config.getStringList("factions."+factionName+".admins")) {
						if(membres.equals(p.getName())) return FactionRank.ADMIN;
					}
				}				
				if(config.contains("factions."+factionName+".membres")) {
					for(String membres : config.getStringList("factions."+factionName+".membres")) {
						if(membres.equals(p.getName())) return FactionRank.MEMBER;
					}	
				}				
			}
			return FactionRank.NOTHING;
		}else {
			return FactionRank.NOTHING;
		}
	}
	
	public void addMember(String factionName, OfflinePlayer p) {
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
	
	public void delMember(String factionName, OfflinePlayer p) {
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
				}else if (this.getRank(p).equals(FactionRank.ADMIN)) {
					YamlConfiguration config = this.config.getNewConfiguration();
					List<String> admins = new ArrayList<>();
					for(String admin : config.getStringList("factions."+factionName+".admins")) {
						admins.add(admin);
					}
					admins.remove(p.getName());
					config.set("factions."+factionName+".admins", admins);
					
					try {
						config.save(this.config.getFile());
					} catch(IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public void rankupMember(String factionName, OfflinePlayer p) {
		if(this.hasFaction(p)) {
			if(this.getFaction(p).equals(factionName)) {
				if(this.getRank(p).equals(FactionRank.MEMBER)) {
					this.delMember(factionName, p);
					YamlConfiguration config = this.config.getNewConfiguration();
					List<String> admins = new ArrayList<>();
					for(String admin : config.getStringList("factions."+factionName+".admins")) {
						admins.add(admin);
					}
					admins.add(p.getName());	
					config.set("factions."+factionName+".admins", admins);

					
					try {
						config.save(this.config.getFile());
					} catch(IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public void rankdownMember(String factionName, OfflinePlayer p) {		
		if(this.hasFaction(p)) {
			if(this.getFaction(p).equals(factionName)) {
				if(this.getRank(p).equals(FactionRank.ADMIN)) {
					this.delMember(factionName, p);
					this.addMember(factionName, p);				
				}
			}
		}
	}
	
	public void setFactionHome(String factionName, Player p) {
		if(this.hasFaction(p)) {
			if(this.getFaction(p).equals(factionName)) {
				if (this.getRank(p).equals(FactionRank.OWNER)) {
					YamlConfiguration config = this.config.getNewConfiguration();
					config.set("factions."+factionName+".home.x", p.getLocation().getX());
					config.set("factions."+factionName+".home.y", p.getLocation().getY());
					config.set("factions."+factionName+".home.z", p.getLocation().getZ());
					
					try {
						config.save(this.config.getFile());
					} catch(IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public void tpFactionHome(String factionName, Player p) {
		if(this.hasFaction(p)) {
			if(this.getFaction(p).equals(factionName)) {
				this.tpJoueur.add(p);
				p.sendMessage("Téléportation dans 5 seconde, ne vous deplacé pas !");
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
					
					@Override
					public void run() {
						tpScheduler(p, factionName);
					}
				}, 100L);
			}
		}
	}
	
	public Location locReader (String homePath) {	
		YamlConfiguration config = this.config.getNewConfiguration();
		return new Location(Bukkit.getWorld("world"), (double) config.get(homePath+".x"),
				(double) config.get(homePath+".y"), (double) config.get(homePath+".z"));
	}
	
	public void tpScheduler(Player p, String factionName) {
		if(this.tpJoueur.contains(p)){
			p.teleport(this.locReader("factions."+factionName+".home"));
			this.tpJoueur.remove(p);
		}	
	}
	
}