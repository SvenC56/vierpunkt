package de.dhbw.vierpunkt.objects;
import de.dhbw.vierpunkt.logic.AlphaBeta;

public class Turn {
	
	/**************************************************************/
	/******************* Attribute ********************************/
	/**************************************************************/
	private int turnID;
	private Player player;
	private int x;
	private int y;
	private boolean turnActive = false;
	private AlphaBeta ki = new AlphaBeta();
	private static int depth = 6;
	
	
	
	/**************************************************************/
	/******************* KONSTRUKTOR *******************************/
	/**************************************************************/

		
	
	public Turn(int turnID, Player player, int x, int y) {
	this.turnID = turnID;
	this.player = player;
	this.x= x;
	this.y = y;
		
	}
	
	/**************************************************************/
	/****************** Getter / Setter ***************************/
	/**************************************************************/
	
	
	public Player getPlayer() {
		return this.player;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getTurnID() {
		return turnID;
	}

	public void setTurnID(int turnID) {
		this.turnID = turnID;
	}

	public boolean isTurnActive() {
		return turnActive;
	}

	public void setTurnActive(boolean turnActive) {
		this.turnActive = turnActive;
	}
	
	
	
	/**************************************************************/
	/******************* METHODEN *********************************/
	/**************************************************************/
	
	
}
	


