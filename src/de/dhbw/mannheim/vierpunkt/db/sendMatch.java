package de.dhbw.mannheim.vierpunkt.db;

public class sendMatch extends Thread {
	int M_ID;
	int G_ID;

	public sendMatch(int M_ID, int G_ID) {
		this.M_ID = M_ID;
		this.G_ID = G_ID;
	}

	@Override
	public void run() {
		connectHSQL dbConnection = new connectHSQL();
		dbConnection.handOverMatch(M_ID, G_ID);
	}

}
