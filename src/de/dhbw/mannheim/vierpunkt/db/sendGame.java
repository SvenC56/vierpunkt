package de.dhbw.mannheim.vierpunkt.db;

public class sendGame implements Runnable {
	int G_ID;
	String OPPONENT;
	String WINNER;
	int POINTS;

	public sendGame(int G_ID, String OPPONENT, String WINNER, int POINTS) {
		this.G_ID = G_ID;
		this.OPPONENT = OPPONENT;
		this.WINNER = WINNER;
		this.POINTS = POINTS;
	}

	@Override
	public void run() {
		connectHSQL dbConnection = new connectHSQL();
		dbConnection.handOverGame(G_ID, OPPONENT, WINNER, POINTS);
	}

}
