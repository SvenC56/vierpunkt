package de.dhbw.mannheim.vierpunkt.objects;

import de.dhbw.mannheim.vierpunkt.db.DBConnector;

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
	private Player player[] = new Player[PLAYER];
	private Player winner = null;
	private Match[] match;
	private Match currentMatch;
	private DBConnector db = new DBConnector();
	//maximale Anzahl Matches = 3
	private static final int MATCHES = 2;
	
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
		
		void setPlayer (int i, Player player) {
			this.player[i] = player;
		}
		
		Player getPlayer(int i) {
			return this.player[i];
		}

	
	
	/**************************************************************/
	/******************* METHODEN *********************************/
	/**************************************************************/
	public void startGame(String name1, String name2, int isServer) {
		
		this.player[0] = new Player(name1);
		this.player[1] = new Player(name2);
		if (isServer == 1) {
			this.player[0].setIsServer(true);
		}
		else {
			this.player[1].setIsServer(true);
		}
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
				db.createMatch(db.getGameID(), match[i].getMatchID());
			 }
	}
	}
}
