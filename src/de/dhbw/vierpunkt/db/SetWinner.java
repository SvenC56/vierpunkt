/**
 * @author Sven Cieslok
 * @version 1.0
 */
package de.dhbw.vierpunkt.db;

public class SetWinner implements Runnable {
	int G_ID;
	String WINNER;

	public SetWinner(int G_ID, String POINTS) {
		this.G_ID = G_ID;
		this.WINNER = WINNER;
	}

	@Override
	public void run() {
		ConnectHSQL dbConnection = new ConnectHSQL();
		dbConnection.updateWinner(G_ID, WINNER);
	}

}
