/**
 * @author Sven Cieslok
 * @version 1.0
 */
package de.dhbw.vierpunkt.db;

public class GetTurn implements Runnable{

	
	
	private int G_ID;
	private int M_ID;

	public GetTurn(int G_ID, int M_ID){
		this.G_ID = G_ID;
		this.M_ID = M_ID;
	}
	
	@Override
	public void run() {
		ConnectHSQL getTurn = new ConnectHSQL();
		getTurn.getHighscoreTurn(G_ID, M_ID);
	}
}