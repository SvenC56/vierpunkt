package de.dhbw.mannheim.vierpunkt.db;

public class sendWinner implements Runnable {

	private int G_ID;
	private String Winner;

	public sendWinner(int G_ID, String Winner){
		this.G_ID = G_ID;
		this.Winner = Winner;
	}
	
	@Override
	public void run() {
		connectHSQL dbConnection = new connectHSQL();
		dbConnection.executeSQL("Select * from Game");
	}

}
