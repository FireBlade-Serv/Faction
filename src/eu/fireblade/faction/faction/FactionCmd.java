package eu.fireblade.faction.faction;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FactionCmd implements CommandExecutor {

	private FactionConfig config;
	private FactionManager fm;
	private int changed = 0;
	
	public FactionCmd(FactionManager fm, FactionConfig config) {
		this.fm = fm;
		this.config = config;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player)sender;
			if(label.equals("f")) {
				if(args.length > 0) {
					if(args[0].equals("create")) {
						if(!fm.hasFaction(p)) {
							if(args.length == 2) {
								fm.createFaction(args[1], p);
								p.sendMessage("La faction \""+args[1]+"\" a bien été créée");
							}else p.sendMessage("Utilisation de cette commande: /f create [NomDeLaFaction]");
						}else p.sendMessage("Tu as déjà une faction !");
					}else if(args[0].equals("delete")) {
						if(fm.hasFaction(p)) {
							if(fm.getRank(p).equals(FactionRank.OWNER)) {
								if(args.length == 1) {
									p.sendMessage("La faction \""+fm.getFaction(p)+"\"a bien été suppriméée");
									fm.removeFaction(fm.getFaction(p), p);		
								}else p.sendMessage("Utilisation de cette commande: /f delete");
							}else p.sendMessage("Seul l'owner de ta faction peut utiliser cette commande !");
						}else p.sendMessage("Tu n'as pas de faction !");
					}else if (args[0].equals("help")){
						//liste des commandes
					}else if(args[0].equals("add")) {
						if(args.length == 2) {
							if(fm.hasFaction(p)) {
								if(fm.getRank(p).equals(FactionRank.ADMIN) || fm.getRank(p).equals(FactionRank.OWNER)) {
									for(Player Online : Bukkit.getOnlinePlayers()) {
										if(Online.getName().equals(args[1])) {
											changed++;	
											if(!fm.hasFaction(Online)) {
												fm.addMember(fm.getFaction(p), Online);
												p.sendMessage("Joueur ajouté à la faction.");	
											}else p.sendMessage("Ce joueur a déjà une faction.");
										}
									}
									if(changed == 0) p.sendMessage("Ce joueur est hors ligne.");
									else changed=0;
								} else p.sendMessage("Seul l'owner et les admins d'une faction peuvent utiliser cette commande !");
							} else p.sendMessage("Il faut une faction pour utiliser cette commande !");						
						}else p.sendMessage("Utilisation de cette commande: /f add [NomDuJoueur]");
					}else if(args[0].equals("del")){
						if(args.length == 2) {
							if(fm.hasFaction(p)) {
								if(fm.getRank(p).equals(FactionRank.ADMIN) || fm.getRank(p).equals(FactionRank.OWNER)) {
									for(String membre : config.getNewConfiguration().getStringList("factions."+fm.getFaction(p)+".membres")) {
										if(membre.equals(args[1])) {
											OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(membre);
											if(fm.hasFaction(target)) {
												if(fm.getFaction(p).equals(fm.getFaction(target))) {
													changed++;	
													fm.delMember(fm.getFaction(p), target);
													p.sendMessage("Joueur supprimé de la faction.");
													return false;
												}else p.sendMessage("Ce joueur n'est pas dans ta faction.");
											}else p.sendMessage("Ce joueur n'est pas dans ta faction.");
										}
									}
									for(String admin : config.getNewConfiguration().getStringList("factions."+fm.getFaction(p)+".admins")) {
										if(admin.equals(args[1])) {
											OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(admin);
											if(fm.hasFaction(target)) {
												if(fm.getFaction(p).equals(fm.getFaction(target))) {
													changed++;	
													fm.delMember(fm.getFaction(p), target);
													p.sendMessage("Joueur supprimé de la faction.");
													return false;
												}else p.sendMessage("Ce joueur n'est pas dans ta faction.");
											}else p.sendMessage("Ce joueur n'est pas dans ta faction.");
										}
									}
									if(config.getNewConfiguration().get("factions."+fm.getFaction(p)+".owner").equals(args[1])) {
										changed++;
										p.sendMessage("Vous ne pouvez pas supprimer l'owner !");
									}
									if(changed == 0) p.sendMessage("Ce joueur est introuvable.");
									else changed=0;
								} else p.sendMessage("Seul l'owner et les admins d'une faction peuvent utiliser cette commande !");
							}else p.sendMessage("Il faut une faction pour utiliser cette commande !");
						}else p.sendMessage("Utilisation de cette commande: /f del [NomDuJoueur]");
					}else if(args[0].equals("rankup")){
						if(args.length == 2) {
							if(fm.hasFaction(p)) {
								if(fm.getRank(p).equals(FactionRank.OWNER)) {
									for(String membre : config.getNewConfiguration().getStringList("factions."+fm.getFaction(p)+".membres")) {
										if(membre.equals(args[1])) {
											OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(membre);
											if(fm.hasFaction(target)) {
												if(fm.getFaction(p).equals(fm.getFaction(target))) {
													changed++;
													fm.rankupMember(fm.getFaction(p), target);
													p.sendMessage("Joueur mit au grade: admin");
													return false;
												}else p.sendMessage("Ce joueur n'est pas dans ta faction.");
											}else p.sendMessage("Ce joueur n'est pas dans ta faction.");
										}
									}
									for(String admin : config.getNewConfiguration().getStringList("factions."+fm.getFaction(p)+".admins")) {
										if(admin.equals(args[1])) {
											OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(admin);
											if(fm.hasFaction(target)) {
												if(fm.getFaction(p).equals(fm.getFaction(target))) {
													changed++;
													p.sendMessage("On ne peut pas rankup au dessus d'admin !");
													return false;
												}else p.sendMessage("Ce joueur n'est pas dans ta faction.");
											}else p.sendMessage("Ce joueur n'est pas dans ta faction.");
										}
									}
									if(config.getNewConfiguration().get("factions."+fm.getFaction(p)+".owner").equals(args[1])) {
										changed++;
										p.sendMessage("Vous ne pouvez pas rankup l'owner !");
									}
									if(changed == 0) p.sendMessage("Ce joueur est introuvable.");
									else changed=0;
								}else p.sendMessage("Seul l'owner peut utiliser cette commande !");
							}else p.sendMessage("Il faut une faction pour utiliser cette commande !");
						}else p.sendMessage("Utilisation de cette commande: /f rankup [NomDuJoueur]");
					}else if(args[0].equals("rankdown")){
						if(args.length == 2) {
							if(fm.hasFaction(p)) {
								if(fm.getRank(p).equals(FactionRank.OWNER)) {
									for(String membre : config.getNewConfiguration().getStringList("factions."+fm.getFaction(p)+".membres")) {
										if(membre.equals(args[1])) {
											OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(membre);
											if(fm.hasFaction(target)) {
												if(fm.getFaction(p).equals(fm.getFaction(target))) {
													changed++;
													p.sendMessage("On ne peut pas rankdown en dessous de membre !");
													return false;
												}else p.sendMessage("Ce joueur n'est pas dans ta faction.");
											}else p.sendMessage("Ce joueur n'est pas dans ta faction.");
										}
									}
									for(String admin : config.getNewConfiguration().getStringList("factions."+fm.getFaction(p)+".admins")) {
										if(admin.equals(args[1])) {
											OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(admin);
											if(fm.hasFaction(target)) {
												if(fm.getFaction(p).equals(fm.getFaction(target))) {
													changed++;
													fm.rankdownMember(fm.getFaction(p), target);
													p.sendMessage("Joueur mit au grade: membre");
													return false;
												}else p.sendMessage("Ce joueur n'est pas dans ta faction.");
											}else p.sendMessage("Ce joueur n'est pas dans ta faction.");
										}
									}
									if(config.getNewConfiguration().get("factions."+fm.getFaction(p)+".owner").equals(args[1])) {
										changed++;
										p.sendMessage("Vous ne pouvez pas rankdown l'owner !");
									}
									if(changed == 0) p.sendMessage("Ce joueur est introuvable.");
									else changed=0;
								}else p.sendMessage("Seul l'owner peut utiliser cette commande !");
							}else p.sendMessage("Il faut une faction pour utiliser cette commande !");
						}else p.sendMessage("Utilisation de cette commande: /f rankup [NomDuJoueur]");
					}else if(args[0].equals("sethome")) {
						if(fm.hasFaction(p)) {
							if(fm.getRank(p).equals(FactionRank.OWNER)) {
								fm.setFactionHome(fm.getFaction(p), p);
								p.sendMessage("L'home de ta faction a bien été modifié !");
							}else p.sendMessage("Seul l'owner peut utiliser cette commande !");
						}else p.sendMessage("Il faut une faction pour utiliser cette commande !"); 						
					}else if(args[0].equals("home")) {
						if(fm.hasFaction(p)) {
							if(config.getNewConfiguration().contains("factions."+fm.getFaction(p)+".home")) {
								fm.tpFactionHome(fm.getFaction(p), p);
							}else p.sendMessage("Ta faction n'a pas créée d'home !");					
						}else p.sendMessage("Il faut une faction pour utiliser cette commande !"); 		
					}else p.sendMessage("Cette commande n'existe pas utilise \"/f help\" pour avoir la liste des commandes.");
				}else p.sendMessage("Cette commande n'existe pas utilise \"/f help\" pour avoir la liste des commandes.");
			}
		}
		return false;
	}
}
