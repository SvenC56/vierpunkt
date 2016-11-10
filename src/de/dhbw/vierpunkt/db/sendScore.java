package de.dhbw.vierpunkt.db;

public class sendScore implements Runnable {
	int G_ID;
	String Winner;
	String Points;

	public sendScore(int G_ID, String Winner, String Points) {
		this.G_ID = G_ID;
		this.Winner = Winner;
		this.Points = Points;
	}

	@Override
	public void run() {
		connectHSQL dbConnection = new connectHSQL();
		dbConnection.handOverScore(G_ID, Winner, Points);
	}

}
