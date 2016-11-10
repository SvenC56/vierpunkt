/**
 * @author Sven Cieslok
 * @version 1.0
 */
package de.dhbw.vierpunkt.db;

public class GetGame  implements Runnable{

	@Override
	public void run() {
		ConnectHSQL getGame = new ConnectHSQL();
		getGame.getHighscoreGame();
	}
}