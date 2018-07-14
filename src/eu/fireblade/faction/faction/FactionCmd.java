package eu.fireblade.faction.faction;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FactionCmd implements CommandExecutor {

	private FactionManager fm;

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
					}else {
						p.sendMessage("Cette commande n'existe pas utilise \"/f help\" pour avoir la liste des commandes.");
					}
				
				}else {
					p.sendMessage("Cette commande n'existe pas utilise \"/f help\" pour avoir la liste des commandes.");
				}
			}
		}
		return false;
	}
	
}
