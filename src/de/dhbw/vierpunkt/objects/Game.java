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
	private int matchID;
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

	  public Match getCurrentMatch() {
			return currentMatch;
		}


		 void setCurrentMatch(Match currentMatch) {
			this.currentMatch = currentMatch;
		}
		 
		 
		Player getPlayer(int i) {
			return this.player[i];
		}
		
		DBConnector getDb() {
			return this.db;
		}
		
		int getMatchID() {
			return matchID;
		}



		void setMatchID(int matchID) {
			this.matchID = matchID;
		}
	
	
	/**************************************************************/
	/******************* METHODEN *********************************/
	/**************************************************************/
		
	/**Startet ein neues Spiel und legt hierfuer zwei Spieler an
	 * Diese Klasse laeuft und legt jeweils ein neues Match an sobald ein Match abgeschlossen wurde, maximal 3
	 * (non-Javadoc)
	 * @see de.dhbw.vierpunkt.objects.NameListener#startGame(java.lang.String, java.lang.String)
	 **/
	public void startGame(String name1, String name2) {
		
		this.player[0] = new Player(name1);
		this.player[1] = new Player(name2);
		this.player[1].setIsOpponent(true);
		this.winner = null;
		db.createGame(name1, name2);
		startMatch();
		
		}

	
	public void startMatch() {
		 for (int i = 0; i <= MATCHES; i++) {
			 if (this.match[i] == null) {
				this.match[i] = new Match(this, i);
				this.currentMatch = match[i];
				this.currentMatch.setCurrentPlayer(this.player[0]);
				db.createMatch(db.getGameID(), match[i].getMatchNumber()); // MatchID
				setMatchID(db.getMatchID());
				this.currentMatch.setMatchActive(true);
				this.currentMatch.setNewTurn();
				break;
			 }
		 }
			while (this.currentMatch.getMatchNumber() <= MATCHES || !this.currentMatch.getEven() || this.currentMatch.winnerIs() == null) {
				if (this.currentMatch.getMatchActive() == false) {
					startMatch();
					this.currentMatch.setNewTurn();
				}
				}
		 
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
				//this.currentMatch.setCurrentPlayer(player[i]);
				this.player[i] = currentPlayer;
			}
		}
	}
}
