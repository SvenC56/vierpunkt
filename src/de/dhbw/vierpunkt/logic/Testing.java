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
		String[][] test = db.getLastFailMatch();
		for (int k=0;k<test.length ;k++ ) {
			  for (int l=0;l<test[k].length ;l++ ) {
			    System.out.print(test[k][l] + " ");
			  } // end of for
			  System.out.println();
			} // end of for
//		System.out.println(test == null);
//		System.out.println(test.length);
		int mid = db.transformStringToInt(test[0][1]);
		System.out.println("G_ID: " +db.getGIDByMID(mid)+ " M_ID: " + mid );
//		db.deleteGame(db.getGIDByMID(mid), mid);
		Thread.sleep(500);
	}

}
