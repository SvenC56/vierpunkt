/**
 * @author Sven Cieslok
 * @version 1.0
 */
package de.dhbw.vierpunkt.db;

public class SetScore implements Runnable {
	int M_ID;
	String SCORE;

	public SetScore(int M_ID, String SCORE) {
		this.M_ID = M_ID;
		this.SCORE = SCORE;
	}

	@Override
	public void run() {
		ConnectHSQL dbConnection = new ConnectHSQL();
		dbConnection.updateMatch(M_ID, SCORE);
	}

}
