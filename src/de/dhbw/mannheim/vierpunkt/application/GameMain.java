package de.dhbw.mannheim.vierpunkt.application;

import de.dhbw.mannheim.vierpunkt.gui.TestGui;
import de.dhbw.mannheim.vierpunkt.logic.GameLogic;


/**
 * Main-Methode zum Testen / Zum Reinkommen in die Spiellogik
 * Autoren: Gruppe 4 (vier.) - Verantwortlich: Tobias Jung
 **/
public class GameMain {

	/**Main-Methode fuer Spielablauf**/
	public static void main(String[] args) {
	
	/**Initialisierung des Spielfeldes**/
	TestGui gui = new TestGui();
	GameLogic game = new GameLogic();
	gui.main(args);
	
	
	
	}
}
