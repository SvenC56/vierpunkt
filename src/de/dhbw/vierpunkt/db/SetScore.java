/**
 * @author Sven Cieslok
 * @version 1.0
 */
package de.dhbw.vierpunkt.db;

public class SetScore implements Runnable {
	int G_ID;
	String Winner;
	String Points;

	public SetScore(int G_ID, String Winner, String Points) {
		this.G_ID = G_ID;
		this.Winner = Winner;
		this.Points = Points;
	}

	@Override
	public void run() {
		ConnectHSQL dbConnection = new ConnectHSQL();
		dbConnection.setScoreDb(G_ID, Winner, Points);
	}

}
