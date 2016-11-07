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
	private Match match;
	
	
	
	/**************************************************************/
	/******************* KONSTRUKTOR *******************************/
	/**************************************************************/

		
	
	public Turn(Match match, int turnID, Player player, int x, int y) {
	this.match = match;
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
	
	public void playOpponentTurn(Match match int x) {
		Turn turn = this.startTurn(this.player, x);
		if (this.turnID >= 4) {
		}
	}
	
	public int playAgentTurn(Match match) {
		if (!player.getIsOpponent()){
			if (this.turnID>0){
			x = ki.calcMove(this.match, this.depth);
			}
			else {
				x=3;
			}
			
		}
		int y = match.validPosition(x);

		return this.turn[turnNumber];
		}
		return turn.getX();
	}

		
	}
	
	


