/**
 * @author Sven Cieslok
 * @version 1.0
 */
package de.dhbw.vierpunkt.db;

public class SetGame implements Runnable {
	String Player1;
	String Player2;
	String WINNER;

	public SetGame(String Player1, String Player2, String WINNER) {
		this.Player1 = Player1;
		this.Player2 = Player2;
		this.WINNER = WINNER;
	}

	@Override
	public void run() {
		ConnectHSQL dbConnection = new ConnectHSQL();
		dbConnection.setGameDb(Player1, Player2, WINNER);
	}

}
