package de.dhbw.vierpunkt.db;



public class DBConnector {
	
	private connectHSQL db = new connectHSQL();
	
	
	/**
	 * Ruft in der Datenbank die aktuelle GameID ab, welche einem Spiel
	 * entspricht. Gibt einen int-Wert zurueck
	 * 
	 * @return
	 */
	
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
		sendTurn turnDBThread = new sendTurn(matchID,playername, x, y);
		turnDBThread.run();
	}
	
	
	public void saveGame(int gameID, String playerName1, String playerName2, String winner, String score) {
		sendGame g1 = new sendGame(playerName1, playerName2, winner, score);
		g1.run();
	}
	
	public void saveScore(int gameID, String winner, String score) {
		sendScore g2 = new sendScore(gameID, winner, score);
		g2.run();
	}
	
	public void saveMatch(int gameID, int matchID) {
		sendMatch m1 = new sendMatch(gameID, matchID);
		m1.run();
	}
	
	
	
	public void createGame(String player1, String player2) {
		sendGame dbGame = new sendGame(player1, player2, null, null);
		dbGame.run();
	}
	
	
	public void createMatch(int gameID, int matchID) {
		sendMatch dbMatch = new sendMatch(matchID, gameID);
		dbMatch.run();
	}
	
	
	
	
	//Methode, die Anhand der GameID, die entsprechende MatchID zurueckgibt!
	
	


}
