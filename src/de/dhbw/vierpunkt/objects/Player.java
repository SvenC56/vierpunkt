package de.dhbw.vierpunkt.objects;

public class Player {
	
	/**************************************************************/
	/******************* Attribute ********************************/
	/**************************************************************/

	private String name = null;	//Jeder Spieler hat einen Namen
	private int wins = 0;	//Siege des Spielers im Match
	private boolean isServer = false;
	
	
	/**************************************************************/
	/******************* KONSTRUKTOR *******************************/
	/**************************************************************/
	public Player (String name) {
		this.name = name;
		wins = 0;
	}
	
	/**************************************************************/
	/****************** Getter / Setter ***************************/
	/**************************************************************/

	
	 void setIsServer(boolean isServer) {
		this.isServer = isServer;
	}
	
	 boolean getIsServer() {
		return this.isServer;
	}
	
	 int getWins() {
		return this.wins;
	}
	
	 void setWins() {
		this.wins++;
	}

	 String getName() {
		return name;
	}

	 void setName(String name) {
		this.name = name;
	}

	/**************************************************************/
	/******************* METHODEN *********************************/
	/**************************************************************/
}
