/**
 * @author Sven Cieslok
 * @version 1.0
 */
package de.dhbw.vierpunkt.db;

public class GetMatch implements Runnable{

private int G_ID;

public GetMatch(int G_ID){
	this.G_ID = G_ID;
}	
	
	@Override
	public void run() {
		ConnectHSQL getMatch = new ConnectHSQL();
		getMatch.getHighscoreMatch(G_ID);
	}
}