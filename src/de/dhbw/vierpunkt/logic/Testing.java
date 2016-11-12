package de.dhbw.vierpunkt.logic;

import de.dhbw.vierpunkt.db.ConnectHSQL;

public class Testing {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
//		Game game = new Game();
//		game.startGame("Tobi", "Sven");
//		game.startMatch();
//		Thread.sleep(500);
		ConnectHSQL db = new ConnectHSQL();
		String[][] test = db.catchWrongState();
		for (int k=0;k<test.length ;k++ ) {
			  for (int l=0;l<test[k].length ;l++ ) {
			    System.out.print(test[k][l] + " ");
			  } // end of for
			  System.out.println();
			} // end of for

	}

}
