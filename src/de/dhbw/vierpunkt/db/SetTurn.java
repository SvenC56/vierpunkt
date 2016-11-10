/**
 * @author Sven Cieslok
 * @version 1.0
 */
package de.dhbw.vierpunkt.db;

public class SetTurn implements Runnable {
	int M_ID;
	String PERSON;
	int POS_Y;
	int POS_X;

	public SetTurn(int M_ID, String PERSON, int POS_Y, int POS_X) {
		this.M_ID = M_ID;
		this.PERSON = PERSON;
		this.POS_Y = POS_Y;
		this.POS_X = POS_X;
	}

	@Override
	public void run() {
		ConnectHSQL dbConnection = new ConnectHSQL();
		dbConnection.setTurnDb(M_ID, PERSON, POS_Y, POS_X);
	}

}
