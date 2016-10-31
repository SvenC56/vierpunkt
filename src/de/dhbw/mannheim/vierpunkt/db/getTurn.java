package de.dhbw.mannheim.vierpunkt.db;

public class getTurn implements Runnable{

	
	
	private int G_ID;
	private int M_ID;

	public getTurn(int G_ID, int M_ID){
		this.G_ID = G_ID;
		this.M_ID = M_ID;
	}
	
	@Override
	public void run() {
		connectHSQL getTurn = new connectHSQL();
		getTurn.getHighscoreTurn(G_ID, M_ID);
	}
}