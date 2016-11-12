package de.dhbw.vierpunkt.logic;
import java.util.ArrayList;
import java.util.List;

import de.dhbw.vierpunkt.gui.GameWinnerListener;
import de.dhbw.vierpunkt.interfaces.GewinnerListener;
import de.dhbw.vierpunkt.logic.AlphaBeta;

public class Turn {
	
	/**************************************************************/
	/******************* Attribute ********************************/
	/**************************************************************/
	private int turnNumber;
	private Player player;
	private int x=0;
	private int y=0;
	private AlphaBeta ki = new AlphaBeta();
	private static int depth = 6;
	private Match match;
	private static List<GameWinnerListener> GameWinnerListeners = new ArrayList<GameWinnerListener>();
	
	
	
	/**************************************************************/
	/******************* KONSTRUKTOR *******************************/
	/**************************************************************/

		
	
	public Turn(int turnNumber, Player player, Match match) {
		this.turnNumber=turnNumber;
		this.player=player;
		this.match = match;
		this.x= 0;
		this.y=0;
	}
	

	/**************************************************************/
	/****************** Getter / Setter ***************************/
	/**************************************************************/
	
	Player getPlayer() {
		return this.player;
	}

	int getX() {
		return x;
	}

	void setX(int x) {
		this.x = x;
	}

	int getY() {
		return y;
	}

	void setY(int y) {
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
	 if (this.match.getMatchWinner() == null || !this.match.getEven())	{
		 this.x = x;
		 this.y = this.match.validPosition(this.x);
		 if (this.y != -1) {
		 this.match.setField(this.x, this.y, this.player); //In unser virtuelles Spielfeld legen (fuer KI)
		 this.match.getGame().getDb().saveTurn(this.match.getGame().getMatchID(), this.match.getCurrentPlayer().getName(), x, this.y);
		 //Prueft auf Gewinner
		 if (this.match.checkWinner() != null || this.match.getEven()) { //wenn Gewinner oder unentschieden
			 //Hier wird die Datenbank informiert und der Score gespeichert
			 this.match.getGame().getDb().saveMatchScore(this.match.getGame().getMatchID(), this.match.getScore());;
			 //Prueft auf Spielgewinner
			 if (this.match.getMatchNumber() == 2){
			 if ( this.match.getGame().checkWinner() != null) {
				 this.match.getGame().getDb().saveGameWinner(this.match.getGame().getGameID(), this.match.getGame().checkWinner().getName());
				 fireGameWinnerEvent(this.match.getGame().getWinner().getName());
				 }
		 }}}
	 this.match.getGame().setNextPlayer();
	 this.match.setTurnActive(false); 
	 this.match.setNewTurn();
	 }
	} 
	
	
	
	/**
	 * Spielt den Zug des Agent und ruft die KI auf
	 * @return
	 */
	public int startAgentTurn() {
		System.out.println("Start Agent turnID:" + this.turnNumber);
		if (this.turnNumber == 0) {
			this.match.setCurrentPlayer(this.match.getGame().getPlayer(0));
			this.x = 3;
		}
		else {
		if (this.match.getMatchWinner() == null || !this.match.getEven())	{
		 this.x = ki.calcMove(this.match, this.depth);
		 System.out.println("Unsere KI empfiehlt: " + this.x);
		 if (this.x == -1) {
			this.x = this.setValidRandomTurn(); 
		 }
		 else {
			 this.y = this.match.validPosition(x);
			 this.match.setField(this.x, this.y, this.player); //In unser virtuelles Spielfeld legen (fuer KI)
		 }
		 this.match.getGame().getDb().saveTurn(this.match.getGame().getMatchID(), this.match.getCurrentPlayer().getName(), x, this.y);
		 //Prueft auf Gewinner
		 if (this.match.checkWinner() != null || this.match.getEven()) { //wenn Gewinner oder unentschieden
			 //Hier wird die Datenbank informiert und der Score gespeichert
			 this.match.getGame().getDb().saveMatchScore(this.match.getGame().getMatchID(), this.match.getScore());;
			 if (this.match.getMatchNumber() == 2){
				 if ( this.match.getGame().checkWinner() != null) {
					 this.match.getGame().getDb().saveGameWinner(this.match.getGame().getGameID(), this.match.getGame().checkWinner().getName());
					 fireGameWinnerEvent(this.match.getGame().getWinner().getName());
					 }
			 }}}
		 this.match.getGame().setNextPlayer();
		 this.match.setTurnActive(false);
		 this.match.setNewTurn();
		}
		return this.x;
	}
	
	public void addGameWinnerListener (GameWinnerListener toAdd) {
		GameWinnerListeners.add(toAdd);
	}
	
	public void fireGameWinnerEvent(String gewinnerName){
		for(GameWinnerListener gwl : GameWinnerListeners){
			gwl.gewinnermethode(gewinnerName);
		}
	}
	
	/**
	 * Setzt einen zufaelligen Zug, sollte KI oder Game zu langsam sein
	 * @return
	 */
	public int setValidRandomTurn() {
		System.out.println("HANDLE ERROR!");
		int y=-1;
		  while (y == -1) {
			 int x = (int) (Math.random()*7);
			 y = this.match.validPosition(x);
		  }
		  this.match.setField(this.x, this.y, this.player);
		  
		 return x;
	}
	
}
	


