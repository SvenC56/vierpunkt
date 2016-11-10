/**
 * @author Sven Cieslok
 * @version 1.0
 */
package de.dhbw.vierpunkt.db;

public class SetMatch implements Runnable {
	int M_ID;
	int G_ID;
	int MATCHNUMBER;

	public SetMatch(int M_ID, int G_ID, int MATCHNUMBER) {
		this.M_ID = M_ID;
		this.G_ID = G_ID;
		this.MATCHNUMBER = MATCHNUMBER;
	}

	@Override
	public void run() {
		ConnectHSQL dbConnection = new ConnectHSQL();
		dbConnection.setMatchDb(M_ID, G_ID, MATCHNUMBER);
	}

}
