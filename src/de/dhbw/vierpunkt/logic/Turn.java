package de.dhbw.vierpunkt.logic;
import de.dhbw.vierpunkt.logic.AlphaBeta;

public class Turn {
	
	/**************************************************************/
	/******************* Attribute ********************************/
	/**************************************************************/
	private int turnNumber;
	private Player player;
	private int x;
	private int y;
	private AlphaBeta ki = new AlphaBeta();
	private static int depth = 6;
	private Match match;
	
	
	
	/**************************************************************/
	/******************* KONSTRUKTOR *******************************/
	/**************************************************************/

		
	
	public Turn(int turnNumber, Player player, Match match) {
		this.turnNumber=turnNumber;
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
	
	
	
	/**************************************************************/
	/******************* METHODEN *********************************/
	/**************************************************************/
	/**
	 * Spielt den Zug des Gegners, ohne die KI aufzurufen
	 * @param x
	 */
	public void startOpponentTurn(int x) {
		if (this.turnNumber == 0) {
			this.match.setCurrentPlayer(this.match.getGame().getPlayer(1));
		}
	 this.y = this.match.validPosition(x);
	 this.x = x;
	 this.match.setField(this.x, this.y, this.player); //In unser virtuelles Spielfeld legen (fuer KI)
	 this.match.getGame().getDb().saveTurn(this.match.getGame().getMatchID(), this.match.getCurrentPlayer().getName(), x, this.y);
	 this.match.checkWinner(); //Prueft, ob es einen Gewinner im Match gibt
	 if (this.match.getMatchWinner() != null || this.match.getEven()) { //wenn Gewinner oder unentschieden
		//Hier wird die Datenbank informiert und der Score gespeichert
		 this.match.getGame().getDb().saveMatchScore(this.match.getGame().getMatchID(), this.match.getScore());;
	 }
	 this.match.getGame().setNextPlayer();
	 this.match.setTurnActive(false); 
	 this.match.setNewTurn();
	} 
	
	
	
	/**
	 * Spielt den Zug des Agent und ruft die KI auf
	 * @return
	 */
	public int startAgentTurn() {
		int x;
		System.out.println("Start Agent turnID:" + this.turnNumber);
		if (this.turnNumber == 0) {
			this.match.setCurrentPlayer(this.match.getGame().getPlayer(0));
			x = 3;
		}
		else {
		 x = ki.calcMove(this.match, this.depth);
		}
		int y = this.match.validPosition(x);
		 this.match.setField(this.x, this.y, this.player); //In unser virtuelles Spielfeld legen (fuer KI)
		 this.match.getGame().getDb().saveTurn(this.match.getGame().getMatchID(), this.match.getCurrentPlayer().getName(), x, this.y);
		 this.match.checkWinner(); //Prueft, ob es einen Gewinner im Match gibt
		 if (this.match.getMatchWinner() != null || this.match.getEven()) { //wenn Gewinner oder unentschieden
			 //Hier wird die Datenbank informiert und der Score gespeichert
			 this.match.getGame().getDb().saveMatchScore(this.match.getGame().getMatchID(), this.match.getScore());;
		 }
		 this.match.getGame().setNextPlayer();
		 this.match.setTurnActive(false);
		 this.match.setNewTurn();
		return x;
	}
	
}
	


