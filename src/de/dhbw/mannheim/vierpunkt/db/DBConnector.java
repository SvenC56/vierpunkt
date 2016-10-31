package de.dhbw.mannheim.vierpunkt.db;



public class DBConnector {
	
	private connectHSQL db = new connectHSQL();
	
	
	/**
	 * Ruft in der Datenbank die aktuelle GameID ab, welche einem Spiel
	 * entspricht. Gibt einen int-Wert zurueck
	 * 
	 * @return
	 */
	public int getNewGameID() {
		int gameID = db.getMaxId("Game");
		gameID++; // + 1, da zuletzt belegte ID zurueck

		return gameID;
	}
	
	public int getMaxGameID() {
		int gameID = db.getMaxId("Game");

		return gameID;
	}
	
	public int getGameID() {
		int gameID = db.getMaxId("Game");

		return gameID;
	}
	

	/**
	 * Ruft in der Datenbank die aktuelle Match ab, welche einer Runde
	 * entspricht. Gibt einen int-Wert zurueck
	 * 
	 * @return
	 */

	
	public int getMatchID() {
		int matchID = db.getMaxId("Match");

		return matchID;
	}
	
	/**
	 * Speichert den durchgefuehrten Zug in der Datenbank
	 * @param x
	 * @param y
	 */
	public void saveTurn(int turnID, int matchID, String playername, int x, int y) {
		sendTurn turnDBThread = new sendTurn(turnID,matchID,playername, x, y);
		turnDBThread.run();
	}
	
	
	public void saveGame(int gameID, String playerName1, String playerName2, String winner, String score) {
		sendGame g1 = new sendGame(gameID, playerName1, playerName2, winner, score);
		g1.run();
	}
	
	
	public void saveMatch(int gameID, int matchID) {
		sendMatch m1 = new sendMatch(gameID, matchID);
		m1.run();
	}
	
	public int createGame(String player1, String player2) {
		int gameID = this.getNewGameID();
		sendGame dbGame = new sendGame(gameID, player1, player2, null, null);
		dbGame.run();
		return gameID;
	}
	
	
	public int getNewMatchID(int gameID) {
		int matchID = db.getMaxMatchId(gameID);
		matchID++;
		return matchID;
	}
	
	public int createMatch(int gameID) {
		int matchID = this.getNewMatchID(gameID);
		sendMatch dbMatch = new sendMatch(matchID, gameID);
		dbMatch.run();
		return matchID;
	}
	
	
	
	
	//Methode, die Anhand der GameID, die entsprechende MatchID zurueckgibt!
	
	


}
