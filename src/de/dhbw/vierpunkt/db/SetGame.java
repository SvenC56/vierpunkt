/**
 * @author Sven Cieslok
 * @version 1.0
 */
package de.dhbw.vierpunkt.db;

public class SetGame implements Runnable {
	String Player1;
	String Player2;
	String WINNER;
	String POINTS;

	public SetGame(String Player1, String Player2, String WINNER, String POINTS) {
		this.Player1 = Player1;
		this.Player2 = Player2;
		this.WINNER = WINNER;
		this.POINTS = POINTS;
	}

	@Override
	public void run() {
		ConnectHSQL dbConnection = new ConnectHSQL();
		dbConnection.setGameDb(Player1, Player2, WINNER, POINTS);
	}

}
