package fr.minekahest.core;

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import fr.minekahest.commands.TimeCommandExecutor;

public class TimeControl extends JavaPlugin {
	
	// Init
	public static TimeControl instance;
	
	public TimeControl() {
		instance = this;
	}
	
	// Variables
	public static final Logger log = Logger.getLogger("Minecraft");
	private HashMap<String, WorldConfig> worldConfiguration = new HashMap<String, WorldConfig>();
	private HashMap<String, Integer> taskList = new HashMap<String, Integer>();
	
	@Override
	public void onEnable() {
		
		// Cree un fichier de config si inexistant
		this.saveDefaultConfig();
		// Chargement de la configuration
		this.loadWorldsConfiguration();		
		//Commandes
		this.getCommand("timecontrol").setExecutor(new TimeCommandExecutor(this));
		
	}
	
	@Override
	public void onDisable() {
		//Kill des tasks
		this.getServer().getScheduler().cancelTasks(this);
	}
	
	// Lancement des synchro du monde
	public void runTimeController(World targetWorld, int dayLenght, int nightLenght, int refreshRate) {
		int taskId = getServer().getScheduler().scheduleSyncRepeatingTask(this, new TimeControlRunnable(targetWorld, dayLenght, nightLenght, refreshRate), 0, refreshRate);
		this.taskList.put("TC_"+targetWorld.getName(), taskId);
		
	}
	
	// Chargement des mondes/configs
	public void loadWorldsConfiguration() {
		for (World w : getServer().getWorlds()) {
			
			// Seul les mondes avec un environnement normal nous intéresse
			if (w.getEnvironment().getId() == 0) {
				String worldName = w.getName();
				//Gestion du temps
				int dayLenght = getConfig().getInt("Worlds." + worldName + ".day");
				int nightLenght = getConfig().getInt("Worlds." + worldName + ".night");
				int refreshRate = getConfig().getInt("Worlds." + worldName + ".refresh");
				//Task de gestion du temps
				runTimeController(getServer().getWorld(worldName), dayLenght, nightLenght, refreshRate);
				
				//Sauvegarde des paramètres du monde
				WorldConfig wC = new WorldConfig(dayLenght, nightLenght, refreshRate);
				this.worldConfiguration.put(worldName, wC);
				
				log.info("Chargement de la configuration du monde: " + worldName);
				log.info("Duree du jour: " + Integer.toString(dayLenght));
				log.info("Duree de la nuit: " + Integer.toString(nightLenght));
				log.info("Refresh Rate: " + Integer.toString(refreshRate));
			}
		}
	}

	public HashMap<String, WorldConfig> getWorldConfiguration() {
		return worldConfiguration;
	}
	
	public void setWorldConfiguration(HashMap<String, WorldConfig> worldConfiguration) {
		this.worldConfiguration = worldConfiguration;
	}

	public HashMap<String, Integer> getTaskList() {
		return taskList;
	}

	public void setTaskList(HashMap<String, Integer> taskList) {
		this.taskList = taskList;
	}
	
}
