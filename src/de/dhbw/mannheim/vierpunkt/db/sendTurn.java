package de.dhbw.mannheim.vierpunkt.db;

public class sendTurn implements Runnable {
	int T_ID;
	int M_ID;
	String PERSON;
	int POS_Y;
	int POS_X;

	public sendTurn(int T_ID, int M_ID, String PERSON, int POS_Y, int POS_X) {
		this.T_ID = T_ID;
		this.M_ID = M_ID;
		this.PERSON = PERSON;
		this.POS_Y = POS_Y;
	}

	@Override
	public void run() {
		connectHSQL dbConnection = new connectHSQL();
		dbConnection.handOverTurn(T_ID, M_ID, PERSON, POS_Y, POS_X);
	}

}
