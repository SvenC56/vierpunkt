package de.dhbw.vierpunkt.objects;
import de.dhbw.vierpunkt.objects.AlphaBeta;

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

	public int getTurnNumber() {
		return turnNumber;
	}

	public void setTurnNumber(int turnNumber) {
		this.turnNumber = turnNumber;
	}

	
	
	
	/**************************************************************/
	/******************* METHODEN *********************************/
	/**************************************************************/
	/**
	 * Spielt den Zug des Gegners, ohne die KI aufzurufen
	 * @param x
	 */
	public void startOpponentTurn(int x) {
	 this.y = this.match.validPosition(x);
	 this.x = x;
	 this.match.setField(this.x, this.y, this.player); //In unser virtuelles Spielfeld legen (fuer KI)
	 this.match.getGame().getDb().saveTurn(this.match.getMatchNumber(), this.match.getCurrentPlayer().getName(), x, this.y);
	 this.match.checkWinner(); //Prueft, ob es einen Gewinner im Match gibt
	 if (this.match.getMatchWinner() != null || this.match.getEven()) { //wenn Gewinner oder unentschieden
		 Player winner = this.match.winnerIs();
		//Hier muss die GUI informiert werden
	 }
	 this.match.setTurnActive(false);
	 this.match.getGame().setNextPlayer();
	} 
	
	
	
	/**
	 * Spielt den Zug des Agent und ruft die KI auf
	 * @return
	 */
	public int startAgentTurn() {
		int x;
		System.out.println("Start Agent turnID:" + this.turnNumber);
		if (this.turnNumber == 0) { //Beim ersten Zug immer Spalte 3
			x=3;
		}
		else {
		 x = ki.calcMove(this.match, this.depth);
		}
		int y = this.match.validPosition(x);
		 this.match.setField(this.x, this.y, this.player); //In unser virtuelles Spielfeld legen (fuer KI)
		 this.match.getGame().getDb().saveTurn(this.match.getMatchNumber(), this.match.getCurrentPlayer().getName(), x, this.y);
		 this.match.checkWinner(); //Prueft, ob es einen Gewinner im Match gibt
		 if (this.match.getMatchWinner() != null || this.match.getEven()) { //wenn Gewinner oder unentschieden
			 Player winner = this.match.winnerIs();
			 //Hier muss die GUI informiert werden
		 }
		this.match.setTurnActive(false);
		this.match.getGame().setNextPlayer();
		return x;
	}
	
}
	


