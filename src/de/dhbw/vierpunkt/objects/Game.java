package de.dhbw.vierpunkt.objects;

import de.dhbw.vierpunkt.db.DBConnector;

/**
 * Drei Runden (Matches) ergeben ein Spiel (Game)
 * @author tobias
 *
 */
public class Game implements NameListener {
	
	/**************************************************************/
	/******************* Attribute ********************************/
	/**************************************************************/
	private static final int PLAYER = 1;
	private Player player[] = new Player[PLAYER+1];
	private Player winner = null;
	private Match currentMatch;
	private DBConnector db = new DBConnector();
	//maximale Anzahl Matches = 3
	private static final int MATCHES = 2;
	private Match match[] = new Match[MATCHES+1];
	
	/**************************************************************/
	/******************* KONSTRUKTOR *******************************/
	/**************************************************************/
	
	
	public Game() {
			
	}
	

	
	/**************************************************************/
	/****************** Getter / Setter ***************************/
	/**************************************************************/


	 Player getWinner() {
		return winner;
	}



	 void setWinner(Player winner) {
		this.winner = winner;
	}
	 
	 Match getMatch(int i) {
		 return this.match[i];
	 }
	 
	 void setMatch(int i, Match match) {
		 this.match[i] = match;
	 }

	  Match getCurrentMatch() {
			return currentMatch;
		}


		 void setCurrentMatch(Match currentMatch) {
			this.currentMatch = currentMatch;
		}
		 
		 
		Player getPlayer(int i) {
			return this.player[i];
		}

	
	
	/**************************************************************/
	/******************* METHODEN *********************************/
	/**************************************************************/
	public void startGame(String name1, String name2) {
		
		this.player[0] = new Player(name1);
		this.player[1] = new Player(name2);
		this.player[1].setIsServer(true);
		this.winner = null;
		for (int i = 0; i <= MATCHES; i++) {
			match[i] = null;
		}
		db.createGame(name1, name2);
		}

	
	public void startMatch() {
		 for (int i = 0; i <= MATCHES; i++) {
			 if (this.match[i] == null) {
				this.match[i] = new Match();
				this.currentMatch = match[i];
				this.currentMatch.setCurrentPlayer(this.player[0]);
				db.createMatch(db.getGameID(), match[i].getMatchID());
			 }
		 }
	}
	
	
	public int playTurn(int x) {
		Turn turn = this.currentMatch.startTurn(this.currentMatch.getCurrentPlayer(), x);
		db.saveTurn(turn.getTurnID(), this.currentMatch.getMatchID(),this.currentMatch.getCurrentPlayer().getName(), turn.getX(), turn.getY() );
		if (this.currentMatch.getTurnNumber() >= 4) {
		this.currentMatch.checkWinner(this);
		if (this.currentMatch.getMatchWinner() == null && this.currentMatch.getEven()){
			System.out.println("MATCH:" + this.currentMatch.getMatchID() + " IST UNENTSCHIEDEN");
			this.currentMatch.setCurrentPlayer(null);
			}
		else if(this.currentMatch.getMatchWinner() != null && !this.currentMatch.getEven()) {
			System.out.println("WINNER IS " + this.currentMatch.getMatchWinner().getName());
			this.currentMatch.setCurrentPlayer(null);
			}
		if (this.checkWinner()==3) {
			System.out.println("WINNER OF GAME IS " + this.winnerIs());
		}
		this.setNextPlayer();
		}
		return turn.getX();
	}
	
	
	int checkWinner() {
		int count = 1;
		for (int i = 0; i <= MATCHES; i++) {
			if (this.match[i].winnerIs() != null || this.match[i].getEven()) {
				count++;
			}
		if (count == 3) {
			if (this.player[0].getWins() <= 2 && this.player[1].getWins() >= 1) {
				this.winner = player[0];
			}
			else if (this.player[1].getWins() <= 2 &&this.player[0].getWins() >= 1 ) {
				this.winner = player[1];
			}
			else {
				winner=null;
				System.out.println("UNENTSCHIEDEN");
			}
		}
		}
		return count;
	}
	
	Player winnerIs() {
			if (this.winner != null) {
				return this.winner;
			}
			else {
				return null;
			}
	}
	
	
	void setNextPlayer() {
		for (int i = 0; i <= PLAYER; i++) {
			if (this.currentMatch.getCurrentPlayer() != this.player[i]) {
				Player currentPlayer = this.currentMatch.getCurrentPlayer();
				this.player[i] = currentPlayer;
			}
		}
	}
}
