package de.dhbw.vierpunkt.logic;

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
	private int GameID;
	private int matchID;
	private static final int PLAYER = 1;
	private Player player[] = new Player[PLAYER+1];
	private Player winner = null;
	private Match currentMatch;
	private DBConnector db = new DBConnector();
	//maximale Anzahl Matches = 3
	private static final int MATCHES = 2;
	private Match match[] = new Match[MATCHES+1];
	private Match manMatch;
	

	
	/**************************************************************/
	/****************** Getter / Setter ***************************/
	/**************************************************************/


	 Player getWinner() {
		return winner;
	}



	 void setWinner(Player winner) {
		this.winner = winner;
	}
	 

	  public Match getCurrentMatch() {
			return currentMatch;
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



		private void setMatchID(int matchID) {
			this.matchID = matchID;
		}
		
		 int getGameID() {
			return GameID;
		}



		private void setGameID(int gameID) {
			GameID = gameID;
		}
	
	
	/**************************************************************/
	/******************* METHODEN *********************************/
	/**************************************************************/
		
	/**Startet ein neues Spiel und legt hierfuer zwei Spieler an
	 * Diese Klasse laeuft und legt jeweils ein neues Match an sobald ein Match abgeschlossen wurde, maximal 3
	 * (non-Javadoc)
	 * @see de.dhbw.vierpunkt.logic.NameListener#startGame(java.lang.String, java.lang.String)
	 **/
		
	public void startManGame(String name1, String name2) {
		this.player[0] = new Player(name1);
		this.player[1] = new Player(name2);
		this.player[1].setIsOpponent(true);
		manMatch = new Match(this, 0);

	}
	
	/**
	 * Startet manuellen Turn
	 * @param name
	 * @param x
	 * @param y
	 */
	public void setManTurn(String name, int x, int y) {
		this.manMatch.startManTurn(name, x, y);
	}
		
	public void startGame(String name1, String name2) {
		
		this.player[0] = new Player(name1);
		this.player[1] = new Player(name2);
		this.player[1].setIsOpponent(true);
		this.winner = null;
		db.createGame(name1, name2);
		this.setGameID(db.getGameID());
		startMatch();
		
		}

	/**
	 * Diese Methode startet automastisiert bis zu maximal 3 Matches (3 Saetze)
	 */
	public void startMatch() {
		 for (int i = 0; i <= MATCHES; i++) {	//durch das Match-Array
			 if (this.match[i] == null) {	//wenn freie Position gefunden
				this.match[i] = new Match(this, i);		//neues Match erstellen
				this.currentMatch = match[i];			//als currentMatch() setzen
				db.createMatch(db.getGameID(), match[i].getMatchNumber()); // Match in DB speichern
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
	
	/**
	 * Ueberprueft ob es ein Game-Gewinner gibt und liefert den Gewinner zurueck
	 * @return winner
	 */
	Player checkWinner() {
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
		return this.winner;
	}
	

	/**
	 * Der naechste Spieler ist dran. Methode wechselt den currentPlayer
	 */
	void setNextPlayer() {
		System.out.println("Gerade spielt: " + this.currentMatch.getCurrentPlayer().getName());
		if (this.currentMatch.getCurrentPlayer() == this.getPlayer(0) ) {
			this.currentMatch.setCurrentPlayer(this.player[1]);
		}
		else {
			this.currentMatch.setCurrentPlayer(this.player[0]);
		}
	}
	
}
