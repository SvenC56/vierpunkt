package de.dhbw.mannheim.vierpunkt.db;

public class getMatch implements Runnable{

private int G_ID;

public getMatch(int G_ID){
	this.G_ID = G_ID;
}	
	
	@Override
	public void run() {
		connectHSQL getMatch = new connectHSQL();
		getMatch.getHighscoreMatch(G_ID);
	}
}