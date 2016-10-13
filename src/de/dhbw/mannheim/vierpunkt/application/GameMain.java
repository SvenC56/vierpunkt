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
	
		/**************************************************************/
		/*******************TEMPORAER TEST-METHODS*********************/
		/**************************************************************/
		GameLogic game = new GameLogic();
		
		for (int z=0; z <= 2; z++) {
		game.randomGame();
		System.out.println(game.getField(3, 0));
		System.out.println("################################");
		
		for (int y = 0; y < game.getRow() ; y++) {
			System.out.println();
			for (int x = 0; x < game.getColumn(); x++ ) {
				System.out.print(game.getField(x, y));
				System.out.print("\t");
			}}
		int column=game.bestPath(2);
		
		System.out.println("Die Methode sagt, beste Spalte ist:" + column);
		/*************************************************************/
		
		}
	/**Initialisierung des Spielfeldes**/
	/**TestGui gui = new TestGui();
	gui.main(args);**/
		
	
	}
}
