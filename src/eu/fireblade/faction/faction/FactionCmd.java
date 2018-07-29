package eu.fireblade.faction.faction;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import eu.fireblade.faction.Config;

public class FactionCmd implements CommandExecutor {

	private Config config;
	private FactionConfig fConfig;
	private FactionManager fm;
	private int changed = 0;
	
	public FactionCmd(FactionManager fm, FactionConfig fConfig, Config config) {
		this.fm = fm;
		this.fConfig = fConfig;
		this.config = config;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		YamlConfiguration serverConfig = this.config.getNewConfiguration();
		YamlConfiguration factionConfig = this.fConfig.getNewConfiguration();
		String prefix = (String) serverConfig.get("faction.prefix");
		
		if(sender instanceof Player) {
			Player p = (Player)sender;
			if(label.equals("f")) {
				if(args.length > 0) {
					if(args[0].equals(serverConfig.get("factionCmd.argument.Creation de faction"))) {
						if(args.length == 2) {
							if(fm.isFree(args[1])) {
								if(!fm.hasFaction(p)) {
									fm.createFaction(args[1], p);
									p.sendMessage(prefix+" "+serverConfig.get("factionCmd.message.Creation de faction reussie")
									.toString().replace("[NomDeFaction]", fm.getFaction(p)));
								}else p.sendMessage(prefix+" "+serverConfig.get("factionCmd.message.Le sender a deja une faction"));
							}else p.sendMessage(prefix+" "+serverConfig.get("factionCmd.message.Nom de faction indisponible"));
						}else p.sendMessage(prefix+" "+serverConfig.get("factionCmd.message.Utilisation")
						.toString().replace("[Commande]", "/f "+serverConfig.get("factionCmd.argument.Creation de faction")+" [NomDeFaction]."));
					}else if(args[0].equals(serverConfig.get("factionCmd.argument.Suppression de faction"))) {
						if(fm.hasFaction(p)) {
							if(fm.getRank(p).equals(FactionRank.OWNER)) {
								if(args.length == 1) {
									p.sendMessage(prefix+" "+serverConfig.get("factionCmd.message.Suppression de faction reussie")
									.toString().replace("[NomDeFaction]", fm.getFaction(p)));
									fm.removeFaction(fm.getFaction(p), p);		
								}else p.sendMessage(prefix+" "+serverConfig.get("factionCmd.message.Utilisation")
								.toString().replace("[Commande]", "/f "+serverConfig.get("factionCmd.argument.Suppression de faction")+"."));
							}else p.sendMessage(prefix+" "+serverConfig.get("factionCmd.message.Commande pour owner uniquement"));
						}else p.sendMessage(prefix+" "+serverConfig.get("factionCmd.message.Absence de faction"));
					}else if (args[0].equals(serverConfig.get("factionCmd.argument.Aide"))){
						//liste des commandes
					}else if(args[0].equals(serverConfig.get("factionCmd.argument.Ajout de membre"))) {
						if(args.length == 2) {
							if(fm.hasFaction(p)) {
								if(fm.getRank(p).equals(FactionRank.ADMIN) || fm.getRank(p).equals(FactionRank.OWNER)) {
									for(Player Online : Bukkit.getOnlinePlayers()) {
										if(Online.getName().equals(args[1])) {
											changed++;	
											if(!fm.hasFaction(Online)) {
												fm.addMember(fm.getFaction(p), Online);
												p.sendMessage(prefix+" "+serverConfig.get("factionCmd.message.Ajout reussis"));	
											}else p.sendMessage(prefix+" "+serverConfig.get("factionCmd.message.Joueur deja dans une faction"));
										}
									}
									if(changed == 0) p.sendMessage(prefix+" "+serverConfig.get("factionCmd.message.Joueur hors ligne"));
									else changed=0;
								} else p.sendMessage(prefix+" "+serverConfig.get("factionCmd.message.Commande pour owner et admins uniquement"));
							} else p.sendMessage(prefix+" "+serverConfig.get("factionCmd.message.Absence de faction"));						
						}else p.sendMessage(prefix+" "+serverConfig.get("factionCmd.message.Utilisation")
						.toString().replace("[Commande]", "/f "+serverConfig.get("factionCmd.argument.Ajout de membre")+" [NomDuJoueur]."));
					}else if(args[0].equals(serverConfig.get("factionCmd.argument.Suppression de membre"))){
						if(args.length == 2) {
							if(fm.hasFaction(p)) {
								if(fm.getRank(p).equals(FactionRank.ADMIN) || fm.getRank(p).equals(FactionRank.OWNER)) {
									for(String membre : factionConfig.getStringList("factions."+fm.getFaction(p)+".membres")) {
										if(membre.equals(args[1])) {
											OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(membre);
											changed++;	
											fm.delMember(fm.getFaction(p), target);
											p.sendMessage(prefix+" "+serverConfig.get("factionCmd.message.Joueur supprimer"));
											return false;
										}
									}
									for(String admin : factionConfig.getStringList("factions."+fm.getFaction(p)+".admins")) {
										if(admin.equals(args[1])) {
											OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(admin);
											changed++;	
											fm.delMember(fm.getFaction(p), target);
											p.sendMessage(prefix+" "+serverConfig.get("factionCmd.message.Joueur supprimer"));
											return false;
										}
									}
									if(factionConfig.get("factions."+fm.getFaction(p)+".owner").equals(args[1])) {
										changed++;
										p.sendMessage(prefix+" "+serverConfig.get("factionCmd.message.Erreur suppresion owner"));
									}
									if(changed == 0) p.sendMessage(prefix+" "+serverConfig.get("factionCmd.message.Joueur dans autre fac"));
									else changed=0;
								} else p.sendMessage(prefix+" "+serverConfig.get("factionCmd.message.Commande pour owner et admins uniquement"));
							}else p.sendMessage(prefix+" "+serverConfig.get("factionCmd.message.Absence de faction"));
						}else p.sendMessage(prefix+" "+serverConfig.get("factionCmd.message.Utilisation")
						.toString().replace("[Commande]", "/f "+serverConfig.get("factionCmd.argument.Suppression de membre")+" [NomDuJoueur]."));
					}else if(args[0].equals(serverConfig.get("factionCmd.argument.Rank up de membre"))){
						if(args.length == 2) {
							if(fm.hasFaction(p)) {
								if(fm.getRank(p).equals(FactionRank.OWNER)) {
									for(String membre : factionConfig.getStringList("factions."+fm.getFaction(p)+".membres")) {
										if(membre.equals(args[1])) {
											OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(membre);
											changed++;
											fm.rankupMember(fm.getFaction(p), target);
											p.sendMessage(prefix+" "+serverConfig.get("factionCmd.message.Joueur rankup reussis"));
											return false;
										}
									}
									for(String admin : factionConfig.getStringList("factions."+fm.getFaction(p)+".admins")) {
										if(admin.equals(args[1])) {
											changed++;
											p.sendMessage(prefix+" "+serverConfig.get("factionCmd.message.Joueur rankup erreur"));
											return false;
										}
									}
									if(factionConfig.get("factions."+fm.getFaction(p)+".owner").equals(args[1])) {
										changed++;
										p.sendMessage(prefix+" "+serverConfig.get("Vous ne pouvez pas rankup l'owner !"));
									}
									if(changed == 0) p.sendMessage(prefix+" "+serverConfig.get("factionCmd.message.Joueur dans autre fac"));
									else changed=0;
								}else p.sendMessage(prefix+" "+serverConfig.get("factionCmd.message.Commande pour owner uniquement"));
							}else p.sendMessage(prefix+" "+serverConfig.get("factionCmd.message.Absence de faction"));
						}else p.sendMessage(prefix+" "+serverConfig.get("factionCmd.message.Utilisation")
						.toString().replace("[Commande]", "/f "+serverConfig.get("factionCmd.argument.Rank up de membre")+" [NomDuJoueur]."));
					}else if(args[0].equals(serverConfig.get("factionCmd.argument.Rank down de membre"))){
						if(args.length == 2) {
							if(fm.hasFaction(p)) {
								if(fm.getRank(p).equals(FactionRank.OWNER)) {
									for(String membre : factionConfig.getStringList("factions."+fm.getFaction(p)+".membres")) {
										if(membre.equals(args[1])) {
											changed++;
											p.sendMessage(prefix+" "+serverConfig.get("factionCmd.message.Joueur rankdown erreur"));
											return false;
										}
									}
									for(String admin : factionConfig.getStringList("factions."+fm.getFaction(p)+".admins")) {
										if(admin.equals(args[1])) {
											OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(admin);
											changed++;
											fm.rankdownMember(fm.getFaction(p), target);
											p.sendMessage(prefix+" "+serverConfig.get("factionCmd.message.Joueur rankdown reussis"));
											return false;
										}
									}
									if(factionConfig.get("factions."+fm.getFaction(p)+".owner").equals(args[1])) {
										changed++;
										p.sendMessage(prefix+" "+serverConfig.get("factionCmd.message.Owner rankdown erreur"));
									}
									if(changed == 0) p.sendMessage(prefix+" "+serverConfig.get("factionCmd.message.Joueur dans autre fac"));
									else changed=0;
								}else p.sendMessage(prefix+" "+serverConfig.get("factionCmd.message.Commande pour owner uniquement"));
							}else p.sendMessage(prefix+" "+serverConfig.get("factionCmd.message.Absence de faction"));
						}else p.sendMessage(prefix+" "+serverConfig.get("factionCmd.message.Utilisation")
						.toString().replace("[Commande]", "/f "+serverConfig.get("factionCmd.argument.Rank down de membre")+" [NomDuJoueur]."));
					}else if(args[0].equals(serverConfig.get("factionCmd.argument.Sethome de faction"))) {
						if(fm.hasFaction(p)) {
							if(fm.getRank(p).equals(FactionRank.OWNER)) {
								fm.setFactionHome(fm.getFaction(p), p);
								p.sendMessage(prefix+" "+serverConfig.get("factionCmd.message.modif home fac"));
							}else p.sendMessage(prefix+" "+serverConfig.get("factionCmd.message.Commande pour owner uniquement"));
						}else p.sendMessage(prefix+" "+serverConfig.get("factionCmd.message.Absence de faction")); 						
					}else if(args[0].equals(serverConfig.get("factionCmd.argument.Tp au home de faction"))) {
						if(fm.hasFaction(p)) {
							if(factionConfig.contains("factions."+fm.getFaction(p)+".home")) {
								fm.tpFactionHome(fm.getFaction(p), p);
							}else p.sendMessage(prefix+" "+serverConfig.get("factionCmd.message.absence home fac"));					
						}else p.sendMessage(prefix+" "+serverConfig.get("factionCmd.message.Absence de faction")); 		
					}else p.sendMessage(prefix+" "+serverConfig.get("factionCmd.message.Aide message")
					.toString().replaceAll("[Commande]", "/f "+serverConfig.get("factionCmd.argument.Aide")));
				}else p.sendMessage(prefix+" "+serverConfig.get("factionCmd.message.Aide message")
				.toString().replaceAll("[Commande]", "/f "+serverConfig.get("factionCmd.argument.Aide")));
			}
		}
		return false;
	}
}