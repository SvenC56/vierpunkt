package de.dhbw.mannheim.vierpunkt.db;

public class getGame  implements Runnable{

	@Override
	public void run() {
		connectHSQL getGame = new connectHSQL();
		getGame.getHighscoreGame();
	}
}