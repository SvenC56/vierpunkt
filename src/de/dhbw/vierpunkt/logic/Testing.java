package de.dhbw.vierpunkt.logic;

public class Testing {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		Game game = new Game();
		game.startGame("Tobi", "Sven");
		game.startMatch();
		Thread.sleep(500);
		

	}

}
