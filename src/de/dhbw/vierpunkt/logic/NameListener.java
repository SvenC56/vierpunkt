package de.dhbw.vierpunkt.logic;
/**
 * Das Interface fuer die GUI
 * @author tobias
 *
 */
public interface NameListener {
	/**
	 * Startet ein neues Spiel
	 * @param name1
	 * @param name2
	 */
	void startGame(String name1, String name2);
	
	/**
	 * Startet ein manuelles Spiel
	 * @param name1
	 * @param name2
	 */
	void startManGame(String name1, String name2);
	
	/**
	 * Setzt den gespielten Zug im manuellen Spiel
	 * @param name
	 * @param x
	 * @param y
	 */
	void setManTurn(String name, int x, int y);
	
}
