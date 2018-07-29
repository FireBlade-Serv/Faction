package eu.fireblade.faction;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {

	private Main main;
	private File file;
	
	public Config(Main main) {
		this.main = main;
	}
	
	public void initFolder() {
		if(!this.main.getDataFolder().exists()) this.main.getDataFolder().mkdirs();
	}
	
	public void initFile() {
		this.file = new File(this.main.getDataFolder(), "config.yml");		
		
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
	
	public void initConfig() {
		YamlConfiguration config = this.getNewConfiguration();
		if(!config.contains("faction.prefix")) config.set("faction.prefix", "§8[§4Fire§6Faction§8]§r");
//---------------------------------------------------------------------------------------------------------------------------------------------------
		if(!config.contains("factionCmd.argument.Creation de faction")) config.set("factionCmd.argument.Creation de faction", "create");
		if(!config.contains("factionCmd.argument.Suppression de faction")) config.set("factionCmd.argument.Suppression de faction", "delete");
		if(!config.contains("factionCmd.argument.Ajout de membre")) config.set("factionCmd.argument.Ajout de membre", "add");
		if(!config.contains("factionCmd.argument.Suppression de membre")) config.set("factionCmd.argument.Suppression de membre", "del");
		if(!config.contains("factionCmd.argument.Rank up de membre")) config.set("factionCmd.argument.Rank up de membre", "rankup");
		if(!config.contains("factionCmd.argument.Rank down de membre")) config.set("factionCmd.argument.Rank down de membre", "rankdown");
		if(!config.contains("factionCmd.argument.Sethome de faction")) config.set("factionCmd.argument.Sethome de faction", "sethome");
		if(!config.contains("factionCmd.argument.Tp au home de faction")) config.set("factionCmd.argument.Tp au home de faction", "home");
		if(!config.contains("factionCmd.argument.Aide")) config.set("factionCmd.argument.Aide", "help");
//---------------------------------------------------------------------------------------------------------------------------------------------------
		if(!config.contains("factionCmd.message.Utilisation")) config.set("factionCmd.message.Utilisation", "Utilisation de cette commande: [Commande]");
		if(!config.contains("factionCmd.message.Creation de faction reussie")) config.set("factionCmd.message.Creation de faction reussie", "La faction [NomDeFaction] a bien été créée !");
		if(!config.contains("factionCmd.message.Suppression de faction reussie")) config.set("factionCmd.message.Suppression de faction reussie", "La faction [NomDeFaction] a bien été suppriméée");
		if(!config.contains("factionCmd.message.Le sender a deja une faction")) config.set("factionCmd.message.Le sender a deja une faction", "Tu as déjà une faction !");
		if(!config.contains("factionCmd.message.Nom de faction indisponible")) config.set("factionCmd.message.Nom de faction indisponible", "Ce nom de faction est occupé !");		
		if(!config.contains("factionCmd.message.Commande pour owner uniquement")) config.set("factionCmd.message.Commande pour owner uniquement", "Seul l'owner de ta faction peut utiliser cette commande !");
		if(!config.contains("factionCmd.message.Commande pour owner et admins uniquement")) config.set("factionCmd.message.Commande pour owner et admins uniquement", "Seul l'owner et les admins d'une faction peuvent utiliser cette commande !");
		if(!config.contains("factionCmd.message.Absence de faction")) config.set("factionCmd.message.Absence de faction", "Il faut une faction pour utiliser cette commande !");
		if(!config.contains("factionCmd.message.Ajout reussis")) config.set("factionCmd.message.Ajout reussis", "Joueur ajouté à la faction.");
		if(!config.contains("factionCmd.message.Joueur supprimer")) config.set("factionCmd.message.Joueur supprimer", "Joueur supprimé de la faction.");
		if(!config.contains("factionCmd.message.Joueur deja dans une faction")) config.set("factionCmd.message.Joueur deja dans une faction", "Ce joueur a déjà une faction.");
		if(!config.contains("factionCmd.message.Joueur hors ligne")) config.set("factionCmd.message.Joueur hors ligne", "Ce joueur est hors ligne.");
		if(!config.contains("factionCmd.message.Joueur dans autre fac")) config.set("factionCmd.message.Joueur dans autre fac", "Ce joueur n'est pas dans ta faction !");
		if(!config.contains("factionCmd.message.Joueur rankup reussis")) config.set("factionCmd.message.Joueur rankup reussis", "Joueur mit au grade: admin");
		if(!config.contains("factionCmd.message.Joueur rankup erreur")) config.set("factionCmd.message.Joueur rankup erreur", "On ne peut pas rankup au dessus d'admin !");
		if(!config.contains("factionCmd.message.Owner rankup erreur")) config.set("factionCmd.message.Owner rankup erreur", "Vous ne pouvez pas rankup l'owner !");
		if(!config.contains("factionCmd.message.Joueur rankdown reussis")) config.set("factionCmd.message.Joueur rankdown reussis", "Joueur mit au grade: membre");
		if(!config.contains("factionCmd.message.Joueur rankdown erreur")) config.set("factionCmd.message.Joueur rankdown erreur", "On ne peut pas rankdown en dessous de membre !");
		if(!config.contains("factionCmd.message.Owner rankdown erreur")) config.set("factionCmd.message.Owner rankdown erreur", "Vous ne pouvez pas rankdown l'owner !");
		if(!config.contains("factionCmd.message.Erreur suppresion owner")) config.set("factionCmd.message.Erreur suppresion owner", "Vous ne pouvez pas supprimer l'owner !");
		if(!config.contains("factionCmd.message.Modif home fac")) config.set("factionCmd.message.Modif home fac", "L'home de ta faction a bien été modifié !");
		if(!config.contains("factionCmd.message.Absence home fac")) config.set("factionCmd.message.Absence home fac", "Ta faction n'a pas créée d'home !");
		if(!config.contains("factionCmd.message.Aide message")) config.set("factionCmd.message.Aide message", "Cette commande n'existe pas utilise [Commande] pour avoir la liste des commandes.");
//---------------------------------------------------------------------------------------------------------------------------------------------------
		if(!config.contains("ChunkCmd.argument.Claim")) config.set("ChunkCmd.argument.Claim", "claim");
		if(!config.contains("ChunkCmd.argument.UnClaim")) config.set("ChunkCmd.argument.UnClaim", "unclaim");
//---------------------------------------------------------------------------------------------------------------------------------------------------
		if(!config.contains("ChunkCmd.message.Chunk claim")) config.set("ChunkCmd.message.Chunk claim", "Chunk claim");
		if(!config.contains("ChunkCmd.message.Chunk indisponible")) config.set("ChunkCmd.message.Chunk indisponible", "Ce chunk est déjà claim !");
		if(!config.contains("ChunkCmd.message.Claim pour owner et admins uniquement")) config.set("ChunkCmd.message.Claim pour owner et admins uniquement", "Seul l'owner et les administrateurs d'une faction peuvent claim !");
		if(!config.contains("ChunkCmd.message.Absence de faction")) config.set("ChunkCmd.message.Absence de faction", "Vous ne pouvez pas claim sans avoir de faction !");
		if(!config.contains("ChunkCmd.message.Claim dans overworld")) config.set("ChunkCmd.message.Claim dans overworld", "Vous ne pouvez pas claim ailleur que dans l'overworld !");
		if(!config.contains("ChunkCmd.message.Chunk unclaim")) config.set("ChunkCmd.message.Chunk unclaim", "Chunk unclaim");
		if(!config.contains("ChunkCmd.message.Claim non posseder")) config.set("ChunkCmd.message.Claim non posseder", "Ce claim ne vous appartient pas !");
		if(!config.contains("ChunkCmd.message.Claim libre")) config.set("ChunkCmd.message.Claim libre", "Ce chunk n'est pas claim !");
		
		try {
			config.save(this.file);
		} catch (IOException e) {
		 	e.printStackTrace();
		}
	}
}
