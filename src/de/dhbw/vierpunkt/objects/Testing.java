package de.dhbw.vierpunkt.objects;

public class Testing {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
<<<<<<< HEAD
//		Game game = new Game();
//
//		Thread.sleep(500);
		String MIDRESULT = "TEST";
		int G_ID = 1;
		int M_ID = 2;
		System.out.println("UPDATE MATCH SET MIDRESULT='" + MIDRESULT + "' WHERE G_ID=" + G_ID + " AND M_ID= " + M_ID + ";");
=======
		Game game = new Game();
		game.startGame("Tobi", "Sven");
		game.startMatch();
		Thread.sleep(500);
		

>>>>>>> origin/master
	}

}
