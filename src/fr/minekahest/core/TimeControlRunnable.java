package fr.minekahest.core;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class TimeControlRunnable implements Runnable {
	
	protected World world = null;
	protected float dayLenght;
	protected float nightLenght;
	protected float oldRealTime;
	protected int refreshRate;
	protected float timeBalance;
	protected float newRealTime;
	protected long estimatedRealTime;
	//protected int frozenTime = -1;
	
	public TimeControlRunnable(World targetWorld, float dayLenght, float nightLenght, int refreshRate) {
		this.world = targetWorld;
		this.oldRealTime = world.getTime();
		this.dayLenght = dayLenght;
		this.nightLenght = nightLenght;
		this.refreshRate = refreshRate;
	}
	
	@Override
	public void run() {
		
		//MineKahest.log.info("Resultat " + Float.toString(world.getTime()));
		
		// Le temps a été modifié par une commande
		if (world.getTime() != estimatedRealTime) {
			//MineKahest.log.info("Modification du temps detecte, ajustement");
			newRealTime = world.getTime();
		}
		
		// Nuit tick
		if (newRealTime > 12000)
			timeBalance = refreshRate * (12000 / (nightLenght * 20));
		// Jour tick
		else
			timeBalance = refreshRate * (12000 / (dayLenght * 20));
		
		/*
		 * Note a moi meme, la balance prend deja en compte l'ajustement
		 * en fonction du nombre de ticks, ne pas oublier
		 */
		newRealTime += timeBalance;
		
		//MineKahest.log.info("NEWREALTIME " + Float.toString(newRealTime));
		//MineKahest.log.info("OLDREALTIME " + Float.toString(oldRealTime));
		
		// Le temps change pour la nuit
		if (newRealTime > oldRealTime && oldRealTime <= 12000 && newRealTime > 12000) {
			for (Player p : world.getPlayers()) {
				p.sendMessage(ChatColor.YELLOW + "Le soleil se couche... ");
			}
		}
		// Le temps changer pour le jour
		else if (oldRealTime > newRealTime && oldRealTime > 12000 && newRealTime <= 12000) {
			for (Player p : world.getPlayers()) {
				p.sendMessage(ChatColor.YELLOW + "Le soleil se lève... ");
			}
		}
		
		// Application des modifications
		world.setTime((long) newRealTime);
		oldRealTime = newRealTime;
		// Prediction
		estimatedRealTime = (long) newRealTime + refreshRate;
		
		//MineKahest.log.info("balance: " + Float.toString(timeBalance));
		//MineKahest.log.info("Estimation du prochain time world " + Long.toString(estimatedRealTime));
	}
}
