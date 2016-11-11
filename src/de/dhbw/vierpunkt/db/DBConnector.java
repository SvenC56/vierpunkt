/**
 * Schnittstelle zum Programm
 *
 * @author Sven Cieslok
 * @version 1.0
 */
package de.dhbw.vierpunkt.db;

public class DBConnector {
	
	private ConnectHSQL db = new ConnectHSQL();
	
	/**
	 * uebermittlung der letzten 10 Spiele aus Game
	 */
	public String[][] getLastTenGames() {
		String[][] highscore = db.getLastTenGames();
		return highscore;
	}
	
	/**
	 * Speichert den durchgefuehrten Zug in der Datenbank
	 * @param x
	 * @param y
	 */
	public void saveTurn(int matchID, String playername, int x, int y) {
		SetTurn turnDBThread = new SetTurn(matchID,playername, x, y);
		turnDBThread.run();
	}
	
	
	public void saveGame(int gameID, String playerName1, String playerName2) {
		SetGame g1 = new SetGame(playerName1, playerName2);
		g1.run();
	}
	
	public void saveMatchScore(int matchID, String score) {
		SetScore g2 = new SetScore(matchID, score);
		g2.run();
	}
	
	public void saveGameWinner(int gameID, String playerName) {
		SetScore g3 = new SetScore(gameID, playerName);
		g3.run();
	}
	
	public void saveMatch(int gameID, int matchID, int matchnumber) {
		SetMatch m1 = new SetMatch(gameID, matchnumber);
		m1.run();
	}
	
	
	
	public void createGame(String player1, String player2) {
		SetGame dbGame = new SetGame(player1, player2);
		dbGame.run();
	}
	
	
	public void createMatch(int gameID, int matchnumber) {
		SetMatch dbMatch = new SetMatch(gameID, matchnumber);
		dbMatch.run();
	}
	
	public int getGameID() {
		ConnectHSQL dbGID = new ConnectHSQL();
		return dbGID.getMaxId("GAME");
	}
	
	public int getMatchID() {
		ConnectHSQL dbGID = new ConnectHSQL();
		return dbGID.getMaxId("MATCH");
	}
	
	
	//Methode, die Anhand der GameID, die entsprechende MatchID zurueckgibt!
	
	


}
