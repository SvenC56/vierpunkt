package de.dhbw.mannheim.vierpunkt.db;

public class sendGame implements Runnable {
	String Player1;
	String Player2;
	String WINNER;
	String POINTS;

	public sendGame(String Player1, String Player2, String WINNER, String POINTS) {
		this.Player1 = Player1;
		this.Player2 = Player2;
		this.WINNER = WINNER;
		this.POINTS = POINTS;
	}

	@Override
	public void run() {
		connectHSQL dbConnection = new connectHSQL();
		dbConnection.handOverGame(Player1, Player2, WINNER, POINTS);
	}

}
