package de.dhbw.mannheim.vierpunkt.db;

public class sendGame implements Runnable {
	int G_ID;
	String Player1;
	String Player2;
	String WINNER;
	String POINTS;

	public sendGame(int G_ID, String Player1, String Player2, String WINNER, String POINTS) {
		this.G_ID = G_ID;
		this.Player1 = Player1;
		this.Player2 = Player2;
		this.WINNER = WINNER;
		this.POINTS = POINTS;
	}

	@Override
	public void run() {
		connectHSQL dbConnection = new connectHSQL();
		dbConnection.handOverGame(G_ID, Player1, Player2, WINNER, POINTS);
	}

}
