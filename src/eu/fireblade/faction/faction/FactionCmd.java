package eu.fireblade.faction.faction;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FactionCmd implements CommandExecutor {

	private FactionManager fm;
	private int changed = 0;
	
	public FactionCmd(FactionManager fm) {
		this.fm = fm;
	}

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
							}else {
								p.sendMessage("Utilisation de cette commande: /f create [NomDeLaFaction]");
							}
						}else {
							p.sendMessage("Tu as déjà une faction !");
						}
					}else if(args[0].equals("delete")) {
						if(fm.hasFaction(p)) {
							if(fm.getRank(p).equals(FactionRank.OWNER)) {
								if(args.length == 1) {
									p.sendMessage("La faction \""+fm.getFaction(p)+"\"a bien été suppriméée");
									fm.removeFaction(fm.getFaction(p), p);		
								}else {
									p.sendMessage("Utilisation de cette commande: /f delete");
								}
							}else {
								p.sendMessage("Seul l'owner de ta faction peut utiliser cette commande !");
							}
						}else {
							p.sendMessage("Tu n'as pas de faction !");
						}
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
											}else {
												p.sendMessage("Ce joueur a déjà une faction.");
											}
										}
									}
									if(changed == 0) p.sendMessage("Ce joueur est hors ligne.");
									else changed=0;
								}
							}						
						}else {
							p.sendMessage("Utilisation de cette commande: /f add [NomDuJoueur]");
						}
					}else if(args[0].equals("del")){
						if(args.length == 2) {
							if(fm.hasFaction(p)) {
								if(fm.getRank(p).equals(FactionRank.ADMIN) || fm.getRank(p).equals(FactionRank.OWNER)) {
									for(Player Online : Bukkit.getOnlinePlayers()) {
										if(Online.getName().equals(args[1])) {
											if(fm.hasFaction(Online)) {
												if(fm.getFaction(p).equals(fm.getFaction(Online))) {
													changed++;	
													if(!fm.getRank(Online).equals(FactionRank.OWNER)) {
														fm.delMember(fm.getFaction(p), Online);
														p.sendMessage("Joueur supprimé de la faction.");
													}												
												}else {
													p.sendMessage("Ce joueur n'est pas dans la faction.");
												}
											}else {
												p.sendMessage("Ce joueur n'est pas dans la faction.");
											}
										}
									}
									if(changed == 0) p.sendMessage("Ce joueur est hors ligne.");
									else changed=0;
								}
							}
						}
					}else {
						p.sendMessage("Cette commande n'existe pas utilise \"/f help\" pour avoir la liste des commandes.");
					}
				}
			}
		}
		return false;
	}
}
