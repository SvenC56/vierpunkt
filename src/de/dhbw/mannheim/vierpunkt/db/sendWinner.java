package de.dhbw.mannheim.vierpunkt.db;

public class sendWinner implements Runnable {

	private int G_ID;
	private String Winner;
	private String Points;

	public sendWinner(int G_ID, String Winner, String Points){
		this.G_ID = G_ID;
		this.Winner = Winner;
		this.Points = Points;
	}
	
	@Override
	public void run() {
		connectHSQL dbConnection = new connectHSQL();
		dbConnection.executeSQL("UPDATE GAME SET WINNER='"+ Winner +"', POINTS='"+ Points +"' WHERE G_ID="+G_ID+";");
	}

}
