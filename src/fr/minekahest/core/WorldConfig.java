package fr.minekahest.core;

public class WorldConfig {
	
	//Cette classe sert uniquement a garder en memoire la configuration pour la modifier en jeu.
	
	private int dayLenght, nightLenght, refreshRate;
	
	public WorldConfig (int dayLenght, int nightLenght, int refreshRate) {
		this.setDayLenght(dayLenght);
		this.setNightLenght(nightLenght);
		this.setRefreshRate(refreshRate);		
	}

	public int getDayLenght() {
		return dayLenght;
	}

	public void setDayLenght(int dayLenght) {
		this.dayLenght = dayLenght;
	}

	public int getNightLenght() {
		return nightLenght;
	}

	public void setNightLenght(int nightLenght) {
		this.nightLenght = nightLenght;
	}

	public int getRefreshRate() {
		return refreshRate;
	}

	public void setRefreshRate(int refreshRate) {
		this.refreshRate = refreshRate;
	}
}
