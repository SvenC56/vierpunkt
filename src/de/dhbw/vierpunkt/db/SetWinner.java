/**
 * @author Sven Cieslok
 * @version 1.0
 */
package de.dhbw.vierpunkt.db;

public class SetWinner implements Runnable {

	private int G_ID;
	private String Winner;
	private String Points;

	public SetWinner(int G_ID, String Winner, String Points){
		this.G_ID = G_ID;
		this.Winner = Winner;
		this.Points = Points;
	}
	
	@Override
	public void run() {
		ConnectHSQL dbConnection = new ConnectHSQL();
		dbConnection.executeSQL("UPDATE GAME SET WINNER='"+ Winner +"', POINTS='"+ Points +"' WHERE G_ID="+G_ID+";");
	}

}
