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

public class FactionManager {
	
	public List<Player> tpJoueur = new ArrayList<>();
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
					config.set("factions."+factionName+".home", locFormat(p.getLocation()));
					
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
	
	public String locFormat (Location loc) {
		//x=1@y=2@z=3
		return "x="+loc.getX()+"@y="+loc.getY()+"@z="+loc.getZ();
	}
	
	public Location locReader (String loc) {
		String partx = null, party = null, partz = null;
		
		for(String part : loc.split("@")) {
			if(part.startsWith("x=")) partx = part.substring(2);
			else if(part.startsWith("y=")) party = part.substring(2);
			else if(part.startsWith("z=")) partz = part.substring(2);
		}
		
		return new Location(Bukkit.getWorld("world"), Double.valueOf(partx), Double.valueOf(party), Double.valueOf(partz));
	}
	
	public void tpScheduler(Player p, String factionName) {
		if(this.tpJoueur.contains(p)){
			YamlConfiguration config = this.config.getNewConfiguration();
			p.teleport(this.locReader((String) config.get("factions."+factionName+".home")));
			this.tpJoueur.remove(p);
		}	
	}
	
}