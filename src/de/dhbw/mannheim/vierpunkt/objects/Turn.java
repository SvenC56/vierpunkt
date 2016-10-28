package de.dhbw.mannheim.vierpunkt.objects;
import de.dhbw.mannheim.vierpunkt.logic.AlphaBeta;

public class Turn extends Match {
	
	/**************************************************************/
	/******************* Attribute ********************************/
	/**************************************************************/
	private int TurnID = 0;
	private Player player;
	private int x;
	private int y;
	
	
	
	/**************************************************************/
	/******************* KONSTRUKTOR *******************************/
	/**************************************************************/

		
	
	public Turn() {
		this.TurnID = this.getTurnNumber();
		
	}
	
	/**************************************************************/
	/****************** Getter / Setter ***************************/
	/**************************************************************/
	
	public int getID() {
		return this.TurnID;
	}
	
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
	
	
	
	/**************************************************************/
	/******************* METHODEN *********************************/
	/**************************************************************/
	

		
	}
	
	


