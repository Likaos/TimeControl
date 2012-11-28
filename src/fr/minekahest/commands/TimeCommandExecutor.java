package fr.minekahest.commands;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.minekahest.core.TimeControl;

public class TimeCommandExecutor implements CommandExecutor {
	
	private TimeControl plugin;
	
	public TimeCommandExecutor(TimeControl plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// Verification du joueur = player
		if (!(sender instanceof Player)) {
			TimeControl.log.info("Commande uniquement utilisable en jeu");
			return true;
		}
		
		Player player = (Player) sender;
		
		//On check si le joueur a les droits où s'il est op
		if (!(player.hasPermission("timecontrol.admin")) && !player.isOp()) {
			player.sendMessage(ChatColor.RED+"Vous n'avez pas le droit !");
			return true;
		}
		
		if (args.length > 3 || args.length < 1) {
			return false;
		}
		
		// Var
		int duration;
		World targetWorld = player.getWorld();
		
		// Recup les paramètres du monde en cours
		int dayLenght = plugin.getWorldConfiguration().get(targetWorld.getName()).getDayLenght();
		int nightLenght = plugin.getWorldConfiguration().get(targetWorld.getName()).getNightLenght();
		int refreshRate = plugin.getWorldConfiguration().get(targetWorld.getName()).getRefreshRate();
		
		if (args.length == 1 && args[0].equalsIgnoreCase("show")) {
			showCurrentWorldTimeConfiguration(player, targetWorld, dayLenght, nightLenght, refreshRate);
			return true;
		}
		
		else if (args.length >= 2) {
			
			// Recup du temps
			try {
				duration = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				player.sendMessage(ChatColor.RED+"Merci de définir le temps en secondes et rien d'autre !");
				return true;
			}
			
			if (duration == 0) {
				player.sendMessage(ChatColor.RED+"Impossible de forcer une valeur à 0 pour la durée !!");
				return true;
			}
			
			// Recup du choix jour nuit all
			if (args[0].equalsIgnoreCase("day")) {
				dayLenght = duration;
			} else if (args[0].equalsIgnoreCase("night")) {	
				nightLenght = duration;
			} else if (args[0].equalsIgnoreCase("all")) {
				dayLenght = duration;
				nightLenght = duration;
			} else {
				player.sendMessage(ChatColor.RED+"Le choix de la période n'est pas valide !");
				return true;
			}
			
			if (args.length == 3) {				
				// Recup du refresh
				try {
					refreshRate = Integer.parseInt(args[2]);
				} catch (NumberFormatException e) {
					player.sendMessage(ChatColor.RED+"Merci de définir un refreshrate correcte (en ticks) et rien d'autre !");
					return true;
				}
				
				if (refreshRate == 0) {
					player.sendMessage(ChatColor.RED+"Impossible de forcer une valeur à 0 pour le refresh !!");
					return true;
				}				
			}
		}
		
		else {
			return false;
		}
		
		// On annule la tâche si elle existe
		if (plugin.getTaskList().containsKey("TC_" + targetWorld.getName())) {
			int taskId = plugin.getTaskList().get("TC_" + targetWorld.getName());
			plugin.getServer().getScheduler().cancelTask(taskId);
		}
		
		// Lancement de la tâche
		plugin.runTimeController(targetWorld, dayLenght, nightLenght, refreshRate);
		player.sendMessage(ChatColor.RED+"Configuration du temps changée !");
		setCurrentWorldTimeConfiguration(targetWorld, dayLenght, nightLenght, refreshRate);
		showCurrentWorldTimeConfiguration(player, targetWorld, dayLenght, nightLenght, refreshRate);
		return true;
		
	}
	
	// Sauvegarde des params
	private void setCurrentWorldTimeConfiguration(World targetWorld, int dayLenght, int nightLenght, int refreshRate) {
		plugin.getWorldConfiguration().get(targetWorld.getName()).setDayLenght(dayLenght);
		plugin.getWorldConfiguration().get(targetWorld.getName()).setNightLenght(nightLenght);
		plugin.getWorldConfiguration().get(targetWorld.getName()).setRefreshRate(refreshRate);
	}
	
	// Affichage de la conf
	private void showCurrentWorldTimeConfiguration(Player player, World targetWorld, int dayLenght, int nightLenght, int refreshRate) {
		player.sendMessage(ChatColor.GOLD+"Configuration actuelle du monde: " + targetWorld.getName());
		player.sendMessage(ChatColor.GOLD+"Durée du jour: " + Integer.toString(dayLenght) + " seconde(s).");
		player.sendMessage(ChatColor.GOLD+"Durée de la nuit: " + Integer.toString(nightLenght) + " seconde(s).");
		player.sendMessage(ChatColor.GOLD+"Refresh tous les: " + Integer.toString(refreshRate) + " ticks.");
		
	}
	
}
