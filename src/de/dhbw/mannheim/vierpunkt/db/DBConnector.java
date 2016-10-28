package de.dhbw.mannheim.vierpunkt.db;


import de.dhbw.mannheim.vierpunkt.logic.AlphaBeta;

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
	public int getNewMatchID() {
		int matchID = db.getMaxId("Match");
		matchID++; // + 1, da zuletzt belegte ID zurueck

		return matchID;
	}
	
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
	
	public int createGame(String player1, String player2) {
		int gameID = this.getNewMatchID();
		sendGame dbGame = new sendGame(gameID, player1, player2, null, null);
		dbGame.run();
		return gameID;
	}
	//Wo sind in der DB die Matches dem Game zugeordnet??
	
	public int createMatch(int gameID) {
		int matchID = this.getNewMatchID();
		sendMatch dbMatch = new sendMatch(matchID, gameID);
		dbMatch.run();
		return matchID;
	}
	
	//Methode, die Anhand der GameID, die entsprechende MatchID zurueckgibt!
	
	


}
