/**
 * @author Sven Cieslok
 * @version 1.0
 */
package de.dhbw.vierpunkt.db;

public class SetMatch implements Runnable {
	int G_ID;
	int MATCHNUMBER;

	public SetMatch(int G_ID, int MATCHNUMBER) {
		this.G_ID = G_ID;
		this.MATCHNUMBER = MATCHNUMBER;
	}

	@Override
	public void run() {
		ConnectHSQL dbConnection = new ConnectHSQL();
		dbConnection.setMatchDb(G_ID, MATCHNUMBER);
	}

}
