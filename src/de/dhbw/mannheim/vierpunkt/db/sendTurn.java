package de.dhbw.mannheim.vierpunkt.db;

public class sendTurn implements Runnable {
	int M_ID;
	String PERSON;
	int POS_Y;
	int POS_X;

	public sendTurn(int M_ID, String PERSON, int POS_Y, int POS_X) {
		this.M_ID = M_ID;
		this.PERSON = PERSON;
		this.POS_Y = POS_Y;
		this.POS_X = POS_X;
	}

	@Override
	public void run() {
		connectHSQL dbConnection = new connectHSQL();
		dbConnection.handOverTurn(M_ID, PERSON, POS_Y, POS_X);
	}

}
