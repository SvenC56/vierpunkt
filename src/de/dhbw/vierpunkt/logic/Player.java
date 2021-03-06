package de.dhbw.vierpunkt.logic;

public class Player {
	
	/**************************************************************/
	/******************* Attribute ********************************/
	/**************************************************************/

	private String name = null;	//Jeder Spieler hat einen Namen
	private int wins = 0;	//Siege des Spielers im Match
	private boolean isOpponent = false;
	
	
	/**************************************************************/
	/******************* KONSTRUKTOR *******************************/
	/**************************************************************/
	public Player (String name) {
		this.name = name;
		wins = 0;
	}
	
	public Player () {
		
	}
	
	/**************************************************************/
	/****************** Getter / Setter ***************************/
	/**************************************************************/

	
	 void setIsOpponent(boolean isOpponent) {
		this.isOpponent = isOpponent;
	}
	
	 boolean getIsOpponent() {
		return this.isOpponent;
	}
	
	 int getWins() {
		return this.wins;
	}
	
	 public void setWins() {
		this.wins++;
	}

	 public String getName() {
		return name;
	}
}
