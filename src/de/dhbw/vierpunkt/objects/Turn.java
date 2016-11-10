package de.dhbw.vierpunkt.objects;
import de.dhbw.vierpunkt.objects.AlphaBeta;

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
	private Match match;
	
	
	
	/**************************************************************/
	/******************* KONSTRUKTOR *******************************/
	/**************************************************************/

		
	
	public Turn(int turnID, Player player, Match match) {
		this.turnID=turnID;
		this.player=player;
		this.match = match;
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
	
	public void startOpponentTurn(int x) {
		this.match.startTurn(this.player, this.x);
	}
	
	
	
	
	public int startAgentTurn() {
		int x;
		if (turnID == 0) {
			x=3;
		}
		else {
		 x = ki.calcMove(this.match, this.depth);
		}
		this.match.startTurn(this.player, x);
		return x;
	}
	
}
	


