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

		
	
	 Turn(int turnNumber, Player player, Match match) {
		this.turnNumber=turnNumber;
		this.player=player;
		this.match = match;
		this.x= 0;
		this.y=0;
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
		 //this.match.getGame().getDb().saveTurn(this.match.getGame().getMatchID(), this.match.getCurrentPlayer().getName(), x, this.y);
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
		 }
			 this.match.getGame().startMatch();}}
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
		//Beim ersten Wurf ist Spalte in der Mitte optimal
		if (this.turnNumber == 0) {
			this.match.setCurrentPlayer(this.match.getGame().getPlayer(0));
			this.x = 3;
			this.y = this.match.validPosition(x);
			 this.match.setField(this.x, this.y, this.player);
		}
		//Nur Spielen, wenn kein Gewinner in Match feststeht
		if ((this.match.getMatchWinner() == null || !this.match.getEven()))	{ 
		if (this.turnNumber != 0) { // Ab dem zweiten Zug...
			 this.x = ki.calcMove(this.match, this.depth);
			 System.out.println("Unsere KI empfiehlt: " + this.x);
			 if (this.x == -1) { //Sollte ein Fehler bei der Kalkulation auftreten
				 this.x = this.setValidRandomTurn(); 
				 return this.x;
			 	}
			 else {
				 this.y = this.match.validPosition(x);
				 this.match.setField(this.x, this.y, this.player); //In unser virtuelles Spielfeld legen (fuer KI)	 
			 	}
		 }
		 //Speichern des Zuges in der Datenbank --> jetzt in Pusher
		 //this.match.getGame().getDb().saveTurn(this.match.getGame().getMatchID(), this.match.getCurrentPlayer().getName(), x, this.y);
		 //Prueft auf Gewinner	
		 if ((this.match.checkWinner() != null || this.match.getEven()) && this.turnNumber >= 7) { //wenn Gewinner oder unentschieden
			 //Hier wird die Datenbank informiert und der Score gespeichert
			 this.match.getGame().getDb().saveMatchScore(this.match.getGame().getMatchID(), this.match.getScore());;
			 this.match.setMatchActive(false);
			 if (this.match.getMatchNumber() == 2){ //Wenn letztes Match erreicht
				 if ( this.match.getGame().checkWinner() != null) {
					 this.match.getGame().getDb().saveGameWinner(this.match.getGame().getGameID(), this.match.getGame().checkWinner().getName());
					 fireGameWinnerEvent(this.match.getGame().getWinner().getName());
					 }
			 }
			 this.match.getGame().startMatch();}
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
	 * Setzt einen zufaelligen Zug, sollten wir zu langsam sein
	 * @return
	 */
	public int setValidRandomTurn() {
		System.out.println("HANDLE ERROR! Play Random Turn!");
		this.y = -1;
		int randomX=0;
		  while (y == -1) { //Durch die Schleife solange bis validPosition gefunden wurde
			 randomX = (int) (Math.random()*7);
			 this.y = this.match.validPosition(randomX);
		  }
		  this.x = randomX;
		  this.match.setField(this.x, this.y, this.player);
		  System.out.println("Saved Random Turn");
		  //this.match.getGame().getDb().saveTurn(this.match.getGame().getMatchID(), this.match.getCurrentPlayer().getName(), x, this.y);
			 //Prueft auf Gewinner	
			 if ((this.match.checkWinner() != null || this.match.getEven()) && this.turnNumber >= 7) { //wenn Gewinner oder unentschieden
				 //Hier wird die Datenbank informiert und der Score gespeichert
				 this.match.getGame().getDb().saveMatchScore(this.match.getGame().getMatchID(), this.match.getScore());
				 if (this.match.getMatchNumber() == 2){
					 if ( this.match.getGame().checkWinner() != null) {
						 this.match.getGame().getDb().saveGameWinner(this.match.getGame().getGameID(), this.match.getGame().checkWinner().getName());
						 fireGameWinnerEvent(this.match.getGame().getWinner().getName());
						 }
				 }
				 this.match.getGame().startMatch();}
			 this.match.getGame().setNextPlayer();
			 this.match.setTurnActive(false);
			 this.match.setNewTurn();	  
		 return this.x;
	}
}
	


