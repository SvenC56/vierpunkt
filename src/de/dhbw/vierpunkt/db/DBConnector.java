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
	public void saveTurn(int turnID, int matchID, String playername, int x, int y, int gameID) {
		SetTurn turnDBThread = new SetTurn(matchID,playername, x, y, gameID);
		turnDBThread.run();
	}
	
	
	public void saveGame(int gameID, String playerName1, String playerName2, String winner) {
		SetGame g1 = new SetGame(playerName1, playerName2, winner);
		g1.run();
	}
	
	public void saveScore(int gameID, String winner, String score) {
		SetScore g2 = new SetScore(gameID, winner, score);
		g2.run();
	}
	
	public void saveMatch(int gameID, int matchID, int matchnumber) {
		SetMatch m1 = new SetMatch(gameID, matchID, matchnumber);
		m1.run();
	}
	
	
	
	public void createGame(String player1, String player2, String winner) {
		SetGame dbGame = new SetGame(player1, player2, winner);
		dbGame.run();
	}
	
	
	public void createMatch(int gameID, int matchID, int matchnumber) {
		SetMatch dbMatch = new SetMatch(matchID, gameID, matchnumber);
		dbMatch.run();
	}
	
	
	
	
	//Methode, die Anhand der GameID, die entsprechende MatchID zurueckgibt!
	
	


}
